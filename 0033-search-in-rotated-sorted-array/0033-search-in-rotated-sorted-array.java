class Solution {
    public int search(int[] nums, int target) {
        
        int piviot = findPiviot(nums);

        if(piviot == -1)
        {
            return BinarySearch(nums, target, 0, nums.length-1);
        }

        if(nums[piviot] == target)
        {
            return piviot;
        }

        else if(target >= nums[0])
        {
            return BinarySearch(nums, target, 0, piviot-1);
        }
        else
        {
            return BinarySearch(nums, target, piviot+1, nums.length-1);
        }
    }

    public int BinarySearch(int[] arr, int target, int st, int end)
    {
        while(st <= end)
        {
            int mid = st + (end-st)/2;

            if(target == arr[mid])
            {
                return mid;
            }
            else if(target < arr[mid])
            {
                end = mid-1;
            }
            else
            {
                st = mid+1;
            }
        }
        return -1;
    }

    public int findPiviot(int[] arr)
    {
        int st = 0;
        int end = arr.length-1;
        while(st <= end)
        {
            int mid = st + (end-st)/2;

            if(mid < end && arr[mid] > arr[mid+1])
            {
                return mid;
            }
            if(mid > st && arr[mid] < arr[mid-1])
            {
                return mid-1;
            }
            if(arr[mid] <= arr[st])
            {
                end = mid-1;
            }
            else
            {
                st = mid+1;
            }
        }
        return -1;
    }
}