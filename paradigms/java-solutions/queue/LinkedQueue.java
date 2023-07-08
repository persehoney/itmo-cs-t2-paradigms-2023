package queue;

public class LinkedQueue extends AbstractQueue {
    private Node head;
    private Node tail;

    protected void enqueueImpl(Object element) {
        Node node = new Node(element, null);
        if (isEmpty())
            head = tail = node;
        else {
            tail.setNext(node);
            tail = node;
        }
    }

    public Object dequeueImpl() {
        Object element = head.value;
        head = head.next;
        return element;
    }

    public void clearImpl() {
        head = tail = null;
    }

    public Object element() {
        assert size > 0;
        return head.value;
    }

    public int indexOf(Object key) {
        Node element = head;
        int i = 0;
        while (element != null) {
            if (element.value.equals(key)) {
                return i;
            }
            element = element.next;
            i++;
        }
        return -1;
    }

    public int lastIndexOf(Object key) {
        int result = -1;
        Node element = head;
        int i = 0;
        while (element != null) {
            if (element.value.equals(key)) {
                result = i;
            }
            element = element.next;
            i++;
        }
        return result;
    }

    private class Node {
        private final Object value;
        private Node next;

        public Node(Object value, Node next) {
            assert value != null;

            this.value = value;
            this.next = next;
        }

        void setNext(Node node) {
            next = node;
        }
    }
}
