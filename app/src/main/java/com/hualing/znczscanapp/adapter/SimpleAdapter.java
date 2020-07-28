package com.hualing.znczscanapp.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hualing.znczscanapp.R;
import com.hualing.znczscanapp.activities.BaseActivity;
import com.hualing.znczscanapp.activities.OrderRKActivity;

import java.util.ArrayList;
import java.util.List;

public class SimpleAdapter extends BaseAdapter {

    private BaseActivity context;
    private List<String> list;

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public SimpleAdapter(BaseActivity context){
        this.context=context;
        this.list=new ArrayList<String>();
        this.list.add("");
    }

    @Override
    public int getCount() {
        return list.size();
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
        ViewHolder viewHolder = null;
        if (convertView==null){
            viewHolder = new ViewHolder();

            //加载自定义的布局文件
            convertView=context.getLayoutInflater().inflate(R.layout.list_item,null);
            viewHolder.nameTV=(TextView)convertView.findViewById(R.id.name_tv);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.nameTV.setText(list.get(position));
        return convertView;
    }

    class ViewHolder{
        TextView nameTV;
    }
}
