package queue;

// Model: a[head]..a[tail - 1]
// Inv: forall k in [head..tail - 1]:
//      a[k] != null
// Inv: head, tail, size >= 0
// Let: immutable(i, j): forall k in [i..j - 1]:
//      a'[i] = a[i]
public interface Queue {
    // Pre: element != null
    // Post: size' = size + 1 &&
    //       a'[tail] = element &&
    //       tail' = tail + 1 &&
    //       immutable(head, tail)
    void enqueue(Object element);

    // Pre: size > 0
    // Post: R = a[head] &&
    //       size' = size - 1 &&
    //       head' = head + 1 &&
    //       immutable(head', tail)
    Object dequeue();

    // Pre: true
    // Post: R = size &&
    //       size' = size &&
    //       immutable(head, tail)
    int size();

    // Pre: true
    // Post: R = (size = 0) &&
    //       size' = size &&
    //       immutable(head, tail)
    boolean isEmpty();

    // Pre: true
    // Post: size' = 0
    //       head' = tail' = 0
    void clear();

    // Pre: size > 0
    // Post: size' = size
    //       R = a[head]
    //       immutable(head, tail)
    Object element();

    // Pre: element != null
    // Let: count(e): forall k in [head..tail - 1]:
    //      if a[k] = e
    //          count++
    // Post: R = count(element)
    //       size' = size
    //       immutable(head, tail)
    int count(Object element);
}
