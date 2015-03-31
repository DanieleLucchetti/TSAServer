package it.cavelabs.tsaserver.model;

/**
 * 
 * Class that represent a single detection
 * 
 * \author Lucchetti Daniele
 * 
 */
public class Detection
{
	private long timestamp;			// The time of detection
	private double x;				// The x-axis detection
	private double y;				// The y-axis detection
	private double z;				// The z-axis detection

	/**
	 * Constructor
	 * 
	 * \param timestamp The time of detection
	 */
	public Detection( long timestamp, double x, double y, double z )
	{
		this.timestamp = timestamp;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Return the time of detection
	 * 
	 * \return The time of detection
	 */
	public long getTimestamp()
	{
		return this.timestamp;
	}

	/**
	 * Return the x value of detection
	 * 
	 * \return X value
	 */
	public double getX()
	{
		return this.x;
	}

	/**
	 * Return the y value of detection
	 * 
	 * \return Y value
	 */
	public double getY()
	{
		return this.y;
	}

	/**
	 * Return the z value of detection
	 * 
	 * \return Z value
	 */
	public double getZ()
	{
		return this.z;
	}
}