public class Solution {
    // you need to treat n as an unsigned value
    public int hammingWeight(int n) {
        int count = 0;
        int loop = 32;
        while(loop!=0){
            if(n % 1 == 0){
                count++;
            }
            n = n >> 1;
            loop--;
        }
        return count;
    }
    public static void main(String[] args) {
        Solution s = new Solution();
        s.hammingWeight(00000000000000000000000000001011);
    }
}