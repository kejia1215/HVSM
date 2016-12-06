package genematching;

/**
 * Created by root on 10/21/16.
 */
public enum TermDomain {
    CC("CC"),
    BP("BP"),
    MF("MF");
    private String name;

    TermDomain(String cc) {
        this.name = cc;
    }

    @Override
    public String toString() {
        return name;
    }
}
