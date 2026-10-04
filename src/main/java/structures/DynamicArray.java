package structures;

import metrics.OperationMetrics;

public class DynamicArray {
    private int[] data;
    private int size;
    private final OperationMetrics metrics;

    public DynamicArray(OperationMetrics metrics) {
        this.data = new int[10];
        this.size = 0;
        this.metrics = metrics;
    }

    private void ensureCapacity() {
        if (size == data.length) {
            int[] newData = new int[data.length * 2];
            for (int i = 0; i < size; i++) {
                metrics.steps++;
                metrics.moves++;
                newData[i] = data[i];
            }
            data = newData;
        }
    }

    public void add(int element) {
        ensureCapacity();
        metrics.moves++;
        data[size++] = element;
    }

    public void add(int index, int element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        ensureCapacity();
        for (int i = size; i > index; i--) {
            metrics.steps++;
            metrics.moves++;
            data[i] = data[i - 1];
        }
        data[index] = element;
        metrics.moves++;
        size++;
    }

    public int remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        int removed = data[index];
        metrics.steps++;
        for (int i = index; i < size - 1; i++) {
            metrics.steps++;
            metrics.moves++;
            data[i] = data[i + 1];
        }
        size--;
        return removed;
    }

    public int get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        metrics.steps++;
        return data[index];
    }

    public boolean contains(int x) {
        for (int i = 0; i < size; i++) {
            metrics.steps++;
            metrics.comparisons++;
            if (data[i] == x) {
                return true;
            }
        }
        return false;
    }

    public int size() {
        return size;
    }
}