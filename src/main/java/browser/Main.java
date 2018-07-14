package browser;

import java.util.List;

public class Main {
    static int NUMBEROFCLUSTERS = 3;

    public static void main(String[] args) {

        Crawler crawler = new Crawler();

        crawler.addNewPageToPages("http://localhost/fuzzykmeans/index.html");
        crawler.callGetPageLinks("http://localhost/fuzzykmeans/");
        VectorCreater vectorCreater = new VectorCreater(crawler.getGlobalTerm(), crawler.getPages());
        double[][] vectorTable = vectorCreater.createPointsTable();
        ClusterCreater clusterCreater = new ClusterCreater(vectorTable, NUMBEROFCLUSTERS);
        double[][] clusterTable = clusterCreater.createClusters();

        FuzzyKMeans fuzzyKMeans = new FuzzyKMeans(vectorTable,clusterTable, NUMBEROFCLUSTERS);

        double[][][] tab = fuzzyKMeans.doFuzzyKMeans();
        for(int c=0; c<NUMBEROFCLUSTERS; c++){
            System.out.println("Klaster o indeksie: "+ c +" prawdopodobienstwa: ");
            for(int p=0; p<tab[0].length; p++){
                    System.out.println(tab[0][p][c]);
            }
        }
//        PageRank pageRank = new PageRank(crawler.getPages());
//        pageRank.pageRankFormula();
    }
}
