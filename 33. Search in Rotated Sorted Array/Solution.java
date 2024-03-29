class Solution {
    int init = 0;
    int end;
    int mid;
    boolean first = false;
    int ans = -1;

    public void setEnd(int size) {
        end = size - 1;
    }

    public int search(int[] nums, int target) {
        if (!first) {
            setEnd(nums.length);
            first = true;
        }
        if (nums.length == 2) {
            if (nums[init] == target) {
                return init;
            } else if (nums[end] == target) {
                return end;
            }
            return -1;
        }
        if (init == end) {

            if (nums[init] == target) {
                System.out.println(nums[init]);
                ans = init;
            }
            return ans;
        }
        mid = (init + end) / 2;
        System.out.println("init: " + init + " end: " + end + " mid: " + mid);
        if (nums[mid] == target) {
            ans = mid;
            return mid;
        }
        if (nums[mid] >= nums[init]) {
            if ((nums[init] <= target && nums[mid] > target)) {
                end = mid - 1;
            } else {
                init = mid + 1;
            }
        } else {
            if ((nums[end] < target && nums[mid] < target)) {
                init = mid + 1;
            } else {
                // if ((nums.length - 1) < (mid + 1)) {
                //     return -1;
                // }
                    end = mid + 1;
            }
        }

        search(nums, target);
        return ans;
    }


    public static void main(String[] args) {
        Solution s = new Solution();
        System.out.println((s.search(new int[] {5,1,3}, 0)));
        System.out.println((s.search(new int[] {4,5,6,7,0,1,2}, 3)));
        System.out.println((s.search(new int[] {1}, 3)));
    }
}