package assignment3; 

import java.io.FileNotFoundException;
import java.io.IOException;
/**
 * The PathFinder class represents an algorithm to find a path from the entrance to the 
 * treasure chambers, represented as a pyramid map of a national park. 
 * @author chaejinhur
 *
 */
public class PathFinder {
	// private instance variables
	private Map pyramidMap; 		// object of Map representing chambers of the national park 
	
	
	/**
	 * Constructor that creates a PathFinder object with the file name 
	 * Initializes the pyramid map 
	 * @param fileName Name of the file 
	 */
	public PathFinder(String fileName) {		// input: name of file 
		try {
			pyramidMap = new Map(fileName); 	// initialize pyramid map 
			
        } catch (InvalidMapCharacterException e) {
            System.err.println("Invalid Map Character: " + e.getMessage());
            pyramidMap = null; 
            
		} catch (FileNotFoundException e) {
			System.err.println("File not found: " + e.getMessage());
			pyramidMap = null; 
			
        } catch (IOException e) {
            System.err.println("IOException occurred: " + e.getMessage());
            pyramidMap = null; 
        }
    }

	
	
	/**
	 * Finds a path from entrance to the treasure chambers that can be reached 
	 * @return a stack containing the chambers in the path from entrance to the last treasure 
	 */
	public DLStack<Chamber> path() {
		DLStack<Chamber> pathStack = new DLStack<Chamber>(); 
		
		if (pyramidMap == null) {
            System.err.println("Map not initialized correctly. Cannot find the path.");
    		return pathStack; 
		}
		
		int numChambers = pyramidMap.getNumTreasures(); 
		int numTreasures = 0; 
		
		Chamber entrance = pyramidMap.getEntrance(); 
		pathStack.push(entrance); 		// push entrance chamber to stack 
		entrance.markPushed(); 
		
		while (!pathStack.isEmpty()) {
			Chamber currentChamber = pathStack.peek(); 		// get the top chamber from stack 
				
			// when we find treasure chamber, stop searching: 
			if (currentChamber.isTreasure()) {
				numTreasures++; 
			}
			
			// if we find all the treasures, break: 
			if (currentChamber.isTreasure() && numTreasures == numChambers) {
				break; 
			}
			
			// get the best chamber to move to from current chamber 
			Chamber nextChamber = bestChamber(currentChamber); 
			
			if (nextChamber != null) {
				// access next unmarked chamber and update pathStack
				pathStack.push(nextChamber);
				nextChamber.markPushed(); 	
				
			} else {		
				// no valid next chamber: go back by popping current chamber from stack 
				Chamber popped = pathStack.pop(); 
				popped.markPopped(); 
			}
		}
		
		// return the stack with the path to the treasure chambers: 
		return pathStack; 
	}
	
	/**
	 * Accessor method to get the pyramid map 
	 * @return the Map object with the chambers of the national park 
	 */
	public Map getMap() {
		return pyramidMap; 
	}
	
	
	/**
	 * checks if the given chamber has any lighted neighbors 
	 * @param currentChamber The current chamber to check for lighted neighbors 
	 * @return true if the chamber has at least one lighted neighbor, false if not. 
	 */
	public boolean isDim(Chamber currentChamber) {
	    if (currentChamber == null || currentChamber.isSealed() || currentChamber.isLighted()) {
	    	return false; 
	    }
	    
	    // check if the current chamber has a lighted neighbour: 
	    for (int i = 0; i < 6; i++) {
	    	try {
	    		Chamber neighbor = currentChamber.getNeighbour(i);
	    		
	            if (neighbor != null && neighbor.isLighted()) {
	            	return true; 
	            } 
	    	} 
	    	
	        catch (InvalidNeighbourIndexException e) {
	            // handle exception 
                System.err.println("Invalid Neighbour Index: " + e.getMessage());
	    	}
	    }
	    
	    return false; 
	} 	
	
	
	/**
	 * selects the best chamber to move to from currentChamber 
	 * @param currentChamber The current chamber  
	 * @return The selected chamber to move to, or return null if no valid chamber is found. 
	 */
	public Chamber bestChamber(Chamber currentChamber) {
		
		// 1. adjacent unmarked treasure chambers: 
		for (int i = 0; i <= 5; i++) {
			try {
				Chamber neighbor = currentChamber.getNeighbour(i); 
				if (neighbor != null && neighbor.isTreasure() && !neighbor.isMarked()) {
					return neighbor; 	
				}
				
			} catch (InvalidNeighbourIndexException e) {
				// handle exception  
                System.err.println("Invalid Neighbour Index: " + e.getMessage());
			}
		}
		
		// 2. unmarked lighted chambers:
		for (int i = 0; i <= 5; i++) { 
			try {
				Chamber neighbor = currentChamber.getNeighbour(i); 
                if (neighbor != null && neighbor.isLighted() && !neighbor.isMarked()) {
                	return neighbor; 
                }
				
			} catch (InvalidNeighbourIndexException e) {
				// handle exception 
                System.err.println("Invalid Neighbour Index: " + e.getMessage());
			}
		}
			
	
		// 3. unmarked, dim chamber: 
		for (int i = 0; i <= 5; i++) {
			try {
				Chamber neighbor = currentChamber.getNeighbour(i); 
                if (neighbor != null && isDim(neighbor) && !neighbor.isMarked()) {
                	return neighbor; 
                }
				
			} catch (InvalidNeighbourIndexException e) {
                System.err.println("Invalid Neighbour Index: " + e.getMessage());
			}
		}
	
		// when there's no unmarked treasure, lighted or dim chamber: 
		return null; 
	}
	

}
