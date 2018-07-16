package browser;

import java.util.*;

public class Main {


    public static void main(String[] args) {
        int NUMBEROFCLUSTERS = 5;

        Crawler crawler = new Crawler();

        crawler.addNewPageToPages("https://www.stanford.edu/");
        crawler.callGetPageLinks("https://www.stanford");

        System.out.println("Liczba wierzcholków: "+crawler.getPages().size());


        //Set<String> globalTerms = new HashSet<>(Arrays.asList("where", "when", "class"));

        VectorCreater vectorCreater = new VectorCreater(crawler.getGlobalTerm(), crawler.getPages());
        double[][] vectorTable = vectorCreater.createPointsTable();
        vectorCreater.foundMostPopularWords();

//        SaveToFile saveToFile = new SaveToFile("result.txt");
//
//        for(int k=5; k<=20; k=k+5){
//            saveToFile.save("k = "+k);
//            ClusterCreater clusterCreater = new ClusterCreater(vectorTable, k);
//            double[][] clusterTable = clusterCreater.createClusters();
//            FuzzyKMeans fuzzyKMeans = new FuzzyKMeans(vectorTable,clusterTable, k);
//            double[][][] tab = fuzzyKMeans.doFuzzyKMeans();
//            for(int p=0; p<tab[0].length; p++) {
//                saveToFile.save(crawler.getPages().get(p).getUrl());
//            }
//            for(int c=0; c<k; c++){
//                saveToFile.save("Klaster o indeksie: "+ c +" prawdopodobienstwa: ");
//                for(int p=0; p<tab[0].length; p++){
//                    saveToFile.save(String.valueOf(tab[0][p][c]));
//                }
//            }
//        }
//        saveToFile.close();
//        PageRank pageRank = new PageRank(crawler.getPages());
//        pageRank.pageRankFormula();
    }
}
