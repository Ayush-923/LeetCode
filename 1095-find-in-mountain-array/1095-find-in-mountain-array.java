class Solution {
    public int findInMountainArray(int target, MountainArray mountainArr) {
        int length = mountainArr.length();

        int low = 1;
        int high = length - 2;
        while (low != high) {
            int testIndex = (low + high) >> 1;
            int curr = mountainArr.get(testIndex);
            int next = mountainArr.get(testIndex + 1);
            
            if (curr < next) {
                if (curr == target) {
                    return testIndex;
                }
                if (next == target) {
                    return testIndex + 1;
                }
                low = testIndex + 1;
            } else {
                high = testIndex;
            }
        }
        int peakIndex = low;

        low = 0;
        high = peakIndex;
        while (low <= high) {
            int testIndex = (low + high) >> 1;
            int curr = mountainArr.get(testIndex);
                
            if (curr == target) {
                return testIndex;
            } else if (curr < target) {
                low = testIndex + 1;
            } else {
                high = testIndex - 1;
            }
        }

        low = peakIndex + 1;
        high = length - 1;
        while (low <= high) {
            int testIndex = (low + high) >> 1;
            int curr = mountainArr.get(testIndex);
                
            if (curr == target) {
                return testIndex;
            } else if (curr > target) {
                low = testIndex + 1;
            } else {
                high = testIndex - 1;
            }
        }

        return -1;
    }
}