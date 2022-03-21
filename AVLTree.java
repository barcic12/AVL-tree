/**
* Name: Daniel Solaimani
* User: solaimani
* ID: 209082676
*
* Name: Bar Cicurel
* User: barcicurel
* ID: 308064849
*/

/**
 *
 * AVLTree
 *
 * An implementation of a AVL Tree with distinct integer keys and info
 *
 */

public class AVLTree {

	static AVLNode VIRTUAL_NODE;
	AVLNode root;
	AVLNode min;
	AVLNode max;

	// AVLTree constructor
	// complexity: O(1)
	public AVLTree() {
		VIRTUAL_NODE = new AVLNode();
		root = new AVLNode();
		min = new AVLNode();
		max = new AVLNode();
	}

	/**
	 * public boolean empty()
	 *
	 * returns true if and only if the tree is empty
	 *
	 */
	// complexity: O(1)
	public boolean empty() {
		return (this.root.getSize() == 0);
	}

	/**
	 * public String search(int k)
	 *
	 * returns the info of an item with key k if it exists in the tree otherwise,
	 * returns null
	 */
	// complexity: O(log n)
	public String search(int k) {
		IAVLNode node = this.root;
		return searchRec(node, k);
	}

	public String searchRec(IAVLNode node, int k) {
		if (node == null)
			return null;

		int key = node.getKey();

		if (key == k)
			return node.getValue();
		else if (k < key)
			return searchRec(node.getLeft(), k);
		else
			return searchRec(node.getRight(), k);
	}

	/**
	 * public int insert(int k, String i)
	 *
	 * inserts an item with key k and info i to the AVL tree. the tree must remain
	 * valid (keep its invariants). returns the number of rebalancing operations, or
	 * 0 if no rebalancing operations were necessary. promotion/rotation - counted
	 * as one rebalnce operation, double-rotation is counted as 2. returns -1 if an
	 * item with key k already exists in the tree.
	 */
	// complexity: O(log n)
	public int insert(int k, String i) {
		if (empty() == true) {
			root = new AVLNode(k, i, null);
			min = root;
			max = root;
			return 0;
		}
		AVLNode insertAfter = treePosition(k);
		if (insertAfter.key == k) {
			return -1;
		}
		AVLNode insertedNode = new AVLNode(k, i, insertAfter);
		if (k > insertAfter.key) {
			insertAfter.right = insertedNode;
		} else {
			insertAfter.left = insertedNode;
		}

		updateMaxMinAfterInsert(insertedNode);
		updateSizeAfterInsert(insertAfter);
		return insertRebalance(insertAfter);
	}

	/**
	 * returns the last node seen while looking for the a node with key that equals
	 * to k
	 */
	// complexity: O(log n)
	private AVLNode treePosition(int k) {
		AVLNode node = this.root;
		AVLNode prev = node;
		while (node.isRealNode()) {
			prev = node;
			if (node.key == k)
				return node;
			else if (k < node.key)
				node = node.left;
			else
				node = node.right;
		}
		return prev;
	}

	/**
	 * public int delete(int k)
	 *
	 * deletes an item with key k from the binary tree, if it is there; the tree
	 * must remain valid (keep its invariants). returns the number of rebalancing
	 * operations, or 0 if no rebalancing operations were needed. demotion/rotation
	 * - counted as one rebalnce operation, double-rotation is counted as 2. returns
	 * -1 if an item with key k was not found in the tree.
	 */
	// complexity: O(log n)
	public int delete(int k) {

		if (empty())
			return -1;

		AVLNode toDeleteNode = treePosition(k);
		if (toDeleteNode.getKey() != k) {
			return -1;
		}
		updateMaxMinAfterDelete(toDeleteNode);
		AVLNode afterDelete = sortDelete(toDeleteNode);
		if (afterDelete == null) {
			return 0;
		}
		updateSizeAfterDelete(afterDelete);
		return deleteRebalance(afterDelete);
	}

	/**
	 * public String min()
	 *
	 * Returns the info of the item with the smallest key in the tree, or null if
	 * the tree is empty
	 */
	// complexity: O(log n)
	public String min() {

		if (this.empty())
			return null;

		return this.min.getValue();
	}

	/**
	 * public String max()
	 *
	 * Returns the info of the item with the largest key in the tree, or null if the
	 * tree is empty
	 */
	// complexity: O(log n)
	public String max() {

		if (this.empty())
			return null;

		return this.max.getValue();
	}

	/**
	 * public int[] keysToArray()
	 *
	 * Returns a sorted array which contains all keys in the tree, or an empty array
	 * if the tree is empty.
	 */
	// complexity: O(n)
	public int[] keysToArray() {
		int[] array = new int[this.size()];
		inorderRec(this.root, array, 0);
		return array;
	}

	/**
	 * the method fills the array by using inorder walk
	 */
	// complexity: O(n)
	private int inorderRec(AVLNode root, int[] array, int location) {
		if (root.isRealNode()) {
			location = inorderRec(root.left, array, location);
			array[location++] = root.key;
			location = inorderRec(root.right, array, location);
		}
		return location;
	}

	/**
	 * public String[] infoToArray()
	 *
	 * Returns an array which contains all info in the tree, sorted by their
	 * respective keys, or an empty array if the tree is empty.
	 */
	// complexity: O(n)
	public String[] infoToArray() {
		String[] array = new String[size()];
		infoToSortedRec(this.root, array, 0);
		return array;
	}

	/**
	 * the method fills the array by using inorder walk
	 */
	// complexity: O(n)
	private int infoToSortedRec(AVLNode root, String[] array, int location) {
		if (root.isRealNode()) {
			location = infoToSortedRec(root.left, array, location);
			array[location++] = root.info;
			location = infoToSortedRec(root.right, array, location);
		}
		return location;
	}

	/**
	 * public int size()
	 *
	 * Returns the number of nodes in the tree.
	 *
	 * precondition: none
	 * 
	 * postcondition: none
	 */
	// complexity: O(1)
	public int size() {
		return this.root.getSize();
	}

	/**
	 * public IAVLNode getRoot()
	 *
	 * Returns the root AVL node, or null if the tree is empty
	 *
	 * precondition: none
	 * 
	 * postcondition: none
	 */
	// complexity: O(1)
	public IAVLNode getRoot() {
		if (this.empty() == true) {
			return null;
		}
		return this.root;
	}

	/**
	 * public AVLTree[] split(int x)
	 *
	 * splits the tree into 2 trees according to the key x. Returns an array [t1,t2]
	 * with two AVL trees. keys(t1) < x < keys(t2).
	 * 
	 * precondition: search(x) != null (i.e. you can also assume that the tree is
	 * not empty)
	 * 
	 * postcondition: none
	 */
	// complexity: O(log n)
	public AVLTree[] split(int x) {
		AVLNode placeToSplit = treePosition(x);
		AVLNode minT1;
		AVLNode maxT1;
		AVLNode minT2;
		AVLNode maxT2;
		if (placeToSplit == this.min) {
			minT1 = VIRTUAL_NODE;
			maxT1 = VIRTUAL_NODE;
			minT2 = successor(placeToSplit);
			maxT2 = this.max;
		} else if (placeToSplit == this.max) {
			minT1 = this.min;
			maxT1 = predecessor(placeToSplit);
			minT2 = VIRTUAL_NODE;
			maxT2 = VIRTUAL_NODE;
		} else {
			minT1 = this.min;
			maxT1 = predecessor(placeToSplit);
			minT2 = successor(placeToSplit);
			maxT2 = this.max;
		}
		AVLTree T1 = new AVLTree();
		AVLTree T2 = new AVLTree();
		AVLNode biggerSide = placeToSplit.right;
		AVLNode smallerSide = placeToSplit.left;
		T1.root = smallerSide;
		T2.root = biggerSide;
		biggerSide.parent = null;
		smallerSide.parent = null;
		if (placeToSplit == this.root) {
			AVLTree[] result = { T1, T2 };
			T1.min = minT1;
			T1.max = maxT1;
			T2.min = minT2;
			T2.max = maxT2;
			return result;
		}
		AVLTree[] result = splitRec(T1, T2, x, placeToSplit.parent);
		// Update Min Max
		result[0].min = minT1;
		result[0].max = maxT1;
		result[1].min = minT2;
		result[1].max = maxT2;
		return result;
	}

	/**
	 * returns an array with two trees. res[0] keys are smaller than x, res[1] keys
	 * are bigger than x.
	 */
	// complexity: O(log n)
	private AVLTree[] splitRec(AVLTree t1, AVLTree t2, int x, AVLNode node) {
		AVLNode nextNode = node.parent;
		if (node.getKey() < x) {
			AVLTree subTree = new AVLTree();
			subTree.root = node.left;
			subTree.root.parent = null;
			t1.join(node, subTree);
		} else {
			AVLTree subTree = new AVLTree();
			subTree.root = node.right;
			subTree.root.parent = null;
			t2.join(node, subTree);
		}
		if (nextNode == null) {
			AVLTree[] res = { t1, t2 };
			return res;
		} else {
			AVLTree[] res = splitRec(t1, t2, x, nextNode);
			return res;
		}
	}

	/**
	 * public int join(IAVLNode x, AVLTree t)
	 *
	 * joins t and x with the tree. Returns the complexity of the operation
	 * (|tree.rank - t.rank| + 1).
	 * 
	 * precondition: keys(x,t) < keys() or keys(x,t) > keys(). t/tree might be empty
	 * (rank = -1).
	 * 
	 * postcondition: none
	 */
	// complexity: O(log n)
	public int join(IAVLNode x, AVLTree t) {
		int delta = Math.abs(t.root.height - this.root.height) + 1;
		int key = x.getKey();
		String info = x.getValue();
		if (delta == 1) {
			if (this.root.key < t.root.key) {
				x.setHeight(this.root.getHeight() + 1);
				this.max = t.max;
				x.setLeft(this.root);
				x.setRight(t.root);
				this.root = (AVLNode) x;
				x.setParent(null);
				x.setSize(1 + x.getRight().getSize() + x.getLeft().getSize());
			} else if (this.root.key > t.root.key) {
				x.setHeight(this.root.getHeight() + 1);
				this.min = t.min;
				x.setRight(this.root);
				x.setLeft(t.root);
				this.root = (AVLNode) x;
				x.setParent(null);
				x.setSize(1 + x.getLeft().getSize() + x.getRight().getSize());
			} else {
				if (this.root.key == -1) {
					x.setParent(null);
					x.setHeight(0);
					x.setSize(1);
					x.setRight(new AVLNode());
					x.setLeft(new AVLNode());
					min = max = root = (AVLNode) x;
				}
			}
			return delta;
		} else if (empty() == true) {
			t.insert(key, info);
			this.root = t.root;
			this.min = t.min;
			this.max = t.max;
			return delta;
		} else if (t.empty() == true) {
			insert(key, info);
			return delta;
		} else if (this.root.height < t.root.height) {
			if (this.root.key < t.root.key) {
				rightJoin(this, (AVLNode) x, t);
				this.max = t.max;
			} else {
				leftJoin(t, (AVLNode) x, this);
				this.min = t.min;
			}
			this.root = t.root;
			t.root = null;
		} else {
			if (this.root.key < t.root.key) {
				leftJoin(this, (AVLNode) x, t);
				this.max = t.max;
			} else {
				rightJoin(t, (AVLNode) x, this);
				this.min = t.min;
			}
		}

		while (((AVLNode) x) != null) {
			insertRebalance((AVLNode) x);
			deleteRebalance((AVLNode) x);
			x = x.getParent();
		}
		return delta;
	}

	/**
	 * connects t1 and t2.
	 */
	// complexity: O(log n)
	private void rightJoin(AVLTree t1, AVLNode x, AVLTree t2) {
		AVLNode rootA = t1.root;
		AVLNode rootB = t2.root;
		AVLNode c = new AVLNode();
		int rightTreeRank = rootA.height;
		while (rootB.height > rightTreeRank) {
			if (rootB.left.key == -1)
				c = rootB;
			else
				c = rootB.parent;

			rootB = rootB.left;
		}
		if (rootB.isRealNode() == true)
			c = rootB.parent;
		if (c != null) {
			c.left = x;
		}
		x.parent = c;
		x.left = rootA;
		rootA.parent = x;
		x.right = rootB;
		rootB.parent = x;
		x.height = rootB.height + 1;
		x.setSize(1 + x.right.getSize() + x.left.getSize());
		sizeUpdateAfterJoin(x.parent, rootA.getSize());
	}

	/**
	 * connects t1 and t2.
	 */
	// complexity: O(log n)
	private void leftJoin(AVLTree t1, AVLNode x, AVLTree t2) {
		AVLNode rootA = t2.root;
		AVLNode rootB = t1.root;
		int rightTreeRank = rootA.height;
		AVLNode c = new AVLNode();
		while (rootB.height > rightTreeRank) {
			if (rootB.right.key == -1)
				c = rootB;
			else
				c = rootB.parent;

			rootB = rootB.right;
		}
		if (rootB.isRealNode())
			c = rootB.parent;
		if (c != null) {
			c.right = x;
		}
		x.parent = c;
		x.right = rootA;
		rootA.parent = x;
		x.left = rootB;
		rootB.parent = x;
		x.height = rootA.height + 1;
		x.setSize(1 + x.left.getSize() + x.right.getSize());
		sizeUpdateAfterJoin(x.parent, rootA.getSize());
	}

	/**
	 * updates the size of node x recursively until you get to the root that you get from join
	 */
	// complexity: O(log n)
	private void sizeUpdateAfterJoin(AVLNode x, int sizeToAdd) {
		if (x != null) {
			x.setSize(x.getSize() + 1 + sizeToAdd);
			sizeUpdateAfterJoin(x.parent, sizeToAdd);
		}
	}

	/**
	 * Rebalancing AVL tree after insert operation. returns the amount of
	 * rebalancing operations.
	 */
	// complexity: O(log n)
	public int insertRebalance(AVLNode rebalancedNode) {
		if (rebalancedNode == null) {
			return 0;
		}
		String edge = rebalancedNode.findEdges();
		switch (edge) {
		case "L0R1":
		case "L1R0":
			rebalancedNode.promote();
			return 1 + insertRebalance(rebalancedNode.parent);
		case "L0R2":
			return insertRebalanceL0R2(rebalancedNode);
		case "L2R0":
			return insertRebalanceL2R0(rebalancedNode);
		}
		return 0;
	}

	/**
	 * Rebalance method for edge L0R2 returns the number of rebalance operations.
	 */
	// complexity: O(1)
	private int insertRebalanceL0R2(AVLNode nodeToRebalnce) {
		AVLNode child = nodeToRebalnce.left;
		String edge = child.findEdges();
		switch (edge) {
		case "L1R1":
			nodeToRebalnce.promote();
			rotate(true, child);
			nodeToRebalnce.demote();
			child.promote();
			return 4;
		case "L1R2":
			rotate(true, child);
			nodeToRebalnce.demote();
			return 2;
		case "L2R1":
			AVLNode rightChild = child.right;
			rotate(false, rightChild);
			rotate(true, rightChild);
			child.demote();
			nodeToRebalnce.demote();
			rightChild.promote();
			return 5;
		default:
			return 0;
		}
	}

	/**
	 * Rebalance method for edge L2R0. returns the number of rebalance operations.
	 */
	// complexity: O(1)
	private int insertRebalanceL2R0(AVLNode nodeToRebalance) {
		AVLNode child = nodeToRebalance.right;
		String edge = child.findEdges();
		switch (edge) {
		case "L1R1":
			nodeToRebalance.promote();
			rotate(false, child);
			nodeToRebalance.demote();
			child.promote();
			return 4;
		case "L2R1":
			rotate(false, child);
			nodeToRebalance.demote();
			return 2;
		case "L1R2":
			AVLNode leftChild = child.left;
			rotate(true, leftChild);
			rotate(false, leftChild);
			child.demote();
			nodeToRebalance.demote();
			leftChild.promote();
			return 5;
		default:
			return 0;
		}
	}

	/**
	 * checks the type of the delete that we need to use in order to delete the
	 * given node
	 */
	// complexity: O(log n)
	private AVLNode sortDelete(AVLNode node) {
		String location = whereToPlace(node);
		switch (location) {
		case "LEAF":
			return deleteLeaf(node);
		case "INTERNAL":
			return deleteInternalNode(node);
		case "UNARY_RIGHT":
			return deleteUnaryNode(true, node);
		case "UNARY_LEFT":
			return deleteUnaryNode(false, node);
		default:
			throw new RuntimeException();
		}
	}

	/**
	 * deletes an internal node
	 */
	// complexity: O(log n)
	private AVLNode deleteInternalNode(AVLNode internalNode) {
		AVLNode successor = successor(internalNode);
		internalNode.setValue(successor.getValue());
		internalNode.setKey(successor.getKey());
		if (successor == this.max) {
			this.max = internalNode;
		}
		return sortDelete(successor);
	}

	/**
	 * deletes leaf
	 */
	// complexity: O(1)
	private AVLNode deleteLeaf(AVLNode leaf) {
		if (leaf == this.root)
			root = min = max = VIRTUAL_NODE;
		else
			leaf.parent.setSon(leaf.getSide(), new AVLNode());

		return leaf.parent;
	}

	/**
	 * deletes unary node
	 */
	// complexity: O(1)
	private AVLNode deleteUnaryNode(boolean side, AVLNode unaryNode) {
		AVLNode son = getUnarySon(unaryNode);
		son.parent = unaryNode.parent;

		if (unaryNode == this.root)
			this.root = son;
		else
			unaryNode.parent.setSon(unaryNode.getSide(), son);

		return unaryNode.parent;
	}

	/**
	 * Rebalancing the tree after the deletion of a node.
	 */
	// complexity: O(log n)
	private int deleteRebalance(AVLNode rebalanceNode) {
		if (rebalanceNode == null) {
			return 0;
		}
		String edge = rebalanceNode.findEdges();
		switch (edge) {
		case "L2R2":
			rebalanceNode.demote();
			return 1 + deleteRebalance(rebalanceNode.parent);
		case "L1R3":
			return deleteRebalanceL1R3(rebalanceNode);
		case "L3R1":
			return deleteRebalanceL3R1(rebalanceNode);
		default:
			return 0;
		}
	}

	/**
	 * Rebalance method for the case of edge L3R1.
	 */
	// complexity: O(1)
	private int deleteRebalanceL3R1(AVLNode rebalanceNode) {
		AVLNode son = rebalanceNode.right;
		String edge = son.findEdges();
		switch (edge) {
		case "L1R1":
			rotate(false, son);
			rebalanceNode.demote();
			son.promote();
			return 3;
		case "L1R2":
			AVLNode sonOfSon = son.left;
			rotate(true, sonOfSon);
			rotate(false, sonOfSon);
			sonOfSon.promote();
			rebalanceNode.demote();
			rebalanceNode.demote();
			son.demote();
			return 6 + deleteRebalance(sonOfSon.parent);
		case "L2R1":
			rotate(false, son);
			rebalanceNode.demote();
			rebalanceNode.demote();
			return 3 + deleteRebalance(son.parent);
		default:
			return 0;
		}
	}

	/**
	 * Rebalance method for the case of edge L1R3.
	 */
	// complexity: O(1)
	private int deleteRebalanceL1R3(AVLNode rebalanceNode) {
		AVLNode son = rebalanceNode.left;
		String edge = son.findEdges();
		switch (edge) {
		case "L1R1":
			rotate(true, son);
			rebalanceNode.demote();
			son.promote();
			return 3;
		case "L2R1":
			AVLNode sonOfSon = son.right;
			rotate(false, sonOfSon);
			rotate(true, sonOfSon);
			sonOfSon.promote();
			rebalanceNode.demote();
			rebalanceNode.demote();
			son.demote();
			return 6 + deleteRebalance(sonOfSon.parent);
		case "L1R2":
			rotate(true, son);
			rebalanceNode.demote();
			rebalanceNode.demote();
			return 3 + deleteRebalance(son.parent);
		default:
			return 0;
		}
	}

	/**
	 * updates the sizes of the relevant nodes after insertion.
	 */
	// complexity: O(log n)
	public void updateSizeAfterInsert(AVLNode node) {
		while (node != null) {
			node.setSize(node.getSize() + 1);
			node = node.parent;
		}
	}

	/**
	 * updates the sizes of the relevant nodes after deletion.
	 */
	// complexity: O(log n)
	public void updateSizeAfterDelete(AVLNode node) {
		while (node != null) {
			node.setSize(node.getSize() - 1);
			node = node.parent;
		}
	}

	/**
	 * Updates the min and max values after insertion.
	 */
	// complexity: O(1)
	public void updateMaxMinAfterInsert(AVLNode node) {
		if (node.getKey() > this.max.getKey())
			this.max = node;
		if (node.getKey() < this.min.getKey())
			this.min = node;
	}

	/**
	 * Updates the min and max values after deletion.
	 */
	// complexity: O(log n)
	public void updateMaxMinAfterDelete(AVLNode node) {
		if (this.min.getKey() == node.getKey())
			this.min = successor(node);
		if (this.max.getKey() == node.getKey())
			this.max = predecessor(node);
	}

	/**
	 * finds the predecessor of a given node
	 */
	// complexity: O(log n)
	private AVLNode predecessor(AVLNode node) {
		if (!node.left.isRealNode()) {
			if (node.parent != null) {
				return goUpRight(node);
			}
			return VIRTUAL_NODE;
		} else {
			return goDownRight(node.left);
		}
	}

	/**
	 * finds the successor of a given node
	 */
	// complexity: O(log n)
	private AVLNode successor(AVLNode node) {
		if (node.right.isRealNode() == false) {
			if (node.parent != null) {
				return goUpLeft(node);
			}
			return VIRTUAL_NODE;
		} else {
			return goDownLeft(node.right);
		}
	}

	/**
	 * returns the predecessor of node if node.left is not real
	 */
	// complexity: O(log n)
	private AVLNode goUpRight(AVLNode node) {
		if (node.parent.left == node) {
			return goUpRight(node.parent);
		} else {
			return node.parent;
		}
	}

	/**
	 * find the lowest right node for node
	 */
	// complexity: O(log n)
	private AVLNode goDownRight(AVLNode node) {
		if (!node.right.isRealNode()) {
			return node;
		} else {
			return goDownRight(node.right);
		}
	}

	/**
	 * returns the successor of node if node.right is not real
	 */
	// complexity: O(log n)
	private AVLNode goUpLeft(AVLNode node) {
		if (node.parent.right == node) {
			return goUpLeft(node.parent);
		} else {
			return node.parent;
		}
	}

	/**
	 * finds the lowest left node for node
	 */
	// complexity: O(log n)
	private AVLNode goDownLeft(AVLNode node) {
		if (!node.left.isRealNode()) {
			return node;
		} else {
			return goDownLeft(node.left);
		}
	}

	/**
	 * checks if the current node is a leaf, internal node or unary node.
	 */
	// complexity: O(1)
	private String whereToPlace(IAVLNode node) {
		if (node.getLeft().isRealNode()) {
			if (node.getRight().isRealNode() == true) {
				return "INTERNAL";
			} else {
				return "UNARY_LEFT";
			}
		} else if (node.getRight().isRealNode()) {
			return "UNARY_RIGHT";
		} else {
			return "LEAF";
		}
	}

	/**
	 * checks which side to rotate.
	 */
	// complexity: O(1)
	private void rotate(boolean side, AVLNode rotatedNode) {
		if (side == true) {
			rotateRight(rotatedNode);
		} else {
			rotateLeft(rotatedNode);
		}
	}

	/**
	 * rotates right the cross section of rotatedNode
	 */
	// complexity: O(1)
	private void rotateRight(AVLNode rotatedNode) {
		AVLNode A = rotatedNode.parent;
		AVLNode B = rotatedNode;
		AVLNode C = A.right;
		AVLNode x = B.left;
		AVLNode y = B.right;
		if (A == root) {
			root = B;
		} else {
			A.parent.setSon(A.getSide(), B);
		}
		B.right = A;
		B.parent = A.parent;
		A.left = y;
		updateSizesAfterRotation(A, B, C, x, y);

	}

	/**
	 * rotates left the cross section of rotatedNode
	 */
	// complexity: O(1)
	private void rotateLeft(AVLNode rotatedNode) {
		AVLNode A = rotatedNode.parent;
		AVLNode B = rotatedNode;
		AVLNode C = A.left;
		AVLNode x = B.right;
		AVLNode y = B.left;
		if (A == root) {
			root = B;
		} else {
			A.parent.setSon(A.getSide(), B);
		}
		B.left = A;
		B.parent = A.parent;
		A.right = y;
		y.parent = A;
		updateSizesAfterRotation(A, B, C, x, y);

	}

	/**
	 * updates the sizes and the parents of the nodes after the rotation
	 */
	// complexity: O(1)
	private void updateSizesAfterRotation(AVLNode z, AVLNode x, AVLNode y, AVLNode a, AVLNode b) {
		z.parent = x;
		b.parent = z;
		z.size = b.size + y.size + 1;
		x.size = z.size + a.size + 1;
	}

	/**
	 * given an unary node, the function returns its only child
	 */
	// complexity: O(1)
	private AVLNode getUnarySon(AVLNode unaryNode) {
		if (unaryNode.left.isRealNode() == true)
			return unaryNode.left;
		else
			return unaryNode.right;
	}

	/** IAVLNode INTERFACE */
	/**
	 * public interface IAVLNode ! Do not delete or modify this - otherwise all
	 * tests will fail !
	 */
	public interface IAVLNode {
		public int getKey(); // returns node's key (for virtuval node return -1)

		public String getValue(); // returns node's value [info] (for virtuval node return null)

		public void setLeft(IAVLNode node); // sets left child

		public IAVLNode getLeft(); // returns left child (if there is no left child return null)

		public void setRight(IAVLNode node); // sets right child

		public IAVLNode getRight(); // returns right child (if there is no right child return null)

		public void setParent(IAVLNode node); // sets parent

		public IAVLNode getParent(); // returns the parent (if there is no parent return null)

		public boolean isRealNode(); // Returns True if this is a non-virtual AVL node

		public void setHeight(int height); // sets the height of the node

		public int getHeight(); // Returns the height of the node (-1 for virtual nodes)

		// extra methods:
		public void setValue(String info); // sets the value of the node

		public void setKey(int key); // sets the key of the node

		public int getSize(); // returns the size of the node

		public void setSize(int size); // sets the size of the node
	}

	/** AVLNode CLASS */
	public class AVLNode implements IAVLNode {

		private int key; // key of node
		private String info; // value of node
		private int height; // height of subtree
		private int size; // number of nodes in subtree
		AVLNode parent; // parent of node
		AVLNode left; // left subtree
		AVLNode right; // right subtree

		// AVLNode empty constructor
		// complexity: O(1)
		private AVLNode() {
			this.height = -1;
			this.key = -1;
		}

		// AVLNode constructor
		// complexity: O(1)
		public AVLNode(int key, String info, AVLNode parent) {
			this.key = key;
			this.info = info;
			this.height = 0;
			this.size = 1;
			this.parent = parent;
			this.right = new AVLNode();
			this.left = new AVLNode();
		}

		// AVLNode methods

		// complexity: O(1)
		public int getKey() {
			return this.key;
		}

		// complexity: O(1)
		public void setKey(int key) {
			this.key = key;
		}

		// complexity: O(1)
		public String getValue() {
			return this.info;
		}

		// complexity: O(1)
		public void setValue(String info) {
			this.info = info;
		}

		// complexity: O(1)
		public void setLeft(IAVLNode node) {
			this.left = (AVLNode) node;
		}

		// complexity: O(1)
		public IAVLNode getLeft() {
			return this.left;
		}

		// complexity: O(1)
		public void setRight(IAVLNode node) {
			this.right = (AVLNode) node;
		}

		// complexity: O(1)
		public IAVLNode getRight() {
			return this.right;
		}

		// complexity: O(1)
		public void setParent(IAVLNode node) {
			this.parent = (AVLNode) node;
		}

		// complexity: O(1)
		public IAVLNode getParent() {
			return parent;
		}

		// complexity: O(1)
		public boolean isRealNode() {
			return (this.getKey() != -1);
		}

		// complexity: O(1)
		public void setHeight(int height) {
			this.height = height;
		}

		// complexity: O(1)
		public int getHeight() {
			return this.height;
		}

		// complexity: O(1)
		public int getSize() {
			return this.size;
		}

		// complexity: O(1)
		public void setSize(int size) {
			this.size = size;
		}

		// complexity: O(1)
		public void promote() {
			this.height++;
		}

		// complexity: O(1)
		public void demote() {
			this.height--;
		}

		// complexity: O(1)
		public boolean getSide() {
			if (parent.getLeft() == this) {
				return false;
			} else {
				return true;
			}
		}

		// complexity: O(1)
		public void setSon(boolean side, AVLNode son) {
			if (side == false)
				this.left = son;
			else {
				this.right = son;
			}
		}

		/**
		 * check the edges between the current node and its sons
		 */
		// complexity: O(1)
		public String findEdges() {
			int deltaLeft = this.height - this.getLeft().getHeight();
			int deltaRight = this.height - this.getRight().getHeight();
			String st = "L" + deltaLeft + "R" + deltaRight;
			return st;
		}
	}
}
