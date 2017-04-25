package structures;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class BinarySearchTree<T extends Comparable<T>> implements
		BSTInterface<T> {
	protected BSTNode<T> root;

	public boolean isEmpty() {
		return root == null;
	}

	public int size() {
		return subtreeSize(root);
	}

	protected int subtreeSize(BSTNode<T> node) {
		if (node == null) {
			return 0;
		} else {
			return 1 + subtreeSize(node.getLeft())
					+ subtreeSize(node.getRight());
		}
	}

	public boolean contains(T t) {
		if(t == null) {
			throw new NullPointerException();
		}
		if(get(t) == null) {
			return false;
		}
		return true;
	}

	public boolean remove(T t) {
		if (t == null) {
			throw new NullPointerException();
		}
		boolean result = contains(t);
		if (result) {
			root = removeFromSubtree(root, t);
		}
		return result;
	}

	private BSTNode<T> removeFromSubtree(BSTNode<T> node, T t) {
		// node must not be null
		int result = t.compareTo(node.getData());
		if (result < 0) {
			node.setLeft(removeFromSubtree(node.getLeft(), t));
			return node;
		} else if (result > 0) {
			node.setRight(removeFromSubtree(node.getRight(), t));
			return node;
		} else { // result == 0
			if (node.getLeft() == null) {
				return node.getRight();
			} else if (node.getRight() == null) {
				return node.getLeft();
			} else { // neither child is null
				T predecessorValue = getHighestValue(node.getLeft());
				node.setLeft(removeRightmost(node.getLeft()));
				node.setData(predecessorValue);
				return node;
			}
		}
	}

	private T getHighestValue(BSTNode<T> node) {
		// node must not be null
		if (node.getRight() == null) {
			return node.getData();
		} else {
			return getHighestValue(node.getRight());
		}
	}

	private BSTNode<T> removeRightmost(BSTNode<T> node) {
		// node must not be null
		if (node.getRight() == null) {
			return node.getLeft();
		} else {
			node.setRight(removeRightmost(node.getRight()));
			return node;
		}
	}

	public T get(T t) {
		if(t == null) {
			throw new NullPointerException();
		}
		BSTNode<T> node = root;
		while(node != null) {
			if(node.getData().equals(t)) {
				return node.getData();
			}
			else if(node.getData().compareTo(t) > 0) {
				node = node.getLeft();
			}
			else {
				node = node.getRight();
			}
		}
		return null;
	}


	public void add(T t) {
		if (t == null) {
			throw new NullPointerException();
		}
		root = addToSubtree(root, new BSTNode<T>(t, null, null));
	}

	protected BSTNode<T> addToSubtree(BSTNode<T> node, BSTNode<T> toAdd) {
		if (node == null) {
			return toAdd;
		}
		int result = toAdd.getData().compareTo(node.getData());
		if (result <= 0) {
			node.setLeft(addToSubtree(node.getLeft(), toAdd));
		} else {
			node.setRight(addToSubtree(node.getRight(), toAdd));
		}
		return node;
	}

	@Override
	public T getMinimum() {
		if(isEmpty()) {
			return null;
		}
		BSTNode<T> node = root;
		while(node.getLeft() != null) {
			node = node.getLeft();
		}
		return node.getData();
	}


	@Override
	public T getMaximum() {
		if(isEmpty()) {
			return null;
		}
		BSTNode<T> node = root;
		while(node.getRight() != null) {
			node = node.getRight();
		}
		return node.getData();
	}

	@Override
	public int height() {
		return nodeHeight(root);
	}
	
	private int nodeHeight(BSTNode<T> node) {
		if(node == null) {
			return -1;
		}
		
		int heightLeft = nodeHeight(node.getLeft());
		int heightRight = nodeHeight(node.getRight());
		
		return Math.max(heightLeft + 1, heightRight + 1);
	}

	public Iterator<T> preorderIterator() {
		Queue<T> queue = new LinkedList<T>();
		preorderTraverse(queue, root);
		return queue.iterator();
	}
	
	private void preorderTraverse(Queue<T> queue, BSTNode<T> node) {
		if (node != null) {
			queue.add(node.getData());
			preorderTraverse(queue, node.getLeft());
			preorderTraverse(queue, node.getRight());
		}
	}

	public Iterator<T> inorderIterator() {
		Queue<T> queue = new LinkedList<T>();
		inorderTraverse(queue, root);
		return queue.iterator();
	}


	protected void inorderTraverse(Queue<T> queue, BSTNode<T> node) {
		if (node != null) {
			inorderTraverse(queue, node.getLeft());
			queue.add(node.getData());
			inorderTraverse(queue, node.getRight());
		}
	}

	public Iterator<T> postorderIterator() {
		Queue<T> queue = new LinkedList<T>();
		postorderTraverse(queue, root);
		return queue.iterator();
	}
	
	private void postorderTraverse(Queue<T> queue, BSTNode<T> node) {
		if(node != null) {
			postorderTraverse(queue, node.getLeft());
			postorderTraverse(queue, node.getRight());
			queue.add(node.getData());
		}
	}

	@Override
	public boolean equals(BSTInterface<T> other) {
		if(other.size() != size() || other.height() != height()) {
			return false;
		}
		Iterator<T> one = preorderIterator();
		Iterator<T> two = other.preorderIterator();
		while(one.hasNext()) {
			if(!one.next().equals(two.next())) {
				return false;
			}
		}
		one = inorderIterator();
		two = other.inorderIterator();
		while(one.hasNext()) {
			if(!one.next().equals(two.next())) {
				return false;
			}
		}
		one = postorderIterator();
		two = other.postorderIterator();
		while(one.hasNext()) {
			if(!one.next().equals(two.next())) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean sameValues(BSTInterface<T> other) {
		if(other == null) {
			throw new NullPointerException();
		}
		Iterator<T> one = inorderIterator();
		Iterator<T> two = other.inorderIterator();
		while(one.hasNext()) {
			if(!one.next().equals(two.next())) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean isBalanced() {
		int height = height();
		int size = size();
		return (int) Math.pow(2, height) <= size && size < (int) Math.pow(2, height + 1);
	}

	@Override
    @SuppressWarnings("unchecked")

	public void balance() {
		Iterator<T> tree = inorderIterator(); 
		T[] array = (T[]) new Comparable[size()];
		int counter = 0;
		while(tree.hasNext()) {
			array[counter] = tree.next();
			counter++;
		}
		root = bHelper(array, 0, size() - 1);
	}
	
	protected BSTNode<T> bHelper(T[] array, int lower, int upper) {
		if (lower > upper) {
			return null;
		}
		int	mid = (lower + upper)/2;
		BSTNode<T> left = bHelper(array, lower, mid - 1);
		BSTNode<T> right = bHelper(array, mid + 1, upper);
		BSTNode<T> node	= new BSTNode<T>(array[mid], left, right);	
		return node;
	}


	@Override
	public BSTNode<T> getRoot() {
        // DO NOT MODIFY
		return root;
	}

	public static <T extends Comparable<T>> String toDotFormat(BSTNode<T> root) {
		// header
		int count = 0;
		String dot = "digraph G { \n";
		dot += "graph [ordering=\"out\"]; \n";
		// iterative traversal
		Queue<BSTNode<T>> queue = new LinkedList<BSTNode<T>>();
		queue.add(root);
		BSTNode<T> cursor;
		while (!queue.isEmpty()) {
			cursor = queue.remove();
			if (cursor.getLeft() != null) {
				// add edge from cursor to left child
				dot += cursor.getData().toString() + " -> "
						+ cursor.getLeft().getData().toString() + ";\n";
				queue.add(cursor.getLeft());
			} else {
				// add dummy node
				dot += "node" + count + " [shape=point];\n";
				dot += cursor.getData().toString() + " -> " + "node" + count
						+ ";\n";
				count++;
			}
			if (cursor.getRight() != null) {
				// add edge from cursor to right child
				dot += cursor.getData().toString() + " -> "
						+ cursor.getRight().getData().toString() + ";\n";
				queue.add(cursor.getRight());
			} else {
				// add dummy node
				dot += "node" + count + " [shape=point];\n";
				dot += cursor.getData().toString() + " -> " + "node" + count
						+ ";\n";
				count++;
			}

		}
		dot += "};";
		return dot;
	}

	public static void main(String[] args) {
		for (String r : new String[] { "a", "b", "c", "d", "e", "f", "g" }) {
			BSTInterface<String> tree = new BinarySearchTree<String>();
			for (String s : new String[] { "d", "b", "a", "c", "f", "e", "g" }) {
				tree.add(s);
			}
			Iterator<String> iterator = tree.inorderIterator();
			while (iterator.hasNext()) {
				System.out.print(iterator.next());
			}
			System.out.println();
			iterator = tree.preorderIterator();
			while (iterator.hasNext()) {
				System.out.print(iterator.next());
			}
			System.out.println();
			iterator = tree.postorderIterator();
			while (iterator.hasNext()) {
				System.out.print(iterator.next());
			}
			System.out.println();

			System.out.println(tree.remove(r));

			iterator = tree.inorderIterator();
			while (iterator.hasNext()) {
				System.out.print(iterator.next());
			}
			System.out.println();
		}

		BSTInterface<String> tree = new BinarySearchTree<String>();
		for (String r : new String[] { "a", "b", "c", "d", "e", "f", "g" }) {
			tree.add(r);
		}
		System.out.println(tree.size());
		System.out.println(tree.height());
		System.out.println(tree.isBalanced());
		tree.balance();
		System.out.println(tree.size());
		System.out.println(tree.height());
		System.out.println(tree.isBalanced());
	}
}