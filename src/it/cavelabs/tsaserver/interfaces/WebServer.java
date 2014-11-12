package it.cavelabs.tsaserver.interfaces;

public interface WebServer
{

	public void start( int port, WebServerListener listener );

	public void stop();
}