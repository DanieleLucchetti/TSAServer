package it.cavelabs.tsaserver.interfaces;

import com.google.gson.Gson;

public interface WebServerListener
{

	public void connect( int id, Gson name );

	public void receive( int id, Gson data );

	public void disconnect( int id );
}