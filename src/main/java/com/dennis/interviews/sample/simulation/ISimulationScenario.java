package com.dennis.interviews.sample.simulation;

import com.dennis.interviews.sample.Portfolio;

public interface ISimulationScenario {
	void simulateYears(Portfolio portfolio, int yearStart, int yearEnd);
}