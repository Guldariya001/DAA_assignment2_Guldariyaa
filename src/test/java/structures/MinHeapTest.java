package structures;

import metrics.OperationMetrics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class MinHeapTest {
    private MinHeap heap;

    @BeforeEach
    void setUp() {
        heap = new MinHeap(10, new OperationMetrics());
    }

    @Test
    void testHeapPropertyHolds() {
        Random rng = new Random(42);
        for (int i = 0; i < 50; i++) {
            heap.insert(rng.nextInt(1000));
            assertHeapProperty(heap);
        }
        for (int i = 0; i < 25; i++) {
            heap.extractMin();
            assertHeapProperty(heap);
        }
    }

    @Test
    void testSortedOutput() {
        Random rng = new Random(42);
        int n = 100;
        for (int i = 0; i < n; i++) {
            heap.insert(rng.nextInt(1000));
        }

        int prev = heap.extractMin();
        for (int i = 1; i < n; i++) {
            int curr = heap.extractMin();
            assertTrue(prev <= curr, "Output must be in non-decreasing order");
            prev = curr;
        }
    }

    @Test
    void testEmptyHeapExceptions() {
        assertThrows(IllegalStateException.class, () -> heap.extractMin());
        assertThrows(IllegalStateException.class, () -> heap.peekMin());
    }

    @Test
    void testDuplicates() {
        heap.insert(10);
        heap.insert(10);
        heap.insert(5);
        assertEquals(5, heap.extractMin());
        assertEquals(10, heap.extractMin());
        assertEquals(10, heap.extractMin());
    }

    private void assertHeapProperty(MinHeap h) {
        for (int i = 0; i < h.size(); i++) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            if (left < h.size()) {
                assertTrue(h.getRawElement(i) <= h.getRawElement(left));
            }
            if (right < h.size()) {
                assertTrue(h.getRawElement(i) <= h.getRawElement(right));
            }
        }
    }
}