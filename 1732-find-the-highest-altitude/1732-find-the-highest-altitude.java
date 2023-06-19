class Solution {
    public int largestAltitude(int[] gain) {
        int sum = 0;
        int maxaltitude = 0;
        for(int i=0; i<gain.length; i++)
        {
            sum += gain[i]; 
            maxaltitude = Math.max(maxaltitude, sum);           
        }
        return maxaltitude;
    }
}