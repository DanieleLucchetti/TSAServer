package it.cavelabs.tsaserver.interfaces;

import it.cavelabs.tsaserver.model.Comparison;
import it.cavelabs.tsaserver.model.Result;

/**
 * 
 * A method to calculate the distance between two time series
 * 
 * \author Lucchetti Daniele
 * 
 */
public interface Comparator
{

	/**
	 * Calculate the distance between two TimeSeries
	 * 
	 * \param comparison The comparison to do
	 * \param distanceFunction The function to calcolate the distance between two Detection
	 * \return The distance
	 */
	public Result compare( Comparison comparison, DistanceFunction distanceFunction );
}