class Solution {
    public boolean search(int[] nums, int target) {
        Arrays.sort(nums);
        int l=0;
        int h=nums.length-1;
        while(l<=h)
        {
            int mid = l + (h-l)/2;
            if(nums[mid] == target)
            {
                return true;
            }
            else if(nums[mid] > target)
            {
                h = mid-1;
            }
            else
            {
                l = mid+1;
            }
        }
        return false;
    }
}