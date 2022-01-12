public class AlphaBetaPruning {
    int intitalDepth;
    int move = 0;
    double value = 0.0;
    int numberOfNodesVisited = 0;
    int numberOfNodesEvaluated = 0;
    int maxDepthReached = -1;
    double avgFactor;
  
    public AlphaBetaPruning() {
    }

    /**
     * This function will print out the information to the terminal,
     * as specified in the homework description.
     */
    public void printStats() {
      System.out.println("Move: " + move);
      System.out.println("Value: " + value);
      System.out.println("Number of Nodes Visited: " + numberOfNodesVisited);
      System.out.println("Number of Nodes Evaluated: " + numberOfNodesEvaluated);
      System.out.println("Max Depth Reached: " + maxDepthReached);
      System.out.println("Avg Effective Branching Factor: " + avgFactor);
    }

    /**
     * This function will start the alpha-beta search
     * @param state This is the current game state
     * @param depth This is the specified search depth
     */
    public void run(GameState state, int depth, int nTaken) {
        
      intitalDepth = depth;
      double alpha = Double.NEGATIVE_INFINITY;
      double beta = Double.POSITIVE_INFINITY;
      boolean maxPlayer;
      if (nTaken % 2 == 0)
        maxPlayer = true;
      else 
        maxPlayer = false;
      
      
      value = alphabeta(state, 0, alpha, beta, maxPlayer);
      
      avgFactor =  (double) (numberOfNodesVisited -1) / (numberOfNodesVisited - numberOfNodesEvaluated);
      avgFactor =  Math.round(avgFactor  * 10.0) / 10.0;
   
      
    }

    /**
     * This method is used to implement alpha-beta pruning for both 2 players
     * @param state This is the current game state
     * @param depth Current depth of search
     * @param alpha Current Alpha value
     * @param beta Current Beta value
     * @param maxPlayer True if player is Max Player; Otherwise, false
     * @return int This is the number indicating score of the best next move
     */
    private double alphabeta(GameState state, int depth, double alpha, double beta, boolean maxPlayer) {

        numberOfNodesVisited++;
     
        if (depth == intitalDepth || state.getSuccessors().isEmpty()) {
          numberOfNodesEvaluated++;
          maxDepthReached = Math.max(maxDepthReached, depth);
          if (!maxPlayer)
            return state.evaluate();
          else
            return (-1) * state.evaluate();
        }
        
        double eval;
        if (maxPlayer) {
          double v = Double.NEGATIVE_INFINITY;
          int max = 0;
          for (GameState child : state.getSuccessors()) {
            eval = alphabeta(child, depth + 1, alpha, beta, false);
            
            if (eval > v) {
              v = eval;
              max = child.getLastMove();
            } 
            if (v >= beta) {
              move = max;
              return v;
            }
            alpha = Math.max(alpha, eval);
            
          }
          move = max;
          return v;
        } else {
          double v = Double.POSITIVE_INFINITY;
          int min = 0;
          for (GameState child : state.getSuccessors()) {
            eval = alphabeta(child, depth + 1, alpha, beta, true); 
            if (eval < v) {
              v = eval;
              min = child.getLastMove();
            }

            if (v <= alpha) {
              move = min;
              return v;    
            }
                   
            beta = Math.min(beta, eval);
  
          }
          move = min;
          return v;

        }  
    }
}
