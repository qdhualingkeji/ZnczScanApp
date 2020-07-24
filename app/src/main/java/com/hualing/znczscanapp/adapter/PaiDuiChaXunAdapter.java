package com.hualing.znczscanapp.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.json.JSONObject;

import java.util.List;

public class PaiDuiChaXunAdapter extends BaseAdapter {

    List<JSONObject> dataList;

    public List<JSONObject> getDataList() {
        return dataList;
    }

    public void setDataList(List<JSONObject> dataList) {
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
