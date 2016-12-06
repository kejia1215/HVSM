package genematching;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by root on 10/21/16.
 */
public class GOTerm {
    String id;
    String name;
    //MF CC BP
    Set<GOTerm> parent;
    TermDomain domain;
    double weight = Double.MAX_VALUE;
    int child_num = Integer.MAX_VALUE;
    Set<GOTerm> children;
    Set<GOTerm> all_children;

    public GOTerm(String id, String name, TermDomain domain) {
        this.id = id;
        this.name = name;
        this.domain = domain;
        this.parent = new HashSet<GOTerm>();
        this.children = new HashSet<GOTerm>();
    }

    public void setParent(Set<GOTerm> parent) {
        this.parent = parent;
    }

    public Set<GOTerm> getParent() {
        return this.parent;
    }

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

    public void addChild(GOTerm child) {
        this.children.add(child);
    }
}
