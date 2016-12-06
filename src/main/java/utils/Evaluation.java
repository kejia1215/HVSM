package utils;

import genematching.GenePair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by root on 10/25/16.
 */
public class Evaluation {

    public static double personSimilarity(double[] cc_vec1, double[] cc_vec2) {
        double simX = 0;
        double simY = 0;
        double simXX = 0;
        double simYY = 0;
        double simXY = 0;
        int N = 0;
        for (int i = 0; i < cc_vec1.length; i++) {
            //      if(cc_vec1[i]==0 || cc_vec2[i]==0)continue;
            N++;
            simX += cc_vec1[i];
            simY += cc_vec2[i];
            simXX += cc_vec1[i] * cc_vec1[i];
            simYY += cc_vec2[i] * cc_vec2[i];
            simXY += cc_vec1[i] * cc_vec2[i];
        }
        if (N == 0) return 0;
        double numerator = simXY - simX * simY / N;
        double denominator = Math.sqrt((simXX - simX * simX / N)
                * (simYY - simY * simY / N));
        if (denominator == 0) return 0;
        return numerator / denominator;
    }

    public static double cosienSimilarity(double[] vec1, double[] vec2) {
        double sim = 0;
        double simXX = 0;
        double simYY = 0;
        for (int i = 0; i < vec1.length; i++) {
            sim += vec1[i] * vec2[i];
            simXX += vec1[i] * vec1[i];
            simYY += vec2[i] * vec2[i];
        }
        if (simXX == 0 || simYY == 0) return 0;
        return sim / (Math.sqrt(simXX) * Math.sqrt(simYY));
    }

    public static double calROCRes(List<GenePair> pos_pairs, List<GenePair> neg_pairs) {
        for (GenePair pair : pos_pairs) pair.pos = true;
        for (GenePair pair : neg_pairs) pair.pos = false;
        double total_pos = pos_pairs.size();
        double total_neg = neg_pairs.size();
        List<GenePair> pairs = new ArrayList<GenePair>();
        pairs.addAll(pos_pairs);
        pairs.addAll(neg_pairs);
        Collections.sort(pairs, new Comparator<GenePair>() {
            public int compare(GenePair o1, GenePair o2) {
                if (o1.total_sim == o2.total_sim)
                    return 0;
                return o1.total_sim > o2.total_sim ? -1 : 1;
            }
        });
        int pos_num = 0, neg_num = 0;
        double pos = 0, neg = 0;
        double area = 0;
        for (int i = 0; i < pairs.size(); i++) {
            if (pairs.get(i).pos) pos_num++;
            else neg_num++;
            System.out.println(pos_num / total_pos + "\t" + neg_num / total_neg);
            area += (pos + pos_num / total_pos) * (neg_num / total_neg - neg) / 2;
            pos = pos_num / total_pos;
            neg = neg_num / total_neg;
        }
        System.out.println("The AUC value is: " + area);
        return area;
    }

    public static double f1Measure(List<GenePair> pos_pairs, List<GenePair> neg_pairs) {
        for (GenePair pair : pos_pairs) pair.pos = true;
        for (GenePair pair : neg_pairs) pair.pos = false;
        int total_pos = pos_pairs.size();
        int total_neg = neg_pairs.size();
        List<GenePair> pairs = new ArrayList<GenePair>();
        pairs.addAll(pos_pairs);
        pairs.addAll(neg_pairs);
        Collections.sort(pairs, new Comparator<GenePair>() {
            public int compare(GenePair o1, GenePair o2) {
                if (o1.total_sim == o2.total_sim)
                    return 0;
                return o1.total_sim > o2.total_sim ? 1 : -1;
            }
        });
        int tp = total_pos, fp = total_neg, fn = 0;
        double pos = 0, neg = 0;
        double area = 0;
        double f1 = 2.0 * tp / (2 * tp + fp + fn);
        System.out.println(f1 + "\t" + 0l);
        for (int i = 0; i < pairs.size(); i++) {
            if (!pairs.get(i).pos) {
                fn++;
                fp--;
            } else {
                tp--;
            }
            f1 = 2.0 * tp / (2 * tp + fp + fn);
            System.out.println(f1 + "\t" + (i + 1.0) / pairs.size());
        }
        return area;
    }
}
