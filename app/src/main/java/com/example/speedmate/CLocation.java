package com.example.speedmate;

import android.location.Location;

public class CLocation extends Location {
    private boolean bUseMetricUnits = true;

    public CLocation(Location location){
        this(location,true);
    }

    public CLocation(Location location,boolean bUseMetricUnits){
        super(location);
        this.bUseMetricUnits = bUseMetricUnits;
    }

    public boolean getbUseMetricUnits(){
        return this.bUseMetricUnits;
    }


}
