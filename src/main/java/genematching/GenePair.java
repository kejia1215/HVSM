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
}
