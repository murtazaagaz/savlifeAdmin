package com.hackerkernel.admin.savlife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.hackerkernel.admin.savlife.R;
import com.hackerkernel.admin.savlife.pojo.DealListPojo;

import java.util.List;

/**
 * Created by husain on 6/8/2016.
 */
public class DealsListAdapter extends ArrayAdapter {
    private Context context;
    private List<DealListPojo> list;
    private LayoutInflater inflater;
    public DealsListAdapter(Context context, int resource, List<DealListPojo> objects) {
        super(context, resource, objects);
        this.context = context;
        this.list = objects;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null){
            view = inflater.inflate(R.layout.deal_list_row,parent,false);
        }else {
            view = convertView;
        }

        DealListPojo current = list.get(position);
        TextView id  = (TextView) view.findViewById(R.id.deal_id);
        TextView labname = (TextView) view.findViewById(R.id.deal_labname);
        Button deactivate = (Button) view.findViewById(R.id.deal_deactivate_btn);
        id.setText(current.getId());
        labname.setText(current.getLabName());
        deactivate.setTag(current.getId());
        return view;
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
