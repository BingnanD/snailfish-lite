package me.duanbn.snailfish.util.collection;

import java.util.Iterator;

import lombok.Getter;

/**
 * 
 * @author shanwei
 *
 */
public class Tree {

	/** 根节点 */
	@Getter
	private TreeNode<String> root;

	private static final String ROOT_NAME = "$$root$$";

	public Tree() {
		this.root = new TreeNode<String>(ROOT_NAME);
	}

	public Tree addNode(TreeNode<?> treeNode) {
		this.root.getChildren().add(treeNode);
		return this;
	}

	public Iterator<TreeNode<?>> getBFSIterator() {
		return new BFSIterator(this.getRoot());
	}

	public Iterator<TreeNode<?>> getDFSIterator() {
		return new DFSIterator(this.getRoot());
	}

}
