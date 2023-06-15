/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode() {}
 *     TreeNode(int val) { this.val = val; }
 *     TreeNode(int val, TreeNode left, TreeNode right) {
 *         this.val = val;
 *         this.left = left;
 *         this.right = right;
 *     }
 * }
 */
class Solution {
    public int maxLevelSum(TreeNode root) {
        List<Integer> NodeSumlvl = new ArrayList<>();
        dfs(root, 0, NodeSumlvl);
        
        int maxSum = Integer.MIN_VALUE;
        int ans=0;
        
        for(int i=0; i<NodeSumlvl.size(); i++)
        {
            if(maxSum < NodeSumlvl.get(i))
            {
                maxSum = NodeSumlvl.get(i);
                ans = i+1;
            }
        }
        
        return ans;
    }
    
    public void dfs(TreeNode node, int lvl, List<Integer> NodeSumlvl)
    {
        if(node==null) {
            return;
        }
        
        if(NodeSumlvl.size() == lvl)
        {
            NodeSumlvl.add(node.val);
        }
        else
        {
            NodeSumlvl.set(lvl, NodeSumlvl.get(lvl)+node.val);
        }
        
        dfs(node.left, lvl+1, NodeSumlvl);
        dfs(node.right, lvl+1, NodeSumlvl);
    }
}