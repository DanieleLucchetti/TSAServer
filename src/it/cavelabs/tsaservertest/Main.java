package it.cavelabs.tsaservertest;

import it.cavelabs.tsaserver.application.Logic;
import it.cavelabs.tsaserver.model.Client;
import it.cavelabs.tsaserver.model.Detection;
import it.cavelabs.tsaserver.model.TimeSeries;

import java.awt.BorderLayout;
import java.awt.Panel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;

import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;

public class Main implements LogicListener
{
	boolean mStart;
	Logic mLogic;
	HashMap<Client, TimeSeriesCollection> mTimeSeries;
	List<TimeSeriesCollection> mDataset;
	JFrame mFrame;
	ChartsContainer mChartsContainer;

	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		new Main();
	}

	public Main()
	{
		this.mFrame = new JFrame();
		this.mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JButton b = new JButton("Avvia/Chiudi");
		Panel buttonPanel = new Panel();
		this.mStart = false;
		b.addMouseListener(new MouseListener()
		{

			@Override
			public void mouseReleased( MouseEvent arg0 )
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed( MouseEvent arg0 )
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited( MouseEvent arg0 )
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered( MouseEvent arg0 )
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked( MouseEvent arg0 )
			{
				if ( !mStart )
				{
					mLogic.startServer();
					System.out.println("Server started");
				} else
				{
					mLogic.stopServer();
					System.out.println("Server stopped");
				}
				mStart = !mStart;
			}
		});
		this.mFrame.setLayout(new BorderLayout());
		buttonPanel.add(b);
		this.mFrame.add(buttonPanel, BorderLayout.NORTH);
		this.mFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.mFrame.show();

		this.mTimeSeries = new HashMap<Client, TimeSeriesCollection>();
		this.mDataset = new ArrayList<TimeSeriesCollection>();

		this.mChartsContainer = new ChartsContainer();
		this.mFrame.add(this.mChartsContainer, BorderLayout.CENTER);

		this.mLogic = new Logic(this, null);
	}

	public void convertTimeSeries( TimeSeries ts, org.jfree.data.time.TimeSeries tsx, org.jfree.data.time.TimeSeries tsy, org.jfree.data.time.TimeSeries tsz )
	{
		Detection d = null;
		for ( int i = 0; i < ts.getLength(); i++ )
		{
			d = ts.getDataAt(i);
			tsx.add(new TimeSeriesDataItem(new Millisecond(new Date(d.getTimestamp())), d.getX()));
			tsy.add(new TimeSeriesDataItem(new Millisecond(new Date(d.getTimestamp())), d.getY()));
			tsz.add(new TimeSeriesDataItem(new Millisecond(new Date(d.getTimestamp())), d.getZ()));
		}
	}

	@Override
	public void registerClient( Client client )
	{
		org.jfree.data.time.TimeSeries timeSeriesX = new org.jfree.data.time.TimeSeries(client.getName() + "_x");
		org.jfree.data.time.TimeSeries timeSeriesY = new org.jfree.data.time.TimeSeries(client.getName() + "_y");
		org.jfree.data.time.TimeSeries timeSeriesZ = new org.jfree.data.time.TimeSeries(client.getName() + "_z");
		timeSeriesX.setMaximumItemAge(5000);
		timeSeriesY.setMaximumItemAge(5000);
		timeSeriesZ.setMaximumItemAge(5000);
		TimeSeriesCollection collection = new TimeSeriesCollection();
		collection.addSeries(timeSeriesX);
		collection.addSeries(timeSeriesY);
		collection.addSeries(timeSeriesZ);
		this.mTimeSeries.put(client, collection);
		this.mDataset.add(collection);
		TimeSeriesCollection collX = new TimeSeriesCollection();
		TimeSeriesCollection collY = new TimeSeriesCollection();
		TimeSeriesCollection collZ = new TimeSeriesCollection();
		collX.addSeries(timeSeriesX);
		collY.addSeries(timeSeriesY);
		collZ.addSeries(timeSeriesZ);
		this.mChartsContainer.addChart(collection, "Movimento");
		this.mChartsContainer.addChart(collX, "MovimentoX");
		this.mChartsContainer.addChart(collY, "MovimentoY");
		this.mChartsContainer.addChart(collZ, "MovimentoZ");
	}

	@Override
	public void receiveData( Client client, TimeSeries ts )
	{
		TimeSeriesCollection collection = this.mTimeSeries.get(client);
		org.jfree.data.time.TimeSeries timeSeriesX = collection.getSeries(client.getName() + "_x");
		org.jfree.data.time.TimeSeries timeSeriesY = collection.getSeries(client.getName() + "_y");
		org.jfree.data.time.TimeSeries timeSeriesZ = collection.getSeries(client.getName() + "_z");
		convertTimeSeries(ts, timeSeriesX, timeSeriesY, timeSeriesZ);
	}

	@Override
	public void disconnectClient( Client client )
	{
		this.mTimeSeries.remove(client);
	}
}