package browser;

public class Main {
    public static void main(String[] args) {



        Crawler crawler = new Crawler();
      //  crawler.addNewPageToList("https://www.ohio.edu/");
        crawler.addNewPageToPages("https://en.wikipedia.org/wiki/Informatics");
        crawler.callGetPageLinks("https://en.wikipedia.org/wiki/Informatics");

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
