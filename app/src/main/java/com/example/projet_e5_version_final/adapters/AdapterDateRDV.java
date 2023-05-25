package com.example.projet_e5_version_final.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.example.projet_e5_version_final.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AdapterDateRDV extends BaseAdapter {

    private JSONArray jsonarray;

    private Context context;

    private LayoutInflater inflater;
    private String doctor_id;
    private String res_rdv;

    public AdapterDateRDV(Context context, JSONArray jsonarray){
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
            view = inflater.inflate(R.layout.list_date,viewGroup,false);
        }
        JSONObject obj = (JSONObject) getItem(i);
        Button button_date = view.findViewById(R.id.button_date);
        try {
            button_date.setText(obj.getString("available_from") + " - " + obj.getString("available_to"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return view;
    }
}
