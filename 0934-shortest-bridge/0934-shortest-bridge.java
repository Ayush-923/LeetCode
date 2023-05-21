class Solution {
    public int shortestBridge(int[][] grid) {
        int row = grid.length;
        if(row == 0) return 0;
        int col = grid[0].length;

        Queue<int[]> q = new ArrayDeque<>();
        boolean isFlip = false;
        for(int i=0;i<row;i++) {
            if(isFlip) break;
            for(int j=0;j<col;j++) {
                if(grid[i][j] == 1) {
                    isFlip = true;
                    dfs(grid, i, j, q);
                    break;
                }
            }
        }

        int level = 0;
        int[] dir = new int[]{0,1,0,-1,0};
        while(!q.isEmpty()) {
            level++;
            int size = q.size();
            while(size > 0) {
                int[] cur = q.poll();
                int r = cur[0];
                int c = cur[1];
                for(int d = 0;d<4;d++) {
                    int dr = r + dir[d];
                    int dc = c + dir[d+1];
                    if(dr >= row || dr < 0 || dc >= col || dc < 0) continue;
                    if(grid[dr][dc] == 1) return level;
                    if(grid[dr][dc] == 2) continue;
                    q.offer(new int[]{dr,dc});
                    grid[dr][dc] = 2;
                }
                size--;
            }
        }
        return level;

        
    }

    public void dfs(int[][] grid, int i, int j, Queue<int[]> q) {
        if(i >= grid.length || i < 0 || j >= grid[0].length || j < 0) return;
        if(grid[i][j] == 2) return;
        
        
        if(grid[i][j] == 0){
            grid[i][j] = 2;
            q.add(new int[]{i,j});
        } else {
            grid[i][j] = 2;
            dfs(grid, i+1,j,q);
            dfs(grid, i,j-1,q);
            dfs(grid, i-1,j,q);
            dfs(grid, i,j+1,q);
        }
    }
}