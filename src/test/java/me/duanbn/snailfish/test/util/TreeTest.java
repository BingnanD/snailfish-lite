package me.duanbn.snailfish.test.util;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;
import me.duanbn.snailfish.util.collection.Tree;
import me.duanbn.snailfish.util.collection.TreeNode;

@Slf4j
public class TreeTest {

	@Test
	public void test() {
		Tree tree = new Tree();

		TreeNode<String> a = new TreeNode<String>("a", "a");
		TreeNode<String> a1 = new TreeNode<String>("a1", "a1");
		TreeNode<String> a2 = new TreeNode<String>("a2", "a2");
		TreeNode<String> a3 = new TreeNode<String>("a3", "a3");
		a.addChildren(a1).addChildren(a2).addChildren(a3);

		TreeNode<String> b = new TreeNode<String>("b", "b");
		TreeNode<String> b1 = new TreeNode<String>("b1", "b1");
		TreeNode<String> b2 = new TreeNode<String>("b2", "b2");
		TreeNode<String> b3 = new TreeNode<String>("b3", "b3");
		b.addChildren(b1).addChildren(b2).addChildren(b3);

		TreeNode<String> c = new TreeNode<String>("c", "c");
		TreeNode<String> c1 = new TreeNode<String>("c1", "c1");
		TreeNode<String> c2 = new TreeNode<String>("c2", "c2");
		TreeNode<String> c3 = new TreeNode<String>("c3", "c3");
		c.addChildren(c1).addChildren(c2).addChildren(c3);

		tree.addNode(a).addNode(b).addNode(c);

		Iterator<TreeNode<?>> bfsIterator = tree.getBFSIterator();
		while (bfsIterator.hasNext()) {
			log.info("{}", bfsIterator.next().getValue());
		}

		log.info("---------------------------------------------------------");

		Iterator<TreeNode<?>> dfsIterator = tree.getDFSIterator();
		while (dfsIterator.hasNext()) {
			log.info("{}", dfsIterator.next().getValue());
		}

	}

}
