package com.example.projet_e5_version_final.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.projet_e5_version_final.LoginActivity;
import com.example.projet_e5_version_final.R;

import java.util.ArrayList;

public class AdapterShowRDV extends BaseAdapter {

    private Activity activity;
    private ArrayList arraylist;
    private Context context;
    private LayoutInflater inflater;

    public AdapterShowRDV(Context context,ArrayList arraylist,Activity activity){
        this.activity = activity;
        this.context = context;
        this.arraylist = arraylist;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arraylist.size();
    }

    @Override
    public Object getItem(int i) {
        return arraylist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_historique_none, parent, false);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.tv_no_rdv);
        Button button = (Button)  convertView.findViewById(R.id.button_no_rdv);

        textView.setText("Vous n'avez pas de RDV");
        button.setText("Prender un RDV");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(activity, LoginActivity.class);
                activity.startActivity(intent);
            }
        });
        return convertView;
    }
}
