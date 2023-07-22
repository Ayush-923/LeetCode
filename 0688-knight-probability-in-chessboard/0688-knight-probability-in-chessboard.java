class Solution {
    private static final int[][] moves = new int[][]
    {{-2,-1}, {-1,-2}, {1,-2}, {2,-1}, {2,1}, {1,2}, {-1,2}, {-2, 1}};
    
public double knightProbability(int n, int k, int row, int column) {
    if(k == 0) return 1;
    final double[][][] dp = new double[n + 1][n + 1][k + 1];
    for(double[][] e1: dp) {
        for(double[] e2: e1) {
            Arrays.fill(e2, -1);
        }
    }
    return dfs(n, k, row, column, dp);
}

private double dfs(int n, int k, int i, int j, double[][][] dp) {
    if(i < 0 || j < 0 || i >= n || j >= n) return 0;
    if(k == 0) return 1;
    if(dp[i][j][k] >= 0) return dp[i][j][k];
    double result = 0;
    for(int[] e: moves) {
        result += 0.125 * dfs(n, k - 1, i + e[0], j + e[1], dp);
    }
    return dp[i][j][k] = result;
}
}