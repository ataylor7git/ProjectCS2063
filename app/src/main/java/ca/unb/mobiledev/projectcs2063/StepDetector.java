package ca.unb.mobiledev.projectcs2063;

import android.util.Log;

/**
 * Computing and processing accelerometer data.
 */
public class StepDetector {
    private static final String TAG = "Step Detector";
    private static final int INACTIVE_SAMPLE = 12;
    private int currentSample = 0;
    private int stepCount = 0;
    private boolean isActiveCounter;
    private double topLimit = 27;
    private boolean changedSteps = false;

    public StepDetector() {
        isActiveCounter = true;
    }

    public boolean detect(double accelerometerValue,  double currentThreshold) {
        if (currentSample == INACTIVE_SAMPLE) {
            currentSample = 0;
            if (!isActiveCounter)
                isActiveCounter = true;
        }
        if (isActiveCounter && (accelerometerValue > currentThreshold) && (accelerometerValue < topLimit)) {
            currentSample = 0;
            isActiveCounter = false;
            Log.i(TAG, "Acceleration: " + accelerometerValue);
            stepCount++;
            changedSteps = true;
            return true;
        }

        ++currentSample;
        return false;
    }

    public int getStepCount()
    {
        return stepCount;
    }

    public void setStepCount(int steps)
    {
        stepCount = steps;
    }

    public boolean changed()
    {
        if(changedSteps)
        {
            changedSteps = false;
            return true;
        }
        return false;
    }
}