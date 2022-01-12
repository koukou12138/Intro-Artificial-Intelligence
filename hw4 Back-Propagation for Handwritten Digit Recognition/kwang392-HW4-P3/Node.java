import java.util.*;

/**
 * Class for internal organization of a Neural Network. There are 5 types of nodes. Check the type
 * attribute of the node for details. Feel free to modify the provided function signatures to fit
 * your own implementation
 */

public class Node {
  private int type = 0; // 0=input,1=biasToHidden,2=hidden,3=biasToOutput,4=Output
  public ArrayList<NodeWeightPair> parents = null; // Array List that will contain the parents
                                                   // (including the bias node) with weights if
                                                   // applicable

  private double inputValue = 0.0;
  private double outputValue = 0.0;
  private double outputGradient = 0.0;
  private double delta = 0.0; // input gradient
  private double z = 0.0; // total output used for softmax
  private double realValue = 0.0;

  // Create a node with a specific type
  Node(int type) {
    if (type > 4 || type < 0) {
      System.out.println("Incorrect value for node type");
      System.exit(1);

    } else {
      this.type = type;
    }

    if (type == 2 || type == 4) {
      parents = new ArrayList<>();
    }
  }

  // For an input node sets the input value which will be the value of a particular attribute
  public void setInput(double inputValue) {
    if (type == 0) { // If input node
      this.inputValue = inputValue;
    }
  }

  /**
   * Calculate the output of a node. You can get this value by using getOutput()
   */
  public void calculateOutput() {
    if (type == 2 || type == 4) { // Not an input or bias node
      inputValue = 0.0;
      for (NodeWeightPair p : parents)
        inputValue += p.node.getOutput() * p.weight;

      if (type == 2)
        outputValue = Math.max(0, inputValue);

      else if (type == 4)
        outputValue = Math.exp(inputValue) / z;
    }
  }

  // Gets the output value
  public double getOutput() {

    if (type == 0) { // Input node
      return inputValue;
    } else if (type == 1 || type == 3) { // Bias node
      return 1.00;
    } else {
      return outputValue;
    }

  }
  
  // Calculate the delta value of a node.
  public void calculateDelta() {
    if (type == 2) {
      
      if (inputValue > 0) 
        delta = outputGradient;
      else 
        delta = 0;
      
    } else
      delta = realValue - outputValue;
    
  }
  
  public void setRealValue(double value) {
    this.realValue = value;
    
  }
  
  public void updateGradient(ArrayList<Node> nodes) {
    if (type == 2) {
      outputGradient = 0.0;
      for (Node n : nodes) 
        for (NodeWeightPair p : n.parents) 
          if (p.node.equals(this)) 
            outputGradient += (p.weight * n.delta);
    }
    
  }

  // Update the weights between parents node and current node
  public void updateWeight(double learningRate) {
    if (type == 2 || type == 4) 
      for (NodeWeightPair p : parents) 
        p.weight += learningRate * p.node.getOutput() * delta;
     
  }


  public void setZ(ArrayList<Node> nodes) {
    if (type == 4) {
      double value = 0.0;
      for (Node n : nodes) {
        double sum = 0.0;
        for (NodeWeightPair p : n.parents)
          sum += p.node.getOutput() * p.weight;
        
        value += Math.exp(sum);
      }

      this.z = value;
    }
  }

}


