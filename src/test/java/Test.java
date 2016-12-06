import utils.Evaluation;
import genematching.GenePair;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/21.
 */
public class Test {
    public static final String hposPath = "data/experiment/TCSS_results/human/IEA+/positives.tcss_max.IEA.F";
    public static final String hnegPath = "data/experiment/TCSS_results/human/IEA+/negatives.tcss_max.IEA.F";
    public static final String yposPath = "data/experiment/TCSS_results/yeast/IEA+/positives.tcss_max.IEA.F";
    public static final String ynegPath = "data/experiment/TCSS_results/yeast/IEA+/negatives.tcss_max.IEA.F";
    public static void main(String[] args) throws IOException {
        String line = null;
        List<GenePair> pos_pairs = readData(yposPath);
        List<GenePair> neg_pairs = readData(ynegPath);
        Evaluation.calROCRes(pos_pairs,neg_pairs);
    }

    private static List<GenePair> readData(String posPath) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(posPath)));
        String line = null;
        List<GenePair> pairs = new ArrayList<GenePair>();
        while((line = reader.readLine())!=null){
            String[] splits = line.split("\t");
            double sim = Double.valueOf(splits[2]);
            GenePair pair = new GenePair(splits[0],splits[1],-1);
            pair.total_sim = sim;
            pairs.add(pair);
        }
        reader.close();
        return pairs;
    }
}
