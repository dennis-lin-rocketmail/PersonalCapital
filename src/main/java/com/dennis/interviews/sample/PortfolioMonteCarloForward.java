package com.dennis.interviews.sample;

import java.math.BigDecimal;
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

        reportPortfolioList(
                "Aggressive (Future Value, FV Inflation Adjusted)",
                listAggressivePortfolios);
        reportPortfolioList(
                "Conservative (Future Value, FV Inflation Adjusted)",
                listConservativePortfolios);
    }

    private static void reportPortfolioList(
            final String title,
            final List<Portfolio> listPortfolios) {
        DecimalFormat df = new DecimalFormat("0.000000");
        Portfolio portfolioPercentile10 = PortfolioStatistics.getPercentilePortfolio(
                listPortfolios, 10);
        BigDecimal endingBalance10 = portfolioPercentile10.getEndingPrincipal();
        BigDecimal inflationAdjustedEndingBalance10 =
                portfolioPercentile10.getInflationAdjustedEndingPrincipal();

        Portfolio portfolioPercentile90 = PortfolioStatistics.getPercentilePortfolio(
                listPortfolios, 90);
        BigDecimal endingBalance90 = portfolioPercentile10.getEndingPrincipal();
        BigDecimal inflationAdjustedEndingBalance90 =
                portfolioPercentile90.getInflationAdjustedEndingPrincipal();

        BigDecimal medianBalance =
        		PortfolioStatistics.getMedianEndingPrincipal(listPortfolios);
        BigDecimal medianBalanceInflationAdjusted =
        		PortfolioStatistics.getMedianInflationAdjustedEndingPrincipal(
                        listPortfolios);

        System.out.print(title);
        System.out.println(":");

        System.out.print("    Median:          ");
        System.out.print(df.format(medianBalance));
        System.out.print(", ");
        System.out.println(df.format(medianBalanceInflationAdjusted));
        System.out.print("    10th Percentile: ");
        System.out.print(df.format(endingBalance10));
        System.out.print(", ");
        System.out.println(df.format(inflationAdjustedEndingBalance10));
        System.out.print("    90th Percentile: ");
        System.out.print(df.format(endingBalance90));
        System.out.print(", ");
        System.out.println(df.format(inflationAdjustedEndingBalance90));
    }

    private static Portfolio createAggressivePortfolio(final int index) {
        DecimalFormat df = new DecimalFormat("00000");
        Portfolio portfolio =
                new Portfolio(
                        "Aggressive-" + df.format(index),
                        100000,
                        9.4324,
                        15.675,
                        3.5);
        portfolio.simulateYears(1,  20);

        return portfolio;
    }

    private static Portfolio createConservativePortfolio(final int index) {
        DecimalFormat df = new DecimalFormat("00000");
        Portfolio portfolio =
                new Portfolio(
                        "Conservative-" + df.format(index),
                        100000,
                        6.189,
                        6.3438,
                        3.5);
        portfolio.simulateYears(1,  20);

        return portfolio;
    }
}
