class Solution {

    public int maxConsecutiveAnswers(String answerKey, int k) {

        int result = 0;
        int maxFreq = 0;
        int[] counts = new int[128];
        for(int i = 0; i < answerKey.length(); i++) {
            maxFreq = Math.max(maxFreq, ++counts[answerKey.charAt(i)]);
            if(result - maxFreq < k) {
                result++;
            } else {
                counts[answerKey.charAt(i - result)]--;
            }
        }

        return result;

    }

}