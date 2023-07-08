package queue;

import java.util.HashMap;
import java.util.Objects;

public abstract class AbstractQueue implements Queue {
    protected int size = 0;
    protected HashMap<Object, Integer> map = new HashMap<>();

    public void enqueue(Object element) {
        Objects.requireNonNull(element);

        if (!map.containsKey(element))
            map.put(element, 1);
        else {
            int value = map.remove(element);
            map.put(element, value + 1);
        }

        enqueueImpl(element);
        size++;
    }

    protected abstract void enqueueImpl(Object element);

    public Object dequeue() {
        assert size > 0;
        size--;
        Object element = dequeueImpl();

        int value = map.remove(element);
        map.put(element, value - 1);

        return element;
    }

    protected abstract Object dequeueImpl();

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void clear() {
        size = 0;
        clearImpl();
        map.clear();
    }

    protected abstract void clearImpl();

    public int count(Object element) {
        Objects.requireNonNull(element);
        if (map.containsKey(element)) {
            return map.get(element);
        }
        return 0;
    }
}
