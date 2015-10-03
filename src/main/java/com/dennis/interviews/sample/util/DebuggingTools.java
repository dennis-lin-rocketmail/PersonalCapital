package com.dennis.interviews.sample.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import com.dennis.interviews.sample.Portfolio;

public final class DebuggingTools {

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
}