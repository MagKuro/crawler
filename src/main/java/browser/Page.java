package browser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Page {

    private String url;

    private List<String> parents = new ArrayList<>();

    private List<String> childrens = new ArrayList<>();

    private double pageRank;

    public Page(){

    }

    public Page(String url){
        this.url = url;
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
}
