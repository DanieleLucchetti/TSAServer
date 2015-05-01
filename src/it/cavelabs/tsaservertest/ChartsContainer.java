package it.cavelabs.tsaservertest;

import java.awt.Graphics;
import java.awt.Panel;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.tabbedui.VerticalLayout;

public class ChartsContainer extends Panel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<ChartPanel> mCharts;

	public ChartsContainer()
	{
		this.mCharts = new ArrayList<>();
		this.setLayout(new VerticalLayout());
	}

	public void addChart( TimeSeriesCollection collection, String title )
	{
		JFreeChart chart = createChart(title, collection);
		ChartPanel chartPanel = new ChartPanel(chart);
		this.mCharts.add(chartPanel);
		this.add(chartPanel);
		repaint();
	}

	@Override
	public void paint( Graphics g )
	{
		ChartPanel chartPanel;
		Iterator<ChartPanel> iter = this.mCharts.iterator();
		int width = this.getWidth();
		int height = this.getHeight() / this.mCharts.size();
		int i = 0;
		while ( iter.hasNext() )
		{
			chartPanel = iter.next();
			chartPanel.setSize(width, height);
			chartPanel.setLocation(0, height * i++);
		}
		super.paint(g);
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
		//renderer.setBaseItemLabelGenerator(generator);
		renderer.setBaseItemLabelsVisible(true);
		return chart;
	}
}