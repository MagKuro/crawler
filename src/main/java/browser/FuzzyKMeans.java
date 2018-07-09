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
    private int dimension;

    public FuzzyKMeans(int k){
        this.points = new double[][]{{2, 2, 1}, {2,4, 1}, {4,1, 1}, {5,4, 1}, {8,5, 1}, {9,7, 1}, {10,5, 1}};
        this.clusters = new double [][] {{1, 2, 1}, {3, 5, 2}};
        this.k = k;
        this.dimension = points[0].length;
    }

    public FuzzyKMeans(double[][] points, double[][] clusters, int k) {
        this.points = points;
        this.clusters = clusters;
        this.k = k;
        this.dimension = points[0].length;
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

            System.out.println("Nowe wspolrzedne klastra: " + clusters[c][0]+", "+clusters[c][1] +", "+clusters[c][2]);
        }
        return probability;
    }

    private double countDistances(double tabCluster[], double tabPoint[]) {
        double distance = 0;
        for(int d = 0; d < dimension; d++){
            distance = distance + (pow((tabCluster[d] - tabPoint[d]), 2));
        }
        return sqrt(distance);
    }

    // wypełniamy tablice probability i distances
    public boolean assignPointsToClusters(){
        boolean isChange = false;
        for(int p=0; p<points.length; p++){
            double minDistance = 0;
            int minIndex = 0;
            for(int c=0; c<clusters.length; c++){
                if(c==0){
                    //zmiana
                    minDistance = countDistances(clusters[c], points[p]);
                    distances[p][c] = minDistance;
                    minIndex = c;
                    continue;
                }
                double distance = countDistances(clusters[c], points[p]);
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
            double sum [] = new double[points[0].length];
            int occurrence = 0;
            for(int p=0; p<points.length; p++){
                if(probability[p][c]==1){
                    for(int d = 0; d < dimension; d++){
                        sum[d] = sum[d] + points[p][d];
                    }
                    occurrence++;
                }
            }
            try{
                for(int d = 0; d < dimension; d++){
                    clusters[c][d] = sum[d]/occurrence;
                }
            }
            catch(ArithmeticException e){
                System.out.println("UWAGA! DZIELENIE PRZEZ ZERO, ZMIEŃ WSPOŁRZĘDNE CENTROIDU.");
            }

        }
    }
}
