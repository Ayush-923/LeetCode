class Solution {
    public int longestSubarray(int[] nums) {
        int left = 0;
        int zero = 0;
        int maxlen = 0;
        int n = nums.length;

        for(int right = 0; right <n; right++)
        {
            if(nums[right] == 0) {
                zero++;
            }
            while(zero > 1) {
                if(nums[left] == 0) {
                    zero--;
                }
                left++;
            }
            maxlen = Math.max(maxlen, right-left);
        }
        return maxlen;
    }
}