import java.util.ArrayList;

public class RepetitiveMotionDetector {
    private static int repcount;
    private double[] goldmax;
    private double[] goldmin;
    private int nextRepCount;

    RepetitiveMotionDetector(double[] firstRep){
        this.goldmax = getGoldMax(firstRep);
        this.goldmin = getGoldMin(firstRep);
        this.repcount = 0;

        nextRepCount = 0;
    }
    
    public int getRepcount(){
        return this.repcount;
    }
    private int limit = 200;

    public void countIfRep(double[] array) {
     
        this.goldmax = getGoldMax(array);
        this.goldmin = getGoldMin(array);
        double[][] maxarr = characterizeReal(array, false);
        double[][] minarr = characterizeReal(array, true);
        boolean[] momentof = getSpatialDiff(this.goldmax, maxarr, this.goldmin, minarr, minarr[0].length);
        for(int i = 0; i < momentof.length; i++){
            if(momentof[i] == true) {
                repcount++;
                System.out.println("Rep Count: " + repcount);
            }
        }
    }

    //Fairly useless difference method, probably take this out
    public static double getDiff(double perf, double ex){
        double diff = perf - ex;
        return diff;
    }

    //Returns a 1x2 array that has the Maximum value in index 0 and the Index of that value in index 1
    public static double[] getGoldMax(double[] a){
        double[] max=new double[2];
        max[0]=a[0];
        for(int i=0;i<a.length;i++){
            if(a[i]>max[0]){max[0]=a[i]; max[1]=i;}
        }
        return max;
    }

    //Returns a 1x2 array that has the Minimum value in index 0 and the Index of that value in index 1
    public static double[] getGoldMin(double[] b){
        double[] min = new double[2];
        min[0]=b[0];
        for(int i=0;i<b.length;i++){
            if(b[i]<min[0]){min[0]=b[i]; min[1]=i;}
        }
        return min;
    }
    //Extends an array a by the length of array exp--used to extend the perfect rep to each recorded rep in exp data
    //Might not be used !
    public static double[][] extendArr(double[] a, double[][] exp){
        double[][] arr = new double[2][exp[0].length];
        for(int i = 0; i < exp[0].length; i++){
            arr[0][i]=a[0];
            arr[1][i]=(i+1)*a[1];
        }
        return arr;
    }
    //This should characterize an array passed in, picking out the maximums and minimums.
    //Calling the method with false will give the max, and true will give the min.
    public static double[][] characterizeReal(double[] arr, boolean flag) {
        ArrayList max = new ArrayList();
        ArrayList maxi = new ArrayList();
        ArrayList min = new ArrayList();
        ArrayList mini = new ArrayList();
        int len = arr.length;
        for (int i = 20; i < len-20; i=i+20) {
            double leading;
            double following;
            leading = arr[i+20] - arr[i];
            following = arr[i] - arr[i-20];
            if(leading<0 && following>0 && (i+60)< len){
                double leading2 = arr[i+60] - arr[i+40];
                if(leading2<0){
                    max.add(arr[i]);
                    maxi.add(i);
                }
            }
            else if(following<0 && leading>0 && (i-60)> 0){
                double following2 = arr[i-40] - arr[i-60];
                if(following2<0){
                    min.add(arr[i]);
                    mini.add(i);
                }
            }
        }
        int maxl = max.size();
        int minl = min.size();
        double[][] maxarr = new double[2][maxl];
        double[][] minarr = new double[2][minl];
        for(int i = 0; i<maxl; i++){
            maxarr[0][i]=(double)max.get(i);
            maxarr[1][i]=(int)maxi.get(i);
        }
        for(int i = 0; i<minl;i++){
            minarr[0][i]=(double)min.get(i);
            minarr[1][i]=(int)mini.get(i);
        }
        if(flag){
            return minarr;
        }
        else{
            return maxarr;
        }
    }

    // returns 1 if points are within threshold, returns 0 if not
    //Perhaps use this true or false detector to increase the rep count w/in the PatientFragment
    public static boolean[] getSpatialDiff(double [] goldmax, double [][] realmax, double[] goldmin, double[][] realmin, int len) {
        final int threshold = 25;
        boolean[] truth = new boolean[len];

        for(int i = 0 ; i<realmax[0].length && i< realmin[0].length; i++){
            double diff = goldmax[0]-realmax[0][i];
            double diff2 = realmin[0][i]-goldmin[0];
            if(diff > threshold || diff2 >threshold){
                truth[i] = false;
            }
            else{
                truth[i] = true;
            }
        }
        return truth;
    }

    // returns 1 if points are within threshold, returns 0 if not
    // Perhaps
    public static double getTimeDiff(double [][] goldmax, double [][] goldmin, double [][] max, double [][] min, int len) {
        double period;
        double goldtimediff;
        double timediff;
        double maxtimediff;
        double mintimediff;

        for(int i=0; i<len; i++){
            if (goldmax[1][i] > goldmin[1][i])
                goldtimediff = goldmax[1][i] - goldmin[1][i];
            else
                goldtimediff = goldmin[1][i] - goldmax[1][i];

            if (max[1][i] > min[1][i])
                timediff = max[1][i] - min[1][i];
            else
                timediff = min[1][i] - max[1][i];

            maxtimediff = max[1][i] - (goldmax[1][i] + 2*goldtimediff);
            mintimediff = min[1][i] - (goldmin[1][i] + 2*goldtimediff);

            if(maxtimediff > timediff || mintimediff > timediff)
                return 0;
        }

        return 1;
    }
}