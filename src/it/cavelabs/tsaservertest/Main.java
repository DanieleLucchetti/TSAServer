package it.cavelabs.tsaservertest;

import it.cavelabs.tsaserver.application.Logic;
import it.cavelabs.tsaserver.model.Client;
import it.cavelabs.tsaserver.model.Detection;
import it.cavelabs.tsaserver.model.TimeSeries;

import java.awt.Panel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.DynamicTimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.tabbedui.VerticalLayout;

public class Main implements LogicListener
{
	boolean mStart;
	Logic mLogic;
	Map<Client, ChartPanel> mCharts;
	Map<Client, DynamicTimeSeriesCollection> mDynamicTimeSeries;
	JFrame mFrame;

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
		this.mFrame.setLayout(new VerticalLayout());
		buttonPanel.add(b);
		this.mFrame.add(buttonPanel);
		this.mFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.mFrame.show();

		this.mCharts = new HashMap<Client, ChartPanel>();
		this.mLogic = new Logic(this, null);
	}

	public XYSeriesCollection convertTimeSeries( TimeSeries ts )
	{
		XYSeriesCollection series = new XYSeriesCollection();
		XYSeries xSeries = new XYSeries("X");
		XYSeries ySeries = new XYSeries("Y");
		XYSeries zSeries = new XYSeries("Z");
		Detection d = null;
		for ( int i = 0; i < ts.getLength(); i++ )
		{
			d = ts.getDataAt(i);
			xSeries.add(d.getTimestamp(), d.getX());
			ySeries.add(d.getTimestamp(), d.getY());
			zSeries.add(d.getTimestamp(), d.getZ());
		}
		series.addSeries(xSeries);
		series.addSeries(ySeries);
		series.addSeries(zSeries);
		return series;
	}

	public DynamicTimeSeriesCollection dynamicTimeSeries( Client client, TimeSeries ts )
	{
		DynamicTimeSeriesCollection dynamicTimeSeries = this.mDynamicTimeSeries.get(client);
		if ( dynamicTimeSeries == null )
		{
			dynamicTimeSeries = new DynamicTimeSeriesCollection(3, ts.getLength());
			this.mDynamicTimeSeries.put(client, dynamicTimeSeries);
		}
		//dynamicTimeSeries.a
		return null;
	}

	@Override
	public void registerClient( Client client )
	{

	}

	@Override
	public void receiveData( Client client, TimeSeries ts )
	{
		ChartPanel chartPanel = this.mCharts.get(client);
		if ( chartPanel == null )
		{
			JFreeChart chart = createChart(client.getName(), convertTimeSeries(ts));
			chartPanel = new ChartPanel(chart);
			this.mCharts.put(client, chartPanel);
			//chartPanel.setPreferredSize(new Dimension(900, 470));
			chartPanel.setVisible(true);
			this.mFrame.add(chartPanel);
		} else
		{
			chartPanel.setChart(createChart(client.getName(), convertTimeSeries(ts)));
		}
		chartPanel.repaint();
	}

	public JFreeChart createChart( String title, XYDataset dataset )
	{
		final JFreeChart chart = ChartFactory.createXYLineChart(title, "Tempo", "Accelerazione", dataset, PlotOrientation.VERTICAL, true, true, false);

		XYPlot plot = (XYPlot) chart.getPlot();
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, true);
		plot.setRenderer(renderer);
		renderer.setBaseShapesVisible(true);
		renderer.setBaseShapesFilled(true);
		NumberFormat format = NumberFormat.getNumberInstance();
		format.setMaximumFractionDigits(2);
		XYItemLabelGenerator generator = new StandardXYItemLabelGenerator(StandardXYItemLabelGenerator.DEFAULT_ITEM_LABEL_FORMAT, format, format);
		renderer.setBaseItemLabelGenerator(generator);
		renderer.setBaseItemLabelsVisible(true);
		return chart;
	}
}