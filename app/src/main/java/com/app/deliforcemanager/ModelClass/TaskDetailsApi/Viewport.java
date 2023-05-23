
package com.app.deliforcemanager.ModelClass.TaskDetailsApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Viewport {

    @SerializedName("south")
    @Expose
    private Double south;
    @SerializedName("west")
    @Expose
    private Double west;
    @SerializedName("north")
    @Expose
    private Double north;
    @SerializedName("east")
    @Expose
    private Double east;

    public Double getSouth() {
        return south;
    }

    public void setSouth(Double south) {
        this.south = south;
    }

    public Double getWest() {
        return west;
    }

    public void setWest(Double west) {
        this.west = west;
    }

    public Double getNorth() {
        return north;
    }

    public void setNorth(Double north) {
        this.north = north;
    }

    public Double getEast() {
        return east;
    }

    public void setEast(Double east) {
        this.east = east;
    }

}
