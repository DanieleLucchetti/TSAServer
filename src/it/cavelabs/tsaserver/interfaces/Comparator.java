package it.cavelabs.tsaserver.interfaces;

import it.cavelabs.tsaserver.model.Result;
import it.cavelabs.tsaserver.model.TimeSeries;

public interface Comparator
{

	public Result compare(TimeSeries ts1, TimeSeries ts2);
}