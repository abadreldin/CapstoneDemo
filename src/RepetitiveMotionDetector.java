import java.util.ArrayList;

public class RepetitiveMotionDetector {

    public static int GetRepCount(double pitch, double yaw, double roll) {

    /*double timechange = (1 / freq) * 1000;

        long current = System.currentTimeMillis();
        if (prevUpdate == -1 || (current - prevUpdate) >= timechange) {
            prevUpdate = current;
            RepCount++;
        }*/
    return 1;
    }


    public static boolean isPeriodic(ArrayList<Double> data) {
        ArrayList<Integer> min = new ArrayList<Integer>();
        ArrayList<Integer> max = new ArrayList<Integer>();
        ArrayList<Double> frequency = new ArrayList<Double>();

        int downsample = 40;
        int trending = 0;
        int p = 0;
        int k = 0;

        for(int i = 0; i < (data.size()-downsample); i++){

        if ((i%40) == 0 && i != 0){
                if (trending == 0) { //no known trend yet
                    if (data.get(i) < data.get(i + downsample)) //i + downsampled because data(0) more recent
                        trending = -1;
                    else
                        trending = 1;
                }
                else if(trending == -1) { //trending downward so looking for a min
                    if (data.get(i) > data.get(i + downsample)) {
                        min.add(k, i);
                        if (k != 0) {
                            double min_difference = min.get(k) - min.get(k - 1);
                            if (min_difference > 200 && min_difference < 600) {
                                //isPeriodic true
                                double frequency_val = (1 / (min_difference / 1000));
                                frequency.add(0, frequency_val);
                                System.out.println("FREQUENCY: " + frequency_val); //WARNING
                                if (min_difference > 500)
                                    System.out.println("Too Slow"); //WARNING
                                else if (min_difference < 300)
                                    System.out.println("Too Fast"); //WARNING
                            }
                        }
                        trending = 1;
                        k++;
                    }
                }
                else if(trending == 1) {// trending upward so looking for max
                    if (data.get(i) < data.get(i + downsample)) {
                        max.add(p, i);
                        if (p != 0) {
                            double max_difference = max.get(p) - max.get(p - 1);
                            if (max_difference > 200 && max_difference < 600) {
                                //isPeriodic true
                                double frequency_val = (1 / (max_difference / 1000));
                                frequency.add(0, frequency_val);
                                System.out.println("FREQUENCY: " + frequency_val); //WARNING
                                if (max_difference > 500)
                                    System.out.println("Too Slow"); //WARNING
                                else if (max_difference < 300)
                                    System.out.println("Too Slow"); //WARNING
                            }
                        }
                        trending = -1;
                        p++;
                    }
                }
            }
    }
        return true;
    }
}