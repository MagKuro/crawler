package browser;

import java.net.URI;
import java.util.*;

public class GraphAnalysis {

    private List<Page> pages;

    public GraphAnalysis(List<Page> pages){
        this.pages = pages;
    }

    public int getNumberOfVertices(){
        System.out.println("Liczba wierzcholków: "+pages.size());
        return pages.size();
    }

    public int getNumberOfEdges(){
        int numberOfEdges=0;
        for(Page page: pages){
            numberOfEdges=numberOfEdges+page.getParents().size();
        }
        System.out.println("Liczba krawedzi: "+numberOfEdges);
        return numberOfEdges;
    }

    public int distributionOfDegreesIn(){
        SaveToFile saveToFile = new SaveToFile("degreesin.txt");
        for(Page page: pages){
            saveToFile.save(String.valueOf(page.getParents().size()));
            //System.out.println("Strona: "+page.getUrl()+" posiada: "+page.getParents().size()+" stopni IN ");
        }
        saveToFile.close();
        return 0;
    }

    public int distributionOfDegreesOut(){
        SaveToFile saveToFile = new SaveToFile("degreesout.txt");
        for(Page page: pages){
            saveToFile.save(String.valueOf(page.getChildrens().size()));
            //System.out.println("Strona: "+page.getUrl()+" posiada: "+ page.getChildrens().size()+"stopni OUT");
        }
        saveToFile.close();
        return 0;
    }

    public void findShortestPathForAll(){
        String firstUrl;
        String secondUrl;
        SaveToFile saveToFile = new SaveToFile("shortestpath.txt");
        for(int a=0; a<pages.size(); a++){
            firstUrl = pages.get(a).getUrl();
            for(Page page: pages){
                secondUrl = page.getUrl();
                if(!firstUrl.equals(secondUrl)){

                    List<String> bfs = new ArrayList<>();
                    List<Integer> depth = new ArrayList<>();
                    bfs.add(firstUrl);
                    depth.add(0);
                    String actualUrl=firstUrl;
                    int actualDepth =0;

                    for(int i=0; i<bfs.size(); i++ ){
                        //dodanie wszystkich dzieci
                        actualUrl = bfs.get(i);
                        actualDepth = depth.get(i);

                        List<String> children = getChildrenForUrl(actualUrl);
                        for(int k=0; k<children.size(); k++){
                            String actualChild = children.get(k);
                            if(!bfs.contains(actualChild)){
                                bfs.add(actualChild);
                                int cos = actualDepth+1;
                                depth.add(cos);
                                if(actualChild.equals(secondUrl)){
                                    //System.out.println("NAJKROTSZA SCIEZKA WYNOSI: "+cos);
                                    //return cos;
                                    saveToFile.save(String.valueOf(cos));
                                }
                            }
                        }
                    }

                }
            }
        }

        saveToFile.close();
    }


    public int findShortestPath(String firstUrl, String secondUrl){

        List<String> bfs = new ArrayList<>();
        List<Integer> depth = new ArrayList<>();
        bfs.add(firstUrl);
        depth.add(0);
        String actualUrl=firstUrl;
        int actualDepth =0;

        for(int i=0; i<bfs.size(); i++ ){
            //dodanie wszystkich dzieci
            actualUrl = bfs.get(i);
            actualDepth = depth.get(i);

            List<String> children = getChildrenForUrl(actualUrl);
            for(int k=0; k<children.size(); k++){
                String actualChild = children.get(k);
                if(!bfs.contains(actualChild)){
                    bfs.add(actualChild);
                    int cos = actualDepth+1;
                    depth.add(cos);
                    if(actualChild.equals(secondUrl)){
                        //System.out.println("NAJKROTSZA SCIEZKA WYNOSI: "+cos);
                        return cos;
                    }
                }
            }
        }
        return -100;
    }

    public Integer graphDiameter(){
        int maxPath=-1;
        for(int a=0; a<pages.size(); a++){
            int path = findLongestPathInBfs(pages.get(a).getUrl());
            if(path>maxPath){
                maxPath=path;
            }
        }
        System.out.println(maxPath);
        return maxPath;
    }

    public int findLongestPathInBfs(String firstUrl){
        List<String> bfs = new ArrayList<>();
        List<Integer> depth = new ArrayList<>();
        bfs.add(firstUrl);
        depth.add(0);
        String actualUrl=firstUrl;
        int actualDepth =0;
        int maxDepth = -1;
        int childDepth = 0;

        for(int i=0; i<bfs.size(); i++ ){
            //dodanie wszystkich dzieci
            actualUrl = bfs.get(i);
            actualDepth = depth.get(i);
            //System.out.println("przeszlo");

            List<String> children = getChildrenForUrl(actualUrl);
            if(children!=null){
                for(int k=0; k<children.size(); k++){
                   // System.out.println("przeszlo2");
                    String actualChild = children.get(k);
                    if(!bfs.contains(actualChild)){
                        bfs.add(actualChild);
                        childDepth = actualDepth+1;
                        depth.add(childDepth);
                        if(childDepth>maxDepth){
                            maxDepth=childDepth;
                        }
                    }
                }
            }
        }

        return childDepth;
    }

    public List<String> getChildrenForUrl(String url){
        Page pageFound = null;
        Optional optional = pages.stream().filter(a -> a.getUrl().equals(url)).findFirst();
        if(optional.isPresent()){
            pageFound = (Page) optional.get();
        }
//        System.out.println("liczba dzieci:"+pageFound.getChildrens());

        if(pageFound!=null){
            //System.out.println("liczba dzieci:"+pageFound.getChildrens());
            return pageFound.getChildrens();
        }
        else
            return null;
    }

    public double averageDistance(){
        int sum = 0;
        int divisor = 0;
        for(int a=0; a<pages.size(); a++){
            String firstUrl = pages.get(a).getUrl();
            for(int b=0; b<pages.size(); b++){
                String secondUrl = pages.get(b).getUrl();
                int ingredient = findShortestPath(firstUrl, secondUrl);
                if(ingredient>0){
                    sum = sum + ingredient;
                    divisor++;
                }
            }
        }
//        System.out.println("Suma wynosi: "+sum);
//        System.out.println("dzielnik wynosi: " + divisor );
        System.out.println("Średnia odległość wynosi: " + (double)sum/(double)divisor);
        return (double)sum/(double)divisor;
    }

    public void coefficientOfClustering(){
        SaveToFile saveToFile = new SaveToFile("clustering.txt");
        for(Page page: pages){
            String actualUrl = page.getUrl();
            int vertexDegree = page.getChildrens().size() + page.getParents().size();
            int numberOfEdgesBetweenNeighbor = 0;
            List<String> children = getChildrenForUrl(actualUrl);
            List<String> parents = getParentsForUrl(actualUrl);
            for(int k=0; k<children.size(); k++) {
                String firstChild = children.get(k);
                //dzieci z dziecmi
                for(int n=k+1; n<children.size(); n++){
                    String secondChild = children.get(n);
                    List<String> childrenFirstChild = getChildrenForUrl(firstChild);
                    List<String> childrenSecondChild = getChildrenForUrl(secondChild);
                    if(childrenFirstChild.contains(secondChild)){
                        numberOfEdgesBetweenNeighbor++;
                    }
                    if(childrenSecondChild.contains(firstChild)){
                        numberOfEdgesBetweenNeighbor++;
                    }
                }
                //dzieci z rodzicami
                for(int n=k+1; n<parents.size(); n++){
                    String secondChild = parents.get(n);
                    List<String> childrenFirstChild = getParentsForUrl(firstChild);
                    List<String> childrenSecondChild = getParentsForUrl(secondChild);
                    if(childrenFirstChild.contains(secondChild)){
                        numberOfEdgesBetweenNeighbor++;
                    }
                    if(childrenSecondChild.contains(firstChild)){
                        numberOfEdgesBetweenNeighbor++;
                    }
                }
            }
            //rodzice z rodzicami
            for(int k=0; k<parents.size(); k++) {
                String firstChild = parents.get(k);
                //dzieci
                for (int n = k + 1; n < parents.size(); n++) {
                    String secondChild = parents.get(n);
                    List<String> childrenFirstChild = getParentsForUrl(firstChild);
                    List<String> childrenSecondChild = getParentsForUrl(secondChild);
                    if (childrenFirstChild.contains(secondChild)) {
                        numberOfEdgesBetweenNeighbor++;
                    }
                    if (childrenSecondChild.contains(firstChild)) {
                        numberOfEdgesBetweenNeighbor++;
                    }
                }
            }
            double coefficientClustering = 2*numberOfEdgesBetweenNeighbor/((double)vertexDegree*(vertexDegree-1));
            saveToFile.save(String.valueOf(coefficientClustering));
            //System.out.println("Współczynnik klasteryzacji wynosi: " + coefficientClustering);
        }
        saveToFile.close();
    }

    public void crashSimulation(){
        System.out.println("crash simulation");
        List<Integer> numbers = new ArrayList<>();
        for(int d=0; d<10; d++){
            graphDiameter();
            consistencyComponents();
            //losowanie wierzchołka, który zostanie usunięty
            Random generator = new Random();
            int drawnNumber;
            do{
                 drawnNumber = generator.nextInt(getNumberOfVertices());
            }while(numbers.contains(drawnNumber));
            numbers.add(drawnNumber);
            //System.out.println("WYLOSOWANA LICZBA TO: " + drawnNumber);

            String url = pages.get(drawnNumber).getUrl();
            List<String> children = getChildrenForUrl(url);
            List<String> parents = getParentsForUrl(url);
            if(children!=null)
                for(String child : children){
                    pages.stream().filter(page -> page.getUrl().equals(child)).forEach(page -> page.getChildrens().remove(url));
                }
            if(parents!=null)
                for(String parent : parents){
                    pages.stream().filter(page -> page.getUrl().equals(parent)).forEach(page -> page.getParents().remove(url));
                }

            pages.remove(drawnNumber);

        }
//        System.out.println("po usunięciu:");
//        graphDiameter();
//        getNumberOfVertices();
    }

    public List<String> getParentsForUrl(String url){
        Page pageFound = null;
        Optional optional = pages.stream().filter(a -> a.getUrl().equals(url)).findFirst();
        if(optional.isPresent()){
            pageFound = (Page) optional.get();
        }

        if(pageFound!=null){
            return pageFound.getParents();
        }
        else
            return null;
    }

    public void consistencyComponents(){
        List <String> urls = new ArrayList<>();
        List <String> urlsToDelete = new ArrayList<>();
        int components = 0;
        pages.stream().forEach(a->urls.add(a.getUrl()));

        do{
                components++;
                urlsToDelete = bfsUndirectedGraph(urls.get(0));
                for(String urlToDelete: urlsToDelete){
                    urls.remove(urlToDelete);
                    //System.out.println("liczba spojnych skladowych: " +components +"usuwam:" + urlToDelete);
                }
        }while(urls.size()>0);

        System.out.println(components);
    }

    public List<String> bfsUndirectedGraph(String firstUrl){
        String actualUrl =firstUrl;
        List<String> urls = new ArrayList<>();
        List<String> children = new ArrayList<>();
        List<String> parents = new ArrayList<>();
        urls.add(actualUrl);

        for(int i=0; i<urls.size(); i++){
            actualUrl = urls.get(i);
            children = getChildrenForUrl(actualUrl);
            parents = getParentsForUrl(actualUrl);
            if(children!=null)
            for(String child: children){
                if(!urls.contains(child)){
                    urls.add(child);
                    //System.out.println(child);
                }
            }
            if(parents!=null)
            for(String parent: parents){
                if(!urls.contains(parent)){
                    urls.add(parent);
                    //System.out.println(parent);
                }
            }
        }
        return urls;
    }

    public void attackSimulation(){

        System.out.println("attacksimulation");

        graphDiameter();
        consistencyComponents();

        int index = findVertexWithHighestDegree();
        String url = pages.get(index).getUrl();
        List<String> children = getChildrenForUrl(url);
        List<String> parents = getParentsForUrl(url);
        if(children!=null)
            for(String child : children){
                pages.stream().filter(page -> page.getUrl().equals(child)).forEach(page -> page.getChildrens().remove(url));
            }
        if(parents!=null)
            for(String parent : parents){
                pages.stream().filter(page -> page.getUrl().equals(parent)).forEach(page -> page.getParents().remove(url));
            }

        pages.remove(index);

        graphDiameter();
        consistencyComponents();

    }

    public int findVertexWithHighestDegree(){
        String vertexWithHighestDegree = null;
        int maxDegree=-1;
        int degree;
        int index = 0;
        for(Page page: pages){
            degree = page.getParents().size()+page.getChildrens().size();
            if( maxDegree < degree ){
                //vertexWithHighestDegree = page.getUrl();
                maxDegree = degree;
                index = pages.indexOf(page);
            }
        }

        return index;
    }

    //znajdz indeks elementu o zadanym url

}
