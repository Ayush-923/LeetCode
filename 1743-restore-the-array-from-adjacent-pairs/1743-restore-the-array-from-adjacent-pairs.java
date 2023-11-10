class Solution {
    public int[] restoreArray(int[][] adjacentPairs) {
        HashMap<Integer, int[]> map = new HashMap<>();

        for(int out[] : adjacentPairs){
            if(map.containsKey(out[0])){
                map.get(out[0])[1] = out[1];
            }
            else{
                map.put(out[0], new int[]{out[1], Integer.MIN_VALUE});
            }

            if(map.containsKey(out[1])){
                map.get(out[1])[1] = out[0];
            }
            else{
                map.put(out[1], new int[]{out[0], Integer.MIN_VALUE});
            }
        }


        int[] output = new int[adjacentPairs.length + 1];
        int start = Integer.MIN_VALUE;
        for(Map.Entry<Integer, int[]> entr : map.entrySet()){
            if(entr.getValue()[1] == Integer.MIN_VALUE){
                start = entr.getKey();
                break;
            }
        }

        int prev = Integer.MIN_VALUE;
        output[0] = start;
        for(int i = 1; i < output.length ; i++){
            
            int[] out = map.get(start);
            if(prev == out[0]){
                start = out[1];
            }
            else{
                start = out[0];
            }
            prev = output[i-1];
            output[i] = start;
        }
        return output;

    }

}