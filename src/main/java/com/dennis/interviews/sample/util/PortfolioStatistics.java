package com.dennis.interviews.sample.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

import com.dennis.interviews.sample.Portfolio;

public final class PortfolioStatistics {
    public static BigDecimal getMedianEndingPrincipal(
            final List<Portfolio> sortedListPortfolios) {
        int medianIndex = sortedListPortfolios.size() / 2;
        if (sortedListPortfolios.size() % 2 == 1) {
            Portfolio portfolioRight = sortedListPortfolios.get(medianIndex);
            Portfolio portfolioLeft = sortedListPortfolios.get(medianIndex - 1);
            BigDecimal median = portfolioRight.getEndingPrincipal();
            median = median.add(portfolioLeft.getEndingPrincipal());
            median = median.divide(new BigDecimal(2), RoundingMode.HALF_UP);

            return median;
        } else {
            Portfolio medianPortfolio = sortedListPortfolios.get(medianIndex);

            return medianPortfolio.getEndingPrincipal();
        }
    }
    
    public static BigDecimal getMedianInflationAdjustedEndingPrincipal(
            final List<Portfolio> sortedListPortfolios) {
        int medianIndex = sortedListPortfolios.size() / 2;
        if (sortedListPortfolios.size() % 2 == 1) {
            Portfolio portfolioRight = sortedListPortfolios.get(medianIndex);
            Portfolio portfolioLeft = sortedListPortfolios.get(medianIndex - 1);
            BigDecimal median =
                    portfolioRight.getInflationAdjustedEndingPrincipal();
            median = median.add(
                    portfolioLeft.getInflationAdjustedEndingPrincipal());
            median = median.divide(new BigDecimal(2), RoundingMode.HALF_UP);

            return median;
        } else {
            Portfolio medianPortfolio = sortedListPortfolios.get(medianIndex);

            return medianPortfolio.getInflationAdjustedEndingPrincipal();
        }
    }
    

    public static final Portfolio getPercentilePortfolio(
            final List<Portfolio> listPortfolio, final int percentile) {
        BigDecimal expectedIndex = new BigDecimal(percentile);
        expectedIndex =
                expectedIndex.multiply(new BigDecimal(listPortfolio.size()));
        expectedIndex = expectedIndex.divide(BigDecimal.TEN);
        expectedIndex = expectedIndex.divide(BigDecimal.TEN);
        expectedIndex.setScale(0, BigDecimal.ROUND_UP);

        return listPortfolio.get(expectedIndex.intValue());
    }

    public static final void reportPortfolioList(
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
}