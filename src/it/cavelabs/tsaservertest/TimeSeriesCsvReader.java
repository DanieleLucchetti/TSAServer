package it.cavelabs.tsaservertest;

import it.cavelabs.tsaserver.model.Detection;
import it.cavelabs.tsaserver.model.TimeSeries;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * 
 * Read a TimeSeries from a file .csv
 * 
 * \author Lucchetti Daniele
 * 
 */
public class TimeSeriesCsvReader implements TimeSeriesFileReader
{

	@Override
	public TimeSeries getTimeSeries( String filePath )
	{
		if ( !filePath.endsWith(".csv") )
		{
			return null;
		}
		TimeSeries ts = new TimeSeries();
		try
		{
			// Open the file
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			String line = br.readLine();
			line = br.readLine();
			int index;
			long timestamp;
			double x, y, z;
			Detection detection;
			// Read every line end get the coordinates end timestamp
			while ( line != null )
			{
				index = line.indexOf(",");
				timestamp = Long.parseLong(line.substring(0, index));
				line = line.substring(index + 1);
				index = line.indexOf(",");
				x = Double.parseDouble(line.substring(0, index));
				line = line.substring(index + 1);
				index = line.indexOf(",");
				y = Double.parseDouble(line.substring(0, index));
				line = line.substring(index + 1);
				z = Double.parseDouble(line);
				// Create and put the Detection
				detection = new Detection(timestamp, x, y, z);
				ts.put(detection);
				line = br.readLine();
			}
			br.close();
		} catch ( FileNotFoundException e )
		{
			e.printStackTrace();
		} catch ( IOException e )
		{
			e.printStackTrace();
		}
		return ts;
	}
}
