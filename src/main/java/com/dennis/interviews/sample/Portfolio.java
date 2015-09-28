package com.dennis.interviews.sample;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Portfolio implements Comparable {
    private String name;
    private BigDecimal endingPrincipal;
    private BigDecimal inflationAdjustedEndingPrincipal;
    private BigDecimal principal;
    private BigDecimal yieldMean;
    private BigDecimal yieldStdDev;
    private BigDecimal inflation;
    private List<BigDecimal> listYields;
    private List<BigDecimal> listYearEndPrincipals;
    private List<BigDecimal> listYearEndPrincipalsInflationAdjusted;

    public Portfolio(
            final String portfolioName,
            final double principalStart,
            final double portfolioYieldMean,
            final double portfolioYieldStdDev) {
        init(portfolioName, principalStart, portfolioYieldMean,
                portfolioYieldStdDev, 0.0);
    }

    public Portfolio(
            final String portfolioName,
            final double principalStart,
            final double portfolioYieldMean,
            final double portfolioYieldStdDev,
            final double expectedInflation) {
        init(portfolioName, principalStart, portfolioYieldMean,
                portfolioYieldStdDev, expectedInflation);
    }

    private void init(
            final String portfolioName,
            final double principalStart,
            final double portfolioYieldMean,
            final double portfolioYieldStdDev,
            final double expectedInflation) {
        name = portfolioName;
        principal = new BigDecimal(principalStart);
        yieldMean = new BigDecimal(portfolioYieldMean);
        yieldStdDev = new BigDecimal(portfolioYieldStdDev);

        inflation = new BigDecimal(expectedInflation);
        inflation = inflation.divide(BigDecimal.TEN);
        inflation = inflation.divide(BigDecimal.TEN);
        inflation = inflation.add(BigDecimal.ONE);

        listYields = new ArrayList<>();
        listYields.add(BigDecimal.ZERO);
        listYearEndPrincipals = new ArrayList<>();
        listYearEndPrincipalsInflationAdjusted = new ArrayList<>();
    }

    public void setYield(final int year, final double newYield) {
        while (listYields.size() <= year) {
            listYields.add(BigDecimal.ZERO);
        }
        listYields.set(year, new BigDecimal(newYield));

        calculatePortfolio();
    }

    public void simulateYears(final int yearStart, final int yearEnd) {
        endingPrincipal = null;

        while (listYields.size() <= yearEnd) {
            listYields.add(BigDecimal.ZERO);
        }

        Random random = new Random();
        for (int year = yearStart; year <= yearEnd; year++) {
            BigDecimal randomFactor = new BigDecimal(random.nextGaussian());
            BigDecimal randomYield =
                    yieldMean.add(yieldStdDev.multiply(randomFactor));
            listYields.set(year, randomYield);
        }

        calculatePortfolio();
    }

    public int getNumYields() {
        return listYields.size();
    }

    public BigDecimal getYieldForYear(final int year) {
        return listYields.get(year);
    }

    public BigDecimal getEndingPrincipalForYear(final int year) {
        return listYearEndPrincipals.get(year);
    }

    public BigDecimal getInflationAdjustedEndingPrincipalForYear(final int year) {
        return listYearEndPrincipalsInflationAdjusted.get(year);
    }

    private void calculatePortfolio() {
        endingPrincipal = principal;

        listYearEndPrincipals.clear();
        listYearEndPrincipalsInflationAdjusted.clear();

        int year = 0;
        for (BigDecimal yield : listYields) {
            BigDecimal interestFactor = yield.divide(BigDecimal.TEN);
            interestFactor = interestFactor.divide(BigDecimal.TEN);
            interestFactor = BigDecimal.ONE.add(interestFactor);

            endingPrincipal = endingPrincipal.multiply(interestFactor);
            listYearEndPrincipals.add(endingPrincipal);
            BigDecimal inflationFactor = inflation.pow(year);
            inflationAdjustedEndingPrincipal =
                    endingPrincipal.divide(
                            inflationFactor, RoundingMode.HALF_UP);
            listYearEndPrincipalsInflationAdjusted.add(
                    inflationAdjustedEndingPrincipal);
            year++;
        }
    }

    public BigDecimal getEndingPrincipal() {
        return endingPrincipal;
    }

    public BigDecimal getInflationAdjustedEndingPrincipal() {
        return inflationAdjustedEndingPrincipal;
    }

    public BigDecimal getBeginningPrincipal() {
        return principal;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(final Object object) {
        if (object instanceof Portfolio) {
            Portfolio portfolio = (Portfolio) object;
            return getEndingPrincipal().compareTo(portfolio.getEndingPrincipal());
        }
        if (object instanceof Number) {
            Number number = (Number) object;
            BigDecimal value = new BigDecimal(number.doubleValue());
            return getEndingPrincipal().compareTo(value);
        }

        throw new IllegalArgumentException("Can not compare");
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

    public static final void dumpToFile(
            final List<Portfolio> listPortfolio,
            final String filename) {
        try {
            DecimalFormat df = new DecimalFormat("0.000000");
            FileOutputStream outfile = new FileOutputStream(filename);
            outfile.write("Portfolio Name,Beginning Principal,".getBytes());
            outfile.write("Ending Principal\n".getBytes());
            int index = 0;
            for (Portfolio portfolio : listPortfolio) {
                System.out.println("Dumping portfolio #" + index++);
                outfile.write(portfolio.getName().getBytes());
                outfile.write(",".getBytes());
                BigDecimal beginningPrincipal =
                        portfolio.getBeginningPrincipal();
                outfile.write(beginningPrincipal.toString().getBytes());
                outfile.write(",".getBytes());
                BigDecimal endingPrincipal =
                        portfolio.getEndingPrincipal();
                outfile.write(endingPrincipal.toString().getBytes());
                outfile.write(",Yields".getBytes());
                for (int year = 0; year < portfolio.getNumYields(); year++) {
                    outfile.write(",".getBytes());
                    BigDecimal yield = portfolio.getYieldForYear(year);
                    outfile.write(df.format(yield).getBytes());
                }
                outfile.write("\n".getBytes());
                outfile.write(",,,Ending Principal".getBytes());
                for (int year = 0; year < portfolio.getNumYields(); year++) {
                    outfile.write(",".getBytes());
                    BigDecimal principal =
                            portfolio.getEndingPrincipalForYear(year);
                    outfile.write(df.format(principal).getBytes());
                }
                outfile.write("\n".getBytes());
                outfile.write(",,,Inflation Adjusted".getBytes());
                for (int year = 0; year < portfolio.getNumYields(); year++) {
                    outfile.write(",".getBytes());
                    BigDecimal principal =
                        portfolio.getInflationAdjustedEndingPrincipalForYear(
                                year);
                    outfile.write(df.format(principal).getBytes());
                }
                outfile.write("\n".getBytes());
            }
            outfile.close();
        } catch (IOException e) {
            e.printStackTrace();
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
}
