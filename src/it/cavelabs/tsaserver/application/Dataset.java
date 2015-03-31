package it.cavelabs.tsaserver.application;

import it.cavelabs.tsaserver.interfaces.ActiveDataset;
import it.cavelabs.tsaserver.model.Client;
import it.cavelabs.tsaserver.model.Comparison;
import it.cavelabs.tsaserver.model.TimeSeries;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 
 * Containing all TimeSeries to compare and the Client associated
 * 
 * \author Lucchetti Daniele
 * 
 */
public class Dataset implements ActiveDataset
{
	private Map<Client, TimeSeries> mTimeSeries;	// A Map with the TimeSeries mapped with Client
	private Client mMaster;							// The Client setted as master

	/**
	 * Constructor
	 */
	public Dataset()
	{
		this.mTimeSeries = new HashMap<Client, TimeSeries>();
	}

	/**
	 * Insert the new TimeSeries for the Client or merge the TimeSeries if the Client has already a TimeSeries inserted
	 * 
	 * \param client The Client who detects the TimeSeries
	 * \param ts The TimeSeries Detected
	 */
	@Override
	public void insert( Client client, TimeSeries ts )
	{
		TimeSeries oldTs = this.mTimeSeries.get(client);
		if ( oldTs == null )
		{
			this.mTimeSeries.put(client, ts);
		} else
		{
			oldTs.merge(ts);
		}
	}

	/**
	 * Return the list of comparison to do between the master and every slaves
	 * 
	 * \return A Set of Comparison or null if the master wasn't setted
	 */
	@Override
	public Set<Comparison> get()
	{
		// If the master is not setted cannot be calculated comparisons
		if ( this.mMaster == null )
		{
			return null;
		}
		Set<Comparison> comparisons = new HashSet<Comparison>();
		// If there is a single series of data, there are not comparison to do
		if ( this.mTimeSeries.size() > 1 )
		{
			// Obtain the master's time series
			TimeSeries tsMaster = this.mTimeSeries.remove(this.mMaster);
			// If have not data about the master's time series, there are not comparison to do
			if ( tsMaster != null && this.mTimeSeries.size() > 1 )
			{
				// The remains in Map are the slaves
				Set<Client> slaves = this.mTimeSeries.keySet();
				Iterator<Client> iterator = slaves.iterator();
				Client currentSlave;
				while ( iterator.hasNext() )
				{
					// Scan every slaves and add new Comparison
					currentSlave = iterator.next();
					comparisons.add(new Comparison(currentSlave, tsMaster, this.mTimeSeries.remove(currentSlave)));
				}
			}
		}
		return comparisons;
	}

	/**
	 * Set the client as master
	 * 
	 * \param master The client to set as master
	 */
	@Override
	public void setMaster( Client master )
	{
		this.mMaster = master;
	}
}