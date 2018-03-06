import java.util.ArrayList;

public class RepetitiveMotionDetector {

public static int GetRepCount(boolean isPeriodic, double freq, int RepCount, long prevUpdate) {

    double timechange = (1 / freq) * 1000;

        long current = System.currentTimeMillis();
        if (prevUpdate == -1 || (current - prevUpdate) >= timechange) {
            prevUpdate = current;
            RepCount++;
        }

    return RepCount;
}
}