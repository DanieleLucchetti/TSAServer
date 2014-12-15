package it.cavelabs.tsaserver.application;

import it.cavelabs.tsaserver.interfaces.Storage;
import it.cavelabs.tsaserver.model.Client;
import it.cavelabs.tsaserver.model.Detection;
import it.cavelabs.tsaserver.model.TimeSeries;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CsvStorage implements Storage
{
	private String mSavingPath;

	public CsvStorage()
	{

	}

	public CsvStorage( String path )
	{
		this.mSavingPath = path;
	}

	@Override
	public void save( Client client, TimeSeries ts )
	{
		File file = new File(client.getName() + ".csv");
		BufferedWriter bw = null;
		try
		{
			bw = new BufferedWriter(new FileWriter(file,true));
			if ( !file.exists() )
			{
				String head = "TIMESTAMP,X,Y,Z\n";
				bw.write(head);
			}
			Detection data;
			String row;
			for ( int i = 0; i < ts.getLength(); i++ )
			{
				data = ts.getDataAt(i);
				row = data.getTimestamp() + "," + data.getX() + "," + data.getY() + "," + data.getZ() + "\n";
				bw.append(row);
			}
			bw.flush();
		} catch ( IOException e )
		{
			e.printStackTrace();
		} finally
		{
			try
			{
				bw.close();
			} catch ( IOException e )
			{
				e.printStackTrace();
			}
		}
	}
}