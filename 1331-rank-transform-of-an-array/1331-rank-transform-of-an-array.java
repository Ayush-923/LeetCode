class Solution {
    public int[] arrayRankTransform(int[] arr) {
        int temp[] = arr.clone();
        Arrays.sort(temp);

        Map<Integer, Integer> rankMap = new HashMap<>();
        int rank=1;

        for(int i : temp) {
            if(!rankMap.containsKey(i)) {
                rankMap.put(i, rank);
                rank++;
            }
        }

        for(int i=0; i<arr.length; i++) {
            arr[i] = rankMap.get(arr[i]);
        }

        return arr;
    }
}