package it.cavelabs.tsaserver.model;

/**
 * 
 * Represent a comparison from the master and a slave
 * 
 * \author Lucchetti Daniele
 *
 */
public class Comparison
{
	private Client mClient;			// The Client setted as slave
	private TimeSeries mMaster;		// The master's TimeSeries
	private TimeSeries mSlave;		// The slave's TimeSeries

	/**
	 * Constructor
	 * 
	 * \param client The slave
	 * \param master The master's TimeSeries
	 * \param slave The slave's TimeSeries
	 */
	public Comparison( Client client, TimeSeries master, TimeSeries slave )
	{
		this.mClient = client;
		this.mMaster = master;
		this.mSlave = slave;
	}

	/**
	 * Return the slave
	 * 
	 * \return The Client setted as slave
	 */
	public Client getClient()
	{
		return this.mClient;
	}

	/**
	 * Return the master's TimeSeries of comparison
	 * 
	 * \return The TimeSeries of master
	 */
	public TimeSeries getMaster()
	{
		return this.mMaster;
	}

	/**
	 * Return the slave's TimeSeries of comparison
	 * 
	 * \return The TimeSeries of slave
	 */
	public TimeSeries getSlave()
	{
		return this.mSlave;
	}
}