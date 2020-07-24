package com.hualing.znczscanapp.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hualing.znczscanapp.R;
import com.hualing.znczscanapp.activities.PaiDuiChaXunActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PaiDuiChaXunAdapter extends BaseAdapter {

    private List<JSONObject> dataList;
    private PaiDuiChaXunActivity context;

    public List<JSONObject> getDataList() {
        return dataList;
    }

    public void setDataList(List<JSONObject> dataList) {
        this.dataList = dataList;
    }

    public PaiDuiChaXunAdapter(PaiDuiChaXunActivity context){
        this.context=context;
        dataList = new ArrayList<JSONObject>();//这里必须实例化对象，不然getCount()方法那边就会报错
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
        try {
            if(convertView==null){
                convertView = context.getLayoutInflater().inflate(R.layout.item_pai_dui_cha_xun,parent,false);
            }
            JSONObject dataJO = dataList.get(position);
            TextView numTV = (TextView)convertView.findViewById(R.id.num_tv);
            TextView pdhTV = (TextView)convertView.findViewById(R.id.pdh_tv);
            TextView prsjTV = (TextView)convertView.findViewById(R.id.prsj_tv);
            TextView fenLeiTV = (TextView)convertView.findViewById(R.id.fenLei_tv);
            TextView stateTV = (TextView)convertView.findViewById(R.id.state_tv);
            numTV.setText(dataJO.getString("num"));
            pdhTV.setText(dataJO.getString("pdh"));
            prsjTV.setText(dataJO.getString("prsj"));
            fenLeiTV.setText(dataJO.getString("fenLei"));
            stateTV.setText(dataJO.getString("state"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        finally {
            return convertView;
        }
    }
}
