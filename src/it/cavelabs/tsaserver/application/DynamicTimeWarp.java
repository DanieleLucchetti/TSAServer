package it.cavelabs.tsaserver.application;

import it.cavelabs.tsaserver.interfaces.Comparator;
import it.cavelabs.tsaserver.interfaces.DistanceFunction;
import it.cavelabs.tsaserver.model.Comparison;
import it.cavelabs.tsaserver.model.Result;
import it.cavelabs.tsaserver.model.TimeSeries;

/**
 * 
 * Dynamic time warp method
 * 
 * \author Lucchetti Daniele
 * 
 */
public class DynamicTimeWarp implements Comparator
{

	@Override
	public Result compare( Comparison comparison, DistanceFunction distanceFunction )
	{
		TimeSeries ts1 = comparison.getMaster();
		TimeSeries ts2 = comparison.getSlave();
		int m = ts1.getLength();
		int n = ts2.getLength();
		// Matrix that will contain distances between the series
		// The [i][j] element of the matrix will contain the minimum distance between the sub-series
		// of t1 and t2 until the elements i of t1 and the element j of t2
		double[][] DTW = new double[m][n];
		int i, j;
		double tempDistance;

		// Element [0][0] is simply the distance from the first elements of t1 and t2
		DTW[0][0] = distanceFunction.distanceBetween(ts1.getDataAt(0), ts2.getDataAt(0));

		// The first row of DTW contain the distances between the first element of t1 and the j
		// element of t2 summed with its previous t2 element distance (it is necessarily the minimum
		// cost)
		for ( j = 1; j < n; j++ )
		{
			tempDistance = distanceFunction.distanceBetween(ts1.getDataAt(0), ts2.getDataAt(j));
			DTW[0][j] = tempDistance + DTW[0][j - 1];
		}

		// For the first operations in the cycle are worth the same consideration of the previous
		// cycle applied to the first column.
		// Every element of the matrix is the distance between the values of time series
		// with the matrix element indices summed to the distance of the minimum path that can take
		// at there configuration
		for ( i = 1; i < m; i++ )
		{
			tempDistance = distanceFunction.distanceBetween(ts1.getDataAt(i), ts2.getDataAt(0));
			DTW[i][0] = tempDistance + DTW[i - 1][0];
			for ( j = 1; j < n; j++ )
			{
				tempDistance = distanceFunction.distanceBetween(ts1.getDataAt(i), ts2.getDataAt(j));
				DTW[i][j] = tempDistance + Math.min(DTW[i - 1][j], Math.min(DTW[i][j - 1], DTW[i - 1][j - 1]));
			}
		}

		// The [m][n] element contains the minimum distance between the time series considering all
		// elements
		return new Result(comparison, (DTW[m - 1][n - 1]));
	}
}