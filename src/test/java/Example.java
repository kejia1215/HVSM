import genematching.*;
import org.xml.sax.SAXException;
import utils.Evaluation;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.*;

public class Example {
    public static final String cessmxmlPath = "data/experiment/myexp/go_200808-termdb.obo-xml";
    public static final String cessmPath = "data/experiment/myexp/proteinpairs.txt";
    public static final String cessmAssocPath = "data/experiment/myexp/gene_association.goa_uniprot";

    public static final String newxmlPath = "data/experiment/myexp/go_monthly-termdb.obo-xml";
    public static final String geneYPosPath = "data/experiment/datasets/yeast_data/iea+/positives.sgd.iea.";
    public static final String geneYNegPath = "data/experiment/datasets/yeast_data/iea+/negatives.sgd.iea.";
    public static final String geneHPosPath = "data/experiment/datasets/human_data/iea+/positives.human.iea.";
    public static final String geneHNegPath = "data/experiment/datasets/human_data/iea+/negatives.human.iea.";
    public static final Set<String> genePairs = new HashSet<String>();
    public static final String yeastAssocPath = "data/experiment/datasets/yeast_data/gene_association.sgd";
    public static final String humanAssocPath = "data/experiment/datasets/human_data/gene_association.goa_human";

    public static final String expressionCPath = "data/experiment/datasets/expression_data/expression.c";
    public static final String expressionPPath = "data/experiment/datasets/expression_data/expression.p";
    public static final String expressionFPath = "data/experiment/datasets/expression_data/expression.f";
    public static final String expressionAssocPath="data/experiment/datasets/expression_data/gene_association.sgd";

    public static final String hrssHPosPath = "data/experiment/HRSS/hrss/ppi/human_pos_allevi_hrss.";
    public static final String hrssHNegPath = "data/experiment/HRSS/hrss/ppi/human_neg_allevi_hrss.";
    public static final String hrssYPosPath = "data/experiment/HRSS/hrss/ppi/yeast_pos_allevi_hrss.";
    public static final String hrssYNegPath = "data/experiment/HRSS/hrss/ppi/yeast_neg_allevi_hrss.";
    public static final String hrssYAssocPath = "data/experiment/HRSS/data/gene_association.sgd";


    public static final String YPosPath = "data/experiment/datasets/yeast_data/remove_gene/positives.yeast.";
    public static final String YNegPath = "data/experiment/datasets/yeast_data/remove_gene/negatives.yeast.";
    public static final String HPosPath = "data/experiment/datasets/human_data/remove_gene/positives.human.";
    public static final String HNegPath = "data/experiment/datasets/human_data/remove_gene/negatives.human.";

    public static void main(String[] args)
            throws Exception {
//        predictDisData(cessmxmlPath,cessmPath,cessmAssocPath,"UniProtKB");

//        predictDisData(newxmlPath, expressionCPath, expressionAssocPath, "SGD");

//        predictBinaryData(newxmlPath, hrssHPosPath, hrssHNegPath, humanAssocPath, "f", "UniProtKB");
//        predictBinaryData(newxmlPath, hrssYPosPath, hrssYNegPath, yeastAssocPath, "c", "SGD");

        //未删除数据，用之前删除后缀，逗号分隔符
//        predictBinaryData(newxmlPath, geneHPosPath, geneHNegPath, humanAssocPath, "f","UniProtKB");
//        predictBinaryData(newxmlPath, geneYPosPath, geneYNegPath, yeastAssocPath, "f","SGD");
        //删除无ID数据，后缀加intelgo，tab分隔符
//        predictBinaryData(newxmlPath, HPosPath, HNegPath, humanAssocPath, "f","UniProtKB");
//      predictBinaryData(newxmlPath, YPosPath, YNegPath, yeastAssocPath, "f","SGD");
    }

    public static void predictDisData(String ontologyPath, String genePath, String assocPath,String db) throws Exception {
        GOntology ontology = GOntology.parseFromXml(ontologyPath);
        List<GenePair> pairs = getGenePair(genePath, "\t");
        GeneAnnotations annotations = GeneAnnotations.parseFromFile(assocPath, db,
                genePairs, ontology);
        ontology.calTermWeight();
        GeneMatching matching = new GeneMatching(ontology, annotations);//.setWithWeight(true);

        for (GenePair pair : pairs) {
            String pair1 = pair.id1;
            String pair2 = pair.id2;
            double sim = matching.calCCGeneSimilarity(pair1, pair2);
            pair.total_sim = sim;
            System.out.println(pair.id1 + "\t" + pair.id2 + "\t" + sim);
        }
        double corre = Evaluation.calPearsonSimilarity(pairs);
        System.out.println(corre);
    }

    public static void predictBinaryData(String ontologypath, String posPath, String negPath, String assocPath, String family,String db) throws Exception {
        GOntology ontology = GOntology.parseFromXml(ontologypath);
        List<GenePair> pos_pairs = getGenePair(posPath + family, ",");
        List<GenePair> neg_pairs = getGenePair(negPath + family, ",");
        GeneAnnotations annotations = GeneAnnotations.parseFromFile(assocPath, db,
                genePairs, ontology);
        ontology.calTermWeight();
        GeneMatching matching = new GeneMatching(ontology, annotations);//.setWithWeight(true);
        List<GenePair> remove_pairs = new ArrayList<GenePair>();
        int number0 = 0;
        for (GenePair pair : pos_pairs) {
            String pair1 = pair.id1;
            String pair2 = pair.id2;
            if (family.equals("c")) {
                double sim = matching.calCCGeneSimilarity(pair1, pair2);
                pair.total_sim = sim;
            } else if (family.equals("p")) {
                double sim = matching.calBPGeneSimilarity(pair1, pair2);
                pair.total_sim = sim;
            } else {
                double sim = matching.calMFGeneSimilarity(pair1, pair2);
                pair.total_sim = sim;
            }
            if(pair.total_sim == Double.MIN_VALUE || ((Double)pair.total_sim).isNaN()){
                remove_pairs.add(pair);
                continue;
            }
            if(pair.total_sim==0)number0++;
            //System.out.println(pair.id1+"\t"+pair.id2+"\t"+pair.total_sim);
        }
        pos_pairs.removeAll(remove_pairs);
        //System.out.println("-----------------Splitter----------------------------");
        remove_pairs.clear();
        for (GenePair pair : neg_pairs) {
            String pair1 = pair.id1;
            String pair2 = pair.id2;
            if (family.equals("c")) {
                double sim = matching.calCCGeneSimilarity(pair1, pair2);
                pair.total_sim = sim;
            } else if (family.equals("p")) {
                double sim = matching.calBPGeneSimilarity(pair1, pair2);
                pair.total_sim = sim;
            } else {
                double sim = matching.calMFGeneSimilarity(pair1, pair2);
                pair.total_sim = sim;
            }
            if(pair.total_sim == Double.MIN_VALUE || ((Double)pair.total_sim).isNaN()){
                remove_pairs.add(pair);
                continue;
            }
            if(pair.total_sim==0)number0++;
            //System.out.println(pair.id1+"\t"+pair.id2+"\t"+pair.total_sim);
        }
        neg_pairs.removeAll(remove_pairs);
        System.out.println("Number of Zero: "+ number0);
//        Evaluation.calROCRes(pos_pairs, neg_pairs);
      Evaluation.f1Measure(pos_pairs,neg_pairs);
    }

    public static List<GenePair> getGenePair(String path, String splitter) {
        List<GenePair> gene = new ArrayList<GenePair>();
        try {
            InputStream input = new FileInputStream(path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(input, "gb2312"));
            String line = null;
            int index = 0;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                String[] splits = line.split(splitter);
                String gene1 = splits[0];
                String gene2 = splits[1];
                GenePair pa = new GenePair(gene1, gene2, false);
                if (splits.length >= 3) {
                    Double exp_sim = Double.valueOf(splits[2]);
                    //if(exp_sim== -1 || exp_sim == -2)continue;
                    pa.expected_similarity = exp_sim;
                }
                genePairs.add(gene1);
                genePairs.add(gene2);
                gene.add(pa);
            }
            //System.out.println("Total readed Genes: "+gene.size());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return gene;

    }

}
