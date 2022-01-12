import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * A* algorithm search
 * 
 * You should fill the search() method of this class.
 */
public class AStarSearcher extends Searcher {

	/**
	 * Calls the parent class constructor.
	 * 
	 * @see Searcher
	 * @param maze initial maze.
	 */
	public AStarSearcher(Maze maze) {
		super(maze);
	}

	/**
	 * Main a-star search algorithm.
	 * 
	 * @return true if the search finds a solution, false otherwise.
	 */
	public boolean search() {

		// FILL THIS METHOD

		// explored list is a Boolean array that indicates if a state associated with a given position in the maze has already been explored. 
		boolean[][] explored = new boolean[maze.getNoOfRows()][maze.getNoOfCols()];
		// ...

		PriorityQueue<StateFValuePair> frontier = new PriorityQueue<StateFValuePair>();

		// TODO initialize the root state and add
		// to frontier list
		// ...
		Square pos = maze.getPlayerSquare();
        State start = new State(pos, null, 0 ,0);
        if (start.isGoal(maze))
          return true;
        double HValue = getHValue(start, maze);
        
        frontier.add(new StateFValuePair(start, HValue));
		
		while (!frontier.isEmpty()) {
			// TODO return true if a solution has been found
			// TODO maintain the cost, noOfNodesExpanded (a.k.a. noOfNodesExplored),
			// maxDepthSearched, maxSizeOfFrontier during
			// the search
			// TODO update the maze if a solution found

			// use frontier.poll() to extract the minimum stateFValuePair.
			// use frontier.add(...) to add stateFValue pairs
		  
		  
  		    maxSizeOfFrontier = Math.max(maxSizeOfFrontier, frontier.size());
  		    StateFValuePair current = frontier.poll();
            noOfNodesExpanded++;
            State currState = current.getState();
            
            explored[currState.getX()][currState.getY()] = true;
            
            if (currState.isGoal(maze)) {
              cost = currState.getDepth();
              maxDepthSearched = currState.getDepth();
              currState = currState.getParent();
              while (currState.getParent() != null) {
                maze.setOneSquare(currState.getSquare(), '.');
                currState = currState.getParent();
              }
              return true;
            }
            
            for (State successor : currState.getSuccessors(explored, maze)) {
              // check the state is not already in the frontier
              boolean existed = false;
              StateFValuePair same = null;
              for (StateFValuePair pair: frontier) 
                if (pair.getState().getX() == successor.getX() && pair.getState().getY() == successor.getY()) {
                  existed = true;
                  same = pair;
                }
              
              double h = getHValue(successor, maze);
              double f = h + successor.getGValue();
              
              if (!existed) 
                frontier.add(new StateFValuePair(successor, f));
                
              else 
                if (successor.getGValue() < same.getState().getGValue()) {
                  frontier.remove(same);
                  frontier.add(new StateFValuePair(successor, f));
                }
           
            } 
            
		}

		// TODO return false if no solution
		return false;
	}
	
	private double getHValue(State state ,Maze maze) {
	  int u = state.getX();
	  int v = state.getY();
	  int p = maze.getGoalSquare().X;
	  int q = maze.getGoalSquare().Y;
	  
	  return Math.sqrt(Math.pow(u - p, 2) +  Math.pow(v - q, 2));
	}

}
