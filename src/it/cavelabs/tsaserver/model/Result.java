package it.cavelabs.tsaserver.model;

/**
 * 
 * The result of a Comparison
 * 
 * \author Lucchetti Daniele
 * 
 */
public class Result
{
	private Comparison mComparison;		// The comparison of two TimeSeries
	private double mResult;				// The result of the comparison

	/**
	 * Constructor
	 * 
	 * \param comparison The comparison
	 * \param result The result of comparison
	 */
	public Result( Comparison comparison, double result )
	{
		this.mComparison = comparison;
		this.mResult = result;
	}

	/**
	 * Return the comparison
	 * 
	 * \return The comparison
	 */
	public Comparison getComparison()
	{
		return this.mComparison;
	}

	/**
	 * Return the result of comparison
	 * 
	 * \return The result
	 */
	public double getResult()
	{
		return this.mResult;
	}
}