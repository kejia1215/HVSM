import utils.Evaluation;
import genematching.GenePair;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 10/26/16.
 */
public class TCSS_Result {

  public static final String expressionCPath = "data/experiment/datasets/expression_data/expression.c";
  public static final String expressionPPath = "data/experiment/datasets/expression_data/expression.p";
  public static final String expressionFPath = "data/experiment/datasets/expression_data/expression.f";

  public static final String tcssexpCPath = "E:\\WorkSpace\\HVSM\\data\\experiment\\TCSS_results\\expression\\expression.tcss_bma.C";
  public static final String tcssexpPPath = "E:\\WorkSpace\\HVSM\\data\\experiment\\TCSS_results\\expression\\expression.tcss_bma.P";
  public static final String tcssexpFPath = "E:\\WorkSpace\\HVSM\\data\\experiment\\TCSS_results\\expression\\expression.tcss_max.F";


  public static final String hrssHPosPath = "data/experiment/HRSS/hrss/ppi/human_pos_allevi_hrss.p";
  public static final String hrssHNegPath = "data/experiment/HRSS/hrss/ppi/human_neg_allevi_hrss.p";


  private static final String humanRemovePosPath = "data/experiment/TCSS_results/human/IEA+/positives.tcss_max.IEA.C";
  private static final String humanRemoveNegPath = "data/experiment/TCSS_results/human/IEA+/negatives.tcss_max.IEA.C";
  private static final String yeastRemovePosPath = "data/experiment/TCSS_results/yeast/IEA+/positives.tcss_max.IEA.F";
  private static final String yeastRemoveNegPath = "data/experiment/TCSS_results/yeast/IEA+/negatives.tcss_max.IEA.F";

  //intelgo数据，加后缀.intelgo
  private static final String intelgoHPosPath = "data/experiment/TCSS_results/human/IEA+/positives.tcss_bma.IEA.F.intelgo";
  private static final String intelgoHNegPath = "data/experiment/TCSS_results/human/IEA+/negatives.tcss_bma.IEA.F.intelgo";
  private static final String intelgoYPosPath = "data/experiment/TCSS_results/yeast/IEA+/positives.tcss_bma.IEA.F.intelgo";
  private static final String intelgoYNegPath = "data/experiment/TCSS_results/yeast/IEA+/negatives.tcss_bma.IEA.F.intelgo";

  private static final String netsimYeastPosPath = "E:\\JK\\yeastArabidopsisPackage\\data\\genepair\\positives.tcss_max.IEA.F";
  private static final String netsimYeastNegPath = "E:\\JK\\yeastArabidopsisPackage\\data\\genepair\\negatives.tcss_max.IEA.F";

  private static final String humanPosPath = "data/experiment/TCSS_results/human/IEA+/positives.tcss_max.IEA.F";
  private static final String humanNegPath = "data/experiment/TCSS_results/human/IEA+/negatives.tcss_max.IEA.F";
  private static final String yeastPosPath = "data/experiment/TCSS_results/yeast/IEA+/positives.tcss_max.IEA.F";
  private static final String yeastNegPath = "data/experiment/TCSS_results/yeast/IEA+/negatives.tcss_max.IEA.F";

  public static void main(String[] args) throws Exception {
    List<GenePair> pos_pair = Example.getGenePair(yeastPosPath,"\t");
    for(GenePair pair:pos_pair)pair.total_sim = pair.expected_similarity;
    List<GenePair> neg_pair = Example.getGenePair(yeastNegPath,"\t");
    for(GenePair pair:neg_pair)pair.total_sim = pair.expected_similarity;
//    Evaluation.calROCRes(pos_pair,neg_pair);
    Evaluation.f1Measure(pos_pair,neg_pair);
    /*List<GenePair> pairs = Example.getGenePair(expressionFPath, "\t");
    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(tcssexpFPath),"utf-8"));
    String line = null;
    Map<GenePair,Double> pairDoubleMap = new HashMap<GenePair,Double>();
    while((line = br.readLine())!=null){
      String[] splits = line.split("\t");
      double sim = Double.valueOf(splits[2]);
      GenePair pair = new GenePair(splits[0],splits[1],sim);
      pairDoubleMap.put(pair,sim);
    }
    br.close();
    for(GenePair pair:pairs){
      double sim = pairDoubleMap.get(pair);
      pair.total_sim = sim;
    }
    double sim = Evaluation.calPearsonSimilarity(pairs);
    System.out.println(sim);*/
  }
}
