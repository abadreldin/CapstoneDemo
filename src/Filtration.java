import java.util.Arrays;

public class Filtration {

    /* This function will return a 2D Array that has the flipped and filtered arrays as two rows */
    public static double[][] Filter(double[] data, int capacity, String angle){
        int pitch_flip = 0;
        int roll_flip = 0;
        int yaw_flip = 0;
        double pitchB[] = {};
        double rollB[] = {};
        double yawB[] = {};
        double filtered_data[] = new double[capacity];

        if (angle == "pitch") {
            pitch_flip = FlipCheck(data, pitch_flip);
            if (pitch_flip == 1) {
                data[0] = data[0] + 360;
            }
            filtered_data = Convolution(pitchB, data);
        }

        else if (angle == "roll") {
            roll_flip = FlipCheck(data, roll_flip);
            if (roll_flip == 1) {
                data[0] = data[0] + 360;
            }
            filtered_data = Convolution(rollB, data);
        }

        else if (angle == "yaw") {
            yaw_flip = FlipCheck(data, yaw_flip);
            if (yaw_flip == 1) {
                data[0] = data[0] + 360;
            }
            filtered_data = Convolution(yawB, data);
        }

        double[][] result = new double[][]{data, filtered_data};

        return result;

    }

    /* This function returns either 0 or 1 to indicate whether the data is flipped */
    public static int FlipCheck(double[] data, int alreadyFlipped) {
        int i = 0; //this is used as an index to get the previous data point and current
        int flip; //this returned, 0 is false, 1 is true
        double lastVal = data[i + 1];
        double currVal = data[i];

        //if the data is already flipped we are checking for the point when the current angle is
        //less than 360 and the last was over 360
        if (alreadyFlipped == 1) {  //alreadyflipped is passed in
            if ((lastVal > 360) && (currVal > 300))
                flip = 0; //if it is no longer flipped, we return false
            else
                flip = 1; //otherwise we return true
        }

        //if the last point was not flipped then we are checking if the last point was over 300
        //and the current is under 50--> I can show all this logic graphically if it's confusing
        else {
            if ((lastVal > 300) && (currVal < 50)) {
                flip = 1;
            } else
                flip = 0;
        }

        return flip; //basically returning a true or false on whether the data is still flipped
    }

    /*This function will return the data array after the FIR filter has been applied*/
    private static double[] Convolution(double[] b, double[] data) {
        int sizeofb = b.length; //the size of b
        int sizeofdata = data.length; //the number of data points we are storing
        int numrows = (sizeofdata + sizeofb) - 1; //the number of rows depends on the number of delays
        double[][] multi = new double[numrows][3]; //a 2-d matrix to store a matrix with 0s for convolution
        double[] y = new double[numrows];
        int r = 0; //used to index rows
        int c = 0; //used to index columns


        for (r = 0; r < sizeofdata; r++) { //add zeros before the first data point and shift along
            for (c = 0; ((c <= r) && (c < sizeofb)); c++) {
                multi[r][c] = data[r - c];
            }
        }

        for (r = (sizeofdata - 1); r < numrows; r++) { //add zeros once the last data point has moved through
            for (c = 0; c < sizeofb; c++) {
                if ((r - c) < sizeofdata) {
                    multi[r][c] = data[r - c];
                }
            }
        }

        for (r = 0; r < numrows; r++) { //multiply each row by b and sum
            double sum = 0;
            for (c = 0; c < sizeofb; c++) {
                sum = (multi[r][c] * b[c]) + sum;
            }
            y[r] = sum;
        }

        return y;

    }


}

