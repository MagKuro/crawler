package browser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PageRank {

    private List<Page> pages;
    private double damping;
    private static final int numberOfIterations = 101;
    SaveToFile pagerank1 = new SaveToFile("pagerank1.txt");
    SaveToFile pagerank20 = new SaveToFile("pagerank20.txt");
    SaveToFile pagerank100 = new SaveToFile("pagerank100.txt");

    public PageRank(List<Page> pages){
        this.pages = pages;
        this.damping = 1;
    }

    public PageRank(List<Page> pages, double damping){
        this.pages = pages;
        this.damping = damping;
    }

    public void pageRankFormula(){
        int size = pages.size();
        double[][] tab = new double[numberOfIterations][size];
        int numberOfChildren = 0;
        double parentValueOfPagerank = 0;
        double valueOfPagerank = 0;
        int parentIndex = 0;

        for(int i=0; i<size; i++){
            tab[0][i] = 1.00/size;
        }

        for(int i=1; i<numberOfIterations; i++){
            for(int vertex=0; vertex<size; vertex++){
                List<String> parents = new ArrayList();
                parents = pages.get(vertex).getParents();
                //System.out.println("VERTEX "+ vertex);

                if(parents.size()>0){
                    for(String parent: parents){
                        parentIndex = findIndexForUrl(parent);
                        numberOfChildren = pages.get(parentIndex).getChildrens().size();
                        parentValueOfPagerank = tab[i-1][parentIndex];
                        valueOfPagerank = valueOfPagerank + (parentValueOfPagerank/numberOfChildren);
                        //System.out.println("valueOfPagerank: "+valueOfPagerank);
                    }
                    tab[i][vertex] = valueOfPagerank;
                    valueOfPagerank = 0;
                }
                else{
                    System.out.println("liczba rodziców zero!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                }

                if(i==1){
                    pagerank1.save(String.valueOf(tab[i][vertex]));
                }
                if(i==20){
                    pagerank20.save(String.valueOf(tab[i][vertex]));
                }
                if(i==100){
                    pagerank100.save(String.valueOf(tab[i][vertex]));
                }
            }

        }

        pagerank1.close();
        pagerank20.close();
        pagerank100.close();

        for(int i=0; i<size; i++){
            valueOfPagerank = (1.0-damping)/size+damping*tab[numberOfIterations-1][i];
            pages.get(i).setPageRank(valueOfPagerank);
            System.out.println("pagerank wynosi: " + pages.get(i).getPageRank());
        }

        double suma=0;
        for (int i=0; i<size; i++){
            suma = suma + pages.get(i).getPageRank();
        }
        System.out.println("suma wynosi: "+ suma);
    }

    public int findIndexForUrl(String url){
        Page urlFound = null;
        Optional optional = pages.stream().filter(a -> a.getUrl().equals(url)).findFirst();
        if(optional.isPresent()){
            urlFound = (Page) optional.get();
        }
        return pages.indexOf(urlFound);
    }
}
