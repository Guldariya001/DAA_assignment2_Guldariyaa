package structures;

import metrics.OperationMetrics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class MyLinkedListTest {
    private MyLinkedList list;

    @BeforeEach
    void setUp() {
        list = new MyLinkedList(new OperationMetrics());
    }

    @Test
    void testAddAndGet() {
        list.add(10);
        list.add(20);
        assertEquals(10, list.get(0));
        assertEquals(20, list.get(1));
    }

    @Test
    void testAddAtIndex() {
        list.add(10);
        list.add(30);
        list.add(1, 20);
        assertEquals(10, list.get(0));
        assertEquals(20, list.get(1));
        assertEquals(30, list.get(2));
    }

    @Test
    void testRemoveEdgeCases() {
        list.add(10);
        list.add(20);
        list.add(30);
        assertEquals(10, list.remove(0));
        assertEquals(30, list.remove(1));
        assertEquals(1, list.size());
        assertEquals(20, list.get(0));
    }

    @Test
    void testContains() {
        list.add(42);
        assertTrue(list.contains(42));
        assertFalse(list.contains(100));
    }

    @Test
    void testInvalidIndexThrowsException() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(0));
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(5, 10));
    }

    @Test
    void testRandomDataAgainstStd() {
        Random rng = new Random(42);
        java.util.LinkedList<Integer> expected = new java.util.LinkedList<>();
        for (int i = 0; i < 100; i++) {
            int val = rng.nextInt();
            list.add(val);
            expected.add(val);
        }
        for (int i = 0; i < 100; i++) {
            assertEquals(expected.get(i), list.get(i));
        }
    }
}