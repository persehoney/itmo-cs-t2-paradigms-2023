package search;

public class BinarySearchShift {
    // Post pred for main
    public static void main(String[] args) {
        int[] a = new int[args.length];
        for (int i = 0; i < args.length; i++) {
            a[i] = Integer.parseInt(args[i]);
        }
        BinarySearchShift binarySearchShift = new BinarySearchShift();
        System.out.println(binarySearchShift.iterativeSearch(a));
    }

    //
    // Pre: forall i != k -> a[i] > a[i + 1]
    // Post: R = k: a[k] < a[k + 1]
    // Inv: n >= 0 && left >= 0 && right >= -1
    public int iterativeSearch(int[] a) {
        int left = 0;
        int right = a.length - 1;
        while (left < right) {
            int mid = (left + right) / 2;
            // mid = left + (right - left) / 2
            // left >= 0 && right >= 0 -> mid >= 0
            if (a[mid] < a[mid + 1]) {
                // R == mid + 1
                return mid + 1;
            } else {
                if (a[left] < a[mid])
                    // R < mid
                    right = mid;
                    // mid >= 0 ->
                    // right >= 0
                else {
                    if (mid == a.length - 2)
                        // R = 0
                        return 0;
                    // R > mid
                    left = mid;
                    // mid >= 0 ->
                    // left >= 0
                }
            }
        }
        // left > right
        // n = 0
        // R = 0
        return 0;
    }
}