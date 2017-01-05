package genematching;

/**
 * Created by root on 10/5/16.
 */
public class GenePair {
    public String id1;
    public String id2;
    public double CC_similarity;
    public double BP_similarity;
    public double MF_similarity;
    public double total_sim;
    public double expected_similarity;
    public boolean pos;

    public GenePair(String id1, String id2, double exp) {
        this.id1 = id1;
        this.id2 = id2;
        this.CC_similarity = 0;
        this.BP_similarity = 0;
        this.total_sim = 0;
        this.MF_similarity = 0;
        this.expected_similarity = exp;
    }

    public GenePair(String id1, String id2, boolean is_pos) {
        this.id1 = id1;
        this.id2 = id2;
        this.CC_similarity = 0;
        this.BP_similarity = 0;
        this.total_sim = 0;
        this.MF_similarity = 0;
        this.pos = is_pos;
    }

    @Override
    public String toString() {
        return id1+"\t"+id2;
    }

    @Override
    public boolean equals(Object obj) {
        GenePair other = (GenePair)obj;
        if(id1.equals(other.id1) && id2.equals(other.id2))return true;
        return id1.equals(other.id2) && id2.equals(other.id1);
    }

    @Override
    public int hashCode() {
        return id1.hashCode() + id2.hashCode();
    }
}
