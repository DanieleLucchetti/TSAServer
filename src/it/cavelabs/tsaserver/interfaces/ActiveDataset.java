package it.cavelabs.tsaserver.interfaces;

import it.cavelabs.tsaserver.model.Client;
import it.cavelabs.tsaserver.model.Comparison;
import it.cavelabs.tsaserver.model.TimeSeries;

import java.util.Set;

public interface ActiveDataset
{

	public void insert( Client client, TimeSeries ts );

	public Set<Comparison> get();
	
	public void setMaster( Client master );
}
