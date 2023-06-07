package com.example.projet_e5_version_final.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.projet_e5_version_final.MessageActivity;
import com.example.projet_e5_version_final.MessageDetailsActivity;
import com.example.projet_e5_version_final.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AdaptaterMessage extends BaseAdapter {

    private JSONArray jsonarray;

    private Context context;

    private LayoutInflater inflater;
    private String type;

    public AdaptaterMessage(Context context, JSONArray jsonarray,String type){
        this.type = type;
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
            view = inflater.inflate(R.layout.list_messages,viewGroup,false);
        }
        JSONObject obj = (JSONObject) getItem(i);

        TextView message_name = view.findViewById(R.id.message_name);
        TextView message_title = view.findViewById(R.id.message_title);
        TextView date_public = view.findViewById(R.id.date_public);
        if (this.type.equals("patient")){
            try {
                message_name.setText(obj.getString("first_name") + " " + obj.getString("last_name"));
                message_title.setText(obj.getString("title"));
                date_public.setText(obj.getString("date_public"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }else{
            try {
                message_name.setText(obj.getString("first_name") + " " + obj.getString("last_name"));
                message_title.setText(obj.getString("title"));
                date_public.setText(obj.getString("date_public"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                try {
                    intent.putExtra("id_message",obj.getString("id"));
                    intent.putExtra("type",type);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                intent.setClass(context, MessageDetailsActivity.class);
                context.startActivity(intent);
            }
        });


        return view;
    }
}
