class Solution {
    public int minSubArrayLen(int target, int[] nums) {
        int left = 0;
        int cWindow = 0;
        int res = Integer.MAX_VALUE;
        
        for(int right=0; right<nums.length; right++)
        {
            cWindow += nums[right];
            
            while(cWindow >= target)
            {
                res = Math.min(res, right-left+1);
                cWindow -= nums[left];
                left++;
            }
        }
        
        return(res != Integer.MAX_VALUE) ? res : 0;
        
    }
}