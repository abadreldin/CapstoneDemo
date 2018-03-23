/**
 * Created by janellesomerville on 2018-02-22.
 */

//import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import java.io.File;
//import java.io.IOException;
//import java.util.Iterator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.PrintWriter;

public class CSV {
    public static double[] Read(int j, int file) {
        String csvFile = "/Users/janellesomerville/Desktop/csvs/" + Integer.toString(file) + ".csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        double[] output = new double[3];
        int i = -1;

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                if(i == j) {

                    // use comma as separator
                    String[] csvdata = line.split(cvsSplitBy);

                    output[0] = Double.parseDouble(csvdata[3].replace("\"",""));
                    output[1] = Double.parseDouble(csvdata[4].replace("\"",""));
                    output[2] = Double.parseDouble(csvdata[5].replace("\"", ""));


                   // System.out.println("Country [pitch= " + output[0] + "roll= " + output[1] + " , yaw=" + output[2] + "]");
                }

                i++;


            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return output;

    }

    public static double SignificantAngle(int file) {
        String csvFile = "/Users/janellesomerville/Desktop/csvs/" + Integer.toString(file) + ".csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        double output = 0;
        int i = 0;

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                if(i == 1) {

                    // use comma as separator
                    String[] csvdata = line.split(cvsSplitBy);

                    if(!(csvdata[7].replace("\"","").isEmpty())){
                        output = Double.parseDouble(csvdata[7].replace("\"",""));
                    }
                    //System.out.println("Country [pitch= " + output + "]");
                }

                i++;


            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return output;

    }

    public static int getRepTimes(int file, int j) {
        String csvFile = "/Users/janellesomerville/Desktop/csvs/" + Integer.toString(file) + ".csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        double seconds = 0.0;
        int output = 0;
        int i = -1;

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                if(i == j) {

                    // use comma as separator
                    String[] csvdata = line.split(cvsSplitBy);

                    if(!(csvdata[9].replace("\"","").isEmpty())){
                        seconds= Double.parseDouble(csvdata[9].replace("\"", ""));
                        output = (int)(seconds*100);
                    }

                    else{
                        output = 0;
                    }

                    //System.out.println("Country [pitch= " + output[0] + "roll= " + output[1] + " , yaw=" + output[2] + "]");
                }

                i++;


            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return output;

    }

    public static int[] getIdealRepCounts(int file) {
        String csvFile = "/Users/janellesomerville/Desktop/csvs/" + Integer.toString(file) + ".csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        double seconds = 0.0;
        int[] output = new int[2];
        int i = -1;

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                if(i == 0) {

                    // use comma as separator
                    String[] csvdata = line.split(cvsSplitBy);

                    if(!(csvdata[8].replace("\"","").isEmpty())){
                        output[1]= (int)Double.parseDouble(csvdata[8].replace("\"", ""));
                    }

                    else{
                        output[1] = 0;
                    }

                }
                else if (i == 1){
                    // use comma as separator
                    String[] csvdata = line.split(cvsSplitBy);

                    if(!(csvdata[8].replace("\"","").isEmpty())){
                        output[0]= (int)Double.parseDouble(csvdata[8].replace("\"", ""));
                    }

                    else{
                        output[0] = output[1];
                    }
                    //System.out.print("Output " + output[0] + " Second " + output[1]);
                }

                i++;


            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return output;

    }

    public static void Write(String[] DesiredFinalRepCount, String[] ActualFinalRepCount, String[] DesiredTotalRepCount, String[] ActualTotalRepCount, int capacity) throws FileNotFoundException{
        PrintWriter pw = new PrintWriter(new File("Results.csv"));
        StringBuilder sb = new StringBuilder();
        /*sb.append("Desired Angle");
        sb.append(',');
        sb.append("Actual Angle");
        sb.append(',');
        sb.append("Difference");
        sb.append(',');
        sb.append("Actual Index");
        sb.append(',');
        sb.append("Desired Index");
        sb.append(',');*/
        sb.append("Desired Final Repetition Count");
        sb.append(',');
        sb.append("Actual Final");
        sb.append(',');
        sb.append("Desired Total Repetition Count");
        sb.append(',');
        sb.append("Actual Total");
        sb.append('\n');

        for(int i = 0; i < capacity; i++) {
            sb.append(DesiredFinalRepCount[i]);
            sb.append(',');
            sb.append(ActualFinalRepCount[i]);
            sb.append(',');
            sb.append(DesiredTotalRepCount[i]);
            sb.append(',');
            sb.append(ActualTotalRepCount[i]);
            sb.append('\n');
        }

        pw.write(sb.toString());
        pw.close();
        System.out.println("done!");
    }
}
