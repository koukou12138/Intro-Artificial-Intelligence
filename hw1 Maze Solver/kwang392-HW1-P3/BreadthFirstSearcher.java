import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Breadth-First Search (BFS)
 * 
 * You should fill the search() method of this class.
 */
public class BreadthFirstSearcher extends Searcher {

	/**
	 * Calls the parent class constructor.
	 * 
	 * @see Searcher
	 * @param maze initial maze.
	 */
	public BreadthFirstSearcher(Maze maze) {
		super(maze);
	}

	/**
	 * Main breadth first search algorithm.
	 * 
	 * @return true if the search finds a solution, false otherwise.
	 */
	public boolean search() {
		// FILL THIS METHOD

		// explored list is a 2D Boolean array that indicates if a state associated with a given position in the maze has already been explored.
		boolean[][] explored = new boolean[maze.getNoOfRows()][maze.getNoOfCols()];

		// ...

		// Queue implementing the Frontier list
		LinkedList<State> queue = new LinkedList<State>();
		Square pos = maze.getPlayerSquare();
        State start = new State(pos, null, 0 ,0);
        if (start.isGoal(maze))
          return true;
        queue.add(start);
        
		while (!queue.isEmpty()) {
			// TODO return true if find a solution
			// TODO maintain the cost, noOfNodesExpanded (a.k.a. noOfNodesExplored),
			// maxDepthSearched, maxSizeOfFrontier during
			// the search
			// TODO update the maze if a solution found
		  
		    // use queue.pop() to pop the queue.
            // use queue.add(...) to add elements to queue
		    maxSizeOfFrontier = Math.max(maxSizeOfFrontier, queue.size());
		    State current = queue.pop();
            noOfNodesExpanded++;
		    
		    explored[current.getX()][current.getY()] = true;
		    
            if (current.isGoal(maze)) {
              cost = current.getDepth();
              maxDepthSearched = current.getDepth();
              current = current.getParent();
              while (current.getParent() != null) {
                maze.setOneSquare(current.getSquare(), '.');
                current = current.getParent();
              }
              return true;
            }
		        
		    for (State successor : current.getSuccessors(explored, maze)) {
		      // check the state is not already in the frontier
		      boolean existed = false;
		      for (State state: queue) 
		        if (state.getX() == successor.getX() && state.getY() == successor.getY())
		          existed = true;
		      if (!existed)
		        queue.add(successor);
		    }  
			
		}

		// TODO return false if no solution
		return false;
	}
	
}
