package com.example.projet_e5_version_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.projet_e5_version_final.adapters.AdaptaterRDV;
import com.example.projet_e5_version_final.adapters.AdapterRechercher;
import com.example.projet_e5_version_final.services.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class RDVActivity extends AppCompatActivity {
    private String id,type,key;
    private Button button_recherche;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rdvactivity);

        Intent intent = getIntent();
        this.id = intent.getStringExtra("id");
        this.type = intent.getStringExtra("type");
        this.key = intent.getStringExtra("key");
        if (key != null){
            try {
                SearchView searchView = findViewById(R.id.sv_rechercher_rdv);
                searchView.setQuery(key,false);
                find_doctor_By_key_mainActivity(key);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        this.button_recherche = findViewById(R.id.button_recherche);

        button_recherche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    find_doctor_By_key();
                } catch (InterruptedException | JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    protected void find_doctor_By_key() throws InterruptedException, JSONException {
        SearchView searchView = findViewById(R.id.sv_rechercher_rdv);

        String query = searchView.getQuery().toString();

        String sp = query;

        String values = "{key:" +
                sp + "}";

        ArrayList<String> listValues = new ArrayList<>(Arrays.asList("find_doctors_By_key", "None", "Jiojio000608.", values));
        Api api = new Api(listValues);
        api.start();

        api.join();

        String res = api.get_Values();

        JSONArray json_array = new JSONArray(res);

        if (json_array.length() > 0){
            ListView list_res = findViewById(R.id.list_res);
            BaseAdapter adapter_res = new AdaptaterRDV(this,json_array,id);
            list_res.setAdapter(adapter_res);
        }else {
            ListView list_res = findViewById(R.id.list_res);
            JSONArray json = new JSONArray("[{\"id\":\"1\",\"first_name\":\"Aucune\",\"last_name\":\"RDV trouver\",\"specialty\":\"null\",\"phone_number\":\"null\",\"email\":\"null\",\"office_address\":\"null\",\"password\":\"Jiojio000608.\"}]");
            BaseAdapter adapter_res = new AdaptaterRDV(this,json,id);
            list_res.setAdapter(adapter_res);
            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
        }
    }

    protected void find_doctor_By_key_mainActivity(String key) throws InterruptedException, JSONException {

        String query = key;

        String sp = query;

        String values = "{key:" +
                sp + "}";

        ArrayList<String> listValues = new ArrayList<>(Arrays.asList("find_doctors_By_key", "None", "Jiojio000608.", values));
        Api api = new Api(listValues);
        api.start();

        api.join();

        String res = api.get_Values();

        JSONArray json_array = new JSONArray(res);

        if (json_array.length() > 0){
            ListView list_res = findViewById(R.id.list_res);
            BaseAdapter adapter_res = new AdaptaterRDV(this,json_array,id);
            list_res.setAdapter(adapter_res);
        }else {
            ListView list_res = findViewById(R.id.list_res);
            JSONArray json = new JSONArray("[{\"id\":\"1\",\"first_name\":\"Aucune\",\"last_name\":\"RDV trouver\",\"specialty\":\"null\",\"phone_number\":\"null\",\"email\":\"null\",\"office_address\":\"null\",\"password\":\"Jiojio000608.\"}]");
            BaseAdapter adapter_res = new AdaptaterRDV(this,json,id);
            list_res.setAdapter(adapter_res);
            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
        }
    }
}