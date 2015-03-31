package it.cavelabs.tsaservertest;

import it.cavelabs.tsaserver.model.Client;
import it.cavelabs.tsaserver.model.TimeSeries;

public interface LogicListener
{
	public void registerClient(Client client);
	public void receiveData(Client client, TimeSeries ts);
}