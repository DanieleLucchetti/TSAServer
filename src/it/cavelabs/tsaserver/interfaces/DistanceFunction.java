package it.cavelabs.tsaserver.interfaces;

import it.cavelabs.tsaserver.model.Detection;

/**
 * 
 * A function to calculate the distance between two Detection
 * 
 * \author Lucchetti Daniele
 * 
 */
public interface DistanceFunction
{
	/**
	 * Return a double value that represent the distance from the data
	 * 
	 * \param data1 First detection
	 * \param data2 Second detection
	 * \return The distance
	 */
	public double distanceBetween( Detection data1, Detection data2 );
}