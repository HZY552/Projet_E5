package com.example.projet_e5_version_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projet_e5_version_final.services.Api;

import java.util.ArrayList;
import java.util.Arrays;
import com.example.projet_e5_version_final.tools.Regular_validation;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private String email,password;
    private EditText ed_email,ed_password;
    private Button button_login;
    private Regular_validation rv;
    public static LoginActivity instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoginActivity.instance = this;
        login();
        this.rv = new Regular_validation();

        Button button_inscription_l = findViewById(R.id.button_inscription_l);

        button_inscription_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this,InscriptionActivity.class);
                startActivity(intent);
                LoginActivity.instance.finish();
            }
        });
    }

    protected void login(){
        button_login = findViewById(R.id.button_login);

        button_login.setOnClickListener(v->{

            ed_email = findViewById(R.id.edtext_email);
            ed_password = findViewById(R.id.edtext_password);
            email = String.valueOf(ed_email.getText());
            password = String.valueOf(ed_password.getText());

            if (check_edittext_login(email,password)){
                String values = "{email:" + email + ",password:" + password + "}";
                ArrayList<String> listValues = new ArrayList<>(Arrays.asList("login", "None", "Jiojio000608.", values));
                Api api = new Api(listValues);
                api.start();

                try {
                    api.join();
                    String api_string = api.get_Values();
                    System.out.println(api_string);
                    JSONObject api_json = new JSONObject(api_string);

                    if (api_json.getString("login").equals("true")){
                        Toast.makeText(this, "Bonjour! " + api_json.getString("first_name") + " "
                                + api_json.getString("last_name"), Toast.LENGTH_SHORT).show();

                        Intent intent_main = new Intent();
                        intent_main.putExtra("type",api_json.getString("type"));
                        intent_main.putExtra("id",api_json.getString("id"));
                        intent_main.setClass(LoginActivity.this,MainActivity.class);
                        startActivity(intent_main);
                        LoginActivity.instance.finish();
                    }else{
                        Toast.makeText(this, "Password Incorrect", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException | InterruptedException e) {
                    throw new RuntimeException(e);
                }


            }else{
                Toast.makeText(this, "Erreur inconnue, veuillez contacter l'administrateur!", Toast.LENGTH_SHORT).show();
            }

        });
    }

    protected boolean check_edittext_login(String email,String password){
        if (check_value_null(email)){
            if (rv.check_email(email)){
                if (check_value_null(password)){
                    return true;
                }else{
                    Toast.makeText(this, "Password Null!", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }else{
                Toast.makeText(this, "Error Email Format!", Toast.LENGTH_SHORT).show();
                return false;
            }

        }else{
            Toast.makeText(this, "Email Null!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    protected boolean check_value_null(String value){
        if (!value.equals("") && !value.equals(" ")){
            return true;
        }else{
            return false;
        }
    }
}