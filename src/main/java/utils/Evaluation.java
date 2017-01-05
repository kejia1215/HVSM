package utils;

import genematching.GenePair;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

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

    public static double calPearsonSimilarity(List<GenePair> pairs) throws Exception {
        double[] cal_sim = new double[pairs.size()];
        double[] exp_sim = new double[pairs.size()];
        for(int i=0;i<pairs.size();i++){
            GenePair pair = pairs.get(i);
            cal_sim[i] = pair.total_sim;
            exp_sim[i] = pair.expected_similarity;
        }
        return personSimilarity(cal_sim,exp_sim);
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

    /*
    Precision：P=TP/(TP+FP)
    Recall：R=TP/(TP+FN)
    F1-score：2/(1/P+1/R)
    ROC/AUC：TPR=TP/(TP+FN), FPR=FP/(FP+TN)
     */
    public static double calROCRes(List<GenePair> pos_pairs, List<GenePair> neg_pairs) {
        for (GenePair pair : pos_pairs) pair.pos = true;
        for (GenePair pair : neg_pairs) pair.pos = false;
        double total_pos = pos_pairs.size();
        double total_neg = neg_pairs.size();
        List<GenePair> pairs = new ArrayList<GenePair>();
        pairs.addAll(pos_pairs);
        pairs.addAll(neg_pairs);
        Collections.shuffle(pairs);
        Collections.sort(pairs, new Comparator<GenePair>() {
            public int compare(GenePair o1, GenePair o2) {
                if (o1.total_sim == o2.total_sim)
                    return 0;
                return o1.total_sim > o2.total_sim ? -1 : 1;
                /*Double a = o1.total_sim;
                Double b = o2.total_sim;
                return b.compareTo(a);*/
            }
        });
        int pos_num = 0, neg_num = 0;
        int TP=0,FP=0,TN=neg_pairs.size(),FN=pos_pairs.size();
        double x =0,y=0;
        double pos = 0, neg = 0;
        double area = 0;
        for (int i = 0; i < pairs.size(); i++) {
            if(pairs.get(i).pos){
                TP++;FN--;
            }else{
                FP++;TN--;
            }
            area+=(y+TP*1.0/(TP+FN))*(FP*1.0/(FP+TN)-x)/2.0;
            x = FP*1.0/(FP+TN);
            y = TP*1.0/(TP+FN);
            System.out.println(x+"\t"+y);
            /*if (pairs.get(i).pos) pos_num++;
            else neg_num++;
            System.out.println(pos_num / total_pos + "\t" + neg_num / total_neg);
            area += (pos + pos_num / total_pos) * (neg_num / total_neg - neg) / 2;
            pos = pos_num / total_pos;
            neg = neg_num / total_neg;*/
        }
        System.out.println("The AUC value is: " + area);
        return area;
    }

    public static double f1Measure(List<GenePair> pos_pairs, List<GenePair> neg_pairs) {
        for (GenePair pair : pos_pairs) pair.pos = true;
        for (GenePair pair : neg_pairs) pair.pos = false;
        double total_pos = pos_pairs.size();
        double total_neg = neg_pairs.size();
        List<GenePair> pairs = new ArrayList<GenePair>();
        pairs.addAll(pos_pairs);
        pairs.addAll(neg_pairs);
//        Collections.shuffle(pairs);
        Collections.sort(pairs, new Comparator<GenePair>() {
            public int compare(GenePair o1, GenePair o2) {
                if (o1.total_sim == o2.total_sim)
                    return 0;
                return o1.total_sim > o2.total_sim ? 1 : -1;
                /*Double a = o1.total_sim;
                Double b = o2.total_sim;
                return b.compareTo(a);*/
            }
        });
        int pos_num = 0, neg_num = 0;
        int TP=pos_pairs.size(),FP=neg_pairs.size(),TN=0,FN=0;
        double pos = 0, neg = 0;
        double x =0,y=2.0*TP / (2*TP + FP + FN);
        System.out.println(x +"\t" + y);
        double area = 0, max = 0;
        for (int i = 0; i < pairs.size(); i++) {
            double sim = pairs.get(i).total_sim;
            if(pairs.get(i).pos){
                TP--;FN++;
            }else{
                FP--;TN++;
            }
            if(sim < 0)continue;
            if(sim > 1)continue;
            area+=(y+2.0*TP / (2*TP + FP + FN))*(sim-x)/2.0;
            x = sim;
            y = 2.0 * TP / (2*TP + FP + FN);
            max = max > y ? max : y;
            System.out.println(x+"\t"+y);
            /*if (pairs.get(i).pos) pos_num++;
            else neg_num++;
            System.out.println(pos_num / total_pos + "\t" + neg_num / total_neg);
            area += (pos + pos_num / total_pos) * (neg_num / total_neg - neg) / 2;
            pos = pos_num / total_pos;
            neg = neg_num / total_neg;*/
        }
        System.out.println("The F1-Score Area value is: " + area);
        System.out.println("The Max F1-Score value is: "+max);
        return area;
    }
}
