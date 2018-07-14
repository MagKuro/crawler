package browser;

import java.util.Random;

public class ClusterCreater {
    private int dimension;
    private int numberOfClusters;
    private double[][] clusters;
    private double[][] vectorTable;

    public ClusterCreater(double[][] vectorTable, int numberOfClusters){
        this.dimension = vectorTable[0].length;
        this.numberOfClusters = numberOfClusters;
        this.vectorTable = vectorTable;
        clusters = new double[numberOfClusters][dimension];
    }

    private double findMinValue(int dimensionId){
        double minValue = 0;
        for(int p=0; p<vectorTable.length; p++){
            if(minValue>vectorTable[p][dimensionId]){
                minValue = vectorTable[p][dimensionId];
            }
        }
        return minValue;
    }

    private double findMaxValue(int dimensionId){
        double maxValue = vectorTable[0][0];
        for(int p=0; p<vectorTable.length; p++){
            if(maxValue<vectorTable[p][dimensionId]){
                maxValue = vectorTable[p][dimensionId];
            }
        }
        return maxValue;
    }

    public double[][] createClusters(){
        Random r = new Random();
        for (int d=0; d<dimension; d++){
            double rangeMin = findMinValue(d);
            double rangeMax = findMaxValue(d);
            for(int c=0; c<numberOfClusters; c++){
                clusters[c][d] = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
            }
        }
       return clusters;
    }
}
