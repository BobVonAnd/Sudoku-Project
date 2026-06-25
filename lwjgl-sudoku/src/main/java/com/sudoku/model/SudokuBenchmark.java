package com.sudoku.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SudokuBenchmark {

    static class BenchmarkResult {
        int size;
        int fieldsToRemove;
        int givens;
        String method;

        int runs;
        double avgMs;
        double minMs;
        double maxMs;
        double medianMs;

        double avgAttempts;
        double avgAttempt;
        @Override
        public String toString() {
            return String.format(
                "%s | size=%d | remove=%d | givens=%d | runs=%d | avg=%.2f ms | median=%.2f ms | min=%.2f ms | max=%.2f ms | avgAttempt=%.2f",
                method, size, fieldsToRemove, givens, runs, avgMs, medianMs, minMs, maxMs, avgAttempts
            );
        }
    }

    public static void main(String[] args) {
        int[] sizes = {9};
        int[] fieldsToRemoveValues = {61};
        int runsPerTest = 10;

        List<BenchmarkResult> results = new ArrayList<>();

        for (int size : sizes) {
            for (int fieldsToRemove : fieldsToRemoveValues) {
                results.add(runBenchmark(size, fieldsToRemove, runsPerTest, false));
            }
        }
        // for (int size : sizes) {
        //     for (int fieldsToRemove : fieldsToRemoveValues) {
        //         results.add(runBenchmark(size, fieldsToRemove, runsPerTest, true));
        //     }
        // }

        System.out.println("===== RESULTS =====");
        for (BenchmarkResult result : results) {
            System.out.println(result);
        }
    }

    private static BenchmarkResult runBenchmark(
            int size,
            int fieldsToRemove,
            int runs,
            boolean recursive
    ) {
        List<Double> timesMs = new ArrayList<>();
        List<Integer> attemptsPerRun = new ArrayList<>();

        // Optional warmup run.
        // Java gets faster after the JVM has warmed up.
        algoXSolver warmupSolver = new algoXSolver();
        if (recursive) {
            warmupSolver.algoXCreateUniqueRecursive(size, fieldsToRemove);
        } else {
            warmupSolver.algoXCreateUnique(size, fieldsToRemove);
        }

        for (int i = 0; i < runs; i++) {
            algoXSolver xSolver = new algoXSolver();

            long start = System.nanoTime();

            if (recursive) {
                xSolver.algoXCreateUniqueRecursive(size, fieldsToRemove);
            } else {
                xSolver.algoXCreateUnique(size, fieldsToRemove);
            }

            long end = System.nanoTime();

            double elapsedMs = (end - start) / 1_000_000.0;
            timesMs.add(elapsedMs);

            int attemptsThisRun = xSolver.attempts.size();
            xSolver.attempts = new ArrayList<>();
            attemptsPerRun.add(attemptsThisRun);
        }

        BenchmarkResult result = new BenchmarkResult();
        result.size = size;
        result.fieldsToRemove = fieldsToRemove;
        result.givens = size * size - fieldsToRemove;
        result.method = recursive ? "Recursive" : "Non-recursive";
        result.runs = runs;

        result.avgMs = averageDouble(timesMs);
        result.minMs = Collections.min(timesMs);
        result.maxMs = Collections.max(timesMs);
        result.medianMs = median(timesMs);

        result.avgAttempts = averageInt(attemptsPerRun);


        return result;
    }

    private static int sumAttempts(List<Integer> attempts) {
        int sum = 0;
        for (int value : attempts) {
            sum += value;
        }
        return sum;
    }

    private static double averageDouble(List<Double> values) {
        if (values.isEmpty()) return 0;

        double sum = 0;
        for (double value : values) {
            sum += value;
        }

        return sum / values.size();
    }

    private static double averageInt(List<Integer> values) {
        if (values.isEmpty()) return 0;

        int sum = 0;
        for (int value : values) {
            sum += value;
        }

        return (double) sum / values.size();
    }

    private static double median(List<Double> values) {
        if (values.isEmpty()) return 0;

        List<Double> sorted = new ArrayList<>(values);
        Collections.sort(sorted);

        int middle = sorted.size() / 2;

        if (sorted.size() % 2 == 1) {
            return sorted.get(middle);
        } else {
            return (sorted.get(middle - 1) + sorted.get(middle)) / 2.0;
        }
    }
}