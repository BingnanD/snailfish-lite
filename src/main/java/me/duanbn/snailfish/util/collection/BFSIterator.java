package me.duanbn.snailfish.util.collection;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 广度优先遍历器
 * 
 * @author shanwei
 *
 */
public class BFSIterator implements Iterator<TreeNode<?>> {

	private Queue<TreeNode<?>> treeNodeQ;

	public BFSIterator(TreeNode<?> root) {
		assert root != null;

		this.treeNodeQ = new LinkedList<TreeNode<?>>();
		List<TreeNode<?>> children = root.getChildren();
		for (TreeNode<?> treeNode : children) {
			this.treeNodeQ.offer(treeNode);
		}
	}

	@Override
	public boolean hasNext() {
		return !this.treeNodeQ.isEmpty();
	}

	@Override
	public TreeNode<?> next() {
		TreeNode<?> treeNode = this.treeNodeQ.poll();

		if (treeNode != null) {
			for (TreeNode<?> treeNode2 : treeNode.getChildren()) {
				this.treeNodeQ.offer(treeNode2);
			}
		}

		return treeNode;
	}

}
