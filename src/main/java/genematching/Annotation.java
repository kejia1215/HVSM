package genematching;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by root on 10/23/16.
 */
public class Annotation {
    String geneId;
    Set<GOTerm> annotates;

    public Annotation(String id) {
        this.geneId = id;
        annotates = new HashSet<GOTerm>();
    }

    public Annotation(String id, Set<GOTerm> terms) {
        this.geneId = id;
        this.annotates = terms;
    }

    public void addAnnotate(GOTerm term) {
        annotates.add(term);
    }

    public void addAnnotate(GOTerm term, GOntology ontology) {
        addAnnotate(term.id, ontology);
    }

    public void addAnnotate(String id, GOntology ontology) {
        List<GOTerm> related = ontology.getRelatedTerms(id);
        if (related != null) {
            annotates.addAll(related);
        } else {
            System.out.println("Not Found Terms: " + id);
        }
    }

    public Set<GOTerm> getAnnotates() {
        return this.annotates;
    }

    public String getGeneId() {
        return this.geneId;
    }
}
