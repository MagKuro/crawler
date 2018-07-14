package browser;

import java.util.List;

public class Main {
    static int NUMBEROFCLUSTERS = 2;

    public static void main(String[] args) {

        Crawler crawler = new Crawler();

        crawler.addNewPageToPages("https://pg.edu.pl/");
        crawler.callGetPageLinks("https://pg.edu.pl/");
        VectorCreater vectorCreater = new VectorCreater(crawler.getGlobalTerm(), crawler.getPages());
        FuzzyKMeans fuzzyKMeans = new FuzzyKMeans(vectorCreater.createPointsTable(), NUMBEROFCLUSTERS);
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
