class Solution {
        

    public int maximumDetonation(int[][] bombs) {
        List<Set<Integer>> list = new ArrayList<>();

        for(int i=0; i< bombs.length; i++){           
            list.add(new HashSet<>());
        }

        for(int i=0; i< bombs.length; i++){
            for(int j=i+1; j<bombs.length; j++){                
                long distance2 = dis(bombs, i, j);
                if((long)bombs[i][2]*(long)bombs[i][2] >= distance2){
                    list.get(i).add(j);
                }
                if((long)bombs[j][2]*(long)bombs[j][2] >= distance2){
                    list.get(j).add(i);
                }
            }
        }
        // System.out.printf("list: %s%n", list);        
  
        int max = 0;    
        for(int i=0; i< list.size(); i++){
            max = Math.max(max, dfs(i, list, new boolean[list.size()]));
            if(max == list.size()){
                return max;
            }
        }        
        
        return max;
    }

    public int dfs(int bomb, List<Set<Integer>> list, boolean[] visited) {
        if(visited[bomb]){
            return 0;
        }
        visited[bomb] = true;
        int count = 1;

        Set<Integer> edges = list.get(bomb);
        for(int react : edges){
            count += dfs(react, list, visited);
        }        
        return count;
    }

    private long dis(int[][] bombs, int i, int j) {
        return (long)(bombs[i][0] - bombs[j][0]) * (long)(bombs[i][0] - bombs[j][0]) + (long)(bombs[i][1] - bombs[j][1]) * (long)(bombs[i][1] - bombs[j][1]);
    }
    
}