package org.vollib.j_lets_be_rational;

import au.com.bytecode.opencsv.CSVWriter;
import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Maricris on 19/04/2017.
 */
public class SpeedBenchmarkingTest {

    @Test
    public void testRun() throws Exception {
        LinkedHashMap<String, String> results = new LinkedHashMap<>();
        results.put("","0");

        for (Double d : linspace(2, 6, 10)) {
            int n = Double.valueOf(Math.pow(10, d)).intValue();
            List<Double> K = linspace(145, 150, n);
            double price = 0.001;
            double F = 100;
            char flag = 'c';
            double q = 1;// c = 1; p = -1
            double r = 0.01;
            double t = 0.5;

            long start = System.currentTimeMillis();
            for (Double k : K) {
                LetsBeRational.implied_volatility_from_a_transformed_rational_guess(price,F, k, t, q);
            }
            long end = System.currentTimeMillis();
            double seconds = Double.valueOf(end - start)/1000.0;
            System.out.printf("%d calls in %f seconds\n", K.size(), seconds);

            results.put(String.valueOf(n), String.valueOf(seconds));

        }

        toCsv(results, "j_lets_be_rational_results.csv");

    }

    public void toCsv(Map<String, String> values, String csvFilename) throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter(csvFilename), ',', CSVWriter.NO_QUOTE_CHARACTER);

        String[] mapKeys = values.keySet().toArray(new String[]{});
        String[] mapValues = values.values().toArray(new String[]{});

        writer.writeNext(mapKeys);
        writer.writeNext(mapValues);
        writer.close();
    }

    public List<Double> linspace(double startValue, double endValue, int numElements) {
        double increment = (endValue-startValue) / (numElements-1);
        List<Double> doubles = IntStream.rangeClosed(0, numElements-1).mapToDouble(i -> startValue + i * increment).boxed().collect(Collectors.toList());
        return doubles;
    }



}
