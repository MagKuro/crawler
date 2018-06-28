package browser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Page {

    private String url;

    private List<String> parents = new ArrayList<>();

    private List<String> childrens = new ArrayList<>();

    private double pageRank;

    private Map<String, Integer> localTerms;

    public Page(){

    }

    public Page(String url, Map<String, Integer> localTerms){
        this.url = url;
        this.localTerms = localTerms;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getParents() {
        return parents;
    }

    public List<String> getChildrens() {
        return childrens;
    }

    public double getPageRank(){
        return pageRank;
    }

    public void setPageRank(double pageRank){
        this.pageRank = pageRank;
    }

    public void addParent(String url){
        parents.add(url);
    }

    public void addChildren(String url){
        childrens.add(url);
    }

    public Map<String, Integer> getLocalTerms() {
        return localTerms;
    }

    public void setLocalTerms(Map<String, Integer> localTerms) {
        this.localTerms = localTerms;
    }
}
