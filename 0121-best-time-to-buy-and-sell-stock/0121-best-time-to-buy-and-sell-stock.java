class Solution {
    public int maxProfit(int[] prices) {
        int maxProfit = 0;
        int smallest = Integer.MAX_VALUE;
        
        for(int i=0; i<prices.length; i++)
        {
            smallest = Math.min(smallest, prices[i]);
            maxProfit = Math.max(maxProfit, prices[i] - smallest);
        }
        return maxProfit;
    }
}