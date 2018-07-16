package browser;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class VectorCreater {
    private final int numberOfMosPopularWords = 10;
    private List<String> globalTerms;
    List<Page> pages;
    double[][] vectorTable;

    public VectorCreater(Set globalTerms, List<Page> pages){
        this.globalTerms = new ArrayList<>(globalTerms);
        this.pages = pages;
        vectorTable = new double[pages.size()][globalTerms.size()];
    }

    public double[][] createPointsTable(){

        for(int p=0; p<pages.size(); p++){
            Page currentPage = pages.get(p);

            for(int g=0; g<globalTerms.size(); g++){
                String term = globalTerms.get(g);
                Integer occurrence = currentPage.getLocalTerms().get(term);

                if(occurrence != null){
                    vectorTable[p][g] = occurrence;
                } else{
                    vectorTable[p][g] = 0;
                }
            }
        }
        return vectorTable;
    }

    public void foundMostPopularWords(){
        double[] sum = new double[globalTerms.size()];
        double[] mostPopular = new double[numberOfMosPopularWords];
        for(int p=0; p<pages.size(); p++){
            for(int g=0; g<globalTerms.size(); g++){
                sum[g]=sum[g]+vectorTable[p][g];
            }
        }

        for(int i =0; i<numberOfMosPopularWords; i++){
            double box;
            String boxWord;
            for(int g=0; g<sum.length-1; g++){
                if(sum[g]>sum[g+1]){
                    box = sum[g+1];
                    sum[g+1] = sum[g];
                    sum[g] = box;

                    boxWord = globalTerms.get(g+1);
                    globalTerms.set(g+1, globalTerms.get(g));
                    globalTerms.set(g, boxWord);
                }
            }
        }
        for(int i = sum.length-1; i>sum.length-1-10; i--){
            System.out.println("Słowo: "+globalTerms.get(i)+ " wystapiło "+sum[i]+" razy.");
        }
    }
}
