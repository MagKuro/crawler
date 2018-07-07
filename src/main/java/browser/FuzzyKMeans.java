package browser;

import java.util.Map;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class FuzzyKMeans {

    private double points[][];
    private double clusters[][];
    private int  k;
    private double[][] distances; //= new double[points.length][clusters.length];
    private double[][] probability; // = new double[points.length][clusters.length];

    public FuzzyKMeans(int k){
        this.points = new double[][]{{2, 2}, {2,4}, {4,1}, {5,4}, {8,5}, {9,7}, {10,5}};
        this.clusters = new double [][] {{1, 2}, {3, 5}};
        this.k = k;
    }

    public FuzzyKMeans(double[][] points, double[][] clusters, int k) {
        this.points = points;
        this.clusters = clusters;
        this.k = k;
    }

    public double[][] doKMeans(){
        distances = new double[points.length][clusters.length];
        probability = new double[points.length][clusters.length];
        boolean isRepeated;
        do{
            isRepeated = assignPointsToClusters();
            calculateTheNewCentroid();
        }while(isRepeated);
        for(int c =0; c<clusters.length; c++){
            System.out.println("Nowe punkty klastra: " + clusters[c][0]+", "+clusters[c][1]);
        }
        return probability;
    }

    private double countDistances(double xCluster, double yCluster, double xPoint, double yPoint) {
        return sqrt((pow((xCluster - xPoint), 2)) + (pow((yCluster - yPoint), 2)));
    }

    // wypełniamy tablice probability i distances
    public boolean assignPointsToClusters(){
        boolean isChange = false;
        for(int p=0; p<points.length; p++){
            double minDistance = 0;
            int minIndex = 0;
            for(int c=0; c<clusters.length; c++){
                if(c==0){
                    minDistance = countDistances(clusters[c][0], clusters[c][1], points[p][0], points[p][1]);
                    distances[p][c] = minDistance;
                    minIndex = c;
                    continue;
                }
                double distance = countDistances(clusters[c][0], clusters[c][1], points[p][0], points[p][1]);
                distances[p][c] = distance;
                if(minDistance>distance){
                    minDistance = distance;
                    minIndex = c;
                }
            }
            for(int c=0; c<clusters.length; c++){
                if(c==minIndex){
                    if(probability[p][c]==0){
                        isChange = true;
                    }
                    probability[p][c] = 1;
                    continue;
                }
                probability[p][c] = 0;
            }
        }
        return isChange;
    }

    private void calculateTheNewCentroid(){
        for(int c=0; c<clusters.length; c++){
            double sumX = 0;
            double sumY = 0;
            int occurrence = 0;
            for(int p=0; p<points.length; p++){
                if(probability[p][c]==1){
                    sumX = sumX + points[p][0];
                    sumY = sumY + points[p][1];
                    occurrence++;
                }
            }
            try{
                clusters[c][0] = sumX/occurrence;
                clusters[c][1] = sumY/occurrence;
            }
            catch(ArithmeticException e){
                System.out.println("UWAGA! DZIELENIE PRZEZ ZERO, ZMIEŃ WSPOŁRZĘDNE CENTROIDU.");
            }

        }
    }
}
