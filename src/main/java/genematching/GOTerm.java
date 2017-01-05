package genematching;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GOTerm {
    String id;
    String name;
    //MF CC BP
    Set<GOTerm> parent;
    Set<GOTerm> part_of;
    TermDomain domain;
    double weight = Double.MAX_VALUE;
    int child_num = Integer.MAX_VALUE;
    Set<GOTerm> children;
    Set<GOTerm> parts;
    Set<GOTerm> all_children;
    int gene_number = 0;

    public GOTerm(String id, String name, TermDomain domain) {
        this.id = id;
        this.name = name;
        this.domain = domain;
        this.parent = new HashSet<GOTerm>();
        this.part_of = new HashSet<GOTerm>();
        this.parts = new HashSet<GOTerm>();
        this.children = new HashSet<GOTerm>();
    }

    public void setParent(Set<GOTerm> parent) {
        this.parent = parent;
    }
    public void setPart_of(Set<GOTerm> part_of){this.part_of = part_of;}
    public void setParts(Set<GOTerm> parts){this.parts = parts;}

    public Set<GOTerm> getParent() {
        return this.parent;
    }
    public Set<GOTerm> getPartOf(){return this.part_of;}
    public Set<GOTerm> getParts(){return  this.parts;}

    public void setChildren(Set<GOTerm> children) {
        this.children = children;
    }

    public Set<GOTerm> getChildren() {
        return this.children;
    }

    public void setWeight(double w) {
        this.weight = w;
    }

    public double getWeight() {
        return weight;
    }

    public void addParent(GOTerm parent) {
        this.parent.add(parent);
    }
    public void addPartOf(GOTerm term){
        this.part_of.add(term);
    }

    public void addChild(GOTerm child) {
        this.children.add(child);
    }
    public void addPart(GOTerm part){
        this.parts.add(part);
    }
    public void addGeneAnnotation(){this.gene_number++;}

    @Override
    public String toString() {
        return this.id;
    }
}
