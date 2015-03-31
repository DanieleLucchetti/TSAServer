package it.cavelabs.tsaserver.application;

import it.cavelabs.tsaserver.interfaces.WebServer;
import it.cavelabs.tsaserver.interfaces.WebServerListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * 
 * Implementation of WebServer
 * 
 * \author Lucchetti Daniele
 * 
 */
public class MyWebServer implements WebServer
{
	private HttpServer mServer;										// The HTTP server
	private String hostname = "192.168.1.6";						// IP of the server
	private static final int port = 8000;							// The port of the server
	private static final int bufferSize = 99999;					// Size of the buffer to get the body message 
	private static WebServerListener mListener;						// The listener to which notify events
	private static int mId;											// ID for next connected client

	/**
	 * Constructor
	 */
	public MyWebServer()
	{
		mId = 1;
	}

	/**
	 * Called when the server must start
	 */
	@Override
	public void start( int port, WebServerListener listener )
	{
		try
		{
			this.mServer = HttpServer.create(new InetSocketAddress(hostname, MyWebServer.port), 0);
			this.mServer.createContext("/", new MyHandler());
			this.mServer.setExecutor(null);
			this.mServer.start();
			this.mListener = listener;
		} catch ( IOException e )
		{
			e.printStackTrace();
		}
	}

	/**
	 * Called when the server must stop
	 */
	@Override
	public void stop()
	{
		this.mServer.stop(0);
	}

	/**
	 * Return the next available id
	 * 
	 * \return The id
	 */
	public static int nextId()
	{
		return mId++;
	}

	/**
	 * 
	 * Class to handle request of the client
	 * 
	 * \author Lucchetti Daniele
	 * 
	 */
	public static class MyHandler implements HttpHandler
	{

		/**
		 * Called when a request is arrived
		 */
		@Override
		public void handle( HttpExchange he ) throws IOException
		{
			String response = "";
			String body = null;
			int responseStatus = 400;
			try
			{
				body = getBody(he.getRequestBody());
			} catch ( IOException ex )
			{
				responseStatus = 400;
				response = "Invalid request";
				he.sendResponseHeaders(responseStatus, response.length());
				he.close();
			}
			Packet packet = toPacket(body);
			if ( he.getRequestMethod().equals("POST") && packet.type != Packet.ERROR_PACKET )
			{
				responseStatus = 200;

				switch ( packet.type )
				{
				case Packet.CONNECT_PACKET:
					int id = nextId();
					mListener.connect(id, packet.name);
					response = "{\"id\":" + id + "}";
					break;
				case Packet.DATA_PACKET:
					mListener.receive(packet.id, packet.data.toString());
					break;
				}
			} else
			{
				response = "Invalid request";
			}
			he.sendResponseHeaders(responseStatus, response.length());
			OutputStream os = he.getResponseBody();
			os.write(response.getBytes());
			os.close();
			he.close();
		}

		/**
		 * Extract the information from the body and specify what type of request it is
		 * 
		 * \param body THe body of the message
		 * \return A Packet with the information extracted
		 */
		private Packet toPacket( String body )
		{
			// Unpack the data in a Packet
			Packet packet = new Gson().fromJson(body, Packet.class);
			// Select the type of Packet
			if ( packet.name != null && packet.id == 0 && packet.data == null )
			{
				packet.type = Packet.CONNECT_PACKET;
			} else
			{
				if ( packet.name == null && packet.id >= 1 && packet.data != null )
				{
					packet.type = Packet.DATA_PACKET;
				} else
				{
					packet.type = Packet.ERROR_PACKET;
				}
			}
			System.out.println(body);
			return packet;
		}

		/**
		 * Get the body from the request
		 * 
		 * \param is The InputStream of HTTP request
		 * \return The body content
		 */
		private String getBody( InputStream is ) throws UnsupportedEncodingException, IOException
		{
			Reader in = new InputStreamReader(is, "UTF-8");
			StringBuilder out = new StringBuilder();
			char[] buffer = new char[bufferSize];
			for ( ;; )
			{
				int rsz = in.read(buffer, 0, buffer.length);
				if ( rsz < 0 )
				{
					break;
				}
				out.append(buffer, 0, rsz);
			}
			in.close();
			return out.toString();
		}
	}

	/**
	 * 
	 * Class that contain all information about the request arrived
	 * 
	 * Warning: not change the variable's name
	 * 
	 * \author Lucchetti Daniele
	 * 
	 */
	private class Packet
	{
		final static int ERROR_PACKET = 1;			// Represent a wrong packet
		final static int CONNECT_PACKET = 2;		// Represent a packet with information to connect a new client
		final static int DATA_PACKET = 3;			// Represent a packet with information of data of a client

		int type = 0;
		String name = null;
		int id = 0;
		Object data = null;
	}
}