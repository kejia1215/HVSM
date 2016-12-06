import utils.Evaluation;
import genematching.GenePair;

import java.util.List;

/**
 * Created by root on 10/26/16.
 */
public class TCSS_Result {
  private static final String humanPosPath = "/home/jk/workspace/experiment/TCSS_results/human/IEA+/positives.tcss_bma.IEA.F";
  private static final String humanNegPath = "/home/jk/workspace/experiment/TCSS_results/human/IEA+/negatives.tcss_bma.IEA.F";
  private static final String yeastPosPath = "/home/jk/workspace/experiment/TCSS_results/yeast/IEA+/positives.tcss_bma.IEA.F";
  private static final String yeastNegPath = "/home/jk/workspace/experiment/TCSS_results/yeast/IEA+/negatives.tcss_bma.IEA.F";

  public static void main(String[] args) {
    List<GenePair> pos_pair = Example.getGenePair(yeastPosPath,"\t");
    for(GenePair pair:pos_pair)pair.total_sim = pair.expected_similarity;
    List<GenePair> neg_pair = Example.getGenePair(yeastNegPath,"\t");
    for(GenePair pair:neg_pair)pair.total_sim = pair.expected_similarity;
    //Evaluation.calROCRes(pos_pair,neg_pair);
    Evaluation.f1Measure(pos_pair,neg_pair);
  }
}
