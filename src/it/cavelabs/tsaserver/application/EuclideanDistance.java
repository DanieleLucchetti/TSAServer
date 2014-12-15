package it.cavelabs.tsaserver.application;

import it.cavelabs.tsaserver.interfaces.DistanceFunction;
import it.cavelabs.tsaserver.model.Detection;

/**
 * 
 * Euclidean distance
 * 
 * \author Lucchetti Daniele
 * 
 */
public class EuclideanDistance implements DistanceFunction
{

	@Override
	public double distanceBetween( Detection data1, Detection data2 )
	{
		return Math.sqrt(Math.pow(data1.getX() - data2.getX(), 2) + Math.pow(data1.getY() - data2.getY(), 2) + Math.pow(data1.getZ() - data2.getZ(), 2));
	}
}