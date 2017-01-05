package main;

import genematching.GOntology;
import genematching.GeneAnnotations;
import genematching.GeneMatching;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import static main.Main.DB.CC;

/**
 * Created by Administrator on 2017/1/5.
 */
public class HVSM {
    private static Properties properties = new Properties();
    static {
        properties.put("GO", "gene_ontology.obo");
        properties.put("Annotation", "gene_association.sgd");
        try {
            properties.load(new FileInputStream("HVSM.ini"));
        } catch (IOException e) {
            System.err.println("Not Found Configuration File, use default values.");
        }
    }

    public static void RunTermSet(Main.Orgonism org, Main.DB db, InputStream is, OutputStream os) throws Exception {
        GOntology ontology = GOntology.parseFromXml(properties.getProperty("GO"));
        GeneMatching matching = new GeneMatching(ontology, null);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
        String line = null;
        while((line = reader.readLine())!=null){
            String[] splits = line.split("\t");
            if(splits.length != 2)throw new Exception("Input Must be two genes and split by \"tab\"");
            Set<String> termset1 = new HashSet<String>();
            for(String termid: splits[0].split(",")) {
                termset1.add(termid);
            }
            Set<String> termset2 = new HashSet<String>();
            for(String termid: splits[1].split(",")) {
                termset2.add(termid);
            }
            double similarity = matching.calTermSetSimilarity(termset1, termset2);
            os.write((similarity + "\n").getBytes());
            os.flush();
        }
        is.close();
        os.close();
    }

    public static void RunGenePair(Main.Orgonism org, Main.DB db, InputStream is, OutputStream os) throws Exception {
        GOntology ontology = GOntology.parseFromXml(properties.getProperty("GO"));
        GeneAnnotations annotations = GeneAnnotations.parseFromFile(properties.getProperty("Annotation"), org.name,
                null, ontology);
        GeneMatching matching = new GeneMatching(ontology, annotations);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
        String line = null;
        while((line = reader.readLine())!=null){
            String[] splits = line.split("\t");
            if(splits.length != 2)throw new Exception("Input Must be two genes and split by \"tab\"");
            double similarity;
            switch (db){
                case CC: similarity = matching.calCCGeneSimilarity(splits[0], splits[1]);break;
                case BP: similarity = matching.calBPGeneSimilarity(splits[0], splits[1]);break;
                default: similarity = matching.calMFGeneSimilarity(splits[0], splits[1]);
            }
            os.write((similarity + "\n").getBytes());
            os.flush();
        }
        is.close();
        os.close();
    }
}
