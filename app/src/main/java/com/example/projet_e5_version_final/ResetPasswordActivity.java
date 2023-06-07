package com.example.projet_e5_version_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projet_e5_version_final.services.Api;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class ResetPasswordActivity extends AppCompatActivity {

    EditText ed_mdp,ed_mdpConfir,ed_email,ed_phone;
    Button button_check,button_confir;
    String type,user_id;

    public static ResetPasswordActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ResetPasswordActivity.instance = this;

        ed_email = findViewById(R.id.ed_email);
        ed_phone = findViewById(R.id.ed_phone);
        ed_mdp = findViewById(R.id.ed_mdp);
        ed_mdpConfir = findViewById(R.id.ed_mdpconfirm);

        button_check = findViewById(R.id.button_check_emailAndphone);
        button_confir = findViewById(R.id.button_confirmer);


        change_enable(false);

        button_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    check_user_info();
                } catch (InterruptedException | JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    protected void check_user_info() throws InterruptedException, JSONException {
        String email = String.valueOf(ed_email.getText());
        String phone_number = String.valueOf(ed_phone.getText());

        String values = "{email:" + email + ",phone:" + phone_number + "}";
        ArrayList<String> listValues = new ArrayList<>(Arrays.asList("check_user_phone_email", "None", "Jiojio000608.", values));

        Api api = new Api(listValues);
        api.start();

        api.join();
        String api_string = api.get_Values();
        JSONObject res_obj = new JSONObject(api_string);

        user_id = res_obj.getString("id");

        if (user_id.equals("false")){
            Toast.makeText(this, "Email Incorrect Ou Numéro Incorrect", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "email", Toast.LENGTH_SHORT).show();
            type = res_obj.getString("type");

            change_enable(true);

            button_confir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        change_password();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }

    protected void change_password() throws InterruptedException, JSONException {

        String password = String.valueOf(ed_mdp.getText());
        String password_confirm = String.valueOf(ed_mdpConfir.getText());

        if (password.equals(password_confirm)){
            String values = "{id:" + user_id + ",password:" + password + ",type:" + type + "}";
            ArrayList<String> listValues = new ArrayList<>(Arrays.asList("update_password_By_userId", "None", "Jiojio000608.", values));

            Api api = new Api(listValues);
            api.start();

            api.join();
            String api_string = api.get_Values();
            
            JSONObject jsonObject = new JSONObject(api_string);

            if (jsonObject.getBoolean("res")){
                Toast.makeText(this, "Le mot de passe a été changé", Toast.LENGTH_SHORT).show();
                change_enable(false);
                Intent intent = new Intent();
                intent.setClass(ResetPasswordActivity.this,LoginActivity.class);
                startActivity(intent);
                ResetPasswordActivity.instance.finish();
            }else{
                Toast.makeText(this, "Erreur inconnue, veuillez contacter le développeur", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Veuillez entrer le même mot de passe", Toast.LENGTH_SHORT).show();
        }
        
    }

    protected void change_enable(Boolean c){
        ed_mdp.setEnabled(c);
        ed_mdpConfir.setEnabled(c);
        button_confir.setEnabled(c);

        ed_phone.setEnabled(!c);
        ed_email.setEnabled(!c);
        button_check.setEnabled(!c);
    }

}