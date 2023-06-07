package com.example.projet_e5_version_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.projet_e5_version_final.services.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddRdvActivity extends AppCompatActivity {
    Intent intent;
    public static AddRdvActivity instance;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rdv);
        AddRdvActivity.instance = this;

        intent = getIntent();
        id = intent.getStringExtra("id");

        load_spinner();

        Button button_add = findViewById(R.id.button_add_rdvDoctor);

        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    add_rdv();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    protected void add_rdv() throws InterruptedException, JSONException {
        Spinner spinner = findViewById(R.id.sp_date);
        Spinner sp_h_d = findViewById(R.id.sp_h_d);
        Spinner sp_h_f = findViewById(R.id.sp_h_f);
        Spinner sp_m_d = findViewById(R.id.sp_m_d);
        Spinner sp_m_f = findViewById(R.id.sp_m_f);
        Spinner sp_s_d = findViewById(R.id.sp_s_d);
        Spinner sp_s_f = findViewById(R.id.sp_s_f);

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date date = sdf.parse(spinner.getSelectedItem().toString());

            sdf = new SimpleDateFormat("yyyy-MM-dd");
            String newDateString = sdf.format(date);

            String available_from = newDateString + " "
                    + sp_h_d.getSelectedItem().toString() + ":"
                    + sp_m_d.getSelectedItem().toString() + ":"
                    + sp_s_d.getSelectedItem().toString();

            String available_to = newDateString + " "
                    + sp_h_f.getSelectedItem().toString() + ":"
                    + sp_m_f.getSelectedItem().toString() + ":"
                    + sp_s_f.getSelectedItem().toString();
            String values = "{doctor_id:" + id + ",available_from:" + available_from + ",available_to:" + available_to + "}";
            ArrayList<String> listValues = new ArrayList<>(Arrays.asList("add_rdv", "None", "Jiojio000608.", values));
            Api api = new Api(listValues);
            api.start();

            api.join();

            String api_string = api.get_Values();
            if (api_string.equals("true")){
                Toast.makeText(this, api_string, Toast.LENGTH_SHORT).show();
                Intent intent_back = new Intent();
                intent_back.putExtra("id",id);
                intent_back.setClass(AddRdvActivity.this,DoctorsRDVActivity.class);
                startActivity(intent_back);
                AddRdvActivity.instance.finish();
            }else{
                Toast.makeText(this, api_string, Toast.LENGTH_SHORT).show();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    protected void load_spinner(){
        Spinner spinner = findViewById(R.id.sp_date);

        ArrayList<String> dates = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy, EEEE", Locale.getDefault());

        Calendar c = Calendar.getInstance();
        for (int i = 0; i < 90; i++) {  // 90 days
            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) {
                dates.add(sdf.format(c.getTime()));
            }
            c.add(Calendar.DAY_OF_MONTH, 1);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dates);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Spinner sp_h_d = findViewById(R.id.sp_h_d);
        Spinner sp_h_f = findViewById(R.id.sp_h_f);
        ArrayList<String> hs = new ArrayList<>();
        for (int h = 1;h < 25; h++){
            hs.add(String.valueOf(h));
        }

        ArrayAdapter<String> adapter_h = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, hs);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_h_d.setAdapter(adapter_h);
        sp_h_f.setAdapter(adapter_h);
        sp_h_d.setSelection(13);
        sp_h_f.setSelection(14);

        Spinner sp_m_d = findViewById(R.id.sp_m_d);
        Spinner sp_m_f = findViewById(R.id.sp_m_f);
        Spinner sp_s_d = findViewById(R.id.sp_s_d);
        Spinner sp_s_f = findViewById(R.id.sp_s_f);

        ArrayList<String> ms = new ArrayList<>();

        for (int m = 0;m < 60; m++){
            if (m<10){
                ms.add("0" + m);
            }else{
                ms.add(String.valueOf(m));
            }
        }

        ArrayAdapter<String> adapter_m = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ms);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_m_d.setAdapter(adapter_m);
        sp_m_f.setAdapter(adapter_m);
        sp_s_d.setAdapter(adapter_m);
        sp_s_f.setAdapter(adapter_m);
        sp_m_d.setSelection(30);
        sp_m_f.setSelection(30);
        sp_s_d.setSelection(30);
        sp_s_f.setSelection(30);
    }
}