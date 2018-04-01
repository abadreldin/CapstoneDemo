/**
 * Created by janellesomerville on 2018-02-22.
 */

//import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import java.io.File;
//import java.io.IOException;
//import java.util.Iterator;

import sun.security.krb5.internal.crypto.Des;

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

    public static double[] getIdealRepCounts(int file) {
        String csvFile = "/Users/janellesomerville/Desktop/csvs/" + Integer.toString(file) + ".csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        double seconds = 0.0;
        double[] output = new double[4];
        int i = -1;

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                if(i == 0) {

                    // use comma as separator
                    String[] csvdata = line.split(cvsSplitBy);

                    if(!(csvdata[8].replace("\"","").isEmpty())){
                        output[1]= Double.parseDouble(csvdata[8].replace("\"", ""));
                    }

                    if(!(csvdata[9]).replace("\"","").isEmpty()){
                        output[2] = Double.parseDouble(csvdata[9].replace("\"", ""));
                    }

                    if(!(csvdata[7]).replace("\"","").isEmpty()){
                        output[3] = Double.parseDouble(csvdata[7].replace("\"", ""));
                    }

                    else{
                        output[1] = 0;
                    }

                }
                else if (i == 1){
                    // use comma as separator
                    String[] csvdata = line.split(cvsSplitBy);

                    if(!(csvdata[8].replace("\"","").isEmpty())){
                        output[0]= Double.parseDouble(csvdata[8].replace("\"", ""));
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

    public static double[][] getMinsAndMaxes(int file, int expected) {
        String csvFile = "/Users/janellesomerville/Desktop/csvs/" + Integer.toString(file) + ".csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        double seconds = 0.0;
        double[][] output = new double[4][100];
        int i = -1;

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {



               if(i != -1 && i < (expected+1)) { // use comma as separator
                   String[] csvdata = line.split(cvsSplitBy);

                   if (!(csvdata[10].replace("\"", "").isEmpty())) {
                       output[0][i] = Double.parseDouble(csvdata[10].replace("\"", ""));

                   }

                   if (!(csvdata[11].replace("\"", "").isEmpty())) {
                       output[1][i] = Double.parseDouble(csvdata[11].replace("\"", ""));
                   }

                   if (!(csvdata[12].replace("\"", "").isEmpty())) {
                       output[2][i] = Double.parseDouble(csvdata[12].replace("\"", ""));
                   }

                   if (!(csvdata[13].replace("\"", "").isEmpty())) {
                       output[3][i] = Double.parseDouble(csvdata[13].replace("\"", ""));
                   }
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

    public static void Write(String[] DesiredFinalRepCount, String[] ActualFinalRepCount, String[] DesiredTotalRepCount, String[] ActualTotalRepCount, int capacity,
    String[] ideal_p2p, String[] desired_p2p, String[] DesiredSigAng, String[] ActualSigAng) throws FileNotFoundException{
        PrintWriter pw = new PrintWriter(new File("ResultsCombined.csv"));
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
        sb.append(",");
        sb.append("Desired Peak 2 Peak");
        sb.append(",");
        sb.append("Actual Peak 2 Peak");
        sb.append(",");
        sb.append("Desired Significant Angle");
        sb.append(",");
        sb.append("Actual Significant Angle");
        sb.append(",");
        sb.append('\n');

        for(int i = 0; i < capacity; i++) {
            sb.append(DesiredFinalRepCount[i]);
            sb.append(',');
            sb.append(ActualFinalRepCount[i]);
            sb.append(',');
            sb.append(DesiredTotalRepCount[i]);
            sb.append(',');
            sb.append(ActualTotalRepCount[i]);
            sb.append(',');
            sb.append(desired_p2p[i]);
            sb.append(',');
            sb.append(ideal_p2p[i]);
            sb.append(',');
            sb.append(DesiredSigAng[i]);
            sb.append(',');
            sb.append(ActualSigAng[i]);
            sb.append('\n');
        }

        pw.write(sb.toString());
        pw.close();
        System.out.println("done!");
    }

    public static void Write2(String[][] DesiredMaximums, String[][] ActualMaximums, String[][] DesiredMinimums, String[][] ActualMinimums,
                              String[][] DesiredMaximumValues, String[][] ActualMaximumValues, String[][]DesiredMinimumValues,
                              String[][] ActualMinimumValues, int g) throws FileNotFoundException{
        PrintWriter pw = new PrintWriter(new File("ResultsFile" + g + ".csv"));
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
        sb.append("Desired Maximum Indexes");
        sb.append(',');
        sb.append("Actual Maximum Indexes");
        sb.append(',');
        sb.append("Desired Minimum Indexes");
        sb.append(',');
        sb.append("Actual Minimum Indexes");
        sb.append(",");
        sb.append("Desired Maximum Values");
        sb.append(",");
        sb.append("Actual Maximum Values");
        sb.append(",");
        sb.append("Desired Minimum Values");
        sb.append(",");
        sb.append("Actual Minimum Values");
        sb.append('\n');

        for(int i = 0; i < 25; i++) {
            //if(Double.parseDouble(DesiredMaximums[g][i]) != 0) {
                sb.append(DesiredMaximums[g][i]);
                sb.append(',');
           // }
            //if(Double.parseDouble(ActualMaximums[g][i]) != 0) {
                sb.append(ActualMaximums[g][i]);
                sb.append(',');
           // }
           // if(Double.parseDouble(DesiredMinimums[g][i]) != 0) {
                sb.append(DesiredMinimums[g][i]);
                sb.append(',');
           // }
           // if(Double.parseDouble(ActualMinimums [g][i]) != 0) {
                sb.append(ActualMinimums[g][i]);
                sb.append(',');
         //   }
           // if(Double.parseDouble(DesiredMaximumValues[g][i]) != 0) {
                sb.append(DesiredMaximumValues[g][i]);
                sb.append(',');
            //}
           // if(Double.parseDouble(ActualMaximumValues[g][i]) != 0) {
                sb.append(ActualMaximumValues[g][i]);
                sb.append(',');
           // }
          //  if(Double.parseDouble(DesiredMinimumValues[g][i]) != 0) {
                sb.append(DesiredMinimumValues[g][i]);
                sb.append(',');
         //   }
         //   if(Double.parseDouble(ActualMinimumValues[g][i]) != 0) {
                sb.append(ActualMinimumValues[g][i]);
                sb.append(',');
          //  }
            sb.append('\n');
        }

        pw.write(sb.toString());
        pw.close();
        System.out.println("done!");
    }
}
