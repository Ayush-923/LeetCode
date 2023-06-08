class Solution {
    public int countNegatives(int[][] grid) {
        
        int row = grid.length - 1; 
        int col = grid[0].length;
        int negative = 0; 

        int i = row; 
        int j = 0; 

        while(i >= 0 && j < col)
        {
            if(grid[i][j] < 0)
            {
                negative += (col-j);
                --i;
                continue;
            } 
            else
            {
                ++j;
            }
        }
        return negative; 
    }
}