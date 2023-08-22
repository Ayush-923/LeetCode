class Solution {
    public String convertToTitle(int columnNumber) {
       
       StringBuilder sb = new StringBuilder();
       while(columnNumber>0)
       {
           columnNumber--;
           char curr = (char)(columnNumber%26 + 'A');
            columnNumber /= 26;
            sb.append(curr);
       }
       sb.reverse();
       return sb.toString();
    }
}