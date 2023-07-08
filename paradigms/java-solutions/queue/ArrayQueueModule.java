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
public class ArrayQueueModule {
    private static int head;
    private static int tail;
    private static Object[] elements = new Object[5];

    // Pre: element != null
    // Post: size' = size + 1 &&
    //       a'[tail] = element &&
    //       immutable(head, tail)
    public static void enqueue(Object element) {
        Objects.requireNonNull(element);

        ensureCapacity(size() + 1);
        elements[tail] = element;
        tail = (tail + 1) % elements.length;
    }

    // Pre: true
    // Post: size' = size
    private static void ensureCapacity(int capacity) {
        if (capacity > elements.length - 1) {
            if (head < tail)
                elements = Arrays.copyOf(elements, 2 * capacity);
            else {
                Object[] newArray = new Object[2 * capacity];
                System.arraycopy(elements, 0, newArray, 0, tail + 1);
                System.arraycopy(elements, head, newArray, newArray.length - elements.length + head,
                        elements.length - head);

                head = newArray.length - elements.length + head;
                elements = newArray;
            }
        }
    }

    // Pre: size > 0
    // Post: R = a[head] &&
    //       size' = size - 1 &&
    //       head' = (head + 1) % n &&
    //       immutable(head', tail)
    public static Object dequeue() {
        assert size() > 0;

        Object element = elements[head];
        head = (head + 1) % elements.length;

//        for (Object o : elements) System.out.print(o + " ");
//        System.out.print("\n");
//        System.out.println(head + " " + tail);

        return element;
    }

    // Pre: true
    // Post: R = size &&
    //       size' = size &&
    //       immutable(head, tail)
    public static int size() {
        if (head > tail)
            return elements.length - head + tail;
        else
            return tail - head;
    }

    // Pre: true
    // Post: R = (size = 0) &&
    //       size' = size &&
    //       immutable(head, tail)
    public static boolean isEmpty() {
        return head == tail;
    }

    // Pre: true
    // Post: size' = 0
    //       head' = tail' = 0
    public static void clear() {
        head = tail = 0;
    }

    // Pre: size > 0
    // Post: size' = size
    //       R = a[head]
    //       immutable(head, tail)
    public static Object element() {
        assert size() > 0;

        return elements[head];
    }

    // Pre: true
    // Post: size' = size
    //       R = '[' a[head] ', ' ... ', ' a[tail - 1] ']'
    //       immutable(head, tail)
    public static String toStr() {
        if (head == tail)
            return "[]";

        StringBuilder sb = new StringBuilder();
        sb.append(elements[head]);
        for (int i = 1; i < size(); i++) {
            sb.append(", ").append(elements[(head + i) % elements.length]);
        }
        return "[" + sb + "]";
    }

    public static Object[] toArray() {
        if (size() == 0) {
            return new Object[0];
        }
        Object[] result = new Object[size()];

        if (tail > head) System.arraycopy(elements, head, result, 0, tail - head);
        else {
            System.arraycopy(elements, head, result, 0, elements.length - head);
            System.arraycopy(elements, 0, result, elements.length - head, tail);
        }
        return result;
    }
}
