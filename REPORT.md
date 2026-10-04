# Data Structures Performance Analysis & Empirical Verification

## 1. Complexity Table

The table below summarizes the theoretical Time and Space Complexity for each data structure and operation evaluated in this benchmark.

| Data Structure | Operation | Best Case | Average Case | Worst Case | Space Complexity |
| :--- | :--- | :---: | :---: | :---: | :---: |
| **DynamicArray** | `get(index)` | $O(1)$ | $O(1)$ | $O(1)$ | $O(n)$ |
| **MyLinkedList** | `get(index)` | $O(1)$ | $O(n)$ | $O(n)$ | $O(n)$ |
| **DynamicArray** | `contains(value)` | $O(1)$ | $O(n)$ | $O(n)$ | $O(n)$ |
| **MyLinkedList** | `contains(value)` | $O(1)$ | $O(n)$ | $O(n)$ | $O(n)$ |
| **DynamicArray** | `addHead` / `removeHead` | $O(n)$ | $O(n)$ | $O(n)$ | $O(n)$ |
| **MyLinkedList** | `addHead` / `removeHead` | $O(1)$ | $O(1)$ | $O(1)$ | $O(n)$ |
| **DynamicArray** | `addMiddle` / `removeMiddle` | $O(n)$ | $O(n)$ | $O(n)$ | $O(n)$ |
| **MyLinkedList** | `addMiddle` / `removeMiddle` | $O(1)^*$ | $O(n)$ | $O(n)$ | $O(n)$ |
| **MinHeap** | `insert` / `deleteMin` | $O(1)$ | $O(\log n)$ | $O(\log n)$ | $O(n)$ |

*\*Note: Insertion/deletion at a known node in LinkedList is $O(1)$, but finding the middle node requires $O(n)$ traversal.*

---

## 2. Theoretical Complexity Proofs

### 2.1 DynamicArray Random Access — $O(1)$
* **Proof:** Memory in a contiguous array is allocated sequentially in a contiguous block. The memory address of the $i$-th element is computed directly using the formula:
  $$\text{Address}(i) = \text{BaseAddress} + i \times \text{ElementSize}$$
  Since this calculation involves only basic arithmetic operations ($1$ multiplication and $1$ addition), index-based retrieval takes constant time **$O(1)$**.

### 2.2 LinkedList Random Access — $O(n)$
* **Proof:** Nodes in a linked list are non-contiguous in memory and stored via references/pointers. Accessing the $i$-th element requires traversing the list starting from the `head` node through $i$ reference pointers (`head.next.next...`). In the worst/average case, traversing $n$ nodes requires $n/2$ steps, yielding a linear time complexity of **$O(n)$**.

### 2.3 Insertion / Deletion at Head — $O(n)$ vs $O(1)$
* **DynamicArray ($O(n)$):** Inserting or removing an element at index `0` requires shifting all $n-1$ remaining elements by one position to maintain index continuity. Thus, the operation performs $n$ memory moves:
  $$\sum_{i=1}^{n-1} 1 = n - 1 \implies O(n)$$
* **MyLinkedList ($O(1)$):** Inserting at the head only requires instantiating a new node, setting its `next` pointer to the current `head`, and updating the `head` pointer. No existing nodes are shifted or modified, taking **$O(1)$** operations.

### 2.4 Binary Heap Operations — $O(\log n)$
* **Proof:** A complete binary heap of $n$ elements has a tree height bounded by $h = \lfloor \log_2 n \rfloor$. During `insert` (`siftUp`) or `deleteMin` (`siftDown`), an element moves along a single path from the root to a leaf (or vice versa). The maximum number of comparisons and swaps is bounded by the tree height $h$, resulting in **$O(\log n)$** complexity.

---

## 3. Experimental Plots and Discussion

### 3.1 W1 — Random Access (`get(index)`)
* **Plot Analysis:**
    * The step count for `DynamicArray` remains strictly constant at $\approx 10^4$ across all values of $n$, maintaining near-zero runtime ($\approx 1\text{ ms}$).
    * `MyLinkedList` shows a strict linear increase in both step count and execution time (`time_ms`) as $n$ scales from $10^2$ to $10^5$.
* **Discussion:** The experimental data directly confirms the $O(1)$ complexity of contiguous array lookup versus the $O(n)$ traversal requirement of linked lists.

---

### 3.2 W2 — Search (`contains(value)`)
* **Plot Analysis:**
    * Both structures demonstrate linear growth $O(n)$ in step count and comparisons.
    * Despite equivalent operation counts, `DynamicArray` consistently achieves lower execution time (`time_ms`) than `MyLinkedList`.
* **Discussion:** The superior performance of `DynamicArray` in execution time is attributed to **CPU Cache Locality**. Because array elements occupy contiguous memory blocks, prefetching loads multiple array elements into the CPU L1/L2 cache simultaneously. Conversely, pointer-chasing in `MyLinkedList` leads to frequent CPU cache misses.

---

### 3.3 W3 — Head and Middle Modifications
* **Plot Analysis:**
    * `DynamicArray` exhibits massive element movement metrics (`DA head - moves` and `DA middle - moves`) reaching up to $10^8$ operations at $n = 10^5$.
    * `MyLinkedList` maintains minimal element moves for head modifications (`List head - moves`), staying at $O(1)$.
* **Discussion:** The plot highlights the fundamental trade-off between array shift costs and linked list pointer modifications. Head insertions on `MyLinkedList` are significantly faster, whereas array reallocations and element copying impose severe performance overheads.

---

### 3.4 W4 — MinHeap Operations
* **Plot Analysis:**
    * Metrics for `steps`, `moves`, and `comparisons` grow proportionally on the log-log scale following $O(n \log n)$ aggregate scaling.
    * Runtime remains extremely low ($\approx 10-20\text{ ms}$ even at $n = 10^5$).
* **Discussion:** The empirical results demonstrate the high efficiency of array-backed binary heaps. Logarithmic height bounds keep total operations minimal even for large scale input sizes.

---

## 4. Empirical Anomalies & Environmental Noise
* **Flips/Noise at Small $n$ ($10^2 - 10^3$):** Slight fluctuations in `time_ms` for small dataset sizes (e.g., minor non-monotonic time drops) are caused by hardware and system-level noise, including JVM Just-In-Time (JIT) compilation warmup, Garbage Collection (GC) pauses, and thread scheduling by the host Operating System.
* **Operation Count Reliability:** While `time_ms` is susceptible to environmental hardware noise, theoretical step metrics (`steps`, `comparisons`, `moves`) remain perfectly deterministic and align strictly with Big-O predictions.

---

## 5. Conclusion
1. **DynamicArray** is optimal for read-heavy workloads where fast random access $O(1)$ and iteration speed via cache locality are required.
2. **MyLinkedList** is preferred for workloads requiring frequent prepend/head modifications ($O(1)$) where array shifting would introduce high overhead.
3. **MinHeap** effectively provides $O(\log n)$ priority updates and $O(1)$ minimum element inspection, making it the ideal choice for priority queue structures.