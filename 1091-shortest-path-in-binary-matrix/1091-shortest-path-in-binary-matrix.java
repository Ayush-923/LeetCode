public class Solution {
    public int shortestPathBinaryMatrix(int[][] grid) {
        int n = grid.length;

        // Check if the starting cell or the destination cell is blocked
        if (grid[0][0] == 1 || grid[n - 1][n - 1] == 1) {
            return -1;
        }

        int[][] directions = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{0, 0});
        grid[0][0] = 1; // Mark the starting cell as visited

        int pathLength = 1;

        while (!queue.isEmpty()) {
            int size = queue.size();

            for (int i = 0; i < size; i++) {
                int[] curr = queue.poll();
                int currX = curr[0];
                int currY = curr[1];

                // Check if we have reached the destination
                if (currX == n - 1 && currY == n - 1) {
                    return pathLength;
                }

                // Explore all 8-directional neighbors
                for (int[] dir : directions) {
                    int newX = currX + dir[0];
                    int newY = currY + dir[1];

                    // Check if the neighbor is within the grid bounds and is a valid move
                    if (newX >= 0 && newX < n && newY >= 0 && newY < n && grid[newX][newY] == 0) {
                        queue.offer(new int[]{newX, newY});
                        grid[newX][newY] = 1; // Mark the neighbor as visited
                    }
                }
            }

            pathLength++;
        }

        // No clear path found
        return -1;
    }
}