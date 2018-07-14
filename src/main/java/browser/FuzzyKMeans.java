package browser;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class FuzzyKMeans {

    private double points[][];
    private double clusters[][];
    private int  k;
    private double[][] distances; //= new double[points.length][clusters.length];
    private double[][][] probability; // = new double[points.length][clusters.length];
    private int dimension;

    public FuzzyKMeans(int k){
//        this.points = new double[][]{{1, 3}, {1.5, 3.2}, {1.3, 2.6}, {3, 1}};
//        this.clusters = new double [][] {{1.26, 3}, {3, 1}};
        this.points = new double[][] {{2, 2, 1}, {2,4, 1}, {4,1, 1}, {5,4, 1}, {8,5, 1}, {9,7, 1}, {10,5, 1}};
        this.clusters = new double [][] {{1, 2, 1}, {3, 5, 2}};
        this.k = k;
        this.dimension = points[0].length;
    }

    public FuzzyKMeans(double[][] points, int k) {
        this.points = points;
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

    public double[][][] doFuzzyKMeans(){
        distances = new double[points.length][clusters.length];
        probability = new double[2][points.length][clusters.length];

        boolean isRepeated;
        do{
            isRepeated = !assignPointsToClusters();
            calculateTheNewCentroid();
        }while(isRepeated);
        for(int c =0; c<clusters.length; c++){

            System.out.println("Nowe wspolrzedne klastra: " + clusters[c][0]+", "+clusters[c][1] );
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
        boolean isConvergance = false;
        for(int p=0; p<points.length; p++) { //p1
            //obliczanie dystansu
            for (int c = 0; c < clusters.length; c++) { //c1
                double distance = countDistances(clusters[c], points[p]);
                distances[p][c] = distance;
            }
            //obliczanie prawdopodobieństwa
            for(int c=0; c<clusters.length; c++){
                probability[0][p][c] = 0;
                for(int c2 = 0; c2<clusters.length; c2++){
                    //spr dzielenie przez zero
                    if(distances[p][c]==0){
                        probability[0][p][c] = 1;
                        continue;
                    }
                    if(distances[p][c2]==0){
                        probability[0][p][c] = 0;
                        continue;
                    }
                    probability[0][p][c] = probability[0][p][c]+pow(distances[p][c]/distances[p][c2], 2);
                }
                //spr dzielenie przez zero
                if(probability[0][p][c]!=0){
                    probability[0][p][c] = 1/probability[0][p][c];
                }
            }
        }
        //sprawdzenie zbieznosci
        isConvergance = isConvergent(0.01);

        //zamiana prawdopodobieństwa
        for(int c=0; c<clusters.length; c++){
            for(int p=0; p<points.length; p++){
                probability[1][p][c]=probability[0][p][c];
            }
        }
        return isConvergance;
    }

    private boolean isConvergent(double precision){
        for(int p=0; p<points.length; p++){
            double minuend = 0; //ujemna
            double subtrahend = 0; //ujemnik
            for(int c=0; c<clusters.length; c++){
                minuend = minuend + pow(probability[0][p][c], 2);
                subtrahend = subtrahend + pow(probability[1][p][c], 2);
            }
            if((minuend-subtrahend)>precision){
                return false;
            }
        }
        return true;
    }

    private void calculateTheNewCentroid(){
        for(int c=0; c<clusters.length; c++){
            double sum [] = new double[points[0].length];
            double squareOfProbabilities = 0;
            for(int p=0; p<points.length; p++){
                if(probability[0][p][c]!=0){
                    for(int d = 0; d < dimension; d++){
                        sum[d] = sum[d] + (pow(probability[0][p][c], 2) * points[p][d]) ;
                    }
                    squareOfProbabilities = squareOfProbabilities + pow(probability[0][p][c], 2);
                }
            }
            try{
                for(int d = 0; d < dimension; d++){
                    clusters[c][d] = sum[d]/squareOfProbabilities;
                }
            }
            catch(ArithmeticException e){
                System.out.println("UWAGA! DZIELENIE PRZEZ ZERO, ZMIEŃ WSPOŁRZĘDNE CENTROIDU.");
            }
        }
    }
}
