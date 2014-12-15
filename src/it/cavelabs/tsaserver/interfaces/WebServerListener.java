package it.cavelabs.tsaserver.interfaces;

/**
 * 
 * A callback to get information about WebServer
 * 
 * \author Lucchetti Daniele
 * 
 */
public interface WebServerListener
{
	/**
	 * A client is connected
	 * 
	 * \param id An unique id assigned by WebServer
	 * \param name The name of the client
	 */
	public void connect( int id, String name );

	/**
	 * Data are received by a client
	 * 
	 * \param id The client that has sent data
	 * \param jsonData Data receive as JSON string
	 */
	public void receive( int id, String jsonData );

	/**
	 * A client is disconnected
	 * 
	 * \param id The id of client
	 */
	public void disconnect( int id );
}