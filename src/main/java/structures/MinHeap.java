package structures;

import metrics.OperationMetrics;

public class MinHeap {
    private int[] heap;
    private int size;
    private final OperationMetrics metrics;

    public MinHeap(int capacity, OperationMetrics metrics) {
        this.heap = new int[Math.max(capacity, 10)];
        this.size = 0;
        this.metrics = metrics;
    }

    public MinHeap(int[] array, OperationMetrics metrics) {
        this.metrics = metrics;
        this.heap = array.clone();
        this.size = array.length;
        for (int i = (size / 2) - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    public void insert(int x) {
        if (size == heap.length) {
            int[] newHeap = new int[heap.length * 2];
            System.arraycopy(heap, 0, newHeap, 0, size);
            heap = newHeap;
        }
        heap[size] = x;
        metrics.moves++;
        siftUp(size);
        size++;
    }

    public int peekMin() {
        if (size == 0) {
            throw new IllegalStateException("Heap is empty");
        }
        metrics.steps++;
        return heap[0];
    }

    public int extractMin() {
        if (size == 0) {
            throw new IllegalStateException("Heap is empty");
        }
        int min = heap[0];
        metrics.steps++;
        heap[0] = heap[size - 1];
        metrics.moves++;
        size--;
        siftDown(0);
        return min;
    }

    private void siftUp(int i) {
        while (i > 0) {
            int parent = (i - 1) / 2;
            metrics.steps += 2;
            metrics.comparisons++;
            if (heap[i] < heap[parent]) {
                swap(i, parent);
                i = parent;
            } else {
                break;
            }
        }
    }

    private void siftDown(int i) {
        while (2 * i + 1 < size) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int smallest = left;

            metrics.steps++;
            if (right < size) {
                metrics.steps++;
                metrics.comparisons++;
                if (heap[right] < heap[left]) {
                    smallest = right;
                }
            }

            metrics.steps += 2;
            metrics.comparisons++;
            if (heap[smallest] < heap[i]) {
                swap(i, smallest);
                i = smallest;
            } else {
                break;
            }
        }
    }

    private void swap(int i, int j) {
        int temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
        metrics.moves += 3;
    }

    public int size() {
        return size;
    }

    public int getRawElement(int index) {
        return heap[index];
    }
}