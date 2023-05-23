package com.app.deliforcemanager.Utils;

/**
 * Created by nextbrain on 28/4/18.
 */

public class DrawerItem {
    private int icon;
    private String title;
    private Boolean selected=false;

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public int getIcon() {
        return icon;
    }
    public void setIcon(int icon) {
        this.icon = icon;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
}