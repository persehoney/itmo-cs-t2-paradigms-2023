package queue;

import java.util.Arrays;

public class ArrayQueue extends AbstractQueue {
    private int head;
    private Object[] elements = new Object[5];

    protected void enqueueImpl(Object element) {
        ensureCapacity(size + 1);
        elements[(head + size) % elements.length] = element;
    }

    private void ensureCapacity(int capacity) {
        if (capacity > elements.length - 1) {
            if (head < (head + size) % elements.length)
                elements = Arrays.copyOf(elements, 2 * capacity);
            else {
                int n = elements.length;
                head += n;
                Object[] newArray = new Object[2 * capacity];
                for (int i = 0; i < size; i++) {
                    newArray[(head + i) % (2 * n)] = elements[(head - n + i) % n];
                }
                elements = newArray;
            }
        }
    }

    public Object dequeueImpl() {
        Object element = elements[head];
        head = (head + 1) % elements.length;
        return element;
    }

    public void clearImpl() {
        head = 0;
    }

    public Object element() {
        return elements[head];
    }

    public Object[] toArray() {
        if (size == 0) {
            return new Object[0];
        }
        Object[] result = new Object[size()];

        if ((head + size) % elements.length > head)
            System.arraycopy(elements, head, result, 0, (head + size) % elements.length - head);
        else {
            System.arraycopy(elements, head, result, 0, elements.length - head);
            System.arraycopy(elements, 0, result, elements.length - head, (head + size) % elements.length);
        }
        return result;
    }

    public int indexOf(Object key) {
        int tail = (head + size) % elements.length;
        if (head <= tail) {
            for (int i = head; i < tail; i++) {
                if (elements[i].equals(key)) {
                    return i - head;
                }
            }
        } else {
            for (int i = head; i < elements.length; i++) {
                if (elements[i].equals(key)) {
                    return i - head;
                }
            }

            for (int i = 0; i < tail; i++) {
                if (elements[i].equals(key)) {
                    return i + elements.length - head;
                }
            }
        }
        return -1;
    }

    public int lastIndexOf(Object key) {
        int tail = (head + size) % elements.length;
        int result = -1;
        if (head <= tail) {
            for (int i = head; i < tail; i++) {
                if (elements[i].equals(key)) {
                    result = i - head;
                }
            }
        } else {
            for (int i = head; i < elements.length; i++) {
                if (elements[i].equals(key)) {
                    result = i - head;
                }
            }

            for (int i = 0; i < tail; i++) {
                if (elements[i].equals(key)) {
                    result = i + elements.length - head;
                }
            }
        }
        return result;
    }
}
