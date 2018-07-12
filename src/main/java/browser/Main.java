package browser;

public class Main {
    static int NUMBEROFCLUSTERS = 2;

    public static void main(String[] args) {

        FuzzyKMeans fuzzyKMeans = new FuzzyKMeans(NUMBEROFCLUSTERS);
        double[][][] tab = fuzzyKMeans.doFuzzyKMeans();

        for(int c=0; c<NUMBEROFCLUSTERS; c++){
            System.out.println("Klaster o indeksie: "+ c +" prawdopodobienstwa: ");
            for(int p=0; p<tab[0].length; p++){
                    System.out.println(tab[0][p][c]);
            }
        }

//        Crawler crawler = new Crawler();
//
//        crawler.addNewPageToPages("https://pg.edu.pl/");
//        crawler.callGetPageLinks("https://pg.edu.pl/");

        //GraphAnalysis graphAnalysis = new GraphAnalysis(crawler.getPages());
       // graphAnalysis.findVertexWithHighestDegree();
//        graphAnalysis.getNumberOfVertices();
//        graphAnalysis.getNumberOfEdges();
//        graphAnalysis.distributionOfDegreesIn();
//        graphAnalysis.distributionOfDegreesOut();
//          graphAnalysis.findShortestPathForAll();
//        //graphAnalysis.findShortestPath("https://www.google.pl/", "https://www.google.com/calendar?tab=ic");
//        graphAnalysis.graphDiameter();
//        graphAnalysis.averageDistance();
//        graphAnalysis.coefficientOfClustering();
//
//       graphAnalysis.crashSimulation();
//        graphAnalysis.attackSimulation();
//
//        PageRank pageRank = new PageRank(crawler.getPages());
//        pageRank.pageRankFormula();
    }
}
