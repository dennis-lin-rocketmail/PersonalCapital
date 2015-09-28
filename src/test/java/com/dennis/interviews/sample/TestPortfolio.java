package com.dennis.interviews.sample;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TestPortfolio {
    private final BigDecimal epsilon = new BigDecimal(0.00001);

    private void logErrorMessages(final List<String> listErrorMessages) {
        if ((null == listErrorMessages) || listErrorMessages.isEmpty()) {
            return;
        }

        StringBuilder message = new StringBuilder("Num errors found: " );
        message.append(listErrorMessages.size());
        for (String errorMessage : listErrorMessages) {
            message.append("\n\t");
            message.append(errorMessage);
        }
        Assert.fail(message.toString());
    }

    private boolean equivalent(
            final BigDecimal bd1,
            final BigDecimal bd2) {
        if ((null == bd1) && (null == bd2)) {
            return true;
        }

        if ((null == bd1) && (null != bd2)) {
            return false;
        }

        if ((null != bd1) && (null == bd2)) {
            return false;
        }

        BigDecimal diff = bd1.subtract(bd2);
        diff = diff.abs();

        return (diff.compareTo(epsilon) < 0);
    }

    @Test
    public void testBasePortfolio01() {
        Portfolio portfolio = new Portfolio("TestPortfolio", 100000, 10, 0);
        portfolio.simulateYears(1,  1);
        BigDecimal endingBalance = portfolio.getEndingPrincipal();
        Assert.assertEquals(
                endingBalance.doubleValue(),
                110000.0,
                epsilon.doubleValue());
    }

    @Test
    public void testBasePortfolio02() {
        Portfolio portfolio = new Portfolio("TestPortfolio", 100000, 10, 0);
        portfolio.simulateYears(1,  2);
        BigDecimal endingBalance = portfolio.getEndingPrincipal();
        Assert.assertEquals(
                endingBalance.doubleValue(),
                121000.0,
                epsilon.doubleValue());
    }

    @Test
    public void testBasePortfolio03() {
        Portfolio portfolio = new Portfolio("TestPortfolio", 100000, 20, 20);
        portfolio.setYield(2, BigDecimal.TEN.doubleValue());
        portfolio.setYield(1, BigDecimal.TEN.doubleValue());
        BigDecimal endingBalance = portfolio.getEndingPrincipal();
        Assert.assertEquals(
                endingBalance.doubleValue(),
                121000.0,
                epsilon.doubleValue());
    }

    @Test
    public void testCompareMoreThan() {
        Portfolio portfolio01 = new Portfolio("TestPortfolio01", 100000, 10, 0);
        portfolio01.simulateYears(1, 3);
        Portfolio portfolio02 = new Portfolio("TestPortfolio02", 100000, 10, 0);
        portfolio02.simulateYears(1, 1);

        Assert.assertEquals(portfolio01.compareTo(portfolio02), 1);
    }

    @Test
    public void testCompareLessThan() {
        Portfolio portfolio01 = new Portfolio("TestPortfolio01", 100000, 10, 0);
        portfolio01.simulateYears(1, 3);
        Portfolio portfolio02 = new Portfolio("TestPortfolio02", 100000, 10, 0);
        portfolio02.simulateYears(1, 1);

        Assert.assertEquals(portfolio02.compareTo(portfolio01), -1);
    }

    @Test
    public void testCompareEquals() {
        Portfolio portfolio = new Portfolio("TestPortfolio01", 100000, 10, 0);
        portfolio.simulateYears(1, 3);

        Assert.assertEquals(portfolio, portfolio);
    }

    @Test
    public void testEndingBalanceNoInterestNoInflation() {
        Portfolio portfolio = new Portfolio("TestPortfolio01", 100000, 0, 0, 0);
        portfolio.simulateYears(1, 10);

        BigDecimal expectedBalance = new BigDecimal(100000);
        List<String> listErrorMessages = new ArrayList<>();
        if (!equivalent(expectedBalance, portfolio.getEndingPrincipal())) {
            listErrorMessages.add(
                    String.format(
                            "Ending balance changed: %s, %s",
                            expectedBalance,
                            portfolio.getEndingPrincipal()));
        }
        BigDecimal inflationAdjustedEndingBalance =
                portfolio.getInflationAdjustedEndingPrincipal();
        if (!equivalent(expectedBalance, inflationAdjustedEndingBalance)) {
            listErrorMessages.add(
                    String.format(
                            "Inflation adjusted ending balance changed: %s, %s",
                            expectedBalance,
                            inflationAdjustedEndingBalance));
        }

        logErrorMessages(listErrorMessages);
    }

    @Test
    public void testEndingPortfolioBalancesInflationEqualsReturn() {
        double rateInterest = 3.5;
        double rateInflation = 3.5;
        int numYears = 10;
        Portfolio portfolio =
                new Portfolio(
                        "TestPortfolio01",
                        100000,
                        rateInterest,
                        0,
                        rateInflation);
        portfolio.simulateYears(1, numYears);

        BigDecimal interestFactor = new BigDecimal(rateInterest);
        interestFactor = interestFactor.divide(BigDecimal.TEN);
        interestFactor = interestFactor.divide(BigDecimal.TEN);
        interestFactor = interestFactor.add(BigDecimal.ONE);
        interestFactor = interestFactor.pow(numYears);

        BigDecimal expectedBalance = new BigDecimal(100000);
        expectedBalance = expectedBalance.multiply(interestFactor);
        BigDecimal expectedBalanceInflationAdjusted = new BigDecimal(100000);

        List<String> listErrorMessages = new ArrayList<>();
        if (!equivalent(expectedBalance, portfolio.getEndingPrincipal())) {
            listErrorMessages.add(
                    String.format(
                            "Ending balance changed: %s, %s",
                            expectedBalance,
                            portfolio.getEndingPrincipal()));
        }
        BigDecimal inflationAdjustedEndingBalance =
                portfolio.getInflationAdjustedEndingPrincipal();
        if (!equivalent(
                expectedBalanceInflationAdjusted,
                inflationAdjustedEndingBalance)) {
            listErrorMessages.add(
                    String.format(
                            "Inflation adjusted ending balance changed: %s, %s",
                            expectedBalance,
                            inflationAdjustedEndingBalance));
        }

        List<Portfolio> listPortfolios = new ArrayList<>();
        listPortfolios.add(portfolio);
        Portfolio.dumpToFile(listPortfolios, "test.csv");
        logErrorMessages(listErrorMessages);
    }
}
