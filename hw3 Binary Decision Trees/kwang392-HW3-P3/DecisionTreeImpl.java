import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Fill in the implementation details of the class DecisionTree using this file. Any methods or
 * secondary classes that you want are fine but we will only interact with those methods in the
 * DecisionTree framework.
 */
public class DecisionTreeImpl {
  public DecTreeNode root;
  public List<List<Integer>> trainData;
  public int maxPerLeaf;
  public int maxDepth;
  public int numAttr;
  private Map<Integer, Double> attributeValue;
  private Map<Integer, Integer> attributeTh;

  // Build a decision tree given a training set
  DecisionTreeImpl(List<List<Integer>> trainDataSet, int mPerLeaf, int mDepth) {
    this.trainData = trainDataSet;
    this.maxPerLeaf = mPerLeaf;
    this.maxDepth = mDepth;
    if (this.trainData.size() > 0)
      this.numAttr = trainDataSet.get(0).size() - 1;
    this.attributeValue = new HashMap<Integer, Double>();
    this.attributeTh = new HashMap<Integer, Integer>();
    this.root = buildTree(trainData, 0);
  }

  private DecTreeNode buildTree(List<List<Integer>> dataSet, int depth) {
    if (numAttr == 0)
      return null;

    int label = this.getLabel(dataSet);

    if (depth == maxDepth || dataSet.size() <= this.maxPerLeaf)
      return new DecTreeNode(label, -1, -1);
    
    int best_at = best_attribute(dataSet);
    
    if (attributeValue.get(best_at) == 0) {
      return new DecTreeNode(label, -1, -1);
    }
    
    DecTreeNode node = new DecTreeNode(label, best_at, attributeTh.get(best_at));

    List<List<Integer>> leftDataSet = new ArrayList<>();
    List<List<Integer>> rightDataSet = new ArrayList<>();
    for (int i = 0; i < dataSet.size(); i++) {
      if (dataSet.get(i).get(best_at) <= attributeTh.get(best_at)) {
        leftDataSet.add(dataSet.get(i));
      } else if (dataSet.get(i).get(best_at) > attributeTh.get(best_at)) {
        rightDataSet.add(dataSet.get(i));
      }
    }
    depth++;
    node.left = buildTree(leftDataSet, depth);
    node.right = buildTree(rightDataSet, depth);
    return node;
  }

  private void buildMap(List<List<Integer>> dataSet) {
    attributeValue.clear();
    attributeTh.clear();
    for (int i = 0; i < numAttr; i++)
      best_threshold(i, dataSet);

  }

  private int best_attribute(List<List<Integer>> dataSet) {
    buildMap(dataSet);
    int best_at = 0;
    double best_value = 0;

    for (int attr : attributeValue.keySet()) {
      if (attributeValue.get(attr) - best_value > 0) {
        best_at = attr;
        best_value = attributeValue.get(attr);
      }
    }

    return best_at;
  }

  private void best_threshold(int attr, List<List<Integer>> dataSet) {    
    int[] thresholds = {1,2,3,4,5,6,7,8,9};

    int best_th = 0;
    double best_value = 0;

    for (int th : thresholds) {
      double l_0_count = 0;
      double r_0_count = 0;
      double l_1_count = 0;
      double r_1_count = 0;
      for (int i = 0; i < dataSet.size(); i++) {
        if (dataSet.get(i).get(attr) - th <= 0) {
          if (dataSet.get(i).get(numAttr) == 0)
            l_0_count++;
          else
            l_1_count++;
        } else {
          if (dataSet.get(i).get(numAttr) == 0)
            r_0_count++;
          else
            r_1_count++;
        }
      }
      double leftTotal = l_0_count + l_1_count;
      double rightTotal = r_0_count + r_1_count;
      double total = leftTotal + rightTotal;
      
      double iClass = H((l_0_count+r_0_count)/total, (l_1_count+r_1_count)/total);
      
      double hClass = leftTotal/total * H(l_0_count/leftTotal, l_1_count/leftTotal) + 
          rightTotal/total * H(r_0_count/rightTotal, r_1_count/rightTotal);
      
      double iGain = iClass - hClass;
      
      if (iGain - best_value > 0) {
        best_value = iGain;
        best_th = th;
      }
    }
    attributeValue.put(attr, best_value);
    attributeTh.put(attr, best_th);
  }

  private int getLabel(List<List<Integer>> dataSet) {
    double label0 = 0;
    double label1 = 0;
    for (List<Integer> data : dataSet) {
      if (data.get(numAttr) == 0) {
        label0++;
      } else
        label1++;
    }
    return label0 > label1 ? 0 : 1;
  }

  private double H(double x) {
    if (x == 0)
      return 0;
    return (-1) * x * Math.log(x) / Math.log(2);
  }

  private double H(double x, double y) {
    return H(x) + H(y);
  }

  public int classify(List<Integer> instance) {
    DecTreeNode node = root;
    while (!node.isLeaf()) {
      if (instance.get(node.attribute) <= node.threshold) {
        node = node.left;
      } else
        node = node.right;
    }
    return node.classLabel;
  }

  // Print the decision tree in the specified format
  public void printTree() {
    printTreeNode("", this.root);
  }

  public void printTreeNode(String prefixStr, DecTreeNode node) {
    String printStr = prefixStr + "X_" + node.attribute;
    System.out.print(printStr + " <= " + String.format("%d", node.threshold));
    if (node.left.isLeaf()) {
      System.out.println(" : " + String.valueOf(node.left.classLabel));
    } else {
      System.out.println();
      printTreeNode(prefixStr + "|\t", node.left);
    }
    System.out.print(printStr + " > " + String.format("%d", node.threshold));
    if (node.right.isLeaf()) {
      System.out.println(" : " + String.valueOf(node.right.classLabel));
    } else {
      System.out.println();
      printTreeNode(prefixStr + "|\t", node.right);
    }
  }

  public double printTest(List<List<Integer>> testDataSet) {
    int numEqual = 0;
    int numTotal = 0;
    for (int i = 0; i < testDataSet.size(); i++) {
      int prediction = classify(testDataSet.get(i));
      int groundTruth = testDataSet.get(i).get(testDataSet.get(i).size() - 1);
      System.out.println(prediction);
      if (groundTruth == prediction) {
        numEqual++;
      }
      numTotal++;
    }
    double accuracy = numEqual * 100.0 / (double) numTotal;
    System.out.println(String.format("%.2f", accuracy) + "%");
    return accuracy;
  }

}
