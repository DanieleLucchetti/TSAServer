package it.cavelabs.tsaserver.interfaces;

/**
 * 
 * A server to which a client connect to send data
 * 
 * \author Lucchetti Daniele
 * 
 */
public interface WebServer
{
	/**
	 * Starting the server
	 * 
	 * \param port Number of port to listen clients
	 * \param listener The listener to send information
	 */
	public void start( int port, WebServerListener listener );

	/**
	 * Stopping the server
	 */
	public void stop();
}