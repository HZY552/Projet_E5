package com.example.projet_e5_version_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.projet_e5_version_final.adapters.AdaptaterMessage;
import com.example.projet_e5_version_final.services.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

public class MessageDetailsActivity extends AppCompatActivity {
    String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);

        Intent intent = getIntent();

        String id_message = intent.getStringExtra("id_message");
        type = intent.getStringExtra("type");

        try {
            set_message_lu(id_message);
            get_message(id_message);
        } catch (InterruptedException | JSONException e) {
            throw new RuntimeException(e);
        }
    }

    protected void get_message(String id_message) throws InterruptedException, JSONException {
        String values = "{id:" + id_message + "}";
        ArrayList<String> listValues = new ArrayList<>(Arrays.asList("get_message_details", "None", "Jiojio000608.", values));
        Api api = new Api(listValues);
        api.start();

        api.join();

        String api_string = api.get_Values();

        JSONObject obj_json = new JSONObject(api_string);

        TextView tv_title = findViewById(R.id.tv_title);
        TextView tv_date = findViewById(R.id.tv_date);

        tv_title.setText(obj_json.getString("title"));
        tv_date.setText(obj_json.getString("date_public"));

        TextView tv_sender = findViewById(R.id.tv_sender);
        TextView tv_recipient = findViewById(R.id.tv_recipient);

        tv_sender.setText(obj_json.getString("doctor_first_name") + " " + obj_json.getString("doctor_last_name"));
        tv_recipient.setText(obj_json.getString("patient_first_name") + " " + obj_json.getString("patient_last_name"));

        TextView tv_contenu = findViewById(R.id.tv_contenu);
        tv_contenu.setText(obj_json.getString("contenu"));
    }

    protected void set_message_lu(String id_message) throws InterruptedException, JSONException{
        String values = "{id:" + id_message + "}";
        ArrayList<String> listValues = new ArrayList<>(Arrays.asList("set_message_lu", "None", "Jiojio000608.", values));
        Api api = new Api(listValues);
        api.start();

        api.join();

        System.out.println(api.get_Values());
    }


}