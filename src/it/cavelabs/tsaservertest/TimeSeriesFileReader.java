package it.cavelabs.tsaservertest;

import it.cavelabs.tsaserver.model.TimeSeries;

/**
 * 
 * Interface to define a file reader of TimeSeries
 * 
 * \author Lucchetti Daniele
 * 
 */
public interface TimeSeriesFileReader
{
	/**
	 * Return a TimeSeries that contains the Detections write in the specified file
	 * 
	 * \param filePath The file with Detections
	 * \return The TimeSeries
	 */
	public TimeSeries getTimeSeries( String filePath );
}