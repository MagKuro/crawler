package browser;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class VectorCreater {
    List<String> globalTerms;
    List<Page> pages;
    public VectorCreater(Set globalTerms, List<Page> pages){
        this.globalTerms = new ArrayList<>(globalTerms);
        this.pages = pages;
    }

    public double[][] createPointsTable(){
        double[][] vectorTable = new double[pages.size()][globalTerms.size()];
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
}
