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

import com.example.projet_e5_version_final.adapters.AdaptaterMessage;
import com.example.projet_e5_version_final.services.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class MessageActivity extends AppCompatActivity {

    Intent intent;
    String id,type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        intent = getIntent();

        id = intent.getStringExtra("id");
        type = intent.getStringExtra("type");

        Spinner sp = findViewById(R.id.sp_recherche);

        String[] items = new String[]{"All", "Ont Lu","Non Lu","Organisé par temps","afficher uniquement la réception","afficher l'envoi uniquement"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);

        Button button_recherche = findViewById(R.id.button_find_message);
        Button button_new_message = findViewById(R.id.button_mail);

        if (type.equals("patient")){
            try {
                load_list_patient();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }else{
            try {
                load_list_doctor();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        button_recherche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedData = sp.getSelectedItem().toString();
                if (type.equals("patient")){
                    switch (selectedData) {
                        case "All":
                            try {
                                load_list_patient();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        case "Ont Lu":
                            try {
                                find_message_by_etat_patient("1");
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        case "Non Lu":
                            try {
                                find_message_by_etat_patient("0");
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        case "Organisé par temps":
                            try {
                                find_message_by_time_patient();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        case "afficher uniquement la réception":
                            try {
                                find_message_by_sender_patient("1");
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        case "afficher l'envoi uniquement":
                            try {
                                find_message_by_sender_patient("0");
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        default:
                            break;
                    }
                }else{
                    switch (selectedData) {
                        case "All":
                            try {
                                load_list_doctor();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        case "Ont Lu":
                            try {
                                find_message_by_etat_doctor("1");
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        case "Non Lu":
                            try {
                                find_message_by_etat_doctor("0");
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        case "Organisé par temps":
                            try {
                                find_message_by_time_doctor();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        case "afficher uniquement la réception":
                            try {
                                find_message_by_sender_doctor("0");
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        case "afficher l'envoi uniquement":
                            try {
                                find_message_by_sender_doctor("1");
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
            }
        });

        button_new_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_message = new Intent();
                intent_message.putExtra("id",id);
                intent_message.putExtra("type",type);
                intent_message.setClass(MessageActivity.this,SendMessageActivity.class);
                startActivity(intent_message);
            }
        });
    }

    protected void load_list_patient() throws InterruptedException, JSONException {
        String values = "{id_patient:" + id + "}";
        ArrayList<String> listValues = new ArrayList<>(Arrays.asList("get_message_patient", "None", "Jiojio000608.", values));
        Api api = new Api(listValues);
        api.start();

        api.join();

        String api_string = api.get_Values();

        JSONArray array_json = new JSONArray(api_string);

        ListView list = findViewById(R.id.list_message);

        BaseAdapter adapter = new AdaptaterMessage(this,array_json,type);

        list.setAdapter(adapter);
    }

    protected void load_list_doctor() throws InterruptedException, JSONException {
        String values = "{id_doctor:" + id + "}";
        ArrayList<String> listValues = new ArrayList<>(Arrays.asList("get_message_doctor", "None", "Jiojio000608.", values));
        Api api = new Api(listValues);
        api.start();

        api.join();

        String api_string = api.get_Values();

        JSONArray array_json = new JSONArray(api_string);

        ListView list = findViewById(R.id.list_message);

        BaseAdapter adapter = new AdaptaterMessage(this,array_json,type);

        list.setAdapter(adapter);
    }

    protected void find_message_by_etat_patient(String etat) throws InterruptedException, JSONException {
        String values = "{id_patient:" + id + ",etat:" + etat + "}";
        ArrayList<String> listValues = new ArrayList<>(Arrays.asList("get_message_patient_by_etat", "None", "Jiojio000608.", values));
        Api api = new Api(listValues);
        api.start();

        api.join();

        String api_string = api.get_Values();

        JSONArray array_json = new JSONArray(api_string);

        ListView list = findViewById(R.id.list_message);

        BaseAdapter adapter = new AdaptaterMessage(this,array_json,type);

        list.setAdapter(adapter);
    }

    protected void find_message_by_etat_doctor(String etat) throws InterruptedException, JSONException {
        String values = "{id_doctor:" + id + ",etat:" + etat + "}";
        ArrayList<String> listValues = new ArrayList<>(Arrays.asList("get_message_doctor_by_etat", "None", "Jiojio000608.", values));
        Api api = new Api(listValues);
        api.start();

        api.join();

        String api_string = api.get_Values();

        JSONArray array_json = new JSONArray(api_string);

        ListView list = findViewById(R.id.list_message);

        BaseAdapter adapter = new AdaptaterMessage(this,array_json,type);

        list.setAdapter(adapter);
    }

    protected void find_message_by_time_patient() throws InterruptedException, JSONException{
        String values = "{id_patient:" + id + "}";
        ArrayList<String> listValues = new ArrayList<>(Arrays.asList("get_message_patient_orderBy_time", "None", "Jiojio000608.", values));
        Api api = new Api(listValues);
        api.start();

        api.join();

        String api_string = api.get_Values();

        JSONArray array_json = new JSONArray(api_string);

        ListView list = findViewById(R.id.list_message);

        BaseAdapter adapter = new AdaptaterMessage(this,array_json,type);

        list.setAdapter(adapter);
    }

    protected void find_message_by_time_doctor() throws InterruptedException, JSONException{
        String values = "{id_doctor:" + id + "}";
        ArrayList<String> listValues = new ArrayList<>(Arrays.asList("get_message_doctor_orderBy_time", "None", "Jiojio000608.", values));
        Api api = new Api(listValues);
        api.start();

        api.join();

        String api_string = api.get_Values();

        JSONArray array_json = new JSONArray(api_string);

        ListView list = findViewById(R.id.list_message);

        BaseAdapter adapter = new AdaptaterMessage(this,array_json,type);

        list.setAdapter(adapter);
    }

    protected void find_message_by_sender_patient(String sender) throws InterruptedException, JSONException {
        String values = "{id_patient:" + id + ",sender:" + sender + "}";
        ArrayList<String> listValues = new ArrayList<>(Arrays.asList("get_message_patient_By_sender", "None", "Jiojio000608.", values));
        Api api = new Api(listValues);
        api.start();

        api.join();

        String api_string = api.get_Values();

        JSONArray array_json = new JSONArray(api_string);

        ListView list = findViewById(R.id.list_message);

        BaseAdapter adapter = new AdaptaterMessage(this,array_json,type);

        list.setAdapter(adapter);
    }

    protected void find_message_by_sender_doctor(String sender) throws InterruptedException, JSONException {
        String values = "{id_doctor:" + id + ",sender:" + sender + "}";
        ArrayList<String> listValues = new ArrayList<>(Arrays.asList("get_message_doctor_By_sender", "None", "Jiojio000608.", values));
        Api api = new Api(listValues);
        api.start();

        api.join();

        String api_string = api.get_Values();

        JSONArray array_json = new JSONArray(api_string);

        ListView list = findViewById(R.id.list_message);

        BaseAdapter adapter = new AdaptaterMessage(this,array_json,type);

        list.setAdapter(adapter);
    }
}