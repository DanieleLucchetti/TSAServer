package it.cavelabs.tsaserver.interfaces;

import it.cavelabs.tsaserver.model.Client;
import it.cavelabs.tsaserver.model.TimeSeries;

public interface Storage
{

	public void save( Client client, TimeSeries ts );
}