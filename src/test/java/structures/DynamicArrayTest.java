package structures;

import metrics.OperationMetrics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class DynamicArrayTest {
    private DynamicArray array;

    @BeforeEach
    void setUp() {
        array = new DynamicArray(new OperationMetrics());
    }

    @Test
    void testAddAndGet() {
        array.add(10);
        array.add(20);
        assertEquals(10, array.get(0));
        assertEquals(20, array.get(1));
    }

    @Test
    void testAddAtIndex() {
        array.add(10);
        array.add(30);
        array.add(1, 20);
        assertEquals(10, array.get(0));
        assertEquals(20, array.get(1));
        assertEquals(30, array.get(2));
    }

    @Test
    void testRemove() {
        array.add(10);
        array.add(20);
        array.add(30);
        assertEquals(20, array.remove(1));
        assertEquals(2, array.size());
        assertEquals(30, array.get(1));
    }

    @Test
    void testContains() {
        array.add(5);
        array.add(15);
        assertTrue(array.contains(5));
        assertFalse(array.contains(99));
    }

    @Test
    void testInvalidIndexThrowsException() {
        assertThrows(IndexOutOfBoundsException.class, () -> array.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> array.remove(0));
        assertThrows(IndexOutOfBoundsException.class, () -> array.add(-1, 5));
    }

    @Test
    void testRandomDataAgainstStd() {
        Random rng = new Random(42);
        java.util.ArrayList<Integer> expected = new java.util.ArrayList<>();
        for (int i = 0; i < 100; i++) {
            int val = rng.nextInt();
            array.add(val);
            expected.add(val);
        }
        for (int i = 0; i < 100; i++) {
            assertEquals(expected.get(i), array.get(i));
        }
    }
}