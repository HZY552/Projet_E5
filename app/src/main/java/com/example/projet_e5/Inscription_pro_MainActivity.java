package com.example.projet_e5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Inscription_pro_MainActivity extends AppCompatActivity {

    private String nom,prenom,password,email,tele,naissance,address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription_pro_main);
        Button button_inscription = findViewById(R.id.button_inscription_pro);
        button_inscription.setOnClickListener(v->{
            send_info();
        });
    }

    protected void send_info(){
        Boolean isCheck = check_value();

        if (isCheck){
            Intent intent = getIntent();
            this.nom = intent.getStringExtra("nom");
            this.prenom = intent.getStringExtra("prenom");
            this.password = intent.getStringExtra("password");
            this.email = intent.getStringExtra("email");
            this.tele = intent.getStringExtra("tele");
            this.naissance = intent.getStringExtra("naissance");
            this.address = intent.getStringExtra("address");

            EditText ed_specialiste = findViewById(R.id.ed_specialiste);
            EditText ed_horaires = findViewById(R.id.ed_horaires);

            String specialiste = String.valueOf(ed_specialiste.getText());
            String horaires = String.valueOf(ed_horaires.getText());

            Thread_API api = new Thread_API();
            ArrayList list_values = new ArrayList();
            String function_name = "inscription_pro",
                    token = "Jiojio000608.",
                    table_name = "docteurs",
                    values_send = this.nom+ "," + this.prenom+ "," + this.password+ "," + this.email+ "," + this.tele+ "," + this.naissance+ "," + this.address + "," + specialiste + "," +horaires ;
            list_values.add(0,function_name);
            list_values.add(1,token);
            list_values.add(2,table_name);
            list_values.add(3,values_send);
            api.set_array_list(list_values);
            api.start();

            try{
                api.join();
            }catch (Exception e){
                System.out.println(e);
            }
            System.out.println(api.get_URL_API());
            JSONObject res = api.get_Values();

            try{
                if (res.getString("res").equals("true")){
                    Show_notification("Utilisateur enregistré : " + email);
                    Intent intent_login = new Intent();
                    intent_login.setClass(Inscription_pro_MainActivity.this,LoginMainActivity.class);
                    startActivity(intent_login);
                }else{
                    Show_notification(res.getString("res"));
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }else{
            Show_notification("error");
        }
    }

    protected boolean check_value(){
        EditText ed_specialiste = findViewById(R.id.ed_specialiste);
        EditText ed_horaires = findViewById(R.id.ed_horaires);

        String specialiste = String.valueOf(ed_specialiste.getText());
        String horaires = String.valueOf(ed_horaires.getText());

        if (check_ed_text(specialiste) && check_ed_text(horaires)){
            return true;
        }else {
            return false;
        }
    }

    protected Boolean check_ed_text(String str){
        if (str.equals("") || str.equals(" ") || str == null){
            return false;
        }else{
            return true;
        }
    }

    protected void Show_notification(String info){
        Context context = getApplicationContext();
        CharSequence text = info;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context,text,duration);
        toast.show();
    }
}