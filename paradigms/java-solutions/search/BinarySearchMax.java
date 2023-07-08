package search;

public class BinarySearchMax {
    public static void main(String[] args) {
        int[] a = new int[args.length];
        for (int i = 0; i < args.length; i++) {
            a[i] = Integer.parseInt(args[i]);
        }
        BinarySearchMax binarySearchShift = new BinarySearchMax();
        System.out.println(binarySearchShift.iterativeSearch(a));
    }

    public int iterativeSearch(int[] a) {
        int left = 0;
        int right = a.length - 1;
        while (left < right) {
            int mid = (left + right) / 2;
            if (a[mid] > a[mid + 1]) {
                return a[mid];
            } else {
                if (a[left] > a[mid])
                    right = mid;
                else {
                    if (mid == a.length - 2)
                        return a[a.length - 1];
                    left = mid;
                }
            }
        }
        return a[a.length - 1];
    }
}