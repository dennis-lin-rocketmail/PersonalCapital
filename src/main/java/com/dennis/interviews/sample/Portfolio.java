package com.dennis.interviews.sample;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Portfolio implements Comparable<Portfolio> {
    private String name;
    private BigDecimal endingPrincipal;
    private BigDecimal inflationAdjustedEndingPrincipal;
    private BigDecimal principal;
    private BigDecimal inflation;
    private List<BigDecimal> listYields;
    private List<BigDecimal> listYearEndPrincipals;
    private List<BigDecimal> listYearEndPrincipalsInflationAdjusted;

    public Portfolio(
            final String portfolioName,
            final double principalStart) {
        init(portfolioName, principalStart, 0.0);
    }

    public Portfolio(
            final String portfolioName,
            final double principalStart,
            final double expectedInflation) {
        init(portfolioName, principalStart, expectedInflation);
    }

    private void init(
            final String portfolioName,
            final double principalStart,
            final double expectedInflation) {
        name = portfolioName;
        principal = new BigDecimal(principalStart);

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
    	setYield(year, new BigDecimal(newYield));
    }
    
    public void setYield(final int year, final BigDecimal newYield) {
        while (listYields.size() <= year) {
            listYields.add(BigDecimal.ZERO);
        }
        listYields.set(year, newYield);

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

    public void calculatePortfolio() {
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

    public int compareTo(final Portfolio portfolio) {
        return getEndingPrincipal().compareTo(portfolio.getEndingPrincipal());
    }
}
