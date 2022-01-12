import java.util.ArrayList;
import java.util.List;

public class CrossValidation {
  /*
   * Returns the k-fold cross validation score of classifier clf on training data.
   */
  public static double kFoldScore(Classifier clf, List<Instance> trainData, int k, int v) {
    double score = 0.0;
    List<Instance> trainingSet = new ArrayList<Instance>();
    List<Instance> testingSet = new ArrayList<Instance>();


    int num = trainData.size() / k;
    for (int i = 0; i < k; i++) {
      trainingSet.clear();
      testingSet.clear();


      for (int j = 0; j < trainData.size(); j++) {

        if (j >= i * num && j < (i + 1) * num)
          testingSet.add(trainData.get(j));
        else
          trainingSet.add(trainData.get(j));

      }
      clf.train(trainingSet, v);
      int correct = 0;
      for (Instance instance : testingSet) {
        ClassifyResult res = clf.classify(instance.words);
        if (res.label.equals(instance.label))
          correct++;
      }
      
      score = (double) (score * i * testingSet.size() + correct) / (i+1) / testingSet.size();
    }

    return score;
  }
}
