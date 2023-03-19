package com.example.projet_e5;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projet_e5.adapter.Patients;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.Map;

public class MainPatientsActivity extends AppCompatActivity {
    private String id,email;

    private Context mcontext;
    public static MainPatientsActivity instance = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_patients);

        Intent intent = getIntent();
        this.id = intent.getStringExtra("id");

        try {
            get_all_patients();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    protected String get_id(){
        return this.id;
    }
    protected String[] get_all_patients() throws InterruptedException, JSONException {
        Thread_API api = new Thread_API();
        String function_name = "get_patients",
                token = "Jiojio000608.",
                table_name = "clients",
                values_sends = "docteur," + this.id;

        ArrayList list_values = new ArrayList<>();
        list_values.add(0,function_name);
        list_values.add(1,token);
        list_values.add(2,table_name);
        list_values.add(3,values_sends);

        api.set_array_list(list_values);

        api.start();

        api.join();
        JSONArray obj = api.getObj_array();

        ArrayList total_value = new ArrayList();
        ArrayList email = new ArrayList<>();
        ArrayList nom = new ArrayList<>();
        ArrayList prenom = new ArrayList<>();
        ArrayList id = new ArrayList<>();


        for(int i = 0;i< obj.length();i++){
            JSONObject obj_string = new JSONObject(obj.get(i).toString());
            email.add(i,obj_string.getString("email"));
            nom.add(i,obj_string.getString("nom"));
            prenom.add(i,obj_string.getString("prenom"));
            id.add(i,obj_string.getString("id"));
        }

        total_value.add(0,email);
        total_value.add(1,nom);
        total_value.add(2,prenom);
        total_value.add(3,id);

        ListView lv = findViewById(R.id.list_v);
        Patients patients_adapter = new Patients(getApplicationContext(),total_value);
        lv.setAdapter(patients_adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String[] selctor = new String[]{"Supprimer","Détails"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainPatientsActivity.this);
                builder.setTitle(patients_adapter.getItem(i).toString());
                builder.setItems(selctor, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int num) {
                        if (selctor[num].equals("Supprimer")){
                            Thread_API api = new Thread_API();
                            String function_name = "change_value",
                                    token = "Jiojio000608.",
                                    table_name = "clients",
                                    values_send = "docteur,9,id," + patients_adapter.getId(i);
                            ArrayList list_values = new ArrayList();
                            list_values.add(0,function_name);
                            list_values.add(1,token);
                            list_values.add(2,table_name);
                            list_values.add(3,values_send);

                            api.set_array_list(list_values);

                            api.start();

                            try {
                                api.join();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }

                            JSONObject res = api.get_Values();
                            try {
                                if (res.getString("res").equals("true")){
                                    MainPatientsActivity.instance.finish();
                                    Intent intent = new Intent();
                                    intent.putExtra("id",get_id());
                                    intent.setClass(MainPatientsActivity.this,MainPatientsActivity.class);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(MainPatientsActivity.this, "Erreur Inconnue", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                        }else{

                        }
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        return null;
    }
}