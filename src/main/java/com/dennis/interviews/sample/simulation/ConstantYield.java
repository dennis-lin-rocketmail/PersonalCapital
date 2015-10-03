package com.dennis.interviews.sample.simulation;

import java.math.BigDecimal;

import com.dennis.interviews.sample.Portfolio;

public class ConstantYield extends AbstractSimulationScenario {
	private BigDecimal constantYield = null;
	
	public ConstantYield(final double yield) {
		constantYield = new BigDecimal(yield);
	}
	
	/**
	 *  Default constructor (hidden).
	 */
	@SuppressWarnings("unused")
	private ConstantYield() {
		//  Do nothing.
	}		
	
	@Override
	public BigDecimal simulateYieldForYear(final Portfolio portfolio, final int year) {
		return constantYield;
	}
}
