import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class RepetitiveMotionDetector {
    double motionFrequency = 0;
    boolean isPeriodic;
    boolean motionError;
    boolean toofast;
    double[] motionFrequencyAll = new double[3];
    boolean[] motionErrorAll = new boolean[3];
    boolean[] toofastAll = new boolean[3];
    boolean[] isPeriodicAll = new boolean[3];
    int[] trending = new int[3];
    double[] averageAmplitudes = new double[3];

    int totalReps = 0;

    int entries = 0;

    int numMins = 0;
    int numMaxes = 0;
    double ideal_p2p;
    double upperbound;
    double lowerbound;
    double difference;
    int RepCount = 0;
    ArrayList<Double> pastEntries = new ArrayList<Double>();
    int lastMinIndex = -1;
    int lastMaxIndex = -1;
    int Reptrending = 0;
    double repMinVal = 0;
    double repMaxVal = 0;
    int newMax = 0;
    int newMin = 0;

    public double getfreq(){
        return motionFrequency;
    }

    public boolean isMotionError(){
        return motionError;
    }

    public boolean isToofast(){
        return toofast;
    }

    public int getRepCount() {return RepCount;}

    public int getTotalReps() {return totalReps;}

    /*public double upperlimit(){return upperbound;}

    public double lowerlimit(){return lowerbound;}

    public double currPk2Pk(){return difference;}*/

    public double percentThreshold(){
        double percent;
        if(difference <= lowerbound){
            percent = 0.0;
        }
        else if(difference >= upperbound){
            percent = 100;
        }
        else{
            percent = ((upperbound - difference)/(upperbound - lowerbound))*100;
        }
        return percent;
    }

    public boolean isPeriodic(ArrayList<Double> pitch, ArrayList<Double> roll, ArrayList<Double> yaw, int newfile) {
//        ArrayList<Double> frequency = new ArrayList<Double>();
        isPeriodic = false;
        motionError = false;
        toofast = false;

        if( newfile == 0){
            motionFrequency = 0;
            Arrays.fill(trending, 0);

            totalReps = 0;

            entries = 0;

            numMins = 0;
            numMaxes = 0;
            RepCount = 0;
            pastEntries.clear();
            lastMinIndex = -1;
            lastMaxIndex = -1;
            Reptrending = 0;
            repMinVal = 0;
            repMaxVal = 0;
            newMax = 0;
            newMin = 0;
        }

        entries++;
        int downsample = 40;

        if(entries%downsample == 0){
            checkForReps(pitch.get(0), entries);
        }

        //System.out.println(" NEW CALL ");

        isPeriodicCheck(pitch,downsample, 0);
        isPeriodicCheck(roll,downsample, 1);
        isPeriodicCheck(yaw,downsample, 2);

        if((averageAmplitudes[0] > averageAmplitudes[1]) && (averageAmplitudes[0] > averageAmplitudes[2])) {
            isPeriodic = isPeriodicAll[0];
            motionFrequency = motionFrequencyAll[0];
            motionError = motionErrorAll[0];
            toofast = toofastAll[0];
        }
        else if(averageAmplitudes[1] > averageAmplitudes[2]) {
            isPeriodic = isPeriodicAll[1];
            motionFrequency = motionFrequencyAll[1];
            motionError = motionErrorAll[1];
            toofast = toofastAll[1];
        }
        else{
            isPeriodic = isPeriodicAll[2];
            motionFrequency = motionFrequencyAll[2];
            motionError = motionErrorAll[2];
            toofast = toofastAll[2];
        }

//        if(motionFrequencyAll[0] != 0)
//        System.out.println("Pitch " + isPeriodicAll[0] + " " + motionFrequencyAll[0]+ " " + averageAmplitudes[0]);
//        if(motionFrequencyAll[1] != 0)
//        System.out.println("Roll " + isPeriodicAll[1] + " " + motionFrequencyAll[1]+ " " + averageAmplitudes[1]);
//        if(motionFrequencyAll[2] != 0)
//        System.out.println("Yaw " + isPeriodicAll[2] + " " + motionFrequencyAll[2] + " " + averageAmplitudes[2]);

        return isPeriodic;
    }

    public void isPeriodicCheck(ArrayList<Double> data, int downsample, int angle){
        int p = 0;
        int k = 0;
        ArrayList<Integer> min = new ArrayList<Integer>();
        ArrayList<Integer> max = new ArrayList<Integer>();
        ArrayList<Double> minVal = new ArrayList<Double>();
        ArrayList<Double> maxVal = new ArrayList<Double>();
        isPeriodicAll[angle] = false;
        motionFrequencyAll[angle] = 0.0;
        trending[angle] = 0;
        averageAmplitudes[angle] = 0;

        for(int i = 0; i < (data.size()-downsample); i+=40){
            if (trending[angle] == 0) { //no known trend yet
                if (data.get(i) < data.get(i + downsample)) //i + downsampled because data(0) more recent
                    trending[angle] = 1;
                else
                    trending[angle] = -1;
            }
            else if(trending[angle] == -1) { //trending downward so looking for a min
                if (data.get(i) < data.get(i + downsample)) {
                    min.add(k, i);
                    minVal.add(k, data.get(i));
                    if (k != 0) {
                        double min_difference = min.get(k) - min.get(k - 1);
                        if (min_difference > 200 && min_difference < 600) {
                            //isPeriodic true
                            isPeriodicAll[angle] = true;
                            double frequency_val = (1 / (min_difference / 100));
                            motionFrequencyAll[angle] = (frequency_val + motionFrequencyAll[angle])/2;
                            motionErrorAll[angle] = true;
                            if (min_difference > 500)
                                toofastAll[angle] = false;
                            else if (min_difference < 300)
                                toofastAll[angle] = true;
                            else
                                motionErrorAll[angle] = false;
                        }
                    }
                    trending[angle] = 1;
                    k++;
                }
                else if (data.get(i).equals(data.get(i + downsample))){
                    trending[angle] = 0;
                    //motionFrequency = (0 + motionFrequency)/2;
                }
            }
            else if(trending[angle] == 1) {// trending upward so looking for max
                if (data.get(i) > data.get(i + downsample)) {
                    max.add(p, i);
                    maxVal.add(p, data.get(i));
                    if (p != 0) {
                        double max_difference = max.get(p) - max.get(p - 1);
                        if (max_difference > 200 && max_difference < 600) {
                            //isPeriodic true
                            isPeriodicAll[angle] = true;
                            double frequency_val = (1 / (max_difference / 100));
                            motionFrequencyAll[angle] = (frequency_val + motionFrequencyAll[angle]) / 2;
                            motionErrorAll[angle] = true;
                            if (max_difference > 500)
                                toofastAll[angle] = false;
                            else if (max_difference < 300)
                                toofastAll[angle] = true;
                            else
                                motionErrorAll[angle] = false;
                        }
                    }
                    trending[angle] = -1;
                    p++;
                } else if (data.get(i).equals(data.get(i + downsample))) {
                    trending[angle] = 0;
                    //motionFrequency =  (0 + motionFrequency)/2;
                }
            }
                if(p == k && k == 1) {
                    averageAmplitudes[angle] = (maxVal.get(p-1) - minVal.get(k-1));
                }
                else if (p == k && p != 0) {
                    averageAmplitudes[angle] = (averageAmplitudes[angle] + (maxVal.get(p-1) - minVal.get(k-1)))/2;
                }
        }
    }

    public void checkForReps(double newEntry, int i){
        pastEntries.add(0, newEntry);
        for(int l = pastEntries.size(); l > 1024; l--){
            pastEntries.remove(l);
        }
        if(Reptrending == 0 && pastEntries.size() > 1) { //no known trend yet
            if (pastEntries.get(0) < pastEntries.get(1))
                Reptrending = -1;
            else
                Reptrending = 1;
        }
        else if(Reptrending == -1) { //trending downward so looking for a min
            if (pastEntries.get(0) > pastEntries.get(1)) {
                if (lastMinIndex != -1) {
                    int min_difference = i - lastMinIndex;
                    if (min_difference > 150 && min_difference < 600) {
                        repMinVal = pastEntries.get(1);
                        numMins++;
                        newMin = 1;
                    }
                }
                Reptrending = 1;
                lastMinIndex = i;
            }
        }
        else if(Reptrending == 1) { //trending downward so looking for a min
            if (pastEntries.get(0) < pastEntries.get(1)) {
                if (lastMaxIndex != -1) {
                    int max_difference = i - lastMaxIndex;
                    if (max_difference > 150 && max_difference < 600) {
                        repMaxVal = pastEntries.get(1);
                        numMaxes++;
                        newMax = 1;
                    }
                }
                Reptrending = -1;
                lastMaxIndex = i;
            }
        }
        if(newMax == 1 && newMin == 1){
            if(numMaxes == numMins && numMaxes == 1) {
                difference = (repMaxVal - repMinVal);
                ideal_p2p = difference;
                RepCount = 1;
                totalReps++;
                newMax = 0;
                newMin = 0;
            }
            else if (numMaxes == numMins && numMaxes != 0) {
                difference = repMaxVal - repMinVal;
                lowerbound = ideal_p2p - (ideal_p2p * 0.2);
                upperbound = (ideal_p2p * 0.2) + ideal_p2p;
                if (numMaxes < 4 && difference < upperbound && difference > lowerbound) {
                    ideal_p2p = (ideal_p2p + difference) / 2;
                    RepCount++;
                    totalReps++;
                }
                else if (difference < upperbound && difference > lowerbound){
                    RepCount++;
                    totalReps++;}
                else if (numMaxes < 4 && (difference >= upperbound ||difference <= lowerbound)) {
                    numMaxes = 1;
                    numMins = 1;
                    RepCount = 1;
                    totalReps++;
                    ideal_p2p = difference;
                }
                else
                   // System.out.println("Repetition out of bounds");
                newMax = 0;
                newMin = 0;
            }
        }
        //if(pastEntries.size() > 1)
       // System.out.println("Rep Count: " + RepCount + " last Min At " + repMinVal + " Last Max At " + repMaxVal);
    }
}