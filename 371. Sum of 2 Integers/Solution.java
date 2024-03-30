public class Solution {
    int carry = 0;
    public String findAddBitAt(String binaryA, String binaryB, String ans, int i) {
        System.out.println("function call");
        System.out.println("ans" + ans + "binaryA: " + binaryA.charAt(i) + " binaryB: " + binaryB.charAt(i) + "|");
        if (binaryA.charAt(i) == '0' && binaryB.charAt(i) == '0') {
            if (carry == 1) {
                ans = "1" + ans;
            } else
                ans = "0" + ans;
            carry = 0;
        } else if ((binaryA.charAt(i) == '1' && binaryB.charAt(i) == '0')
                || (binaryA.charAt(i) == '0' && binaryB.charAt(i) == '1')) {
            if (carry == 1) {
                ans = "0" + ans;
                carry = 1;
            } else
                ans = "1" + ans;
        } else {
            if (carry == 1) {
                ans = "1" + ans;
            } else
                ans = "0" + ans;
            carry = 1;
        }

        return ans;
    }

    public int getSum(int a, int b) {
        String binaryA = Integer.toBinaryString(a);
        String binaryB = Integer.toBinaryString(b);
        System.out.println("binaryA: " + binaryA + " binaryB: " + binaryB);
        int maxlen = Math.max(binaryA.length(), binaryB.length());
        String maxlen_c = Integer.toString(maxlen);
        String format = "%" + maxlen_c + "s";
        System.out.println(maxlen_c);
        binaryA = String.format(format, binaryA).replaceAll(" ", "0");
        binaryB = String.format(format, binaryB).replaceAll(" ", "0");
        // String.format("%32s", binaryB).replaceAll(" ", "0");
        // System.out.println(binaryA);
        String ans = "";

        for (int i = maxlen - 1; i >= 0; i--) {
            System.out.println("ans" + ans + "binaryA: " + binaryA.charAt(i) + " binaryB: " + binaryB.charAt(i) + "|");
            ans = findAddBitAt(binaryA, binaryB, ans, i);
        }
        if (carry == 1)
            ans = "1" + ans;
        
        int result = Integer.parseInt(ans,2); 
        System.out.println(result);
            
        return -1;
    }

    public static void main(String[] args) {
        Solution s = new Solution();
        s.getSum(-1, 1);
    }
}