package com.dennis.interviews.sample.simulation;

import java.math.BigDecimal;
import java.util.Random;

import com.dennis.interviews.sample.Portfolio;

public abstract class AbstractSimulationScenario {
	private Random random = new Random();
	
	public final void simulateYears(
			final Portfolio portfolio,
			final int yearStart,
			final int yearEnd) {
		while (portfolio.getNumYields() < yearEnd) {
			portfolio.setYield(portfolio.getNumYields(), 0.0);
		}
		
		for (int currentYear = yearStart; currentYear <= yearEnd; currentYear++) {
        	portfolio.setYield(currentYear, simulateYieldForYear(portfolio, currentYear));
        }

        portfolio.calculatePortfolio();
	}
	
	public abstract BigDecimal simulateYieldForYear(final Portfolio portfolio, final int year);
	
	protected final Random getRandom() {
		return random;
	}
}