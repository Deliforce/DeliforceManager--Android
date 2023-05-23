
package com.app.deliforcemanager.ModelClass.TaskDetailsApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActionBlock {

    @SerializedName("notes")
    @Expose
    private Notes notes;
    @SerializedName("image")
    @Expose
    private Image image;
    @SerializedName("signature")
    @Expose
    private Signature signature;
    @SerializedName("barcode")
    @Expose
    private Barcode barcode;
    @SerializedName("imageCaption")
    @Expose
    private ImageCaption imageCaption;
    @SerializedName("distance")
    @Expose
    private RadiusDistance distance;

    @SerializedName("arrivedDistance")
    @Expose
    private RadiusDistance arrivedDistance;
    public RadiusDistance getDistance() {
        return distance;
    }

    public void setDistance(RadiusDistance distance) {
        this.distance = distance;
    }


    public Notes getNotes() {
        return notes;
    }

    public void setNotes(Notes notes) {
        this.notes = notes;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Signature getSignature() {
        return signature;
    }

    public void setSignature(Signature signature) {
        this.signature = signature;
    }

    public Barcode getBarcode() {
        return barcode;
    }

    public void setBarcode(Barcode barcode) {
        this.barcode = barcode;
    }

    public ImageCaption getImageCaption() {
        return imageCaption;
    }

    public void setImageCaption(ImageCaption imageCaption) {
        this.imageCaption = imageCaption;
    }

    public RadiusDistance getArrivedDistance() {
        return arrivedDistance;
    }

    public void setArrivedDistance(RadiusDistance arrivedDistance) {
        this.arrivedDistance = arrivedDistance;
    }
}
