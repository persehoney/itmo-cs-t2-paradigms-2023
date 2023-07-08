package queue;

import java.util.Arrays;
import java.util.Objects;

// Model: a[head]..a[tail - 1] || a[0]..a[tail - 1] && a[head]..a[n - 1]
// Inv: forall k:
//      (k in head..tail - 1) || (k in 0..tail - 1 && head..n - 1):
//      a[k] != null
// Inv: head, tail in 0..n - 1
// Inv: size = tail - head || size = n - head + tail
// Let: immutable(i, j): forall k:
//      (k in i..j - 1) || (k in 0..j - 1 && i..n - 1):
//      a'[i] = a[i]
public class ArrayQueueADT {
    private int head;
    private int tail;
    private Object[] elements = new Object[5];

    public ArrayQueueADT() {
    }

    // Pre: queue != null && element != null
    // Post: size' = size + 1 &&
    //       a'[tail] = element &&
    //       immutable(head, tail)
    public static void enqueue(ArrayQueueADT queue, Object element) {
        Objects.requireNonNull(element);

        ensureCapacity(queue, size(queue) + 1);
        queue.elements[queue.tail] = element;
        queue.tail = (queue.tail + 1) % queue.elements.length;
    }

    // Pre: true
    // Post: size' = size
    private static void ensureCapacity(ArrayQueueADT queue, int capacity) {
        if (capacity > queue.elements.length - 1) {
            if (queue.head < queue.tail)
                queue.elements = Arrays.copyOf(queue.elements, 2 * capacity);
            else {
                Object[] newArray = new Object[2 * capacity];
                System.arraycopy(queue.elements, 0, newArray, 0, queue.tail + 1);
                System.arraycopy(queue.elements, queue.head, newArray, newArray.length - queue.elements.length
                        + queue.head, queue.elements.length - queue.head);

                queue.head = newArray.length - queue.elements.length + queue.head;
                queue.elements = newArray;
            }
        }
    }

    // Pre: queue != null && size > 0
    // Post: R = a[head] &&
    //       size' = size - 1 &&
    //       head' = (head + 1) % n &&
    //       immutable(head', tail)
    public static Object dequeue(ArrayQueueADT queue) {
        assert size(queue) > 0;

        Object element = queue.elements[queue.head];
        queue.head = (queue.head + 1) % queue.elements.length;

        return element;
    }

    // Pre: queue != null
    // Post: R = size &&
    //       size' = size &&
    //       immutable(head, tail)
    public static int size(ArrayQueueADT queue) {
        if (queue.head > queue.tail)
            return queue.elements.length - queue.head + queue.tail;
        else
            return queue.tail - queue.head;
    }

    // Pre: queue != null
    // Post: R = (size = 0) &&
    //       size' = size &&
    //       immutable(head, tail)
    public static boolean isEmpty(ArrayQueueADT queue) {
        return queue.head == queue.tail;
    }

    // Pre: queue != null
    // Post: size' = 0
    //       head' = tail' = 0
    public static void clear(ArrayQueueADT queue) {
        queue.head = queue.tail = 0;
    }

    // Pre: queue != null && size > 0
    // Post: size' = size
    //       R = a[head]
    //       immutable(head, tail)
    public static Object element(ArrayQueueADT queue) {
        return queue.elements[queue.head];
    }

    // Pre: true
    // Post: size' = size
    //       R = '[' a[head] ', ' ... ', ' a[tail - 1] ']'
    //       immutable(head, tail)
    public static String toStr(ArrayQueueADT queue) {
        if (queue.head == queue.tail) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(queue.elements[queue.head]);
        for (int i = 1; i < size(queue); i++) {
            sb.append(", ").append(queue.elements[(queue.head + i) % queue.elements.length]);
        }
        return "[" + sb + "]";
    }

    public static Object[] toArray(ArrayQueueADT queue) {
        if (size(queue) == 0) {
            return new Object[0];
        }
        Object[] result = new Object[size(queue)];

        if (queue.tail > queue.head) System.arraycopy(queue.elements, queue.head, result, 0, queue.tail - queue.head);
        else {
            System.arraycopy(queue.elements, queue.head, result, 0, queue.elements.length - queue.head);
            System.arraycopy(queue.elements, 0, result, queue.elements.length - queue.head, queue.tail);
        }
        return result;
    }
}
