package com.example.projet_e5_version_final.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.projet_e5_version_final.R;
import com.example.projet_e5_version_final.services.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class AdapterHitorique extends BaseAdapter {

    private JSONArray jsonarray;
    private Context context;
    private LayoutInflater inflater;

    public AdapterHitorique(Context context,JSONArray jsonarray){
        this.context = context;
        this.jsonarray = jsonarray;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return jsonarray.length();
    }

    @Override
    public Object getItem(int position) {
        return jsonarray.optJSONObject(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_historique, parent, false);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.tv_id_Lhistorique);
        TextView tv_nom = (TextView) convertView.findViewById(R.id.tv_nom);
        TextView tv_address = (TextView) convertView.findViewById(R.id.tv_address);

        JSONObject jsonObject = (JSONObject) getItem(position);

        String text = jsonObject.optString("first_name")
                + " "
                + jsonObject.optString("last_name")
                + " ( "
                + jsonObject.optString("specialty")
                + " )"; // 根据键值获取字符串
        String date = jsonObject.optString("available_from") + " - " + jsonObject.optString("available_to");
        String adresse = jsonObject.optString("office_address");
        textView.setText(text);
        tv_nom.setText(date);
        tv_address.setText(adresse);


        return convertView;
    }


}
