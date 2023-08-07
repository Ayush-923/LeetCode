class Solution {
    public boolean searchMatrix(int[][] matrix, int target) {
        int i = matrix.length;
        int c = matrix[0].length-1;
        int r = 0;

        while(r<i && c>=0)
        {
            int val = matrix[r][c];

            if(val == target)
            {
                return true;
            }
            else if(val < target)
            {
                r++;
            }
            else
            {
                c--;
            }
        }
        return false;
    }
}