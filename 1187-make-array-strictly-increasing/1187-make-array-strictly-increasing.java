class Solution {
    public int makeArrayIncreasing(int[] arr1, int[] arr2) {
        Arrays.sort(arr2);
        int n = arr1.length, N = Integer.MAX_VALUE >> 1;
        int[] dp = new int[n + 1];
        Arrays.fill(dp, N);
        dp[0] = -N;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j >= 0; j--) {
                dp[j] = dp[j] < arr1[i] ? arr1[i] : N;
                if (j > 0 && dp[j - 1] < dp[j]) {
                    dp[j] = Math.min(dp[j], binarySearch(arr2, dp[j - 1]));
                }
            }
        }
        for (int i = 0; i <= n; i++) {
            if (dp[i] != N) {
                return i;
            }
        }
        return -1;
    }

    private int binarySearch(int[] arr, int num) {
        int left = 0, right = arr.length - 1;
        while (left <= right) {
            int mid = left + (right - left >> 1);
            if (arr[mid] <= num) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return left == arr.length ? Integer.MAX_VALUE >> 1 : arr[left];
    }
}