package structures;

import metrics.OperationMetrics;

public class MyLinkedList {
    private static class Node {
        int value;
        Node next;
        Node prev;

        Node(int value) {
            this.value = value;
        }
    }

    private Node head;
    private Node tail;
    private int size;
    private final OperationMetrics metrics;

    public MyLinkedList(OperationMetrics metrics) {
        this.metrics = metrics;
        this.size = 0;
    }

    public void add(int element) {
        Node newNode = new Node(element);
        metrics.moves++;
        if (head == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
            metrics.moves++;
        }
        size++;
    }

    private Node getNode(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node curr;
        if (index < size / 2) {
            curr = head;
            for (int i = 0; i < index; i++) {
                metrics.steps++;
                curr = curr.next;
            }
        } else {
            curr = tail;
            for (int i = size - 1; i > index; i--) {
                metrics.steps++;
                curr = curr.prev;
            }
        }
        metrics.steps++;
        return curr;
    }

    public void add(int index, int element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        if (index == size) {
            add(element);
            return;
        }
        Node newNode = new Node(element);
        if (index == 0) {
            newNode.next = head;
            if (head != null) {
                head.prev = newNode;
            }
            head = newNode;
            if (tail == null) {
                tail = head;
            }
            metrics.moves += 2;
        } else {
            Node target = getNode(index);
            newNode.prev = target.prev;
            newNode.next = target;
            target.prev.next = newNode;
            target.prev = newNode;
            metrics.moves += 3;
        }
        size++;
    }

    public int remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node target = getNode(index);

        if (target.prev != null) {
            target.prev.next = target.next;
        } else {
            head = target.next;
        }

        if (target.next != null) {
            target.next.prev = target.prev;
        } else {
            tail = target.prev;
        }

        metrics.moves += 2;
        size--;
        return target.value;
    }

    public int get(int index) {
        return getNode(index).value;
    }

    public boolean contains(int x) {
        Node curr = head;
        while (curr != null) {
            metrics.steps++;
            metrics.comparisons++;
            if (curr.value == x) {
                return true;
            }
            curr = curr.next;
        }
        return false;
    }

    public int size() {
        return size;
    }
}