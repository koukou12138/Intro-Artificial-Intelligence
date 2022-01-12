import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Your implementation of a naive bayes classifier. Please implement all four methods.
 */

public class NaiveBayesClassifier implements Classifier {

  private int v;
  private Map<String, Integer> posWords;
  private Map<String, Integer> negWords;
  private int posCount;
  private int negCount;
  private int posTokenCount;
  private int negTokenCount;

  /**
   * Trains the classifier with the provided training data and vocabulary size
   */
  @Override
  public void train(List<Instance> trainData, int v) {
    // Hint: First, calculate the documents and words counts per label and store them.
    // Then, for all the words in the documents of each label, count the number of occurrences of
    // each word.
    // Save these information as you will need them to calculate the log probabilities later.
    //
    // e.g.
    // Assume m_map is the map that stores the occurrences per word for positive documents
    // m_map.get("catch") should return the number of "catch" es, in the documents labeled positive
    // m_map.get("asdasd") would return null, when the word has not appeared before.
    // Use m_map.put(word,1) to put the first count in.
    // Use m_map.replace(word, count+1) to update the value
    this.v = v;
    posWords = new HashMap<String, Integer>();
    negWords = new HashMap<String, Integer>();
    posCount = 0;
    negCount = 0;
    posTokenCount = v;
    negTokenCount = v;

    for (Instance i : trainData)
      if (i.label.equals(Label.POSITIVE)) {
        posCount++;
        for (String word : i.words) {
          posTokenCount++;
          if (posWords.containsKey(word))
            posWords.replace(word, posWords.get(word) + 1);
          else
            posWords.put(word, 1);
        }
      } else {
        negCount++;
        for (String word : i.words) {
          negTokenCount++;
          if (negWords.containsKey(word))
            negWords.replace(word, negWords.get(word) + 1);
          else
            negWords.put(word, 1);
        }
      }
  }


  /*
   * Counts the number of words for each label
   */
  @Override
  public Map<Label, Integer> getWordsCountPerLabel(List<Instance> trainData) {
    Map<Label, Integer> res = new HashMap<Label, Integer>();
    int pos = 0;
    int neg = 0;
    for (Instance i : trainData)
      if (i.label.equals(Label.POSITIVE))
        pos += i.words.size();
      else
        neg += i.words.size();

    res.put(Label.POSITIVE, pos);
    res.put(Label.NEGATIVE, neg);
    return res;
  }


  /*
   * Counts the total number of documents for each label
   */
  @Override
  public Map<Label, Integer> getDocumentsCountPerLabel(List<Instance> trainData) {
    Map<Label, Integer> res = new HashMap<Label, Integer>();
    int pos = 0;
    int neg = 0;
    for (Instance i : trainData) 
      if (i.label.equals(Label.POSITIVE)) 
        pos++;
      else 
        neg++;
      
    res.put(Label.POSITIVE, pos);
    res.put(Label.NEGATIVE, neg);
    return res;
  }


  /**
   * Returns the prior probability of the label parameter, i.e. P(POSITIVE) or P(NEGATIVE)
   */
  private double p_l(Label label) {
    // Calculate the probability for the label. No smoothing here.
    // Just the number of label counts divided by the number of documents.

    int total = posCount + negCount;
    if (label.equals(Label.POSITIVE))
      return (double) this.posCount / total;
    else
      return (double) this.negCount / total;

  }

  /**
   * Returns the smoothed conditional probability of the word given the label, i.e. P(word|POSITIVE)
   * or P(word|NEGATIVE)
   */
  private double p_w_given_l(String word, Label label) {

    int num = 1;
    if (label.equals(Label.POSITIVE)) {
      if (posWords.containsKey(word))
        num = this.posWords.get(word) + 1;
      return (double) num / posTokenCount;
    } else {
      if (negWords.containsKey(word))
        num = this.negWords.get(word) + 1;
      return (double) num / negTokenCount;
    }

  }

  /**
   * Classifies an array of words as either POSITIVE or NEGATIVE.
   */
  @Override
  public ClassifyResult classify(List<String> words) {
    // Sum up the log probabilities for each word in the input data, and the probability of the
    // label
    // Set the label to the class with larger log probability

    ClassifyResult cr = new ClassifyResult();
    Map<Label, Double> logProbPerLabel = new HashMap<Label, Double>();

    double pos = Math.log(p_l(Label.POSITIVE));
    double neg = Math.log(p_l(Label.NEGATIVE));

    for (String word : words) {
      pos += Math.log(p_w_given_l(word, Label.POSITIVE));
      neg += Math.log(p_w_given_l(word, Label.NEGATIVE));
    }

    logProbPerLabel.put(Label.POSITIVE, pos);
    logProbPerLabel.put(Label.NEGATIVE, neg);


    if (pos > neg)
      cr.label = Label.POSITIVE;
    else
      cr.label = Label.NEGATIVE;

    cr.logProbPerLabel = logProbPerLabel;

    return cr;
  }


}
