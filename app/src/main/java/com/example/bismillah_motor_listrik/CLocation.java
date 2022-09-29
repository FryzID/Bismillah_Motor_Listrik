package com.example.bismillah_motor_listrik;

import android.location.Location;

public class CLocation extends Location {

    private boolean bUseMetricUnits = false;

    public CLocation(Location location) {
        this(location, true);
    }

    public CLocation(Location location, boolean bUseMetricUnits) {
        super(location);
        this.bUseMetricUnits = bUseMetricUnits;
    }

    public CLocation(String provider) {
        super(provider);
    }

    public boolean getUseMetricUnits() {
        return bUseMetricUnits;
    }

    public void setUserMetricUnits(boolean bUseMetricUnits) {
        this.bUseMetricUnits = bUseMetricUnits;
    }

    @Override
    public float distanceTo(Location dest) {
        float nDistance = super.distanceTo(dest);

        if (!this.getUseMetricUnits()){
            nDistance = nDistance * 3.28083989501312f;
        }

        return nDistance;
    }

    @Override
    public double getAltitude() {
        double nAltitude = super.getAltitude();

        if (!this.getUseMetricUnits()){
            //Convert meter/secon to Feet
            nAltitude = nAltitude * 3.28083989501312f;
        }

        return nAltitude;
    }

    @Override
    public float getSpeed() {
         float nSpeed = super.getSpeed() * 3.8f;

        if (!this.getUseMetricUnits()){
            //Convert meter/secon to miles/hour
            nSpeed = nSpeed * 2.23693629f;
        }

        return nSpeed;
    }

    @Override
    public float getAccuracy() {
        float nAccuracy = super.getAccuracy();

        if (!this.getUseMetricUnits()){
            nAccuracy = nAccuracy * 3.28083989501312f;
        }

        return nAccuracy;
    }
}
