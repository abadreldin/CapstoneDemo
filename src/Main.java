
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;


import java.util.Arrays;

import java.util.ArrayList;
import java.util.List;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Creates a simple real-time chart
 */
public class Main {

    public static void main(String[] args) throws Exception {

        int Fs = 100;
        int sizeOfArray = 512;
        CSV csv = new CSV();
        Filtration filtration = new Filtration();
        InitialPeriodicMotionDetector initDetector = new InitialPeriodicMotionDetector(Fs, sizeOfArray);
        InitialPeriodicMotionDetectorArrayList initDetectorList = new InitialPeriodicMotionDetectorArrayList(Fs, sizeOfArray);
        RepetitiveMotionDetector repetitiveMotionDetector = new RepetitiveMotionDetector();


        System.out.println(initDetector.getArraySize());
        System.out.println(initDetector.getFs());


        int file = 2;
        double timeFactor = 0.01;
        double RawtimeFactor = 0.0038;
        //double filteredtimeFactor = 0.05; //DOWNSAMPLE2: delete this variable
        double[] rawtime = new double[sizeOfArray * 5];
        double[] time = new double[sizeOfArray];
        //double[] filteredtime = new double[(sizeOfArray/5)+1]; //DOWNSAMPLE2: delete this variable
        double[] pitchRolling = new double[sizeOfArray];
        double[] rollRolling = new double[sizeOfArray];
        double[] yawRolling = new double[sizeOfArray];
        double[] pitchfiltered = new double[sizeOfArray]; //DOWNSAMPLE2: change (sizeOfArray/5)+1 to sizeofArray
        double[] rollfiltered = new double[sizeOfArray]; //DOWNSAMPLE2: change (sizeOfArray/5)+1 to sizeofArray
        double[] yawfiltered = new double[sizeOfArray]; //DOWNSAMPLE2: change (sizeOfArray/5)+1 to sizeofArray
        //double[] pitchpre = new double[sizeOfArray];
        //double[] rollpre = new double[sizeOfArray];
        //double[] yawpre = new double[sizeOfArray];
        double[] pitchpre = new double[sizeOfArray * 5]; //DOWNSAMPLE1: remove *5
        double[] rollpre = new double[sizeOfArray * 5]; //DOWNSAMPLE1: remove *5
        double[] yawpre = new double[sizeOfArray * 5]; //DOWNSAMPLE1: remove *5

        ArrayList<Double> pitch = new ArrayList<Double>();
        ArrayList<Double> roll = new ArrayList<Double>();
        ArrayList<Double> yaw = new ArrayList<Double>();

        for(int i=0; i < sizeOfArray; i++){
            pitch.add(0.0);
            roll.add(0.0);
            yaw.add(0.0);
        }


        int ActualIndex = 0;
        double Angle = 0.0;
        int FirstRep = 0;
        int n = 0;

        int capacity = 2;

        String[] DesiredAngle = new String[capacity];
        String[] ActualAngle = new String[capacity];
        String[] Difference = new String[capacity];
        String[] RepIn = new String[capacity];

        double[] newPoints = new double[3];
        double idealAngle;

        long prevUpdate = -1;
        boolean newlyPeriodic = true;
        int RepCount = 0;

        double[] freqHz = {0.05,0.39,0.59,0.78,0.98,1.17,1.37,1.56,1.76,1.95,2.15,2.34,2.54,2.73,2.93,3.13,3.32,3.52,3.71,3.91,4.10,4.30,4.49,4.69,4.88,5.08,5.27,5.47,5.66,5.86,6.05,6.25,6.45,6.64,6.84,7.03,7.23,7.42,7.62,7.81,8.01,8.20,8.40,8.59,8.79,8.98,9.18,9.38,9.57,9.77,9.96,10.16,10.35,10.55,10.74,10.94,11.13,11.33,11.52,11.72,11.91,12.11,12.30,12.50,12.70,12.89,13.09,13.28,13.48,13.67,13.87,14.06,14.26,14.45,14.65,14.84,15.04,15.23,15.43,15.63,15.82,16.02,16.21,16.41,16.60,16.80,16.99,17.19,17.38,17.58,17.77,17.97,18.16,18.36,18.55,18.75,18.95,19.14,19.34,19.53,19.73,19.92,20.12,20.31,20.51,20.70,20.90,21.09,21.29,21.48,21.68,21.88,22.07,22.27,22.46,22.66,22.85,23.05,23.24,23.44,23.63,23.83,24.02,24.22,24.41,24.61,24.80,25.00,25.20,25.39,25.59,25.78,25.98,26.17,26.37,26.56,26.76,26.95,27.15,27.34,27.54,27.73,27.93,28.13,28.32,28.52,28.71,28.91,29.10,29.30,29.49,29.69,29.88,30.08,30.27,30.47,30.66,30.86,31.05,31.25,31.45,31.64,31.84,32.03,32.23,32.42,32.62,32.81,33.01,33.20,33.40,33.59,33.79,33.98,34.18,34.38,34.57,34.77,34.96,35.16,35.35,35.55,35.74,35.94,36.13,36.33,36.52,36.72,36.91,37.11,37.30,37.50,37.70,37.89,38.09,38.28,38.48,38.67,38.87,39.06,39.26,39.45,39.65,39.84,40.04,40.23,40.43,40.63,40.82,41.02,41.21,41.41,41.60,41.80,41.99,42.19,42.38,42.58,42.77,42.97,43.16,43.36,43.55,43.75,43.95,44.14,44.34,44.53,44.73,44.92,45.12,45.31,45.51,45.70,45.90,46.09,46.29,46.48,46.68,46.88,47.07,47.27,47.46,47.66,47.85,48.05,48.24,48.44,48.63,48.83,49.02,49.22,49.41,49.61,49.80,50.00,50.20,50.39,50.59,50.78,50.98,51.17,51.37,51.56,51.76,51.95,52.15,52.34,52.54,52.73,52.93,53.13,53.32,53.52,53.71,53.91,54.10,54.30,54.49,54.69,54.88,55.08,55.27,55.47,55.66,55.86,56.05,56.25,56.45,56.64,56.84,57.03,57.23,57.42,57.62,57.81,58.01,58.20,58.40,58.59,58.79,58.98,59.18,59.38,59.57,59.77,59.96,60.16,60.35,60.55,60.74,60.94,61.13,61.33,61.52,61.72,61.91,62.11,62.30,62.50,62.70,62.89,63.09,63.28,63.48,63.67,63.87,64.06,64.26,64.45,64.65,64.84,65.04,65.23,65.43,65.63,65.82,66.02,66.21,66.41,66.60,66.80,66.99,67.19,67.38,67.58,67.77,67.97,68.16,68.36,68.55,68.75,68.95,69.14,69.34,69.53,69.73,69.92,70.12,70.31,70.51,70.70,70.90,71.09,71.29,71.48,71.68,71.88,72.07,72.27,72.46,72.66,72.85,73.05,73.24,73.44,73.63,73.83,74.02,74.22,74.41,74.61,74.80,75.00,75.20,75.39,75.59,75.78,75.98,76.17,76.37,76.56,76.76,76.95,77.15,77.34,77.54,77.73,77.93,78.13,78.32,78.52,78.71,78.91,79.10,79.30,79.49,79.69,79.88,80.08,80.27,80.47,80.66,80.86,81.05,81.25,81.45,81.64,81.84,82.03,82.23,82.42,82.62,82.81,83.01,83.20,83.40,83.59,83.79,83.98,84.18,84.38,84.57,84.77,84.96,85.16,85.35,85.55,85.74,85.94,86.13,86.33,86.52,86.72,86.91,87.11,87.30,87.50,87.70,87.89,88.09,88.28,88.48,88.67,88.87,89.06,89.26,89.45,89.65,89.84,90.04,90.23,90.43,90.63,90.82,91.02,91.21,91.41,91.60,91.80,91.99,92.19,92.38,92.58,92.77,92.97,93.16,93.36,93.55,93.75,93.95,94.14,94.34,94.53,94.73,94.92,95.12,95.31,95.51,95.70,95.90,96.09,96.29,96.48,96.68,96.88,97.07,97.27,97.46,97.66,97.85,98.05,98.24,98.44,98.63,98.83,99.02,99.22,99.41,99.61,99.80,100.00};
        int indexOffset = 1;
        int change = 1;

        double numMins = 0;
        double numMaxes = 0;
        double idealp2p= 0.0;
        int numReps = 0;


        // Create Chart
        /*final XYChart chart = QuickChart.getChart("Raw Data", "Time", "Degrees", "Pitch", rawtime, pitchpre);
        chart.addSeries( "Roll", rawtime, rollpre);
        chart.addSeries("Yaw", rawtime, yawpre);

        // Show it
        final SwingWrapper<XYChart> sw = new SwingWrapper<XYChart>(chart);
        sw.displayChart();*/

        // Create Chart
        final XYChart chartFl = QuickChart.getChart("Flip Corrected Data", "Time", "Degrees", "Pitch", time, pitchRolling);
        chartFl.addSeries( "Roll", time, rollRolling);
        chartFl.addSeries("Yaw", time, yawRolling);

        // Show it
        final SwingWrapper<XYChart> swFl = new SwingWrapper<XYChart>(chartFl);
        swFl.displayChart();

        // Create Chart
        final XYChart chartFi = QuickChart.getChart("Filtered Data", "Time", "Degrees", "Pitch", time, pitchfiltered); //DOWNSAMPLE2: Change filteredtime to time
        chartFi.addSeries( "Roll", time, rollfiltered); //DOWNSAMPLE2: Change filteredtime to time
        chartFi.addSeries("Yaw", time, yawfiltered); //DOWNSAMPLE2: Change filteredtime to time

        // Show it
        final SwingWrapper<XYChart> swFi = new SwingWrapper<XYChart>(chartFi);
        swFi.displayChart();

        // Create Chart
        double[] pitchFreqMag = new double[sizeOfArray];
        double[] rollFreqMag = new double[sizeOfArray];
        double[] yawFreqMag = new double[sizeOfArray];
       XYChart chartF = QuickChart.getChart("Frequency Spectrum", "Frequency (Hz)", "Magnitude", "PitchFreq", Arrays.copyOfRange(freqHz, 0, 20),  Arrays.copyOfRange(pitchFreqMag, 0, 20));
        chartF.addSeries( "RollFreq", Arrays.copyOfRange(freqHz, 0, 20),  Arrays.copyOfRange(rollFreqMag, 0, 20));
        chartF.addSeries( "YawFreq", Arrays.copyOfRange(freqHz, 0, 20),  Arrays.copyOfRange(yawFreqMag, 0, 20));
        // Show it
        final SwingWrapper<XYChart> swF = new SwingWrapper<XYChart>(chartF);
        swF.displayChart();

        //initialise pitch, yaw, and rolling Data
        for(int i=0; i < (sizeOfArray); i++ ){
            time[i] = timeFactor*i;
        }
        for(int i=0; i < (sizeOfArray * 5); i++ ) {
            rawtime[i] = RawtimeFactor*i;
        }
        //for(int i = 0; i < ((sizeOfArray/5)+1); i++){ //DOWNSAMPLE2: Delete this loop
        //    filteredtime[i] = filteredtimeFactor*(i*timeFactor);
        //}
       /* for(int i=0; i < sizeOfArray; i++ ){
            pitchRolling[i] = pitch[i];
        }
        for(int i=0; i < sizeOfArray; i++ ){
            rollRolling[i] = roll[i];
        }
        for(int i=0; i < sizeOfArray; i++ ){
            yawRolling[i] = yaw[i];
        }*/

        while (file < (capacity + 1)) {
            System.out.print("File: " + file + "\n");
            while (pitch.get(0) != 0 || indexOffset < 3) {
                for (int i = 0; i < (sizeOfArray); i++) {
                    time[i] += timeFactor;
                }

                for (int i = 0; i < (sizeOfArray * 5); i++) {
                    rawtime[i] += RawtimeFactor;
                }

                // for(int i = 0; i < ((sizeOfArray/5)+1); i++){ //DOWNSAMPLE2: Delete this loop
                //     filteredtime[i] += filteredtimeFactor*(i*timeFactor);
                // }

            /*for(int i = 0; i < sizeOfArray; i++){
                pitchRolling[i] = pitch[i + indexOffset];
            }
            for(int i = 0; i < sizeOfArray; i++){
                rollRolling[i] = roll[i + indexOffset];
            }
            for(int i = 0; i < sizeOfArray; i++){
                yawRolling[i] = yaw[i + indexOffset];
            }
            indexOffset ++;*/

                pitchRolling = shiftArray(pitchRolling, sizeOfArray);
                rollRolling = shiftArray(rollRolling, sizeOfArray);
                yawRolling = shiftArray(yawRolling, sizeOfArray);

                //pitchpre = shiftArray(pitchpre, (sizeOfArray));
                //rollpre = shiftArray(rollpre, (sizeOfArray));
                //yawpre = shiftArray(yawpre, (sizeOfArray));

                //pitchpre = shiftArray(pitchpre, (sizeOfArray * 5));//DOWNSAMPLE1:remove the *5
                pitch.remove(sizeOfArray - 1);
                roll.remove(sizeOfArray - 1);
                yaw.remove(sizeOfArray - 1);
                //rollpre = shiftArray(rollpre, (sizeOfArray * 5));//DOWNSAMPLE1:remove the *5
                //yawpre = shiftArray(yawpre, (sizeOfArray * 5));//DOWNSAMPLE1:remove the *5

                newPoints = csv.Read(indexOffset, file);
                pitch.add(0, newPoints[0]);
                roll.add(0, newPoints[1]);
                yaw.add(0, newPoints[2]);

               /* System.out.print("New Pitch: " + newPoints[1] + "\n");
                System.out.print("Pre Pitch: ");
                for(int i= 0; i < roll.size(); i++) {
                    System.out.print(roll.get(i));
                }
                System.out.print("\n");*/
                pitch = filtration.PitchFilter(pitch, sizeOfArray, "pitch");
                roll = filtration.PitchFilter(roll, sizeOfArray, "roll");
                yaw = filtration.PitchFilter(yaw, sizeOfArray, "yaw");
                /*System.out.print("Post Pitch: ");
                for(int i= 0; i < roll.size(); i++) {
                    System.out.print(roll.get(i));
                }
                System.out.print("\n");*/

                //pitchRolling = filtration.PitchFilter(pitchpre, sizeOfArray, "pitch")[0];
                //pitchfiltered = filtration.PitchFilter(pitchpre, sizeOfArray, "pitch")[1];
            /*System.out.print("Filtered: ");
            for(int i = 0; i < sizeOfArray; i++){
                System.out.print(pitchfiltered[i] + ", ");
            }
            System.out.print("\n");
            System.out.print("Flipped: ");
            for(int i = 0; i < sizeOfArray; i++){
                System.out.print(pitchRolling[i] + ", ");
            }
            System.out.print("\n"); */

                boolean isPeriodic = repetitiveMotionDetector.isPeriodic(pitch, indexOffset);
                /*freqtext = (float) motion.getfreq();
                motionError = motion.isMotionError();
                toofast = motion.isToofast();

                if(isPeriodic){
                    current = System.currentTimeMillis();
                    if (prevUpdate == -1 || (current - prevUpdate) >= (1f/freqtext) * 1000f) {
                        prevUpdate = current;
                        numReps++;
                        rep = true;
                    }
                }*/
               // System.out.println("isPeriodic? " + isPeriodic + " Rep Count " + numReps);

                rollpre[0] = newPoints[1];
                rollRolling = filtration.Filter(rollpre, sizeOfArray, "roll")[0];
                rollfiltered = filtration.Filter(rollpre, sizeOfArray, "roll")[1];


                yawpre[0] = newPoints[2];
                yawRolling = filtration.Filter(yawpre, sizeOfArray, "yaw")[0];
                yawfiltered = filtration.Filter(yawpre, sizeOfArray, "yaw")[1];

                indexOffset++;

                // filtration.Filtration()


                //initDetector.adjustFirstPeriodicMovement();
                //call InitialPeiodicMotionDetector

                if (initDetector.isPeriodic(pitchRolling, rollRolling, yawRolling)) {
                    if (change == 0) {
                        change = 1;
                        if(FirstRep == 0) {
                            ActualIndex = indexOffset;
                         //   System.out.println("PERIODIC in " + initDetector.getPeakDetected() + " direction at index " + ActualIndex);
                        }
                        FirstRep = 1;
                        if (initDetector.getPeakDetected() == "Pitch")
                            Angle = 1.0;
                        else if (initDetector.getPeakDetected() == "Roll")
                            Angle = 2.0;
                        else if (initDetector.getPeakDetected() == "Yaw")
                            Angle = 3.0;
                        else
                            Angle = 0.0;
                    }
                    yawFreqMag = initDetector.getYawFreq();
                    double[] firstRep = initDetector.getFirstPeriodicMovement();
                /*for(int i = 0; i < firstRep.length; i++)
                    System.out.print(firstRep[i] + "  "  );
                System.out.println("Single Rep");*/
                    //repDetector.countIfRep(firstRep);
                    //repDetector.countIfRep(yawRolling);
                } else {
                    if (change == 1) {
                        change = 0;
                        Angle = 0.0;
                        //System.out.println("NOT PERIODIC at the index " + indexOffset);

                    }
                }

                //double peak = initDetectorList.getPeak();

                //System.out.print("Peak Frequency " + initDetectorList.getPeak() + "Hz\n");
               // System.out.print("Is Periodic? " + initDetectorList.isPeriodic(pitch, roll, yaw) + " in "+ initDetectorList.getPeakDetected() + " direction\n");

               /* double freqtext = initDetectorList.getFreq();
                double magnitude = initDetectorList.getMag();

               /* if(freqtext != 0) {
                    System.out.print("Freq:" + freqtext + "Current Time: " + System.currentTimeMillis() + " in "+ initDetectorList.getPeakDetected() + " direction" + "\n");
                }*/
               /* boolean isPeriodic = initDetectorList.isPeriodic(pitch, roll, yaw);
                //System.out.println("IS PERIODIC: " + isPeriodic + " FREQ " + freqtext + " MAG " + magnitude + " " +  initDetectorList.getPeakDetected());


                if (isPeriodic && newlyPeriodic == true) {
                    newlyPeriodic = false;
                   // System.out.println("START " + System.currentTimeMillis());
                }

                if(!newlyPeriodic){
                    double timechange = (1 /freqtext) * 1000;
                   // System.out.println("TIME CHANGE: " + timechange + " Current " + (System.currentTimeMillis() - prevUpdate));
                  //  System.out.println("IS PERIODIC: " + isPeriodic + " FREQ " + freqtext + " MAG " + magnitude + " " +  initDetectorList.getPeakDetected());

                    long current = System.currentTimeMillis();
                    if (prevUpdate == -1 || (current - prevUpdate) >= timechange) {
                        prevUpdate = current;
                        RepCount++;
                    }
                }




                if (RepCount != n) {
                    System.out.print("Freq: " + freqtext + " Mag: " + magnitude + " Rep Count: " + RepCount + " Current Time: " + System.currentTimeMillis() +"\n");
                    n++;
                }*/



               // if(initDetectorList.isPeriodic(pitch, roll, yaw))
                    //System.out.println(indexOffset);

                //System.out.println(indexOffset);
                pitchFreqMag = initDetector.getPitchFreq();
                rollFreqMag = initDetector.getRollFreq();


                /*System.out.println("Pitch");
                printFreq(pitchFreqMag);
                System.out.println("Roll");
                printFreq(rollFreqMag);
                System.out.println("Yaw");
                printFreq(yawFreqMag);*/
                // Update Chart
                chartF.updateXYSeries("PitchFreq", Arrays.copyOfRange(freqHz, 0, 20), Arrays.copyOfRange(pitchFreqMag, 0, 20), null);
                chartF.updateXYSeries("RollFreq", Arrays.copyOfRange(freqHz, 0, 20), Arrays.copyOfRange(rollFreqMag, 0, 20), null);
                chartF.updateXYSeries("YawFreq", Arrays.copyOfRange(freqHz, 0, 20), Arrays.copyOfRange(yawFreqMag, 0, 20), null);
                swF.repaintChart();
                //}
                // Thread.sleep(10);

                chartFi.updateXYSeries("Pitch", time, pitchfiltered, null); //DOWNSAMPLE2: Change filteredtime to time
                chartFi.updateXYSeries("Roll", time, rollfiltered, null); //DOWNSAMPLE2: Change filteredtime to time
                chartFi.updateXYSeries("Yaw", time, yawfiltered, null); //DOWNSAMPLE2: Change filteredtime to time
                swFi.repaintChart();

                chartFl.updateXYSeries("Pitch", time, pitchRolling, null);
                chartFl.updateXYSeries("Roll", time, rollRolling, null);
                chartFl.updateXYSeries("Yaw", time, yawRolling, null);
                swFl.repaintChart();

           /* chart.updateXYSeries("Pitch", rawtime, pitchpre, null);
            chart.updateXYSeries("Roll", rawtime, rollpre, null);
            chart.updateXYSeries("Yaw", rawtime, yawpre, null);
            sw.repaintChart(); */
            }
            idealAngle = csv.SignificantAngle(file);
            DesiredAngle[(file - 1)] = Double.toString(idealAngle);
            if (Angle == 0 ){
                Difference[(file-1)] = "4";
            }
            else{
                Difference[(file-1)] = Double.toString(Math.abs(Angle - idealAngle));
            }
            ActualAngle[(file -1)] = Double.toString(Angle);

            //Repetition Statistics

            if(ActualIndex == 0){ //No periodicity detected
                int desiredIndex = csv.getReps(file, 0);
                if (desiredIndex != 0){
                    RepIn[(file-1)] = "100"; //Shouldn't be Random
                }
                else
                    RepIn[(file-1)] = "1000"; //No Periodicity Detected Correctly
            }
            else { //Periodicity was detected
                for (int j = 0; j < 10; j++) {
                    int desiredIndex = csv.getReps(file, j);
                    //System.out.print("iNDEX " + index + " INDEX " + Index + " j " + j + "\n");
                    if (desiredIndex == 0 && j == 0){
                       RepIn[(file - 1)] = "10000"; //Periodicity detected when motion was random

                        j = 10;
                    }
                    else if ((desiredIndex > ActualIndex)) {
                        RepIn[(file - 1)] = Integer.toString(j);
                        j = 10;
                    }
                    else if (j > 0 && desiredIndex == 0){
                        RepIn[(file - 1)] = "100000"; //Detected After all reps complete
                        j = 10;
                    }
                }
            }
            //DesiredAngle[(file-1)] = "1.0";
            //ActualAngle[(file-1)] = "1.0";
            //Difference[(file-1)] = "1.0";
            //RepIn[(file-1)] = "1.0";

            file++;
            indexOffset = 1;
            FirstRep = 0;
            change = 0;
            ActualIndex = 0;
            Arrays.fill(pitchpre, 0.0);
            Arrays.fill(rollpre, 0.0);
            Arrays.fill(yawpre, 0.0);
            newlyPeriodic = true;
            prevUpdate = -1;
            RepCount = 0;
            n = 0;
        }
        //csv.Write(DesiredAngle,ActualAngle,Difference,RepIn,capacity);

    }

    private static void printFreq(double[] arr){
        for(double val: arr){
            System.out.print(val + " ");
        }
    }
    private static double[][] getSineData(double phase) {

        double[] xData = new double[100];
        double[] yData = new double[100];
        for (int i = 0; i < xData.length; i++) {
            double radians = phase + (2 * Math.PI / xData.length * i);
            xData[i] = radians;
            yData[i] = Math.sin(radians);
        }
        return new double[][] { xData, yData };
    }

    private static double[] shiftArray(double[] data, int sizeOfArray){
        for(int i = (sizeOfArray -1); i > 0; i--){
            data[i] = data[i-1];
        }

        return data;
    }
}