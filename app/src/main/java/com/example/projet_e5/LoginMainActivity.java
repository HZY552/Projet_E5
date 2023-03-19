package com.example.projet_e5;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LoginMainActivity extends AppCompatActivity {
    private String Username,Password;
    private Button button_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        back_Home();
        get_login();
        go_Inscription();
        go_mdp_oublie();

    }

    protected void go_mdp_oublie(){
        Button button_mdp_oublie = findViewById(R.id.button_mdp_oublie);
        button_mdp_oublie.setOnClickListener(v->{
            Intent intent = new Intent();
            intent.setClass(LoginMainActivity.this,Mdpoublie.class);
            TextView tv_email = findViewById(R.id.text_id);
            if (tv_email.getText() != null){
                intent.putExtra("email",tv_email.getText());
            }
            startActivity(intent);
        });

    }

    protected void back_Home(){
        ImageButton button_home = findViewById(R.id.buttonhome);

        button_home.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(LoginMainActivity.this,MainActivity.class);
            startActivity(intent);
        });
    }

    protected void go_Inscription(){
        Button button_inscription = findViewById(R.id.button5);
        button_inscription.setOnClickListener(v ->{
            Intent intent = new Intent();
            intent.setClass(LoginMainActivity.this,InscriptionMainActivity.class);
            startActivity(intent);
        });
    }

    protected void get_login(){
        Button button_login = findViewById(R.id.button_login);
        TextView text_id,text_password;
        text_id = findViewById(R.id.text_id);
        text_password = findViewById(R.id.text_password);


        button_login.setOnClickListener(v -> {
            this.Username = text_id.getText().toString();
            this.Password = text_password.getText().toString();
            if (check_Login()){
                try {
                    JSONObject res = get_UserfromApi();
                    if (res != null){
                        String id = res.getString("id");
                        String type = res.getString("type");
                        Intent intent = new Intent(LoginMainActivity.this,MainActivity.class);
                        intent.putExtra("id",id);
                        intent.putExtra("type",type);
                        startActivity(intent);
                    }else{
                        Show_notification("utilisateur n'existe pas ou Mot de passe incorrect ! ");
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    protected JSONObject get_UserfromApi() throws InterruptedException {
        CheckBox cb_login = findViewById(R.id.checkBox_docteur);

        Thread_API api = new Thread_API();

        if (cb_login.isChecked()){
            String function_name = "login",
                    token = "Jiojio000608.",
                    table_name = "docteurs",
                    values_send = getUsername() + "," + getPassword();

            ArrayList<String> list_values = new ArrayList<String>();
            list_values.add(0,function_name);
            list_values.add(1,token);
            list_values.add(2,table_name);
            list_values.add(3,values_send);
            api.set_array_list(list_values);

        }else{
            String function_name = "login",
                    token = "Jiojio000608.",
                    table_name = "clients",
                    values_send = getUsername() + "," + getPassword();

            ArrayList<String> list_values = new ArrayList<String>();
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

    protected String getUsername(){
        return this.Username;
    }

    protected String getPassword(){
        return this.Password;
    }

    protected boolean check_Login(){
        String User = this.getUsername();
        String Password = this.getPassword();

        if (User.length() > 0 && Password.length() > 0){
            String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(User.toString());
            boolean isMatched = matcher.matches();
            if (isMatched){
                return true;
            }else{
                Show_notification("Erreur de format d'adresse e-mail!");
                return false;
            }
        }else{
            Show_notification("L'adresse mail ou le mot de passe ne peut pas être vide!");
            return false;
        }
    }

    protected String getMD5Str(String str) {
        byte[] digest = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            digest = md5.digest(str.getBytes("utf-8"));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String md5Str = new BigInteger(1, digest).toString(16);
        return md5Str;
    }

    protected void Show_notification(String info){
        Context context = getApplicationContext();
        CharSequence text = info;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context,text,duration);
        toast.show();
    }



}