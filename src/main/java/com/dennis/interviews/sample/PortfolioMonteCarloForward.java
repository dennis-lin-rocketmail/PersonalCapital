package com.dennis.interviews.sample;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.dennis.interviews.sample.simulation.AbstractSimulationScenario;
import com.dennis.interviews.sample.simulation.GaussianYield;
import com.dennis.interviews.sample.util.DebuggingTools;
import com.dennis.interviews.sample.util.PortfolioStatistics;

public class PortfolioMonteCarloForward {
    public static void main(final String[] args) {
    	final int numPortfolios = 100;
    	final double expectedInflation = 3.5;
    	final double startingPrincipal = 100000;
    	List<Portfolio> listAggressivePortfolios = new ArrayList<>();
    	List<Portfolio> listConservativePortfolios = new ArrayList<>();
    	DecimalFormat df = new DecimalFormat("00000");
    	
    	for (int counter = 0; counter < numPortfolios; counter++) {
    		listAggressivePortfolios.add(
    				new Portfolio("Aggressive-" + df.format(counter),
    						startingPrincipal,
    						expectedInflation));
    		listConservativePortfolios.add(
    				new Portfolio("Conservative-" + df.format(counter),
    						startingPrincipal,
    						expectedInflation));
    	}
    	
    	AbstractSimulationScenario aggressiveScenario =
    			new GaussianYield(9.4324, 15.675);
    	AbstractSimulationScenario conservativeScenario =
    			new GaussianYield(6.189, 6.3438);
    	
    	for (int counter = 0; counter < numPortfolios; counter++) {
    		aggressiveScenario.simulateYears(
    				listAggressivePortfolios.get(counter), 1, 20);
    		conservativeScenario.simulateYears(
    				listConservativePortfolios.get(counter), 1, 20);
    	}
    
    	DebuggingTools.dumpToFile(
    			listAggressivePortfolios,
    			"aggressive.csv");
    	DebuggingTools.dumpToFile(
    			listConservativePortfolios,
    			"conservative.csv");
    	
    	PortfolioStatistics.reportPortfolioList(
                "Aggressive (Future Value, FV Inflation Adjusted)",
                listAggressivePortfolios);
    	PortfolioStatistics.reportPortfolioList(
                "Conservative (Future Value, FV Inflation Adjusted)",
                listConservativePortfolios);
    }
}
