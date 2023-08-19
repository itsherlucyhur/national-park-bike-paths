package assignment3; 
/**
 * The algorithm for finding a path from the entrance to the treasure chambers 
 * involves the two classes: PathFinder and DLStack. The PathFinder class uses a pyramid map to search for the most optimized  
 * path along the chambers using the DLStack. DLStack implements an extended stack ADT with a doubly linked list, which is used for 
 * path exploration in the PathFinder class. 
 * Challenges arose while implementing the pop(int k) method of DLStack, which had to remove and return the k-th item 
 * from the stack accurately. I realized that when removing the k-th item from the stack, I have to traverse through the 
 * stack from the top using getPrevious() instead of getNext(), then update 'previous' and 'next' references of the neighboring 
 * nodes, and return the element from the k-th node. This is because of the way the stack is implemented as a doubly linked list. 
 * The solution was tested by using different map scenarios provided as input files to evaluate the pathfinding algorithm. 
 * 
 * @author chaejinhur
 * @param <T> type of elements of DLStack 
 */
public class DLStack<T> implements DLStackADT<T> {
	
	// private instance variables
	private DoubleLinkedNode<T> top; 		// reference to the node at the top of stack 
	private int numItems; 					// number of data items in the stack 

	
	/**
	 * Constructs an empty DLStack 
	 */
	public DLStack() {
		top = null; 		// initialize top 
		numItems = 0; 		// initialize empty stack 
	}
	
	
	/**
	 * adds the given dataItem to the top of stack
	 * @param dataItem The data item to be added to the top of the stack 
	 */
	public void push (T dataItem) {
		DoubleLinkedNode<T> newNode = new DoubleLinkedNode<>(dataItem); 
		if (isEmpty() == true) {		// when stack is empty: 
			top = newNode; 
			
		// when stack isn't empty: 	
		} else {
			top.setNext(newNode); 
			newNode.setPrevious(top); 
			top = newNode; 		// add to top 
		}
		numItems++; 		// increment number of items 	
	}
	
	
	/**
	 * removes and returns item at the top of stack 
	 * 
	 * @return The data item at the top of stack 
	 * @throws EmptyStackException If the stack is empty 
	 */
	public T pop() throws EmptyStackException {
		if (isEmpty() == true) {
			throw new EmptyStackException("Stack is empty"); 
		}
				
		T dataItem = top.getElement(); 
		top = top.getPrevious();
		numItems--; 		// decrement numItems
		
		return dataItem;
	}
	
	
	/**
	 * removes and returns the k-th item from the top of stack 
	 * @param k The position of the element to remove 
	 * @return The k-th position from the stack 
	 * @throws InvalidItemException If the k-th position is invalid 
	 */
	public T pop(int k) throws InvalidItemException {
		
		// if k is larger than the number of items in the stack or if k is less than or equal to 0: 
		if (isEmpty() || k <= 0 || k > numItems) {
			throw new InvalidItemException("Invalid k-th position");
		}
		
		DoubleLinkedNode<T> current = top; 

		// Case 1: simple pop, k = 1
		if (k == 1) {
			return pop();  	// call pop() 
		}
			
		// Case 2: k = numItems 
		if (k == numItems) {
			// move to the last element (k-th element): 
			for (int i = 1; i < k; i++) {
				current = current.getPrevious(); 
			}
			current.getNext().setPrevious(null);
			
			
		// Case 3: if k is in the middle of the stack 	
		} else {
			for (int i = 1; i < k; i++) {
				current = current.getPrevious(); 
			}
			
			// remove the k-th node from the stack: 
			DoubleLinkedNode<T> previous = current.getPrevious();
	        DoubleLinkedNode<T> next = current.getNext();
	       
	        // connect previous and next elements of middle element to remove the k-th element 
	        previous.setNext(next);
	        if (next != null) {
	        	next.setPrevious(previous);
	        } 
		}

		numItems--; 		// decrement 
		return current.getElement(); 		// return the item from the k-th node removed
	}
	
	
	/**
	 * Returns the item at the top of stack without removing it
	 * @return The data item at the top of the stack 
	 * @throws EmptyStackException if the stack is empty 
	 */
	public T peek() throws EmptyStackException {
		if (isEmpty() == true) {
			throw new EmptyStackException("Stack is empty"); 
		}
		return top.getElement(); 
		
	}
	
	
	/**
	 * checks if stack is empty
	 * @return true if the stack is empty, false if not. 
	 */
	public boolean isEmpty() {
		if (numItems == 0) {		// stack is empty: 
			return true; 
		} else {
			return false; 
		}
	}
	
	
	/**
	 * Returns the number of data items in the stack. 
	 * @return The number of data items 
	 */
	public int size() {
		return numItems; 
	}
	
	
	/**
	 * Returns the top node of the stack. 
	 * @return The top node 
	 */
	public DoubleLinkedNode<T> getTop() {
		return top; 
	}
	

	/**
	 * Returns a string representation of data items from top to bottom in the stack 
	 * @return  string of data items in the stack 
	 */
	public String toString() {
		StringBuilder s = new StringBuilder(); 		// new string
		s.append("[");
		DoubleLinkedNode<T> current = top; 
		
		// while the (current) top is not empty: 
		while (current != null) {
			s.append(current.getElement()); 
			if (current.getNext() != null) {
				s.append(" "); 
			}
			current = current.getNext(); 
	
		}
		s.append("]"); 
		return s.toString(); 
		
		
		
	}
}
