package com.dennis.interviews.sample.simulation;

import java.math.BigDecimal;

import com.dennis.interviews.sample.Portfolio;

public class GaussianYield extends AbstractSimulationScenario {
	private BigDecimal portfolioYieldMean = null;
	private BigDecimal portfolioYieldStdDev = null;
	
	public GaussianYield(final double yieldMean, final double yieldStdDev) {
		portfolioYieldMean = new BigDecimal(yieldMean);
		portfolioYieldStdDev = new BigDecimal(yieldStdDev);
	}
	
	/**
	 *  Default constructor (hidden).
	 */
	@SuppressWarnings("unused")
	private GaussianYield() {
		//  Do nothing.
	}
	
	@Override
	public BigDecimal simulateYieldForYear(final Portfolio portfolio, final int year) {
		BigDecimal randomFactor = new BigDecimal(getRandom().nextGaussian());
        BigDecimal randomYield =
                portfolioYieldMean.add(
                		portfolioYieldStdDev.multiply(randomFactor));	
        
        return randomYield;
	}
}
