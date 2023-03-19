package com.example.projet_e5;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private String id,type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        this.id = intent.getStringExtra("id");
        this.type = intent.getStringExtra("type");
        Button button_login = findViewById(R.id.button_pageLogin);

        if (this.id == null || this.id.equals("null")){
            button_login.setText("Se connecter");
            go_Login();
        }else{

            try {
                JSONObject json_values = get_user_nom();
                button_login.setText(json_values.getString("nom").toString());
                go_user_profil();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            get_patients();
        }


    }


    protected void go_Login(){
        Button button_login = findViewById(R.id.button_pageLogin);
        button_login.setOnClickListener(v ->{
            Intent intent_login = new Intent();
            intent_login.setClass(MainActivity.this,LoginMainActivity.class);
            startActivity(intent_login);
        });
    }

    protected JSONObject get_user_nom() throws InterruptedException {

        Thread_API api = new Thread_API();
        if (this.type.equals("0")){
            String function_name = "get_By_value",
                    token = "Jiojio000608.",
                    table_name = "docteurs",
                    values_send = "id," + this.id;
            ArrayList<String> list_values = new ArrayList<>();
            list_values.add(0,function_name);
            list_values.add(1,token);
            list_values.add(2,table_name);
            list_values.add(3,values_send);
            api.set_array_list(list_values);
        }else{
            String function_name = "get_By_value",
                    token = "Jiojio000608.",
                    table_name = "clients",
                    values_send = "id," + this.id;
            ArrayList<String> list_values = new ArrayList<>();
            list_values.add(0,function_name);
            list_values.add(1,token);
            list_values.add(2,table_name);
            list_values.add(3,values_send);
            api.set_array_list(list_values);
        }


        api.start();
        api.join();

        return api.get_Values();
    }

    protected void go_user_profil(){
        Button button_login = findViewById(R.id.button_pageLogin);
        button_login.setOnClickListener(v->{
            Intent intent_user_profile = new Intent();
            intent_user_profile.setClass(MainActivity.this,UserActivity.class);
            intent_user_profile.putExtra("id",this.id);
            intent_user_profile.putExtra("type",this.type);
            intent_user_profile.putExtra("user_name",button_login.getText().toString());
            startActivity(intent_user_profile);
        });
    }

    protected void get_patients(){
        LinearLayout patients = findViewById(R.id.bSearch2);
        patients.setOnClickListener(v->{
            Intent intent_patients = new Intent();
            intent_patients.putExtra("id",this.id);
            intent_patients.setClass(MainActivity.this,MainPatientsActivity.class);
            startActivity(intent_patients);
        });
    }

}