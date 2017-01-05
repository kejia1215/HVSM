package main;

import org.apache.commons.cli.*;

import java.io.*;

/**
 * Created by Administrator on 2017/1/5.
 */
public class Main {
    private static Options opts = new Options();
    private static CommandLineParser parser = new DefaultParser();
    public static enum DB{
        CC,BP,MF
    }
    public static enum Orgonism{
        HUMAN("UniProtKB"), YEAST("SGD");
        public String name;
        private Orgonism(String name){
            this.name = name;
        }
    }
    private static Orgonism org;
    private static DB db;
    private static InputStream is;
    private static OutputStream os;
    static{
        opts.addOption("h", false, "Print help information");
        opts.addOption("org", true, "human or yeast, orgonism of datasets.");
        opts.addOption("db", true, "C, P or F, denotes the Database of GO");
        opts.addOption("termset", false, "Choose TermSet to calculate the similarity.");
        opts.addOption("gene", false, "Choose Gene Pair to calculate the similarity.");
        opts.addOption("i", true, "The input file of termset or gene pair, split with \"tab\"."
        + "input termset need split with \",\", eg: a,b,c\t d,e,f");
        opts.addOption("o", true, "The output file of similarity, if not specified, result will output to console。");
    }
    public static void printHelp(){
        HelpFormatter hf = new HelpFormatter();
        hf.printHelp("Model Run Options", opts);
    }
    public static void main(String[] args) throws Exception {
        CommandLine cli = parser.parse(opts, args);
        if (cli.getOptions().length > 0) {
            if (cli.hasOption('h')) {
                printHelp();
            } else {
                org = parseOrgonism(cli.getOptionValue("org"));
                is = parseInputStream(cli.getOptionValue("i"));
                os = parseOutPutStream(cli.getOptionValue("o"));
                if(cli.hasOption("termset"))HVSM.RunTermSet(org,db,is,os);
                else if(cli.hasOption("gene")){
                    db = parseDataBase(cli.getOptionValue("db"));
                    HVSM.RunGenePair(org,db,is,os);
                }
                else System.err.println("Error Method Found, Use termset or gene.");
                is.close();
                os.flush();
                os.close();
            }
        } else {
            System.err.println("Wrong Input Parameters.");
            printHelp();
        }
    }

    private static OutputStream parseOutPutStream(String output) throws FileNotFoundException {
        if(output == null){
            return System.out;
        }
        return new FileOutputStream(output);
    }

    private static InputStream parseInputStream(String input) throws Exception {
        if(input == null){
            throw new Exception("Must Specify the Input Path.");
        }
        return new FileInputStream(input);
    }

    private static DB parseDataBase(String db) throws Exception {
        if(db == null){
            throw new Exception("Must Specify the DataBase.");
        }
        if(db.equals("C")){
            return DB.CC;
        }else if(db.equals("P")){
            return DB.BP;
        }else if(db.equals("F")){
            return DB.MF;
        }else{
            throw new Exception("Orgonism Must be eighter C, P or F.");
        }
    }

    private static Orgonism parseOrgonism(String org) throws Exception {
        if(org == null){
            throw new Exception("Must Specify the Orgonism.");
        }
        if(org.equals("human")){
            return Orgonism.HUMAN;
        }else if(org.equals("yeast")){
            return Orgonism.YEAST;
        }else{
            throw new Exception("Orgonism Must be eighter human or yeast.");
        }
    }
}
