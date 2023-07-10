class Solution {
    public int[][] merge(int[][] intervals) {
        Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));

        ArrayList<int[]> list = new ArrayList<>();

        for(int[] currInterval : intervals)
        {
            if(list.size() == 0)
            {
                list.add(currInterval);
            }
            else 
            {
                int prevInterval[] = list.get(list.size()-1);
                
                if(currInterval[0] <= prevInterval[1])
                {
                    prevInterval[1] = Math.max(currInterval[1], prevInterval[1]);
                }
                else
                {
                    list.add(currInterval);
                }
            }
        }
        int res[][] = new int[list.size()][];
        return list.toArray(res);
    }
}