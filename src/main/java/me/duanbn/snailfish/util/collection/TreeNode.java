package me.duanbn.snailfish.util.collection;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author shanwei
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TreeNode<T> {

	/** 全局唯一的名称 */
	@Getter
	private String name;

	/** 父节点 */
	@Setter
	@Getter
	private TreeNode<?> parent;
	/** 子节点 */
	@Setter
	@Getter
	private List<TreeNode<?>> children;
	/** 节点值 */
	@Setter
	@Getter
	private T value;

	public TreeNode<?> addChildren(TreeNode<?> treeNode) {
		treeNode.setParent(this);
		this.children.add(treeNode);
		return this;
	}

	public TreeNode(String name) {
		this(name, null);
	}

	public TreeNode(String name, T value) {
		this.name = name;
		this.value = value;
		this.children = Lists.newArrayList();
	}

	public boolean isRoot() {
		return this.parent == null;
	}

	public boolean isLeaf() {
		return this.children.isEmpty();
	}

}
