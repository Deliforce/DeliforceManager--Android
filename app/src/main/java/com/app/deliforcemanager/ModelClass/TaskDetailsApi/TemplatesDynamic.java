
package com.app.deliforcemanager.ModelClass.TaskDetailsApi;

import com.google.gson.JsonArray;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TemplatesDynamic {

    @SerializedName("fieldName")
    @Expose
    private String fieldName;

    @SerializedName("fieldValue")
    @Expose
    private String fieldValue;

    @SerializedName("dataType")
    @Expose
    private String dataType;

    @SerializedName("permitAgent")
    @Expose
    private String permitAgent;

    @SerializedName("mandatoryFields")
    @Expose
    private String mandatoryFields;

    @SerializedName("order")
    @Expose
    private int order;


    @SerializedName("selectedValues")
    @Expose
    private JsonArray selectedValues;

    @SerializedName("crmRefId")
    @Expose
    private String crmRefId;

    @SerializedName("options")
    @Expose
    private List<DropDownOptions> options;

    @SerializedName("zohoItemInfo")
    @Expose
    private ArrayList<ZohoItemInfo> zohoItemInfo;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getPermitAgent() {
        return permitAgent;
    }

    public void setPermitAgent(String permitAgent) {
        this.permitAgent = permitAgent;
    }

    public String getMandatoryFields() {
        return mandatoryFields;
    }

    public JsonArray getSelectedValues() {
        return selectedValues;
    }

    public void setMandatoryFields(String mandatoryFields) {
        this.mandatoryFields = mandatoryFields;
    }

    public void setOptions(List<DropDownOptions> options) {
        this.options = options;
    }


    public List<DropDownOptions> getOptions() {
        return this.options;
    }

    public void setSelectedValues(JsonArray selectedValues) {
        this.selectedValues = selectedValues;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public ArrayList<ZohoItemInfo> getZohoItemInfo() {
        return zohoItemInfo;
    }

    public void setZohoItemInfo(ArrayList<ZohoItemInfo> zohoItemInfo) {
        this.zohoItemInfo = zohoItemInfo;
    }

    public String getCrmRefId() {
        return crmRefId;
    }

    public void setCrmRefId(String crmRefId) {
        this.crmRefId = crmRefId;
    }
}
