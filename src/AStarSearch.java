import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

public class AStarSearch {

	public static void main(String[] args) {
		
		ArrayList<Integer> goalState = getGoalState(); //Set up goal state
		ArrayList<Integer> inputState = getInputState(); //Get initial state
		
		//Run A* search with misplaced tiles heuristic
		boolean isManhattan = false;
		System.out.println("Misplaced Tiles:");
		aStarSearch(goalState, inputState, isManhattan);
		
		System.out.println();
		
		//Run A* search with Manhattan distance heuristic
		isManhattan = true;
		System.out.println("Manhattan Distance:");
		aStarSearch(goalState, inputState, isManhattan);
	}
	
	//Performs A* search algorithm
	public static void aStarSearch(ArrayList<Integer> goalState, ArrayList<Integer> inputState, boolean isManhattan) {
		//Create initial node
		int heuristicCost = 0;
		if (isManhattan)
			heuristicCost = getManhattanDistance(inputState);
		else
			heuristicCost = getNumMisplacedTiles(inputState, goalState);
		Node initialNode = new Node(inputState, null, "None", 0, heuristicCost);
		
		//Set up node queue (ArrayList) and hashset for state history
		HashSet<ArrayList<Integer>> exploredStates = new HashSet<ArrayList<Integer>>();
		ArrayList<Node> nodeQueue = new ArrayList<Node>();
		exploredStates.add(initialNode.getState());
		nodeQueue.add(initialNode);
		
		//Sets up hashset for goal state to make goal state comparisons easier
		HashSet<ArrayList<Integer>> goalStateSet = new HashSet<ArrayList<Integer>>();
		goalStateSet.add(goalState);
		
		boolean isGoalFound = false;
		int nodeCount = 0;
		
		double startTime = System.nanoTime(); //Gets start time
		
		//Searching for goal
		while (isGoalFound == false && nodeQueue.size() > 0) {
			Node currNode = nodeQueue.get(0); //gets first node in the queue
			nodeCount++;

			if (goalStateSet.contains(currNode.getState())) { //checks if current state matches goal state
				isGoalFound = true;
				break;
			}
			else {
				//Adds child nodes to queue and removes the current node
				getChildNodes(nodeQueue, exploredStates, isManhattan, goalState);
				nodeQueue.remove(0);
			}	
		}
		
		double endTime = System.nanoTime(); //Gets end time
		double timeTaken = (endTime - startTime) / 1000000000.00; //time elapsed in seconds
		
		//When goal is found
		if (isGoalFound) {
			Node goalNode = nodeQueue.get(0);
			System.out.println("Goal Found!");
			//Print the moves made
			System.out.println("Moves: " + getGoalPath(goalNode));
			//Print number of nodes
			System.out.println("Number of Nodes Expanded: " + nodeCount);
			//Print memory taken
			double nodeSize = goalNode.calculateNodeSize(); //Gets estimated size of each node
			double memUsed = nodeCount * nodeSize / 1000; //Calculates total memory used during search
			System.out.println("Memory Used: " + memUsed + " kB");
			//Print time elapsed
			System.out.println("Time Taken: " + timeTaken + " seconds or " + (timeTaken*1000) + " milliseconds");
		}
		//If the goal was never found
		if (nodeQueue.size() == 0) {
			System.out.println("Frontier is empty - Goal not found");
		}
	}
	
	//Finds the number of tiles in the wrong position compared to the goal state
	public static int getNumMisplacedTiles(ArrayList<Integer> currState, ArrayList<Integer> goalState) {
		int numMisplacedTiles = 0;
		
		for (int i = 0; i < 16; i++) {
			if (currState.get(i) != 0) { //skips over the empty space
				if (currState.get(i) != goalState.get(i)) //if current tile is in the wrong place
					numMisplacedTiles++;
			}
		}
		return numMisplacedTiles;
	}
	
	//Finds the sum of the total distances tiles are away from the goal state
	public static int getManhattanDistance(ArrayList<Integer> currState) {
		int manhattanDistance = 0;
		
		for (int i = 0; i < 16; i++) {
			int currTile = currState.get(i);
			if (currTile != 0) { //skips over the empty space
				int verticalDistance = calcVerticalDistance(currTile, i);
				int horizontalDistance = calcHorizontalDistance(currTile, i);
				manhattanDistance = manhattanDistance + verticalDistance + horizontalDistance;
			}
		}
		return manhattanDistance;
	}
	
	//Calculates a tile's vertical distance from its goal location
	public static int calcVerticalDistance(int tileNum, int tileLoc) {		
		 return Math.abs((tileNum-1)/4 - tileLoc/4);	
	}
	
	//Calculates a tile's horizontal distance from its goal location
	public static int calcHorizontalDistance(int tileNum, int tileLoc) {
		return Math.abs((tileNum-1)%4 - tileLoc%4);
	}
	
	//Gets all valid child nodes and adds them to the queue
	public static void getChildNodes(ArrayList<Node> queue, HashSet<ArrayList<Integer>> exploredStates, boolean isManhattan, ArrayList<Integer> goalState) {
		Node currNode = queue.get(0);
		
		//Moving empty space up
		if (currNode.canMoveUp()) {
			ArrayList<Integer> upState = currNode.moveUp();
			if (!(exploredStates.contains(upState))) { //Checks if its a repeated state
				exploredStates.add(upState);
				Node upNode = createNewNode(upState, currNode, "U", isManhattan, goalState); //node created
				insertNodeSorted(queue, upNode); //node added to queue
			}
		}
		
		//Moving empty space down
		if (currNode.canMoveDown()) {
			ArrayList<Integer> downState = currNode.moveDown();
			if (!(exploredStates.contains(downState))) { //Checks if its a repeated state
				exploredStates.add(downState);
				Node downNode = createNewNode(downState, currNode, "D", isManhattan, goalState); //node created
				insertNodeSorted(queue, downNode); //node added to queue
			}
		}

		//Moving empty space left
		if (currNode.canMoveLeft()) {
			ArrayList<Integer> leftState = currNode.moveLeft();
			if (!(exploredStates.contains(leftState))) { //Checks if its a repeated state
				exploredStates.add(leftState);
				Node leftNode = createNewNode(leftState, currNode, "L", isManhattan, goalState); //node created
				insertNodeSorted(queue, leftNode); //node added to queue
			}
		}
		
		//Moving empty space right
		if (currNode.canMoveRight()) {
			ArrayList<Integer> rightState = currNode.moveRight();
			if (!(exploredStates.contains(rightState))) { //Checks if its a repeated state
				exploredStates.add(rightState);
				Node rightNode = createNewNode(rightState, currNode, "R", isManhattan, goalState); //node created
				insertNodeSorted(queue, rightNode); //node added to queue
			}
		}
	}
	
	//Creates a new Node object
	public static Node createNewNode(ArrayList<Integer> state, Node pNode, String pMove, boolean isManhattan, ArrayList<Integer> goalState) {
		int heuristicCost = 0;
		//Gets cost depending on which heuristic is being used
		if (isManhattan) {
			heuristicCost = getManhattanDistance(state);
		}
		else {
			heuristicCost = getNumMisplacedTiles(state, goalState);
		}
		
		Node newNode = new Node(state, pNode, pMove, pNode.getCostSoFar()+1, heuristicCost);
		return newNode;
	}
	
	//Node is inserted into the queue based on its total estimated cost
	public static void insertNodeSorted(ArrayList<Node> nodeQueue, Node n) {
		boolean isAdded = false;
		//Compares current node's cost with each node in the queue
		for (int i = 0; i < nodeQueue.size(); i++) {
			if (n.getTotalCost() < nodeQueue.get(i).getTotalCost()) {
				nodeQueue.add(i, n);
				isAdded = true;
				break;
			}
		}
		//Node is added to the end of the queue (if it has the worst cost)
		if (!isAdded) {
			nodeQueue.add(n);
		}
	}
	
	//Sets up the goal state and returns it
	public static ArrayList<Integer> getGoalState() {
		String goalString = "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 0";
		String[] tokens = goalString.split(" ");
		
		//Adds each goal state value into ArrayList
		ArrayList<Integer> goalState = new ArrayList<Integer>();
		for (int i = 0; i < tokens.length; i++) {
			goalState.add(Integer.parseInt(tokens[i]));
		}
		
		return goalState;
	}
	
	//Receives initial configuration state and returns it as ArrayList
	public static ArrayList<Integer> getInputState() {
		ArrayList<Integer> inputState = new ArrayList<Integer>();

		String userResponse;

		//userResponse = "1 0 3 4 5 2 6 8 9 10 7 11 13 14 15 12";

		//Receives user input from console
		Scanner in = new Scanner(System.in);
		System.out.println("\nEnter list of 16 numbers as starting configuration (0 = empty space):");
		userResponse = in.nextLine();
		in.close();
		
		//Checks if user entered any values
		if (userResponse.length() == 0) {
			System.out.print("Nothing entered - exiting...");
			System.exit(0);
		}
		
		//Checks if user entered correct number of values
		String[] tokensEntered = userResponse.split(" ");
		if (tokensEntered.length != 16) {
			System.out.print("There must be 16 entries! - exiting...");
			System.exit(0);
		}
		
		//Checks for any repeats in the values
		Set<String> setEntered = new HashSet<String>();
		for (int i=0; i<tokensEntered.length; i++)
		{
			setEntered.add(tokensEntered[i]); //Fills hashset
		}
		if (setEntered.size() != 16) //Checks if correct number of values are in hashset
		{
			System.out.print("Duplicate entries! - exiting...");
			System.exit(0);
		}
		
		//Checks if all values are integers in range
		for (int i = 0; i < tokensEntered.length; i++) {
			int numEntered=0;
			try {
				numEntered = Integer.parseInt(tokensEntered[i]); //Checks if they're integers
				
				if(numEntered < 0 || numEntered > 15) {  //Checks if they're in range
					System.out.println("Number out of range! - exiting...");
					System.exit(0);
				}
			}
			catch (NumberFormatException e){
				System.out.println("Invalid number entered! - exiting...");
				System.exit(0);
			}
			inputState.add(numEntered); //Adds value to initial state ArrayList
		}
		
		return inputState;
	}
	
	//Parses back through the parent nodes to return the path to the goal
	public static String getGoalPath(Node goalNode) {
		Node currNode = goalNode;
		String goalPath = "";
		
		while (!(currNode.getPrevMove().equals("None"))) { //while it isn't the root node
			goalPath = currNode.getPrevMove() + " " + goalPath;
			currNode = currNode.getParentNode();
		}
		
		return goalPath;
	}
	
}
