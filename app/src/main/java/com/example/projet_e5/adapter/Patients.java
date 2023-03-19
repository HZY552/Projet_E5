package com.example.projet_e5.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.projet_e5.R;

import java.util.ArrayList;

public class Patients extends BaseAdapter {

    private ArrayList data;
    private Context mContext;

    public Patients(Context mContext,ArrayList data){
        super();
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public int getCount() {
        ArrayList nom = (ArrayList) data.get(0);
        return nom.size();
    }

    @Override
    public Object getItem(int i) {
        ArrayList nom = (ArrayList) data.get(0);
        return nom.get(i);
    }

    public Object getId(int i){
        ArrayList id = (ArrayList) data.get(3);
        return id.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null){
            LayoutInflater inflater = LayoutInflater.from(this.mContext);
            view = inflater.inflate(R.layout.list_patients,null);
        }

        TextView tv_e = view.findViewById(R.id.nom_patient);
        TextView tv_p = view.findViewById(R.id.prenom_patient);
        TextView tv_n = view.findViewById(R.id.email_patient);

        ArrayList list = data;
        ArrayList nom = (ArrayList) list.get(1);
        ArrayList prenom = (ArrayList) list.get(2);
        ArrayList email = (ArrayList) list.get(0);
        tv_e.setText(nom.get(i).toString());
        tv_p.setText(prenom.get(i).toString());
        tv_n.setText(email.get(i).toString());


        return view;
    }
}
