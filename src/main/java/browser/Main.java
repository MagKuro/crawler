package browser;

import java.util.List;

public class Main {
    static int NUMBEROFCLUSTERS = 50;

    public static void main(String[] args) {

        Crawler crawler = new Crawler();

        crawler.addNewPageToPages("https://www.stanford.edu/");
        crawler.callGetPageLinks("https://www.stanford");
        VectorCreater vectorCreater = new VectorCreater(crawler.getGlobalTerm(), crawler.getPages());
        double[][] vectorTable = vectorCreater.createPointsTable();
        ClusterCreater clusterCreater = new ClusterCreater(vectorTable, NUMBEROFCLUSTERS);
        double[][] clusterTable = clusterCreater.createClusters();

        FuzzyKMeans fuzzyKMeans = new FuzzyKMeans(vectorTable,clusterTable, NUMBEROFCLUSTERS);

        double[][][] tab = fuzzyKMeans.doFuzzyKMeans();
        for(int c=0; c<NUMBEROFCLUSTERS; c++){
            System.out.println("Klaster o indeksie: "+ c +" prawdopodobienstwa: ");
            for(int p=0; p<tab[0].length; p++){
                    System.out.println(crawler.getPages().get(p).getUrl());
                    System.out.println(tab[0][p][c]);
            }
        }
//        PageRank pageRank = new PageRank(crawler.getPages());
//        pageRank.pageRankFormula();
    }
}
