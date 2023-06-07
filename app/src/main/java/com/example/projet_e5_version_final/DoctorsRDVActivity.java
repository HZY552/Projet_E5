package com.example.projet_e5_version_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.projet_e5_version_final.adapters.AdapterRDVDoctor;
import com.example.projet_e5_version_final.services.Api;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;

public class DoctorsRDVActivity extends AppCompatActivity {
    private Intent intent;
    private String id,type;
    public static DoctorsRDVActivity instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_rdvactivity);
        DoctorsRDVActivity.instance = this;
        intent = getIntent();

        id = intent.getStringExtra("id");
        type = intent.getStringExtra("type");

        Spinner sp = findViewById(R.id.sp_rdv_doctor);

        String[] items = new String[]{"All", "RDV précédents","RDV à venir "};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);

        try {
            load_all_rdv();
        } catch (InterruptedException | JSONException e) {
            throw new RuntimeException(e);
        }

        Button recherche = findViewById(R.id.button_recherche_rdv);

        recherche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedData = sp.getSelectedItem().toString();

                switch (selectedData) {
                    case "All":
                        try {
                            load_all_rdv();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case "RDV précédents":
                        try {
                            get_allRdv_passe();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case "RDV à venir ":
                        try {
                            get_allRdv_future();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    default:
                        break;
                }
            }
        });

        Button button_add_rdv = findViewById(R.id.button_add_rdv);

        button_add_rdv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_add = new Intent();
                intent_add.putExtra("id",id);
                intent_add.setClass(DoctorsRDVActivity.this,AddRdvActivity.class);
                startActivity(intent_add);
            }
        });
    }

    protected void load_all_rdv() throws InterruptedException, JSONException {
        String values = "{id:" + id + "}";
        ArrayList<String> listValues = new ArrayList<>(Arrays.asList("get_all_rdvByDoctorID", "None", "Jiojio000608.", values));
        Api api = new Api(listValues);
        api.start();

        api.join();

        String api_string = api.get_Values();

        JSONArray jsonArray = new JSONArray(api_string);

        ListView list_rdv_doctor = findViewById(R.id.list_doctor_rdv);

        BaseAdapter adapter = new AdapterRDVDoctor(this,jsonArray);
        list_rdv_doctor.setAdapter(adapter);
    }

    protected void get_allRdv_passe() throws InterruptedException, JSONException{
        String values = "{id:" + id + "}";
        ArrayList<String> listValues = new ArrayList<>(Arrays.asList("get_all_rdvByDoctorID_passe", "None", "Jiojio000608.", values));
        Api api = new Api(listValues);
        api.start();

        api.join();

        String api_string = api.get_Values();

        JSONArray jsonArray = new JSONArray(api_string);

        ListView list_rdv_doctor = findViewById(R.id.list_doctor_rdv);

        BaseAdapter adapter = new AdapterRDVDoctor(this,jsonArray);
        list_rdv_doctor.setAdapter(adapter);
    }

    protected void get_allRdv_future() throws InterruptedException, JSONException{
        String values = "{id:" + id + "}";
        ArrayList<String> listValues = new ArrayList<>(Arrays.asList("get_all_rdvByDoctorID_future", "None", "Jiojio000608.", values));
        Api api = new Api(listValues);
        api.start();

        api.join();

        String api_string = api.get_Values();

        JSONArray jsonArray = new JSONArray(api_string);

        ListView list_rdv_doctor = findViewById(R.id.list_doctor_rdv);

        BaseAdapter adapter = new AdapterRDVDoctor(this,jsonArray);
        list_rdv_doctor.setAdapter(adapter);
    }
}