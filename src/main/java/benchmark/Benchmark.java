package benchmark;

import metrics.OperationMetrics;
import structures.DynamicArray;
import structures.MinHeap;
import structures.MyLinkedList;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;

public class Benchmark {

    private static final int[] SIZES = {100, 1000, 10000, 100000};
    private static final int RUNS = 5;

    public static void runBenchmark() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("results.csv"))) {
            writer.println("workload,variant,structure,n,time_ms,steps,moves,comparisons");

            for (int n : SIZES) {
                runW1(n, writer);
                runW2(n, writer);
                runW3(n, writer);
                runW4(n, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void runW1(int n, PrintWriter writer) {
        runAndRecord("W1", "-", "DynamicArray", n, writer, (metrics) -> {
            DynamicArray arr = new DynamicArray(metrics);
            Random rng = new Random(42);
            for (int i = 0; i < n; i++) {
                arr.add(rng.nextInt());
            }
            metrics.reset();
            for (int i = 0; i < 10000; i++) {
                arr.get(rng.nextInt(n));
            }
        });

        runAndRecord("W1", "-", "MyLinkedList", n, writer, (metrics) -> {
            MyLinkedList list = new MyLinkedList(metrics);
            Random rng = new Random(42);
            for (int i = 0; i < n; i++) {
                list.add(rng.nextInt());
            }
            metrics.reset();
            for (int i = 0; i < 10000; i++) {
                list.get(rng.nextInt(n));
            }
        });
    }

    private static void runW2(int n, PrintWriter writer) {
        runAndRecord("W2", "-", "DynamicArray", n, writer, (metrics) -> {
            DynamicArray arr = new DynamicArray(metrics);
            Random rng = new Random(42);
            int[] values = new int[n];
            for (int i = 0; i < n; i++) {
                values[i] = rng.nextInt(100000);
                arr.add(values[i]);
            }
            metrics.reset();
            for (int i = 0; i < 500; i++) {
                arr.contains(values[i % n]);
            }
            for (int i = 0; i < 500; i++) {
                arr.contains(-100000 - i);
            }
        });

        runAndRecord("W2", "-", "MyLinkedList", n, writer, (metrics) -> {
            MyLinkedList list = new MyLinkedList(metrics);
            Random rng = new Random(42);
            int[] values = new int[n];
            for (int i = 0; i < n; i++) {
                values[i] = rng.nextInt(100000);
                list.add(values[i]);
            }
            metrics.reset();
            for (int i = 0; i < 500; i++) {
                list.contains(values[i % n]);
            }
            for (int i = 0; i < 500; i++) {
                list.contains(-100000 - i);
            }
        });
    }

    private static void runW3(int n, PrintWriter writer) {
        runAndRecord("W3", "head", "DynamicArray", n, writer, (metrics) -> {
            DynamicArray arr = createArray(n, metrics);
            metrics.reset();
            for (int i = 0; i < 1000; i++) {
                arr.add(0, i);
            }
            for (int i = 0; i < 1000; i++) {
                arr.remove(0);
            }
        });

        runAndRecord("W3", "head", "MyLinkedList", n, writer, (metrics) -> {
            MyLinkedList list = createList(n, metrics);
            metrics.reset();
            for (int i = 0; i < 1000; i++) {
                list.add(0, i);
            }
            for (int i = 0; i < 1000; i++) {
                list.remove(0);
            }
        });

        runAndRecord("W3", "middle", "DynamicArray", n, writer, (metrics) -> {
            DynamicArray arr = createArray(n, metrics);
            metrics.reset();
            for (int i = 0; i < 1000; i++) {
                arr.add(arr.size() / 2, i);
            }
            for (int i = 0; i < 1000; i++) {
                arr.remove(arr.size() / 2);
            }
        });

        runAndRecord("W3", "middle", "MyLinkedList", n, writer, (metrics) -> {
            MyLinkedList list = createList(n, metrics);
            metrics.reset();
            for (int i = 0; i < 1000; i++) {
                list.add(list.size() / 2, i);
            }
            for (int i = 0; i < 1000; i++) {
                list.remove(list.size() / 2);
            }
        });
    }

    private static void runW4(int n, PrintWriter writer) {
        runAndRecord("W4", "-", "MinHeap", n, writer, (metrics) -> {
            MinHeap heap = new MinHeap(n, metrics);
            Random rng = new Random(42);
            metrics.reset();
            for (int i = 0; i < n; i++) {
                heap.insert(rng.nextInt());
            }
            for (int i = 0; i < n; i++) {
                heap.extractMin();
            }
        });
    }

    @FunctionalInterface
    interface Task {
        void execute(OperationMetrics metrics);
    }

    private static void runAndRecord(String w, String variant, String struct, int n, PrintWriter writer, Task task) {
        long[] times = new long[RUNS];
        OperationMetrics metrics = new OperationMetrics();

        task.execute(new OperationMetrics());

        for (int r = 0; r < RUNS; r++) {
            metrics.reset();
            long start = System.nanoTime();
            task.execute(metrics);
            times[r] = System.nanoTime() - start;
        }

        Arrays.sort(times);
        double medianTimeMs = times[RUNS / 2] / 1e6;

        writer.printf("%s,%s,%s,%d,%.4f,%d,%d,%d%n",
                w, variant, struct, n, medianTimeMs, metrics.steps, metrics.moves, metrics.comparisons);
    }

    private static DynamicArray createArray(int n, OperationMetrics metrics) {
        DynamicArray arr = new DynamicArray(metrics);
        Random rng = new Random(42);
        for (int i = 0; i < n; i++) {
            arr.add(rng.nextInt());
        }
        return arr;
    }

    private static MyLinkedList createList(int n, OperationMetrics metrics) {
        MyLinkedList list = new MyLinkedList(metrics);
        Random rng = new Random(42);
        for (int i = 0; i < n; i++) {
            list.add(rng.nextInt());
        }
        return list;
    }
}