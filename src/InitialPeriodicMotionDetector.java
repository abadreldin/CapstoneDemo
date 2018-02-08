import java.util.ArrayList;

public class InitialPeriodicMotionDetector {

    int Fs;
    int arraySize;
    public double significantPeak;
    Complex[] dataPhiComplex;
    Complex[] dataPhiFrequency;
    private double[] dataPhiFrequencySpectrum;

    Complex[] dataThetaComplex;
    Complex[] dataThetaFrequency;
    private double[] dataThetaFrequencySpectrum;

    Complex[] dataPsiComplex;
    Complex[] dataPsiFrequency;
    double[] dataPsiFrequencySpectrum;
    public double[] firstPeriodicMovement = {80.14850616,80.12564087,80.10817719,80.09957123,80.03572845,79.91562653,79.84267426,79.95214844,80.20415497,80.46004486,80.60749054,80.63375092,80.62014008,80.65538788,80.76384735,80.909935,81.08303833,81.23756409,81.3493576,81.43107605,81.46389008,81.42539215,81.35132599,81.34351349,81.49992371,81.82836914,82.12723541,82.13444519,81.85236359,81.52705383,81.37695313,81.45658875,81.65753937,81.76304626,81.69212341,81.52700043,81.39430237,81.34116364,81.35736084,81.46394348,81.60385132,81.67286682,81.66210175,81.66308594,81.70985413,81.75709534,81.79367828,81.91552734,82.16725922,82.46808624,82.74829102,82.91863251,82.9877243,83.06419373,83.16661835,83.29158783,83.46130371,83.67137146,83.85097504,83.97845459,84.11410522,84.25371552,84.3829422,84.54003143,84.77734375,85.129776,85.6210556,86.27451324,87.09727478,87.99008942,88.91858673,90.0293045,91.53710938,93.62287903,96.45212555,100.2968597,105.4289474,111.9739151,119.8923798,128.9436951,138.7146759,149.2059479,160.3144073,172.2038116,184.3199463,195.9590149,206.4047852,214.9395447,221.6984406,227.0420532,231.5015717,235.7814026,240.1027832,243.7634277,246.6402435,248.70401,249.9556885,250.8506012,251.741394,252.7605133,253.7644348,254.5521698,255.059082,255.3110352,255.4765625,255.5069275,255.4299622,255.4388885,255.6524658,256.0392151,256.3956909,256.5252686,256.4555359,256.3180542,256.2177429,256.1611328,256.1196289,256.0817261,256.1359253,256.3403931,256.5588379,256.7122803,256.7882996,256.7432861,256.5635071,256.3375854,256.2173157,256.2690735,256.4611511,256.6766968,256.8385925,256.927887,256.9216614,256.835022,256.7330933,256.7128906,256.7528687,256.7922668,256.8411865,256.9121399,256.9824829,256.9791565,256.8825073,256.7071533,256.5133057,256.3511353,256.3085022,256.3873291,256.4970093,256.5588379,256.5010986,256.3213196,256.0777893,255.8883972,255.852066,255.9047699,256.0125427,256.1263123,256.1708374,256.145752,256.1045227,256.0671692,256.0584106,256.0613708,256.0425415,256.0245361,256.0017395,255.9788055,255.9482269,255.8771667,255.7641449,255.6303864,255.4992981,256.0592651,255.9701996,255.8462677,255.6726532,255.4721985,255.2270203,254.9323425,254.6139984,254.3146667,254.0467682,253.7871094,253.497757,253.1882629,252.8475037,252.4021759,251.7230072,250.6484528,249.0484467,246.8949432,244.1787567,240.919281,237.1529083,232.7306671,227.6851501,222.1439209,216.4523163,210.7201843,204.9793091,199.1843567,193.2563782,186.4150238,178.545578,169.5805054,159.6980133,149.6484375,140.3055115,132.2692413,125.7184906,120.1453171,114.5249557,107.9645767,100.473793,93.06520844,88.17816925,86.66981506,87.40933228,88.95980835,89.52469635,88.11155701,85.80877686,83.61880493,83.23468018,84.60814667,85.36591339,85.10765076,84.12686157,83.17337036,82.78987122,82.79541779,82.77806854,82.54958344,82.09269714,81.58166504,81.33309937,81.47276306,81.76643372,81.89372253,81.62377167,81.15860748,80.85119629,80.74666595,80.72606659,80.70344543,80.69221497,80.7250824,80.81280518,80.83229065,80.70163727,80.46900177,80.24803162,80.11468506,80.14894104,80.38993835,80.67208099,80.80936432,80.7172699,80.46523285,80.22898865,80.13378143,80.15129089,80.26589966,80.47402954,80.65519714,80.6264267,80.39479828,80.14031219,80.00365448,80.00296783,80.04283142};

    public double[] getFirstPeriodicMovement(){
        return firstPeriodicMovement;
    }

    public double[] getPitchFreq(){
        return dataPhiFrequencySpectrum;
    }

    public double[] getRollFreq(){
        return dataThetaFrequencySpectrum;
    }

    public double[] getYawFreq(){
        return dataPsiFrequencySpectrum;
    }

    //enum Angle {PHI, THETA, PSI};
    //Angle currAngle = PHI;
    //instead reference phi =1, theta = 2, and psi = 3

    public InitialPeriodicMotionDetector(){}

    public void finalize() {
        System.out.println("Detector is being destroyed");
    }

    public InitialPeriodicMotionDetector(int Fs, int arraySize){
        this.Fs = Fs;
        this.arraySize = arraySize;

        this.dataPhiComplex = new Complex[arraySize];
        this.dataPhiFrequencySpectrum = new double[arraySize];

        this.dataThetaComplex = new Complex[arraySize];
        dataThetaFrequencySpectrum = new double[arraySize];

        this.dataPsiComplex = new Complex[arraySize];
        this.dataPsiFrequencySpectrum = new double[arraySize];
    }

    public int getFs(){
        return Fs;
    }

    public int getArraySize (){
        return arraySize;
    }

    public double getPeak (){
        return significantPeak;
    }

    public boolean isPeriodic(double[] dataPhi, double[] dataTheta, double[] dataPsi) {

        //Analysis for Phi
        //this.currAngle = PHI;
        convertToComplex(dataPhi, 1);
        this.dataPhiFrequency = fft(dataPhiComplex);
        changeToFrequencySpectrum(1);
        double phiPeak = findPeak(1);
        //boolean resultPhi = findPeak2(1);

        //Analysis for Theta
        //this.currAngle = THETA;
        convertToComplex(dataTheta, 2);
        this.dataThetaFrequency = fft(dataThetaComplex);
        changeToFrequencySpectrum(2);
        double thetaPeak = findPeak(2);
        //boolean resultTheta = findPeak2(2);

        //Analysis for Psi
        //this.currAngle = PSI;
        convertToComplex(dataPsi, 3);
        this.dataPsiFrequency = fft(dataPsiComplex);
        changeToFrequencySpectrum(3);
        double psiPeak = findPeak(3);
        //boolean resultPsi =  findPeak2(3);

        //Determine the significant angle
        double[] movementDirection = (phiPeak > thetaPeak && phiPeak > psiPeak) ? dataPhiFrequencySpectrum: (thetaPeak > phiPeak && thetaPeak > psiPeak) ? dataThetaFrequencySpectrum: dataPsiFrequencySpectrum;
        significantPeak = (phiPeak > thetaPeak && phiPeak > psiPeak) ? phiPeak: (thetaPeak > phiPeak && thetaPeak > psiPeak) ? thetaPeak: psiPeak;
        //System.out.println(significantPeak);

        /*if(resultPhi || resultPsi || resultTheta){
            System.out.println("Peak at Phi" + resultPhi + "Peak at Theta" + resultTheta + "Peak at Psi" + resultPsi);
            return true;
        }*/

        if(significantPeak > 0){
            //find the relative time domain peak in that data set
            //Will use function from RepetitiveMotionPeriodicDetector to find rep of new periodic movement and fill that value into firstPeriodicMovement
            //double[] firstPeriodicMovement;
            //System.out.println("Found a peak");
            return true;
        }
        return false;
    }

    /*
    This function converts euler angles into a complex array of data which consists of real and imaginary data
    Input: raw data array and empty Complex arra
    Output: array of Complex data
     */
    public void convertToComplex(double[] data, int currAngle){
        //Convert polar data into complex numbers
        //Using radius to be 1
        int index = 0;
        switch(currAngle){
            case 1:
                for (double val : data) {
                    this.dataPhiComplex[index] = new Complex();
                    this.dataPhiComplex[index].setRe((double)Math.cos(Math.toRadians(val)));
                    this.dataPhiComplex[index].setIm((double)Math.sin(Math.toRadians(val)));
                    index++;
                }
                break;
            case 2:
                for (double val : data) {
                    this.dataThetaComplex[index] = new Complex();
                    this.dataThetaComplex[index].setRe((double)Math.cos(Math.toRadians(val)));
                    this.dataThetaComplex[index].setIm((double)Math.sin(Math.toRadians(val)));
                    index++;
                }
                break;
            case 3:
                for (double val : data) {
                    this.dataPsiComplex[index] = new Complex();
                    this.dataPsiComplex[index].setRe((double)Math.cos(Math.toRadians(val)));
                    this.dataPsiComplex[index].setIm((double)Math.sin(Math.toRadians(val)));
                    index++;
                }
                break;
        }
    }

    //Need to update to remove threshold
    /*
    This function finds the peak values from an array which contains magnitude of the frequency spectrum
    Input: array that contains the frequency spectrum and respective magnitudes
    Output: peak value
     */
    private double findPeak(int currAngle){
        //Frequencies are in Hertz
        double peakVal = 0;
        int countAboveThreshold = 0;
        double threshold = 100;
        switch(currAngle){
            case 1:
                for(int i = 10; i < arraySize; i++){
                    if(this.dataPhiFrequencySpectrum[i] > threshold){
                        countAboveThreshold++;
                        if(peakVal < this.dataPhiFrequencySpectrum[i]){
                            peakVal = this.dataPhiFrequencySpectrum[i];
                        }
                    }
                }
                break;
            case 2:
                for(int i = 10; i < arraySize; i++){
                    if(this.dataThetaFrequencySpectrum[i] > threshold){
                        countAboveThreshold++;
                        if(peakVal < this.dataThetaFrequencySpectrum[i]){
                            peakVal = this.dataThetaFrequencySpectrum[i];
                        }
                    }
                }
                break;
            case 3:
                for(int i = 10; i < arraySize; i++){
                    if(this.dataPsiFrequencySpectrum[i] > threshold){
                        countAboveThreshold++;
                        if(peakVal < this.dataPsiFrequencySpectrum[i]){
                            peakVal = this.dataPsiFrequencySpectrum[i];
                        }
                    }
                }
                break;
        }
        //System.out.println("Maximum frequency power is: " + peakVal + " and number of values above power frequency of 30 are: " + countAboveThreshold);
        return peakVal;
    }

    public boolean findPeak2(int currAngle){
        double[][] peaks;
        switch(currAngle){
            case 1:
                peaks = characterizeReal(this.dataPhiFrequencySpectrum);
                break;
            case 2:
                peaks = characterizeReal(this.dataThetaFrequencySpectrum);
                break;
            case 3:
                peaks = characterizeReal(this.dataPsiFrequencySpectrum);
                break;
            default:
                peaks = characterizeReal(this.dataPhiFrequencySpectrum);
                break;
        }

        int numPeaksLargerThan200 = 0;
        boolean singlePeak = false;
        System.out.println(peaks[0].length);
        for(int i=0; i< peaks[0].length; i++){
            if(peaks[0][i] > 200.0){
                numPeaksLargerThan200++;
                double tempPeak = peaks[0][i];
                for(int j = i; j < peaks.length; j++){
                    if((tempPeak - peaks[0][j] ) < 200.0){
                        singlePeak = false;
                    }
                    else{
                        singlePeak = true;
                    }
                }
            }
        }
        return singlePeak;
    }

    private double[][] characterizeReal(double[] arr) {
        int offset = 1;
        ArrayList max = new ArrayList();
        ArrayList maxi = new ArrayList();
        //ArrayList min = new ArrayList();
        //ArrayList mini = new ArrayList();
        int len = arr.length;
        //float[][] max = new float[2][len];
        //float[][] min = new float[2][len];
        for (int i = offset; i < len-offset; i=i+offset) {
            double leading;
            double following;
            leading = arr[i+offset] - arr[i];
            following = arr[i] - arr[i-offset];
//            System.out.println("Checking index "+i);
//            System.out.println("Leading = "+leading);
//            System.out.println("Following = "+following);

            if(leading<0 && following>0 && (i + offset*3)<len){
                double leading2 = arr[i+offset*3] - arr[i+offset*2];
                if(leading2<0){
                    max.add(arr[i]);
                    maxi.add(i);

                    System.out.println("Max " +arr[i]+" Found at " + i);
                    //System.out.println("Elements of Maxarr" +max);

                    //max[0][i]=arr[i];
                    //max[1][i]=i;
                }
            }
           /* else if(following<0 && leading>0){
                double following2 = arr[i-40] - arr[i-60];
                if(following2<0){
                    min.add(arr[i]);
                    mini.add(i);
                    System.out.println("Min " +arr[i]+" Found at " +i);
                    //System.out.println("Elements of Minarr" +min);
                    //min[0][i]=arr[i];
                    //min[1][i]=i;
                }
            }*/
        }
        /*System.out.println("Length of Max Array List "+max.size());
        System.out.println("Length of Min Array List "+min.size());
        int maxl = max.size();
        int minl = min.size();
        float[][] maxarr = new float[2][maxl];
        float[][] minarr = new float[2][minl];
        System.out.println("Length of Max Array "+maxarr[0].length);
        System.out.println("Length of Min Array "+minarr[0].length);
        */
        int maxl = max.size();
        double[][] maxarr = new double[2][maxl];
        System.out.println("Length of Max Array "+ maxarr[0].length);

        for(int i = 0; i<maxl; i++){
            maxarr[0][i]=(double)max.get(i);
            maxarr[1][i]=(int)maxi.get(i);
//            System.out.println("added max");
        }
        /*for(int i = 0; i<minl;i++){
            minarr[0][i]=(float)min.get(i);
            minarr[1][i]=(int)mini.get(i);
//            System.out.println("added min");
        }*/
       /* if(flag){
            return minarr;
        }
        else{
            return maxarr;
        }*/
        return maxarr;
    }
    /*
    This function converts a complex array of data which consists of real and imaginary data into
    an array that contains the magnitude of the fourier spectrum
    Input: Complex data array
    Output: double data array
     */
    public void changeToFrequencySpectrum(int currAngle){
        //convert into frequency power spectrum
        double factor = (double) Fs/arraySize; //sampling f of 100Hz, divided by fft length of arraysize - 256

        switch(currAngle){
            case 1:
                for(int i = 0; i < arraySize; i++){
                    this.dataPhiFrequencySpectrum[i] = (double) java.lang.Math.sqrt(java.lang.Math.pow(this.dataPhiFrequency[i].re(),2) + java.lang.Math.pow(this.dataPhiFrequency[i].im(),2));
                    //System.out.println(dataFrequencySpectrum[i]);
                    //System.out.println("Current frequency power is: " + dataFrequencySpectrum[i] + " n: " + (i) + " and frequency is: " + factor*i + "Hz");
                }
                break;
            case 2:
                for(int i = 0; i < arraySize; i++){
                    this.dataThetaFrequencySpectrum[i] = (double) java.lang.Math.sqrt(java.lang.Math.pow(this.dataThetaFrequency[i].re(),2) + java.lang.Math.pow(this.dataThetaFrequency[i].im(),2));
                    //System.out.println(dataFrequencySpectrum[i]);
                    //System.out.println("Current frequency power is: " + dataFrequencySpectrum[i] + " n: " + (i) + " and frequency is: " + factor*i + "Hz");
                }
                break;
            case 3:
                for(int i = 0; i < arraySize; i++){
                    this.dataPsiFrequencySpectrum[i] = (double) java.lang.Math.sqrt(java.lang.Math.pow(this.dataPsiFrequency[i].re(),2) + java.lang.Math.pow(this.dataPsiFrequency[i].im(),2));
                    //System.out.println(dataFrequencySpectrum[i]);
                    //System.out.println("Current frequency power is: " + dataFrequencySpectrum[i] + " n: " + (i) + " and frequency is: " + factor*i + "Hz");
                }
                break;
        }
    }

    /*
    This function converts a complex array of data which consists of real and imaginary data into
    an array that contains the frequency spectrum - the fast fourier transform recursively
    Input: Complex data array
    Output: Complex data array
     */
    public Complex[] fft(Complex[] x) {
        int n = x.length;

        // base case
        if (n == 1) return new Complex[] { x[0] };

        // radix 2 Cooley-Tukey FFT
        if (n % 2 != 0) {
            throw new IllegalArgumentException("n is not a power of 2");
        }

        // fft of even terms
        Complex[] even = new Complex[n/2];
        for (int k = 0; k < n/2; k++) {
            even[k] = x[2*k];
        }
        Complex[] q = fft(even);

        // fft of odd terms
        Complex[] odd  = even;  // reuse the array
        for (int k = 0; k < n/2; k++) {
            odd[k] = x[2*k + 1];
        }
        Complex[] r = fft(odd);

        // combine
        Complex[] y = new Complex[n];
        for (int k = 0; k < n/2; k++) {
            double kth = (double) (-2 * k * Math.PI / n);
            Complex wk = new Complex((double)Math.cos(kth), (double)Math.sin(kth));
            y[k]       = q[k].plus(wk.times(r[k]));
            y[k + n/2] = q[k].minus(wk.times(r[k]));
        }
        return y;
    }
}