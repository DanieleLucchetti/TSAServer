package it.cavelabs.tsaserver.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * A list of detections
 * 
 * \author Lucchetti Daniele
 * 
 */
public class TimeSeries
{
	List<Detection> mData;

	/**
	 * Constructor
	 */
	public TimeSeries()
	{
		this.mData = new ArrayList<Detection>();
	}

	/**
	 * Constructor
	 * 
	 * \param data An array of detection to insert in time series
	 */
	public TimeSeries( Detection[] data )
	{
		this();
		put(data);
	}

	/**
	 * Return the number of the elements of the time series
	 */
	public int getLength()
	{
		return this.mData.size();
	}

	/**
	 * Return the element with the specified index
	 */
	public Detection getDataAt( int index )
	{
		return this.mData.get(index);
	}

	/**
	 * Insert the new detection in the time series
	 * 
	 * \param data The detection to insert
	 */
	public void put( Detection data )
	{
		// The List is scanned to maintain it ordered
		int index = this.mData.size();
		if ( index > 0 )
		{
			// The scan start from the tail
			while ( this.mData.get(index - 1).getTimestamp() > data.getTimestamp() )
			{
				// New detection to insert is occurred after than data already inserted
				index--;
			}
		}
		this.mData.add(index, data);
	}

	/**
	 * Insert the new array of detection in time series
	 * 
	 * \param data The array of detection
	 */
	public void put( Detection[] data )
	{
		for ( int i = 0; i < data.length; i++ )
		{
			put(data[i]);
		}
	}

	/**
	 * Merge the two TimeSeries
	 * 
	 * \param ts The TimeSeries to merge with
	 */
	public void merge( TimeSeries ts )
	{
		Detection[] detections = new Detection[ts.getLength()];
		put(ts.mData.toArray(detections));
	}
}