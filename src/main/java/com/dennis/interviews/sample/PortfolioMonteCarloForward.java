package com.dennis.interviews.sample;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.dennis.interviews.sample.util.PortfolioStatistics;

public class PortfolioMonteCarloForward {
    public static void main(final String[] args) {
        List<Portfolio> listAggressivePortfolios = new ArrayList<>();
        List<Portfolio> listConservativePortfolios = new ArrayList<>();

        for (int counter = 0; counter < 10000; counter++) {
            System.out.println("Processing portfolio #" + counter);
            listAggressivePortfolios.add(
                    createAggressivePortfolio(counter));
            listConservativePortfolios.add(
                    createConservativePortfolio(counter));
        }

        System.out.println("Sorting portfolios by ending balance");
        Collections.sort(listAggressivePortfolios);
        Collections.sort(listConservativePortfolios);
        System.out.println("Finished sorting portfolios by ending balance");

        /*
        System.out.println("Dumping aggressive portfolios to CSV");
        Portfolio.dumpToFile(
                listAggressivePortfolios,
                "AggressivePortfolios.csv");
        System.out.println("Dumping conservative portfolios to CSV");
        Portfolio.dumpToFile(
                listConservativePortfolios,
                "ConservativePortfolios.csv");
        //*/

        PortfolioStatistics.reportPortfolioList(
                "Aggressive (Future Value, FV Inflation Adjusted)",
                listAggressivePortfolios);
        PortfolioStatistics.reportPortfolioList(
                "Conservative (Future Value, FV Inflation Adjusted)",
                listConservativePortfolios);
    }

    private static Portfolio createAggressivePortfolio(final int index) {
        DecimalFormat df = new DecimalFormat("00000");
        Portfolio portfolio =
                new Portfolio(
                        "Aggressive-" + df.format(index),
                        100000,
                        //9.4324,
                        //15.675,
                        3.5);
        //portfolio.simulateYears(1,  20);

        return portfolio;
    }

    private static Portfolio createConservativePortfolio(final int index) {
        DecimalFormat df = new DecimalFormat("00000");
        Portfolio portfolio =
                new Portfolio(
                        "Conservative-" + df.format(index),
                        100000,
                        //6.189,
                        //6.3438,
                        3.5);
        //portfolio.simulateYears(1,  20);

        return portfolio;
    }
}
