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
    double[][] AllMins = new double[3][100];
    double[][] AllMaxes = new double[3][100];
    double[][] AllMinValues = new double[3][100];
    double[][] AllMaxValues = new double[3][100];

    int totalReps = 0;

    int chosenAngle = 2;

    int entries = 0;

    int[] numMins = new int[3];
    int[] numMaxes = new int[3];
    double ideal_p2p = 0;
    double upperbound;
    double lowerbound;
    double[] difference = new double[3];
    int RepCount = 0;
    ArrayList<Double> pastPitchEntries = new ArrayList<Double>();
    ArrayList<Double> pastRollEntries = new ArrayList<Double>();
    ArrayList<Double> pastYawEntries = new ArrayList<Double>();
    int[] lastMinIndex = {-1,-1,-1};
    int[] lastMaxIndex = {-1,-1,-1};
    int[] lastCountedMin = {-1,-1,-1};
    int[] lastCountedMax = {-1,-1,-1};
    int[] Reptrending = new int[3];
    double[] repMinVal = new double[3];
    double[] repMaxVal = new double[3];
    int[] newMax = new int[3];
    int[] newMin = new int[3];
    int[] p = {0,0,0};
    int[] l = {0,0,0};

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

    public int getChosenAngle() {return chosenAngle;}

    public double getIdealP2P() {return ideal_p2p;}

    public double[][] getAllMins() {return AllMins;}

    public double[][] getAllMaxes() {return AllMaxes;}

    public double[][] getAllMinValues() {return AllMinValues;}

    public double[][] getAllMaxValues() {return AllMaxValues;}

    /*public double upperlimit(){return upperbound;}

    public double lowerlimit(){return lowerbound;}

    public double currPk2Pk(){return difference;}*/

    public double percentThreshold(){
        double percent;
        if(chosenAngle != -1) {
            if (difference[chosenAngle] <= lowerbound) {
                percent = 0.0;
            } else if (difference[chosenAngle] >= upperbound) {
                percent = 100;
            } else {
                percent = ((upperbound - difference[chosenAngle]) / (upperbound - lowerbound)) * 100;
            }
        }
        else
            percent = 0;
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

            Arrays.fill(numMins, 0);;
            Arrays.fill(numMaxes, 0);;
            RepCount = 0;
            ideal_p2p = 0;
            upperbound = 0;
            lowerbound = 0;
            pastPitchEntries.clear();
            pastRollEntries.clear();
            pastYawEntries.clear();
            Arrays.fill(lastMinIndex, -1);
            Arrays.fill(lastMaxIndex, -1);
            Arrays.fill(lastCountedMin, -1);
            Arrays.fill(lastCountedMax, -1);
            Arrays.fill(Reptrending, 0);
            Arrays.fill(repMinVal, 0);
            Arrays.fill(repMaxVal, 0);
            Arrays.fill(averageAmplitudes, 0);
            Arrays.fill(difference, 0);
            Arrays.fill(newMax, 0);
            Arrays.fill(newMin, 0);
            Arrays.fill(p, 0);
            Arrays.fill(l, 0);
            for (double[] row: AllMaxValues)
                Arrays.fill(row, 0);
            for (double[] row: AllMinValues)
                Arrays.fill(row, 0);
            for (double[] row: AllMaxes)
                Arrays.fill(row, 0);
            for (double[] row: AllMins)
                Arrays.fill(row, 0);
            chosenAngle = -1;

        }

        entries++;
        int downsample = 40;

        if(entries%downsample == 0){
            checkForRepsAllAngles(pitch.get(0), roll.get(0), yaw.get(0), entries);
        }

        //System.out.println(" NEW CALL ");

       // isPeriodicCheck(pitch,downsample, 0);
       // isPeriodicCheck(roll,downsample, 1);
       // isPeriodicCheck(yaw,downsample, 2);

        if(chosenAngle == -1) {
           /* if ((averageAmplitudes[0] > averageAmplitudes[1]) && (averageAmplitudes[0] > averageAmplitudes[2])) {
                isPeriodic = isPeriodicAll[0];
                motionFrequency = motionFrequencyAll[0];
                motionError = motionErrorAll[0];
                toofast = toofastAll[0];
            } else if (averageAmplitudes[1] > averageAmplitudes[2]) {
                isPeriodic = isPeriodicAll[1];
                motionFrequency = motionFrequencyAll[1];
                motionError = motionErrorAll[1];
                toofast = toofastAll[1];
            } else {
                isPeriodic = isPeriodicAll[2];
                motionFrequency = motionFrequencyAll[2];
                motionError = motionErrorAll[2];
                toofast = toofastAll[2];
                }*/
            isPeriodic = isPeriodicAll[0];
            motionFrequency = motionFrequencyAll[0];
            motionError = motionErrorAll[0];
            toofast = toofastAll[0];
        }
        else{
            isPeriodic = isPeriodicAll[chosenAngle];
            motionFrequency = motionFrequencyAll[chosenAngle];
            motionError = motionErrorAll[chosenAngle];
            toofast = toofastAll[chosenAngle];
        }

//        if(motionFrequencyAll[0] != 0)
//        System.out.println("Pitch " + isPeriodicAll[0] + " " + motionFrequencyAll[0]+ " " + averageAmplitudes[0]);
//        if(motionFrequencyAll[1] != 0)
//        System.out.println("Roll " + isPeriodicAll[1] + " " + motionFrequencyAll[1]+ " " + averageAmplitudes[1]);
//        if(motionFrequencyAll[2] != 0)
//        System.out.println("Yaw " + isPeriodicAll[2] + " " + motionFrequencyAll[2] + " " + averageAmplitudes[2]);

        return isPeriodic;
    }

    /*public void isPeriodicCheck(ArrayList<Double> data, int downsample, int angle){
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
*/
    public void checkForRepsAllAngles(double newPitch, double newRoll, double newYaw, int i){
        pastPitchEntries.add(0, newPitch);
        pastRollEntries.add(0, newRoll);
        pastYawEntries.add(0, newYaw);
        checkForReps(pastPitchEntries, 0, i);
        checkForReps(pastRollEntries, 1, i);
        checkForReps(pastYawEntries, 2, i);
//        System.out.print(pastYawEntries.get(0) + ", ");

        //if(chosenAngle != -1 && Math.abs(numMaxes[chosenAngle] - numMins[chosenAngle]) > 1)
        //System.out.println("Index " + (i - lastCountedMax[chosenAngle]) + " " + numMaxes[chosenAngle] + " " + numMins[chosenAngle]);

       /* if(chosenAngle == -1){
            boolean[] ready = new boolean[3];
            for(int x = 0; x < 3; x++){
                if(numMaxes[x] == numMins[x]) {
                    ready[x] = true;
                    difference[x] = (repMaxVal[x] - repMinVal[x]);
                }
            }
            if(ready[0] && difference[0] < 5 || (i - lastCountedMax[0]) > 400 || Math.abs(numMaxes[0] - numMins[0]) > 1){
              /*  if(ready[1] && difference[1] > difference[2] && difference[1] > 0.1) {
                    ideal_p2p = difference[1];
                    RepCount = 1;
                    totalReps++;
                    newMax[1] = 0;
                    newMin[1] = 0;
                    chosenAngle = 1;
                }*/
              /*  if (ready[2] && difference[2] > 0.1){
                    ideal_p2p = difference[2];
                    RepCount = 1;
                    totalReps++;
                    newMax[2] = 0;
                    newMin[2] = 0;
                    chosenAngle = 2;
                }

            }
            else if(ready[0]){
                ideal_p2p = difference[0];
                RepCount = 1;
                totalReps++;
                newMax[0] = 0;
                newMin[0] = 0;
                chosenAngle = 0;
            }
        }*/
        if(chosenAngle == -1 && RepCount < 3){
            boolean[] ready = new boolean[3];
            for(int x = 0; x < 3; x++){
                if(numMaxes[x] == 1 && numMins[x] == 1) {
                    ready[x] = true;
                    difference[x] = (repMaxVal[x] - repMinVal[x]);
                   // System.out.println("Ready in " + x + " With " + difference[x]);
                }
            }
            //System.out.println("Difference " + difference[1] + " Difference2 "+ difference[2]+ " i " + i);
            if(ready[0] && difference[0]>difference[1] && difference[0]>difference[2] && difference[0] > 0.1){
                ideal_p2p = difference[0];
                RepCount = 1;
                totalReps++;
                newMax[0] = 0;
                newMin[0] = 0;
               // numMaxes[0] = 1;
               // numMins[0] = 1;
                chosenAngle = 0;
            }
            else if(ready[1] && difference[1] > difference[2] && difference[1] > 0.1){
                ideal_p2p = difference[1];
                RepCount = 1;
                totalReps++;
                newMax[1] = 0;
                newMin[1] = 0;
               // numMaxes[1] = 1;
               // numMins[1] = 1;
                chosenAngle = 1;
            }
            else if (ready[2] && difference[2] > 0.1){
                ideal_p2p = difference[2];
                RepCount = 1;
                totalReps++;
                newMax[2] = 0;
                newMin[2] = 0;
               // numMaxes[2] = 1;
              //  numMins[2] = 1;
                chosenAngle = 2;
            }
          //  if(chosenAngle != -1)
            //    System.out.println("First Rep at " + i/40 + " with " + chosenAngle);
        }

        /*else if(Math.abs(numMaxes[chosenAngle] - numMins[chosenAngle]) > 1){
            boolean[] ready = new boolean[3];
            for(int x = 0; x < 3; x++){
                if(numMaxes[x] == numMins[x]) {
                    ready[x] = true;
                    difference[x] = (repMaxVal[x] - repMinVal[x]);
                    // System.out.println("Ready in " + x + " With " + difference[x]);
                }
            }
            if(ready[0] && difference[0]>difference[1] && difference[0]>difference[2] && difference[0] > 0.1){
                ideal_p2p = difference[0];
                RepCount = 1;
                totalReps++;
                newMax[0] = 0;
                newMin[0] = 0;
                // numMaxes[0] = 1;
                // numMins[0] = 1;
                chosenAngle = 0;
            }
            else if(ready[1] && difference[1] > difference[2] && difference[1] > 0.1){
                ideal_p2p = difference[1];
                RepCount = 1;
                totalReps++;
                newMax[1] = 0;
                newMin[1] = 0;
                // numMaxes[1] = 1;
                // numMins[1] = 1;
                chosenAngle = 1;
            }
            else if (ready[2] && difference[2] > 0.1){
                ideal_p2p = difference[2];
                RepCount = 1;
                totalReps++;
                newMax[2] = 0;
                newMin[2] = 0;
                // numMaxes[2] = 1;
                //  numMins[2] = 1;
                chosenAngle = 2;
            }
        //    if(chosenAngle != -1)
          //      System.out.println("Rep Count " + RepCount + " at " + i/40 + " with " + chosenAngle + " because the dif was " + difference[chosenAngle] + " upperbound " + upperbound + " lowerbound " + lowerbound);
        }*/
        else if((Math.abs(numMaxes[chosenAngle] - numMins[chosenAngle]) > 1) && RepCount < 3){
            boolean[] ready = new boolean[3];
            for(int x = 0; x < 3; x++){
                if(numMaxes[x] == numMins[x]) {
                    ready[x] = true;
                    difference[x] = (repMaxVal[x] - repMinVal[x]);
                }
            }

            if(ready[0] && difference[0]>difference[1] && difference[0]>difference[2] && difference[0] > 0.1){
                ideal_p2p = difference[0];
                RepCount = 1;
                totalReps++;
                newMax[0] = 0;
                newMin[0] = 0;
                chosenAngle = 0;
            }
            else if(ready[1] && difference[1] > difference[2] && difference[1] > 0.1){
                ideal_p2p = difference[1];
                RepCount = 1;
                totalReps++;
                newMax[1] = 0;
                newMin[1] = 0;
                chosenAngle = 1;
            }
            else if (ready[2] && difference[2] > 0.1){
                ideal_p2p = difference[2];
                RepCount = 1;
                totalReps++;
                newMax[2] = 0;
                newMin[2] = 0;
                chosenAngle = 2;
            }
        }
        else if ((Math.abs(numMaxes[chosenAngle] - numMins[chosenAngle]) > 1) && RepCount >=3){
            numMins[chosenAngle] = 0;
            numMaxes[chosenAngle] = 0;
        }
        else {
            if (newMax[chosenAngle] == 1 && newMin[chosenAngle] == 1) {
//                if (numMaxes == numMins && numMaxes[chosenAngle] == 1) {
//                    difference[chosenAngle] = (repMaxVal[chosenAngle] - repMinVal[chosenAngle]);
//                    ideal_p2p = difference[chosenAngle];
//                    RepCount = 1;
//                    totalReps++;
//                    newMax[chosenAngle] = 0;
//                    newMin[chosenAngle] = 0;
//                }
                if(numMaxes[chosenAngle] == numMins[chosenAngle] && RepCount < 3){ //still in calibration
                    boolean[] ready = new boolean[3];
                    for(int x = 0; x < 3; x++){
                        if(numMaxes[x] == numMins[x] && numMins[x] >= 1) {
                            ready[x] = true;
                            difference[x] = (repMaxVal[x] - repMinVal[x]);
                        }
                    }
                  //  System.out.println("At " + i + " pitch " + numMaxes[0] + " " + numMins[0] + " roll " + numMaxes[1] + " " + numMins[1] + " yaw " + numMaxes[2] + " " + numMins[2] + " chosen " + chosenAngle);
                   // System.out.println("Ideal " + ideal_p2p + " Difference " + difference[chosenAngle] + " " + i);
                    //System.out.println("Ready " + ready[0] + " " + difference[0]+ " " + ready[1] + " " + difference[1]+ " " + ready[2] + " " + difference[2]);
                    lowerbound = ideal_p2p - (ideal_p2p * 0.2);
                    upperbound = (ideal_p2p * 0.2) + ideal_p2p;
                    if(difference[chosenAngle] >= upperbound || difference[chosenAngle] <= lowerbound){ //out of calibration bounds
                        //System.out.println("Repetition out of calibration bounds at " + i + " " + chosenAngle + " " + RepCount + " " + repMaxVal[chosenAngle] + " " + repMinVal[chosenAngle]);
                        if(ready[0] && difference[0]>difference[1] && difference[0]>difference[2]){
                            ideal_p2p = difference[0];
                            RepCount = 1;
                            totalReps++;
                            newMax[0] = 0;
                            newMin[0] = 0;
                            numMaxes[0] = 1;
                            numMins[0] = 1;
                            chosenAngle = 0;
                        }
                        else if(ready[1] && difference[1] > difference[2]){
                            ideal_p2p = difference[1];
                            RepCount = 1;
                            totalReps++;
                            newMax[1] = 0;
                            newMin[1] = 0;
                            numMaxes[1] = 1;
                            numMins[1] = 1;
                            chosenAngle = 1;
                        }
                        else if (ready[2]){
                            ideal_p2p = difference[2];
                            RepCount = 1;
                            totalReps++;
                            newMax[2] = 0;
                            newMin[2] = 0;
                            numMaxes[2] = 1;
                            numMins[2] = 1;
                            chosenAngle = 2;
                        }
                    }
                    else{ //fits in calibration
                        ideal_p2p = (ideal_p2p + difference[chosenAngle]) / 2;
                        RepCount++;
                        totalReps++;
                        newMax[chosenAngle] = 0;
                        newMin[chosenAngle] = 0;
                    }
                 //   if(chosenAngle != -1)
                   //     System.out.println("Rep Count " + RepCount + " at " + i/40 + " with " + chosenAngle + " because the dif was " + difference[chosenAngle] + " upperbound " + upperbound + " lowerbound " + lowerbound);
                }
                else if (numMaxes[chosenAngle] == numMins[chosenAngle] && numMaxes[chosenAngle] != 0) {
                    difference[chosenAngle] = repMaxVal[chosenAngle] - repMinVal[chosenAngle];
                    lowerbound = ideal_p2p - (ideal_p2p * 0.2);
                    upperbound = (ideal_p2p * 0.2) + ideal_p2p;
                    if (numMaxes[chosenAngle] > 4 && difference[chosenAngle] < upperbound && difference[chosenAngle] > lowerbound) {
                        ideal_p2p = (ideal_p2p + difference[chosenAngle]) / 2;
                        RepCount++;
                        totalReps++;
                      //  if(chosenAngle != -1)
                        //    System.out.println("Rep Count " + RepCount + " at " + i/40 + " with " + chosenAngle + " because the dif was " + difference[chosenAngle] + " upperbound " + upperbound + " lowerbound " + lowerbound);
                    } else if (difference[chosenAngle] < upperbound && difference[chosenAngle] > lowerbound) {
                        RepCount++;
                        totalReps++;
                       // if(chosenAngle != -1)
                            //System.out.println("Rep Count " + RepCount + " at " + i/40 + " with " + chosenAngle + " because the dif was " + difference[chosenAngle] + " upperbound " + upperbound + " lowerbound " + lowerbound);
                    } else {
                       // System.out.println("Repetition out of bounds");
                        //System.out.println("Repetition out of bounds at " + i + " " + chosenAngle + " because the dif was " + difference[chosenAngle] + " upperbound " + upperbound + " lowerbound " + lowerbound);
                    }
                        newMax[chosenAngle] = 0;
                        newMin[chosenAngle] = 0;
                }
            }
        }
        //if(pastEntries.size() > 1)
       // System.out.println("Rep Count: " + RepCount + " last Min At " + repMinVal + " Last Max At " + repMaxVal);
    }

    public void checkForReps(ArrayList<Double> data, int angle, int i){
        for(int l = data.size(); l > 1024; l--){
            data.remove(l);
        }
        if(Reptrending[angle] == 0 && data.size() > 1) { //no known trend yet
            if (data.get(0) < data.get(1))
                Reptrending[angle] = -1;
            else
                Reptrending[angle] = 1;
        }
        else if(Reptrending[angle] == -1) { //trending downward so looking for a min
            if (data.get(0) > data.get(1)) {
                if (lastMinIndex[angle] != -1) {
                    int min_difference = i - lastMinIndex[angle];
                    if (min_difference > 150 && min_difference < 600) {
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
                        repMinVal[angle] = data.get(1);
                        numMins[angle]++;
                        newMin[angle] = 1;
                        lastCountedMin[angle] = i;
                        AllMins[angle][l[angle]] = (double)(i-40)/100;
                        AllMinValues[angle][l[angle]] = data.get(1);
                        l[angle]++;
                    }
                }
                Reptrending[angle] = 1;
                lastMinIndex[angle] = i;
            }
        }
        else if(Reptrending[angle] == 1) { //trending downward so looking for a min
            if (data.get(0) < data.get(1)) {
                if (lastMaxIndex[angle] != -1) {
                    int max_difference = i - lastMaxIndex[angle];
                    if (max_difference > 150 && max_difference < 600) {
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
                        repMaxVal[angle] = data.get(1);
                        numMaxes[angle]++;
                        newMax[angle] = 1;
                        lastCountedMax[angle] = i;
                        AllMaxes[angle][p[angle]] = (double)(i-40)/100;
                        AllMaxValues[angle][p[angle]] = data.get(1);
                        p[angle]++;
                    }
                }
                Reptrending[angle] = -1;
                lastMaxIndex[angle] = i;
            }
        }
    }
}