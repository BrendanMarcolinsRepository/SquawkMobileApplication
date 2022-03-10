package com.example.a321projectprototype.User;

import android.graphics.drawable.Drawable;

public class ItemDataModel
{
    String txtname;
    String url;
    int drawable;

    public ItemDataModel(String txtname, String url, int drawable)
    {
        this.txtname = txtname;
        this.url = url;
        this.drawable = drawable;
    }

    public ItemDataModel(String txtname)
    {
        this.txtname = txtname;
        this.url = url;
        this.drawable = drawable;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public String getTxtname()
    {
        return txtname;
    }
}
