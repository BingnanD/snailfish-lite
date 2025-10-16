package me.duanbn.snailfish.util.collection;

import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * 深度优先遍历器
 * 
 * @author shanwei
 *
 */
public class DFSIterator implements Iterator<TreeNode<?>> {

	private Stack<TreeNode<?>> treeNodeStack;

	public DFSIterator(TreeNode<?> root) {
		assert root != null;

		this.treeNodeStack = new Stack<TreeNode<?>>();
		List<TreeNode<?>> children = root.getChildren();
		for (int i = children.size() - 1; i >= 0; i--) {
			treeNodeStack.push(children.get(i));
		}
	}

	@Override
	public boolean hasNext() {
		return !this.treeNodeStack.isEmpty();
	}

	@Override
	public TreeNode<?> next() {
		TreeNode<?> treeNode = this.treeNodeStack.pop();

		if (treeNode != null) {
			List<TreeNode<?>> children = treeNode.getChildren();
			for (int i = children.size() - 1; i >= 0; i--) {
				treeNodeStack.push(children.get(i));
			}
		}

		return treeNode;
	}

}
