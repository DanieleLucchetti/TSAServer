package it.cavelabs.tsaserver.application;

import it.cavelabs.tsaserver.interfaces.ActiveDataset;
import it.cavelabs.tsaserver.interfaces.Comparator;
import it.cavelabs.tsaserver.interfaces.DistanceFunction;
import it.cavelabs.tsaserver.interfaces.Storage;
import it.cavelabs.tsaserver.interfaces.WebServer;
import it.cavelabs.tsaserver.interfaces.WebServerListener;
import it.cavelabs.tsaserver.model.Client;
import it.cavelabs.tsaserver.model.Comparison;
import it.cavelabs.tsaserver.model.Detection;
import it.cavelabs.tsaserver.model.Result;
import it.cavelabs.tsaserver.model.TimeSeries;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

/**
 * 
 * Application logic
 * 
 * \author Lucchetti Daniele
 * 
 */
public class Logic extends TimerTask implements WebServerListener
{
	private WebServer mServer;					// The server to listen the client
	private int mServerPort = 59642;			// The port when the server listen the client
	private Map<Integer, Client> mClients;		// List of clients
	private ActiveDataset mDataset;				// Structure to contain data that are send by client
	private Storage mStorage;					// Method to store data
	private List<Result> mResults;				// List of Result od Comparison
	private final static int PERIOD = 1000;		// Period to execute the check for the Comparison to do

	boolean stop = false;

	/**
	 * CLunstructor
	 */
	public Logic()
	{
		this.mServer = new MyWebServer();
		this.mClients = new HashMap<Integer, Client>();
		this.mDataset = new Dataset();
		this.mStorage = new CsvStorage();
		// Set the timer to remember when check for the Comparison to do
		Timer timer = new Timer(true);
		timer.schedule(this, PERIOD, PERIOD);
	}

	/**
	 * Start the WebServer
	 */
	public void startServer()
	{
		this.startServer(this.mServerPort);
	}

	/**
	 * Start the WebServer with the port specified to listen the client
	 * 
	 * \param port The port when listen the client
	 */
	public synchronized void startServer( int port )
	{
		this.mServer.start(port, this);
	}

	public void stopServer()
	{
		this.mServer.stop();
	}

	/**
	 * Called when a client is connected
	 */
	@Override
	public void connect( int id, String name )
	{
		this.mClients.put(id, new Client(name));
	}

	/**
	 * Called when new data are received by the WebServer
	 */
	@Override
	public void receive( int id, String jsonData )
	{
		// Obtain an array of Detection
		//Detection[] data = new Gson().fromJson(jsonData, Detection[].class);
		JsonArray array = (JsonArray) new JsonParser().parse(jsonData);
		Detection[] data = new Detection[array.size()];
		for ( int i = 0; i < data.length; i++ )
		{
			data[i] = new Gson().fromJson(array.get(i), Detection.class);
		}
		TimeSeries ts = new TimeSeries(data);
		Client client = this.mClients.get(id);
		// The new data are inserted in ActiveDataSet and are stored
		this.mDataset.insert(client, ts);
		this.mStorage.save(client, ts);
	}

	/**
	 * Called when a client disconnect
	 */
	@Override
	public void disconnect( int id )
	{
		// If a Client disconnect, it is removed
		this.mClients.remove(id);
	}

	/**
	 * Called when the Timer expires
	 */
	@Override
	public void run()
	{
		Set<Comparison> comparisons = this.mDataset.get();
		if ( comparisons != null )
		{
			// Scan the Set of Comparison
			Iterator<Comparison> iterator = comparisons.iterator();
			while ( iterator.hasNext() )
			{
				// For each Comparison a Thread is created to calculate the Result
				new ThreadComparator(new DynamicTimeWarp(), new EuclideanDistance(), iterator.next());
			}
		}
	}

	/**
	 * 
	 * Thread that calculate the Result of a Comparison
	 * 
	 * \author Lucchetti Daniele
	 * 
	 */
	class ThreadComparator extends Thread
	{
		private Comparator mComparator;					// The method to use to calculate the distance
		private DistanceFunction mDistanceFunction;		// The function to use to calculate the distance between two data
		private Comparison mComparison;					// The Comparison to do

		/**
		 * Constructor
		 * 
		 * \param comparator The method to use to calculate the distance
		 * \param distanceFunction The function to use to calculate the distance between two data
		 * \param comparison The Comparison to do
		 */
		public ThreadComparator( Comparator comparator, DistanceFunction distanceFunction, Comparison comparison )
		{
			this.mComparator = comparator;
			this.mDistanceFunction = distanceFunction;
			this.mComparison = comparison;
		}

		/**
		 * 
		 */
		@Override
		public void run()
		{
			mResults.add(this.mComparator.compare(mComparison, mDistanceFunction));
		}
	}
}