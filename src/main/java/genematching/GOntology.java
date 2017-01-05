package genematching;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by root on 10/21/16.
 */
public class GOntology {
    private List<GOTerm> all_terms;
    private List<GOTerm> cc_terms;
    private List<GOTerm> bp_terms;
    private List<GOTerm> mf_terms;
    private Map<String, GOTerm> termMap;
    private int total_annotate = 0;

    public GOntology(List<GOTerm> terms, Map<String, GOTerm> termMap) {
        this.all_terms = terms;
        this.termMap = termMap;
        cc_terms = new ArrayList<GOTerm>();
        bp_terms = new ArrayList<GOTerm>();
        mf_terms = new ArrayList<GOTerm>();
        for (GOTerm term : this.all_terms) {
            if (term.domain == TermDomain.CC) {
                cc_terms.add(term);
            } else if (term.domain == TermDomain.BP) {
                bp_terms.add(term);
            } else if (term.domain == TermDomain.MF) {
                mf_terms.add(term);
            } else {
                System.out.println("Not a valid Domain:" + term.domain + " for Term: " + term.id);
            }
        }
    }

    public List<GOTerm> getDataBase(TermDomain domain) {
        if (TermDomain.CC == domain) return cc_terms;
        else if (TermDomain.BP == domain) return bp_terms;
        else if (TermDomain.MF == domain) return mf_terms;
        else {
            System.out.println("Not Found Database: " + domain);
            return null;
        }
    }

    public GOTerm getRelatedTerms(String termid) {
        return termMap.get(termid);
    }

    public void addAnnotate(String termid){
        GOTerm annotate_term = termMap.get(termid);
        if(annotate_term!=null){
            annotate_term.addGeneAnnotation();
        }
    }

    public void setTotalAnnotate(int total){
        total_annotate = total;
    }

    public void calTermWeight() {
        for (GOTerm term : all_terms) {
            term.weight = - Math.log((term.gene_number+1.0)/(total_annotate+1.0));
//            int childs_num = calChildNum(term).size();
//            term.weight = Math.exp(-1.0 * childs_num / all_terms.size());//Math.log(all_terms.size()/childs_num*1.0);
        }
    }

    private Set<GOTerm> calChildNum(GOTerm term) {
        if (term.all_children != null) return term.all_children;
        Set<GOTerm> terms = term.getChildren();
        Set<GOTerm> all_children = new HashSet<GOTerm>();
        all_children.add(term);
        for (GOTerm child : terms) {
            all_children.addAll(calChildNum(child));
        }
        term.all_children = all_children;
        return all_children;
    }

    public static GOntology parseFromXml(String path)
            throws IOException, ParserConfigurationException, SAXException {
        List<GOTerm> terms = new ArrayList<GOTerm>();
        Map<String, GOTerm> map = new HashMap<String, GOTerm>();
        Set<String> obsoletes = new HashSet<String>();
        Map<String, List<String>> parents = new HashMap<String, List<String>>();
        Map<String, List<String>> part_of_map = new HashMap<String, List<String>>();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new File(path));
        NodeList termnodes = doc.getElementsByTagName("term");
        int count = 0;
        for (int i = 0; i < termnodes.getLength(); i++) {
            Node node = termnodes.item(i);
            if (!node.getNodeName().equals("term")) continue;
            String id = null;
            List<String> alt_id = new ArrayList<String>();
            List<String> consider = new ArrayList<String>();
            List<String> is_a = new ArrayList<String>();
            List<String> part_of = new ArrayList<String>();
            List<String> regulates = new ArrayList<String>();
            boolean is_obsolete = false;
            String domain = null;
            String name = null;
            NodeList lists = node.getChildNodes();
            for (int j = 0; j < lists.getLength(); j++) {
                Node child = lists.item(j);
                String nodeName = child.getNodeName();
                String nodeValue = child.getTextContent();
                if (nodeName.equals("id")) id = nodeValue;
                else if (nodeName.equals("name")) name = nodeValue;
                else if (nodeName.equals("namespace")) domain = nodeValue;
                else if (nodeName.equals("is_a")) is_a.add(nodeValue);
                else if (nodeName.equals("alt_id")) alt_id.add(nodeValue);
                else if (nodeName.equals("is_obsolete")) is_obsolete = "1".equals(nodeValue);
                else if (nodeName.equals("consider")) consider.add(nodeValue);
                else if (nodeName.equals("replaced_by")) consider.add(nodeValue);
                else if(nodeName.equals("relationship")){
                    Node iter = child.getFirstChild();
                    int flag = -1;//-1 denotes nothing,0 denotes part_of,1 denotes regulates,2 denotes others
                    while(iter!=null){
                        String part_name = iter.getNodeName();
                        String part_value = iter.getTextContent();
                        if(part_name.equals("type")){
                            if(part_value.equals("part_of"))flag=0;
                            else if(part_value.equals("regulates")) flag = 1;
                            else flag=2;
                        }else if(part_name.equals("to")){
                            if(flag==0)part_of.add(part_value);
                            else if(flag==1)regulates.add(part_value);
                        }
                        iter = iter.getNextSibling();
                    }
                }
            }
            if (is_obsolete) {
                obsoletes.add(id);
                map.put(id,null);
                for (String alt : alt_id) map.put(alt,null);
            } else {
                if (domain.equals("cellular_component"))
                    domain = "CC";
                else if (domain.equals("biological_process"))
                    domain = "BP";
                else if (domain.equals("molecular_function"))
                    domain = "MF";
                GOTerm curTerm = new GOTerm(id, name, TermDomain.valueOf(domain));
                parents.put(id, is_a);
                part_of_map.put(id,part_of);
                terms.add(curTerm);
                map.put(id, curTerm);
                for (String alt : alt_id) map.put(alt, curTerm);
            }
        }
        for (String id : parents.keySet()) {
            List<String> parent = parents.get(id);
            List<String> part_ofs = part_of_map.get(id);
            GOTerm term = map.get(id);
            for (String pa : parent) {
                GOTerm paterm = map.get(pa);
                term.addParent(paterm);
                paterm.addChild(term);
            }
            for (String pa : part_ofs) {
                GOTerm paterm = map.get(pa);
                if(paterm==null)continue;
                term.addPartOf(paterm);
                paterm.addPart(term);
            }
        }
        return new GOntology(terms, map);
    }
}
