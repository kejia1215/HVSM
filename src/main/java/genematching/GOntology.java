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
    private Map<String, List<GOTerm>> obsolsteMap;

    public GOntology(List<GOTerm> terms, Map<String, GOTerm> termMap, Map<String, List<GOTerm>> obsolsteMap) {
        this.all_terms = terms;
        this.termMap = termMap;
        this.obsolsteMap = obsolsteMap;
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

    public List<GOTerm> getRelatedTerms(String termid) {
        List<GOTerm> terms = new ArrayList<GOTerm>();
        GOTerm term = termMap.get(termid);
        if (term != null) terms.add(term);
        if (obsolsteMap.containsKey(termid)) {
            terms.addAll(obsolsteMap.get(termid));
        }
        return terms;
    }

    public void calTermWeight() {
        for (GOTerm term : all_terms) {
            int childs_num = calChildNum(term).size();
            term.weight = Math.exp(-1.0 * childs_num / all_terms.size());//Math.log(all_terms.size()/childs_num*1.0);
        }
    }

    public void calCCTermWeight() {
        for (GOTerm term : cc_terms) {
            int childs_num = calChildNum(term).size();
            term.weight = Math.log(cc_terms.size() / childs_num);
        }
    }

    public void calBPTermWeight() {
        for (GOTerm term : bp_terms) {
            int childs_num = calChildNum(term).size();
            term.weight = Math.log(bp_terms.size() / childs_num);
        }
    }

    public void calMFTermWeight() {
        for (GOTerm term : mf_terms) {
            int childs_num = calChildNum(term).size();
            term.weight = Math.log(mf_terms.size() / childs_num);
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
        Map<String, List<GOTerm>> obsoletes = new HashMap<String, List<GOTerm>>();
        Map<String, List<String>> parents = new HashMap<String, List<String>>();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new File(path));
        NodeList termnodes = doc.getElementsByTagName("term");
        int count = 0;
        Map<String, List<String>> obosoleteMap = new HashMap<String, List<String>>();
        for (int i = 0; i < termnodes.getLength(); i++) {
            Node node = termnodes.item(i);
            if (!node.getNodeName().equals("term")) continue;
            String id = null;
            List<String> alt_id = new ArrayList<String>();
            List<String> consider = new ArrayList<String>();
            List<String> is_a = new ArrayList<String>();
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
            }
            if (is_obsolete) {
                List<String> obsoleteList = is_a;
                obsoleteList.addAll(consider);
                obosoleteMap.put(id, obsoleteList);
                for (String alt : alt_id) obosoleteMap.put(alt, obsoleteList);
            } else {
                if (domain.equals("cellular_component"))
                    domain = "CC";
                else if (domain.equals("biological_process"))
                    domain = "BP";
                else if (domain.equals("molecular_function"))
                    domain = "MF";
                GOTerm curTerm = new GOTerm(id, name, TermDomain.valueOf(domain));
                parents.put(id, is_a);
                terms.add(curTerm);
                map.put(id, curTerm);
                for (String alt : alt_id) map.put(alt, curTerm);
            }
        }
        for (String id : obosoleteMap.keySet()) {
            List<String> reps = obosoleteMap.get(id);
            List<GOTerm> obs = new ArrayList<GOTerm>();
            for (String rep : reps) {
                GOTerm term = map.get(rep);
                obs.add(term);
                obsoletes.put(id, obs);
            }
        }
        for (String id : parents.keySet()) {
            List<String> parent = parents.get(id);
            GOTerm term = map.get(id);
            for (String pa : parent) {
                GOTerm paterm = map.get(pa);
                term.addParent(paterm);
                paterm.addChild(term);
            }
        }
        return new GOntology(terms, map, obsoletes);
    }
}
