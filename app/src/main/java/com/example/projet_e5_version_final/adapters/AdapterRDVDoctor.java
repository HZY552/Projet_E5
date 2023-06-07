package com.example.projet_e5_version_final.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.projet_e5_version_final.DoctorsRDVActivity;
import com.example.projet_e5_version_final.R;
import com.example.projet_e5_version_final.RDVDetailsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AdapterRDVDoctor extends BaseAdapter {

    private JSONArray jsonarray;

    private Context context;

    private LayoutInflater inflater;

    public AdapterRDVDoctor(Context context, JSONArray jsonarray){
        this.context = context;
        this.jsonarray = jsonarray;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return jsonarray.length();
    }

    @Override
    public Object getItem(int i) {
        return jsonarray.optJSONObject(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null){
            view = inflater.inflate(R.layout.list_rdv_doctor,viewGroup,false);
        }

        JSONObject obj = (JSONObject) getItem(i);

        TextView tv_nom_patient = view.findViewById(R.id.tv_nom_patient);
        TextView tv_date_debut = view.findViewById(R.id.tv_date_debut);
        TextView tv_date_to = view.findViewById(R.id.tv_date_to);
        TextView tv_email_patient = view.findViewById(R.id.tv_email_patient);

        try {
            if (obj.getString("first_name").equals("null")){
                tv_nom_patient.setText("RDV Disponible");
                tv_email_patient.setText("Unknow");
            }else{
                tv_nom_patient.setText(obj.getString("first_name") + " " + obj.getString("last_name"));
                tv_email_patient.setText(obj.getString("email"));
            }

            tv_date_debut.setText(obj.getString("available_from"));
            tv_date_to.setText(obj.getString("available_to"));


        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                try {
                    intent.putExtra("id",obj.getString("id"));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                intent.setClass(context, RDVDetailsActivity.class);
                context.startActivity(intent);
                DoctorsRDVActivity.instance.finish();
            }
        });

        return view;
    }
}
