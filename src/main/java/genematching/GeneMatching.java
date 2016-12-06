package genematching;

import utils.Evaluation;

import java.util.*;

public class GeneMatching {
    private GeneAnnotations annotations;
    private GOntology ontology;
    private boolean withWeight = false;

    public GeneMatching(GOntology ontology, GeneAnnotations annotations) {
        this.annotations = annotations;
        this.ontology = ontology;
        this.withWeight = false;
    }

    public GeneMatching setWithWeight(boolean withWeight) {
        this.withWeight = withWeight;
        return this;
    }

    private List<GOTerm> getIntersectTerm(List<GOTerm> related_terms1, List<GOTerm> related_terms2) {
        List<GOTerm> terms1 = new ArrayList<GOTerm>();
        for (GOTerm term : related_terms1) {
            terms1.addAll(term.getChildren());
        }
        List<GOTerm> terms2 = new ArrayList<GOTerm>();
        for (GOTerm term : related_terms2) {
            terms2.addAll(term.getChildren());
        }
        terms1.retainAll(terms2);
        return terms1;
    }

    public double calCCSimilarity(String gene1, String gene2) {
        List<GOTerm> cc_terms = ontology.getDataBase(TermDomain.CC);
        List<GOTerm> related_terms1 = annotations.getRelatedTerms(gene1, TermDomain.CC);
        if (related_terms1.size() <= 3) return Double.MIN_VALUE;

        List<GOTerm> related_terms2 = annotations.getRelatedTerms(gene2, TermDomain.CC);
        if (related_terms2.size() <= 3) return Double.MIN_VALUE;
        List<GOTerm> child_terms = getIntersectTerm(related_terms1, related_terms2);

        double[] vector1 = calVector(cc_terms, related_terms1, child_terms);
        double[] vector2 = calVector(cc_terms, related_terms2, child_terms);
        return Evaluation.cosienSimilarity(vector1, vector2) * Math.log(related_terms1.size() + related_terms2.size());
    }

    public double calBPSimilarity(String gene1, String gene2) {
        List<GOTerm> cc_terms = ontology.getDataBase(TermDomain.BP);
        List<GOTerm> related_terms1 = annotations.getRelatedTerms(gene1, TermDomain.BP);
        if (related_terms1.size() <= 3) return Double.MIN_VALUE;

        List<GOTerm> related_terms2 = annotations.getRelatedTerms(gene2, TermDomain.BP);
        if (related_terms2.size() <= 3) return Double.MIN_VALUE;

        List<GOTerm> child_terms = getIntersectTerm(related_terms1, related_terms2);
        double[] vector1 = calVector(cc_terms, related_terms1, child_terms);
        double[] vector2 = calVector(cc_terms, related_terms2, child_terms);
        return Evaluation.cosienSimilarity(vector1, vector2) * Math.log(related_terms1.size() + related_terms2.size());
    }

    public double calMFSimilarity(String gene1, String gene2) {
        List<GOTerm> cc_terms = ontology.getDataBase(TermDomain.MF);
        List<GOTerm> related_terms1 = annotations.getRelatedTerms(gene1, TermDomain.MF);
        if (related_terms1.size() <= 3) return Double.MIN_VALUE;

        List<GOTerm> related_terms2 = annotations.getRelatedTerms(gene2, TermDomain.MF);
        if (related_terms2.size() <= 3) return Double.MIN_VALUE;

        List<GOTerm> child_terms = getIntersectTerm(related_terms1, related_terms2);

        double[] vector1 = calVector(cc_terms, related_terms1, child_terms);
        double[] vector2 = calVector(cc_terms, related_terms2, child_terms);
        return Evaluation.cosienSimilarity(vector1, vector2) * Math.log(related_terms1.size() + related_terms2.size());
    }

    private double[] calVector(List<GOTerm> terms, List<GOTerm> related_terms, List<GOTerm> intersectChild) {
        double[] vector = new double[terms.size()];
        Map<String, Integer> mapIndex = new HashMap<String, Integer>();
        for (GOTerm term : terms) {
            mapIndex.put(term.id, mapIndex.size());
        }
        Queue<GOTerm> queue = new LinkedList<GOTerm>(related_terms);
        Queue<GOTerm> backup = new LinkedList<GOTerm>();
        double init_value = 1.0;
        int level = 3;/*
        while (!queue.isEmpty()) {
            if (level <= 0) break;
            GOTerm term = queue.poll();
            int index = mapIndex.get(term.id);
            if (withWeight) {
                vector[index] = term.weight * (vector[index] >0 ? vector[index]+0.15 : init_value);//Math.max(vector[index], init_value);
            } else {
                vector[index] = vector[index] >0 ? vector[index]+0.15 : init_value;//Math.max(vector[index], init_value);
            }
            for (GOTerm parent : term.getParent()) {
                backup.add(parent);
            }
            if (queue.isEmpty()) {
                Queue<GOTerm> temp = queue;
                queue = backup;
                backup = temp;
                init_value /= 2;
                level--;
            }
        }*/
        Queue<GOTerm> child_queue = new LinkedList<GOTerm>();
        child_queue.addAll(intersectChild);
        level = 2;
        init_value = 0.5;
        backup.clear();
        while (!child_queue.isEmpty()) {
            if (level <= 0) break;
            GOTerm term = child_queue.poll();
            int index = mapIndex.get(term.id);
            if (withWeight) {
                vector[index] = term.weight * (vector[index] > 0 ? vector[index] + 0.15 : init_value);//Math.max(vector[index], init_value);
            } else {
                vector[index] = vector[index] > 0 ? vector[index] + 0.15 : init_value;//Math.max(vector[index], init_value);
            }
            for (GOTerm parent : term.getChildren()) {
                backup.add(parent);
            }
            if (child_queue.isEmpty()) {
                Queue<GOTerm> temp = child_queue;
                child_queue = backup;
                backup = temp;
                init_value /= 2;
                level--;
            }
        }
        return vector;
    }
}
