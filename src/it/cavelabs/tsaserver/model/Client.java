package it.cavelabs.tsaserver.model;

/**
 * 
 * A connected device
 * 
 * \author Lucchetti Daniele
 * 
 */
public class Client
{
	private String mName;		// The name of the client

	/**
	 * Constructor
	 * 
	 * \param name The name
	 */
	public Client( String name )
	{
		this.mName = name;
	}

	/**
	 * Return the name of the client
	 * 
	 * \return The name
	 */
	public String getName()
	{
		return this.mName;
	}
}