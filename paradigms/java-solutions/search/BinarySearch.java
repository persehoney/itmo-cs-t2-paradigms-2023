package search;

public class BinarySearch {
    public static void main(String[] args) {
        int sum = 0;
        sum += Integer.parseInt(args[0]);
        int[] a = new int[args.length - 1];
        for (int i = 1; i < args.length; i++) {
            a[i - 1] = Integer.parseInt(args[i]);
            sum += Integer.parseInt(args[i]);
        }
        BinarySearch binarySearch = new BinarySearch();
        if (sum % 2 == 0)
            System.out.println(binarySearch.recursiveSearch(Integer.parseInt(args[0]), a, 0, a.length - 1));
        else
            System.out.println(binarySearch.iterativeSearch(Integer.parseInt(args[0]), a));
    }

    // Pre: forall i in [0, n - 1): a[i] >= a[i + 1]
    // Post: R = j: a[j] <= x && j = min ||
    //       R = n (if forall i: a[i] > x)
    // Inv: n >= 0 && left >= 0 && right >= -1
    public int iterativeSearch(int x, int[] a) {
        int left = 0;
        int right = a.length - 1;
        while (left <= right) {
            // right >= 0
            int mid = (left + right) / 2;
            // mid = left + (right - left) / 2
            // left >= 0 && right >= 0 -> mid >= 0
            if (a[mid] > x) {
                if (mid != a.length - 1 && a[mid + 1] <= x)
                    // R == mid + 1
                    return mid + 1;
                if (mid == a.length - 2 || mid == a.length - 1)
                    // forall i: a[i] > x
                    // R = n
                    return a.length;
                // R > mid
                left = mid;
                // mid >= 0 ->
                // left >= 0
            } else {
                if (mid == 0 || a[mid - 1] > x)
                    // a[mid] <= x &&
                    // a[mid - 1] > x -> mid = min ->
                    // R = mid
                    return mid;
                // R <= mid
                right = mid;
                // mid >= 0 ->
                // right >= 0
            }
        }
        // left > right
        // n = 0
        // R = 0
        return 0;
    }

    //pred: for all i: a[i] < a[i - 1] // left -? right -?
    //post: i' == i or i' == a.length (if for all j: a[j] > x)
    public int recursiveSearch(int x, int[] a, int left, int right) {
        if (left > right)
            //a is empty
            return 0;
        else {
            int mid = (left + right) / 2;
            //mid = left + (right - left) / 2

            if (a[mid] > x) {
                if (mid != a.length - 1 && a[mid + 1] <= x)
                    //i == mid + 1
                    return mid + 1;
                if (mid == a.length - 2 || mid == a.length - 1)
                    //for all j: a[j] > x
                    return a.length;
                //i: a[i] <= x && i -> min
                //i > mid
                return recursiveSearch(x, a, mid + 1, right);
            }
            else {
                if (mid == 0 || a[mid - 1] > x)
                    //i == mid
                    return mid;
                //i <= mid
                return recursiveSearch(x, a, left, mid);
            }
        }
    }
}