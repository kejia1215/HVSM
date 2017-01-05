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
    public double calCCGeneSimilarity(String gene1, String gene2) {
        return calCCGeneSimilarity(gene1,gene2,Math.E);
    }

    public double calCCGeneSimilarity(String gene1, String gene2, double base){
        List<GOTerm> cc_terms = ontology.getDataBase(TermDomain.CC);
        List<GOTerm> related_terms1 = annotations.getRelatedTerms(gene1, TermDomain.CC);
        List<GOTerm> related_terms2 = annotations.getRelatedTerms(gene2, TermDomain.CC);
        return calCCSimilarity(cc_terms, related_terms1, related_terms2, base);
    }

    public double calCCTermSetSimilarity(Set<String> termset1, Set<String> termset2){
        return calCCTermSetSimilarity(termset1, termset2, Math.E);
    }

    public double calCCTermSetSimilarity(Set<String> termset1, Set<String> termset2, double base){
        List<GOTerm> cc_terms = ontology.getDataBase(TermDomain.CC);
        List<GOTerm> related_terms1 = annotations.getRelatedTerms(termset1, TermDomain.CC);
        List<GOTerm> related_terms2 = annotations.getRelatedTerms(termset2, TermDomain.CC);
        return calCCSimilarity(cc_terms, related_terms1, related_terms2, base);
    }

    private double calCCSimilarity(List<GOTerm> cc_terms, List<GOTerm> related_terms1, List<GOTerm> related_terms2, double base) {
        if(related_terms1==null && related_terms2==null)return -3;
        if(related_terms1==null || related_terms2==null)return -2;
        if(related_terms1.size()==0 && related_terms2.size()==0)return -3;
        if(related_terms1.size()==0 || related_terms2.size()==0)return -2;

        List<GOTerm> child_terms = getIntersectTerm(related_terms1, related_terms2);

        double[] vector1 = calArcTanVector(cc_terms, related_terms1, child_terms);
        double[] vector2 = calArcTanVector(cc_terms, related_terms2, child_terms);
        int all_terms = related_terms1.size() + related_terms2.size();
        if(all_terms==0)return -1;
        return Evaluation.cosienSimilarity(vector1, vector2) * Math.log(all_terms)/Math.log(base);
    }

    public double calBPGeneSimilarity(String gene1, String gene2) {
        return calBPGeneSimilarity(gene1,gene2,Math.E);
    }

    public double calBPGeneSimilarity(String gene1, String gene2, double base){
        List<GOTerm> cc_terms = ontology.getDataBase(TermDomain.BP);
        List<GOTerm> related_terms1 = annotations.getRelatedTerms(gene1, TermDomain.BP);
        List<GOTerm> related_terms2 = annotations.getRelatedTerms(gene2, TermDomain.BP);
        return calBPSimilarity(cc_terms, related_terms1, related_terms2, base);
    }

    public double calBPTermSetSimilarity(Set<String> termset1, Set<String> termset2){
        return calBPTermSetSimilarity(termset1, termset2, Math.E);
    }

    public double calBPTermSetSimilarity(Set<String> termset1, Set<String> termset2, double base){
        List<GOTerm> cc_terms = ontology.getDataBase(TermDomain.BP);
        List<GOTerm> related_terms1 = annotations.getRelatedTerms(termset1, TermDomain.BP);
        List<GOTerm> related_terms2 = annotations.getRelatedTerms(termset2, TermDomain.BP);
        return calBPSimilarity(cc_terms, related_terms1, related_terms2, base);
    }

    private double calBPSimilarity(List<GOTerm> cc_terms, List<GOTerm> related_terms1, List<GOTerm> related_terms2, double base) {
        if(related_terms1==null && related_terms2==null)return -3;
        if(related_terms1==null || related_terms2==null)return -2;
        if(related_terms1.size()==0 && related_terms2.size()==0)return -3;
        if(related_terms1.size()==0 || related_terms2.size()==0)return -2;

        List<GOTerm> child_terms = getIntersectTerm(related_terms1, related_terms2);
        double[] vector1 = calArcTanVector(cc_terms, related_terms1, child_terms);
        double[] vector2 = calArcTanVector(cc_terms, related_terms2, child_terms);
        int all_terms = related_terms1.size() + related_terms2.size();
        if(all_terms==0)return 1;
        return Evaluation.cosienSimilarity(vector1, vector2)* Math.log(all_terms)/Math.log(base);
    }
    public double calMFGeneSimilarity(String gene1, String gene2) {
        return calMFGeneSimilarity(gene1,gene2,Math.E);
    }

    public double calMFGeneSimilarity(String gene1, String gene2, double base){
        List<GOTerm> cc_terms = ontology.getDataBase(TermDomain.MF);
        List<GOTerm> related_terms1 = annotations.getRelatedTerms(gene1, TermDomain.MF);
        List<GOTerm> related_terms2 = annotations.getRelatedTerms(gene2, TermDomain.MF);
        return calMFSimilarity(cc_terms, related_terms1, related_terms2, base);
    }

    public double calMFTermSetSimilarity(Set<String> termset1, Set<String> termset2){
        return calMFTermSetSimilarity(termset1, termset2, Math.E);
    }

    public double calMFTermSetSimilarity(Set<String> termset1, Set<String> termset2, double base){
        List<GOTerm> cc_terms = ontology.getDataBase(TermDomain.MF);
        List<GOTerm> related_terms1 = annotations.getRelatedTerms(termset1, TermDomain.MF);
        List<GOTerm> related_terms2 = annotations.getRelatedTerms(termset2, TermDomain.MF);
        return calMFSimilarity(cc_terms, related_terms1, related_terms2, base);
    }

    private double calMFSimilarity(List<GOTerm> cc_terms, List<GOTerm> related_terms1, List<GOTerm> related_terms2, double base) {
        if(related_terms1==null && related_terms2==null)return -3;
        if(related_terms1==null || related_terms2==null)return -2;
        if(related_terms1.size()==0 && related_terms2.size()==0)return -3;
        if(related_terms1.size()==0 || related_terms2.size()==0)return -2;

        List<GOTerm> child_terms = getIntersectTerm(related_terms1, related_terms2);

        double[] vector1 = calArcTanVector(cc_terms, related_terms1, child_terms);
        double[] vector2 = calArcTanVector(cc_terms, related_terms2, child_terms);
        int all_terms = related_terms1.size() + related_terms2.size();
        if(all_terms==0)return 1;
        return Evaluation.cosienSimilarity(vector1, vector2) * Math.log(all_terms)/Math.log(base);
    }

    private double[] calVector(List<GOTerm> terms, List<GOTerm> related_terms, List<GOTerm> intersectChild) {
        double[] vector = new double[terms.size()];
        Map<String, Integer> mapIndex = new HashMap<String, Integer>();
        for (GOTerm term : terms) {
            mapIndex.put(term.id, mapIndex.size());
        }
        Queue<GOTerm> backup = new LinkedList<GOTerm>();
        Queue<GOTerm> part_ofs = new LinkedList<GOTerm>();
        for(GOTerm term:related_terms) {
            for (GOTerm part_of : term.part_of) {
                part_ofs.add(part_of);
            }
        }

        double init_value = 1.0;
        int level = 3;

        Queue<GOTerm> queue = new LinkedList<GOTerm>(related_terms);
        double[] increase = {0,init_value/6,init_value/12};
        while (!queue.isEmpty()) {
            if (level <= 0) break;
            GOTerm term = queue.poll();
            int index = mapIndex.get(term.id);
            if (withWeight) {
                vector[index] = term.weight * (vector[index] >0 ? vector[index]+increase[level] : init_value);//Math.max(vector[index], init_value);
            } else {
                vector[index] = vector[index] >0 ? vector[index]+increase[level] : init_value;//Math.max(vector[index], init_value);
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
        }
        Queue<GOTerm> child_queue = new LinkedList<GOTerm>();
        child_queue.addAll(intersectChild);
        level = 2;
        init_value = 0.0;
        increase[1] = init_value/6;
        increase[2] = init_value/12;
        backup.clear();
        while (!child_queue.isEmpty()) {
            if (level <= 0) break;
            GOTerm term = child_queue.poll();
            int index = mapIndex.get(term.id);
            if (withWeight) {
                vector[index] = term.weight * (vector[index] > 0 ? vector[index] + increase[level] : init_value);//Math.max(vector[index], init_value);
            } else {
                vector[index] = vector[index] > 0 ? vector[index] + increase[level] : init_value;//Math.max(vector[index], init_value);
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

        init_value = 0.3;
        level = 2;
        double[] partof_increase = {0,0.1,0.05};
        backup.clear();
        while (!part_ofs.isEmpty()) {
            if (level <= 0) break;
            GOTerm term = part_ofs.poll();
            int index = mapIndex.get(term.id);
            if (withWeight) {
                vector[index] = term.weight * (vector[index] >0 ? vector[index]+partof_increase[level] : init_value);//Math.max(vector[index], init_value);
            } else {
                vector[index] = vector[index] >0 ? vector[index]+partof_increase[level] : init_value;//Math.max(vector[index], init_value);
            }
            for (GOTerm part_of : term.part_of) {
                backup.add(part_of);
            }
            if (part_ofs.isEmpty()) {
                Queue<GOTerm> temp = part_ofs;
                part_ofs = backup;
                backup = temp;
                init_value /= 2;
                level--;
            }
        }
        return vector;
    }

    private double[] calArcTanVector(List<GOTerm> terms, List<GOTerm> related_terms, List<GOTerm> intersectChild) {
        double[] vector = new double[terms.size()];
        Map<String, Integer> mapIndex = new HashMap<String, Integer>();
        for (GOTerm term : terms) {
            mapIndex.put(term.id, mapIndex.size());
        }
        Queue<GOTerm> backup = new LinkedList<GOTerm>();
        Queue<GOTerm> part_ofs = new LinkedList<GOTerm>();
        for(GOTerm term:related_terms) {
            for (GOTerm part_of : term.part_of) {
                part_ofs.add(part_of);
            }
        }

        int level = 3;
        int[][] times = new int[terms.size()][9];//[0] self,[1] is_a, [2]is_a is_a,[3] is,[4] is is,[5] part_of,[6] part_of part_of,[7] part,[8] part part
        Queue<GOTerm> queue = new LinkedList<GOTerm>(related_terms);
        while (!queue.isEmpty()) {
            if (level <= 0) break;
            GOTerm term = queue.poll();
            int index = mapIndex.get(term.id);
            switch (level){
                case 3:times[index][0]++;break;
                case 2:times[index][1]++;break;
                case 1:times[index][2]++;break;
            }
            for (GOTerm parent : term.getParent()) {
                backup.add(parent);
            }
            if (queue.isEmpty()) {
                Queue<GOTerm> temp = queue;
                queue = backup;
                backup = temp;
                level--;
            }
        }
        Queue<GOTerm> child_queue = new LinkedList<GOTerm>();
        child_queue.addAll(intersectChild);
        level = 2;
        backup.clear();
        while (!child_queue.isEmpty()) {
            if (level <= 0) break;
            GOTerm term = child_queue.poll();
            int index = mapIndex.get(term.id);
            switch (level){
                case 2:times[index][3]++;break;
                case 1:times[index][4]++;break;
            }
            for (GOTerm parent : term.getChildren()) {
                backup.add(parent);
            }
            if (child_queue.isEmpty()) {
                Queue<GOTerm> temp = child_queue;
                child_queue = backup;
                backup = temp;
                level--;
            }
        }

        level = 2;
        backup.clear();
        while (!part_ofs.isEmpty()) {
            if (level <= 0) break;
            GOTerm term = part_ofs.poll();
            int index = mapIndex.get(term.id);
            switch (level){
                case 2:times[index][5]++;break;
                case 1:times[index][6]++;break;
            }
            for (GOTerm part_of : term.part_of) {
                backup.add(part_of);
            }
            if (part_ofs.isEmpty()) {
                Queue<GOTerm> temp = part_ofs;
                part_ofs = backup;
                backup = temp;
                level--;
            }
        }
        double initValue = 1.0;
        for(int i=0;i<vector.length;i++){
            if(times[i][0]>0){//for direct annotated term, given 1 contribute.
                vector[i]=initValue;
                //System.out.print(i+":0:"+times[i][0]+"\t");
            }else if(times[i][1]>0){//for is_a relation term
                vector[i]= 0.5*(initValue + Math.tanh(times[i][1]-1)*2/Math.PI);
                //System.out.print(i+":1:"+times[i][1]+"\t");
            }else if(times[i][2]>0){//for is_a's is_a relation term
                vector[i]= 0.25*(initValue + Math.tanh(times[i][2]-1)*2/Math.PI);
                //System.out.print(i+":2:"+times[i][2]+"\t");
            }else if(times[i][3]>0){//for is relation term
                vector[i]= 0.2*(initValue + Math.tanh(times[i][3]-1)*2/Math.PI);
                //System.out.print(i+":3:"+times[i][3]+"\t");
            }else if(times[i][4]>0){//for is' is relation term
                vector[i]= 0.1*(initValue + Math.tanh(times[i][4]-1)*2/Math.PI);
                //System.out.print(i+":4:"+times[i][4]+"\t");
            }else if(times[i][5]>0){//for part_of relation term
                vector[i]= 0.35*(initValue + Math.tanh(times[i][5]-1)*2/Math.PI);
                //System.out.print(i+":5:"+times[i][5]+"\t");
            }else if(times[i][6]>0){//for part_of part_of relation term
                vector[i]= 0.17*(initValue + Math.tanh(times[i][6]-1)*2/Math.PI);
                //System.out.print(i+":6:"+times[i][6]+"\t");
            }else if(times[i][7]>0){//for part relation term
                vector[i]= 0.17*(initValue + Math.tanh(times[i][7]-1)*2/Math.PI);
                //System.out.print(i+":7:"+times[i][7]+"\t");
            }else if(times[i][8]>0){//for part part relation term
                vector[i]= 0.09*(initValue + Math.tanh(times[i][8]-1)*2/Math.PI);
            }else{//for no relation term.
                vector[i]= 0.0;
            }
        }
        /*System.out.println();
        for(int i=0;i<vector.length;i++){
            if(vector[i]!=0){
                System.out.print(i+":"+vector[i]+"\t");
            }
        }
        System.out.println();*/
        return vector;
    }
}
