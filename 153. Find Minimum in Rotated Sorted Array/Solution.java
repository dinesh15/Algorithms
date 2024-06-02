class Solution{
    int init = 0;
    int end;
    boolean flag = false;
    // int i =0;
    int ans=0;
    public void setEnd(int size){
            end = size - 1;
        }

    public int findMin(int[] nums) {
        if (!flag) {
            setEnd(nums.length);
            flag = true;
        }
        int mid = (init + end) / 2;
        if(init == end){
            // System.out.println(init + " " + end + " " + mid);
            // System.out.println("i am here" + nums[init]);
            if(init > mid){
                ans = nums[init];
            }
            else
                ans = nums[mid];
            // ans = nums[mid];
            return ans;
        }
        // System.out.println("Iteration:"+ (++i) + " init: " + init + " end: " + end + " mid: " + mid);
        if (nums[end] < nums[mid] &&  nums[end] < nums[init]) {
            init = mid + 1;
            findMin(nums);
        } else if (nums[init] < nums[mid] && nums[init] < nums[end]) {
            end = mid - 1;
            findMin(nums);
        } else {
            end = mid;
            findMin(nums);
        }
        return ans;
    }



    public static void main(String[] args){
        int[] arr = {3,4,5,1,2};
        Solution s = new Solution();
        System.out.println(s.findMin(arr));
    }
}