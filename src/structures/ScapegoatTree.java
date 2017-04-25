package structures;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class ScapegoatTree<T extends Comparable<T>> extends
		BinarySearchTree<T> {
	private int upperBound;

	@Override
	public void add(T t) {
		if (t == null) {
			throw new NullPointerException();
		}
		upperBound++;
		BSTNode<T> node = new BSTNode<T>(t, null, null);
		root = addToSubtree(root, node);
		if(Math.log(upperBound)/Math.log(3.0/2.0) < height()) {
			while((double)subtreeSize(node)/(double)subtreeSize(getParent(node)) <= 2.0/3.0) {
				node = getParent(node);
			}
			node = getParent(node);
			balanceSubtree(node);
		}
	}

	@Override
	public boolean remove(T element) {
		boolean rem = super.remove(element);
		if(upperBound > 2*size()) {
			balance();
			upperBound = size();
		}
		return rem;
	}
	
	private BSTNode<T> getParent(BSTNode<T> element) {
		if(element == null) {
			throw new NullPointerException();
		}
		BSTNode<T> node = root;
		while(node != null) {
			if(node.getLeft() == element || node.getRight() == element) {
				return node;
			}
			else if(node.getData().compareTo(element.getData()) > 0) {
				node = node.getLeft();
			}
			else {
				node = node.getRight();
			}
		}
		return null;
	}
	
	private void balanceSubtree(BSTNode<T> node) {
		Queue<T> queue = new LinkedList<T>();
		inorderTraverse(queue, node);
		T[] array = (T[]) new Comparable[queue.size()];
		queue.toArray(array);
		BSTNode<T> temp = bHelper(array, 0, array.length - 1);
		node.setData(temp.getData());
		node.setLeft(temp.getLeft());
		node.setRight(temp.getRight());
	} 
}
