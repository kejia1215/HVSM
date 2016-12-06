package genematching;

import java.io.*;
import java.util.*;

/**
 * Created by root on 10/23/16.
 */
public class GeneAnnotations {
    private GOntology ontology;
    List<Annotation> annotations;
    private Map<String, Annotation> annotationMap;

    public GeneAnnotations(GOntology ontology) {
        this.ontology = ontology;
        this.annotations = new ArrayList<Annotation>();
        this.annotationMap = new HashMap<String, Annotation>();
    }

    public GeneAnnotations(GOntology ontology, List<Annotation> annotations,
                           Map<String, Annotation> annotationMap) {
        this.ontology = ontology;
        this.annotations = annotations;
        this.annotationMap = annotationMap;
    }

    public void addAnnotate(Annotation anno) {
        this.annotations.add(anno);
        annotationMap.put(anno.geneId, anno);
    }

    public List<GOTerm> getRelatedTerms(String geneId, TermDomain domain) {
        Annotation annotation = annotationMap.get(geneId);
        List<GOTerm> related_terms = new ArrayList<GOTerm>();
        for (GOTerm term : annotation.annotates) {
            if (term.domain == domain) {
                related_terms.add(term);
            }
        }
        return related_terms;
    }

    public static GeneAnnotations parseFromFile(String path, String targetDb, GOntology ontology)
            throws IOException {
        return parseFromFile(path, targetDb, null, ontology);
    }

    public static GeneAnnotations parseFromFile(String path, String targetDb, Set<String> filter, GOntology ontology)
            throws IOException {
        return parseFromFile(path, targetDb, filter, "gb2312", ontology);
    }

    public static GeneAnnotations parseFromFile(final String path,
                                                final String targetDb, final Set<String> filterGene, final String encode, final GOntology ontology)
            throws IOException {
        InputStream input = new FileInputStream(path);
        BufferedReader reader = new BufferedReader(new InputStreamReader(input, encode));
        String line = null;
        int flag = 0;
        List<Annotation> annotations = new ArrayList<Annotation>();
        Map<String, Annotation> annotationMap = new HashMap<String, Annotation>();
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            String kb = line.split("\t")[0].trim();
            if (!kb.equals(targetDb))
                continue;
            String[] values = line.split("\t");
            String gene = values[1].trim();
            String term = values[4].trim();
            String domain = values[8].trim();
            String evidence = values[6].trim();
            if (filterGene != null && !filterGene.contains(gene))
                continue;
            Annotation annotation = null;
            if (annotationMap.containsKey(gene)) {
                annotation = annotationMap.get(gene);
            } else {
                annotation = new Annotation(gene);
            }
            annotation.addAnnotate(term, ontology);
            annotationMap.put(gene, annotation);
        }
        return new GeneAnnotations(ontology, annotations, annotationMap);
    }
}
