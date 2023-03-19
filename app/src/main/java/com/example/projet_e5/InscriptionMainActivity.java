package com.example.projet_e5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InscriptionMainActivity extends AppCompatActivity {
    private ImageButton button_add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(this.getClass().getName(),"onCreate");
        setContentView(R.layout.activity_inscription_main);
        button_add = findViewById(R.id.ib_add);
        go_home();
        inscription();
        check_box();
        add_image();
    }

    protected void go_home(){
        ImageButton button_home = findViewById(R.id.button_home);
        button_home.setOnClickListener(v ->{
            Intent intent = new Intent();
            intent.setClass(InscriptionMainActivity.this,MainActivity.class);
            startActivity(intent);
        });
    }

    protected void inscription(){
        Button button_inscription = findViewById(R.id.button_inscription);
        TextView ed_nom = findViewById(R.id.ed_nom);
        TextView ed_prenom = findViewById(R.id.ed_prenom);
        TextView ed_password = findViewById(R.id.ed_password);
        TextView ed_password_confirmer = findViewById(R.id.ed_passwordconfirmer);
        TextView ed_email = findViewById(R.id.ed_email);
        TextView ed_tele = findViewById(R.id.ed_tele);
        TextView ed_date_naissance = findViewById(R.id.ed_naissance);
        TextView ed_address = findViewById(R.id.ed_address);
        button_inscription.setOnClickListener(v->{
            String nom = ed_nom.getText().toString();
            String prenom = ed_prenom.getText().toString();
            String password = ed_password.getText().toString();
            String password_confirmer = ed_password_confirmer.getText().toString();
            String email = ed_email.getText().toString();
            String tele = ed_tele.getText().toString();
            String naissance = ed_date_naissance.getText().toString();
            String address = ed_address.getText().toString();
            if (check_ed_text(nom)){
                if(check_ed_text(prenom)){
                    if(check_ed_text(password) && check_ed_text(password_confirmer)){
                        if(password.equals(password_confirmer)){
                            if(check_ed_text(email)){
                                String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
                                Pattern regex = Pattern.compile(check);
                                Matcher matcher = regex.matcher(email);
                                boolean isMatched = matcher.matches();
                                if(isMatched){
                                    if(check_ed_text(tele)){
                                        Thread_API api = new Thread_API();
                                        ArrayList list_values = new ArrayList();
                                        String function_name = "inscription",
                                                token = "Jiojio000608.",
                                                table_name = "clients",
                                                values_send = nom+ "," +prenom+ "," +password+ "," +email+ "," +tele+ "," +naissance+ "," +address;
                                        list_values.add(0,function_name);
                                        list_values.add(1,token);
                                        list_values.add(2,table_name);
                                        list_values.add(3,values_send);
                                        api.set_array_list(list_values);
                                        api.start();
                                        try {
                                            api.join();
                                        }catch (Exception e){
                                            System.out.println(e);
                                        }

                                        JSONObject res = api.get_Values();
                                        try {
                                            if (res.getString("res").equals("true")){
                                                Show_notification("Utilisateur enregistré : " + email);
                                                Intent intent = new Intent();
                                                intent.setClass(InscriptionMainActivity.this,LoginMainActivity.class);
                                                startActivity(intent);
                                            }else{
                                                Show_notification(res.getString("res"));
                                            }
                                        } catch (JSONException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }else{
                                        Show_notification("Le numéro de téléphone ne peut pas être vide");
                                    }
                                }else{
                                    Show_notification("Erreur de format d'adresse e-mail!");
                                }
                            }else{
                                Show_notification("L'e-mail ne peut pas être vide");
                            }
                        }else{
                            Show_notification("Veuillez confirmer votre mot de passe");
                        }
                    }else{
                        Show_notification("le mot de passe ne peut pas être vide");
                    }
                }else{
                    Show_notification("le prenom ne peut pas être vide !");
                }
            }else{
                Show_notification("le nom ne peut pas être vide !");
            }
         });

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

    protected void check_box(){
        CheckBox cb = findViewById(R.id.cb_medecin);
        cb.setOnClickListener(v->{
            if (cb.isChecked()){
                Button button_inscription = findViewById(R.id.button_inscription);
                TextView ed_nom = findViewById(R.id.ed_nom);
                TextView ed_prenom = findViewById(R.id.ed_prenom);
                TextView ed_password = findViewById(R.id.ed_password);
                TextView ed_password_confirmer = findViewById(R.id.ed_passwordconfirmer);
                TextView ed_email = findViewById(R.id.ed_email);
                TextView ed_tele = findViewById(R.id.ed_tele);
                TextView ed_date_naissance = findViewById(R.id.ed_naissance);
                TextView ed_address = findViewById(R.id.ed_address);
                button_inscription.setOnClickListener(view->{
                    Intent intent = new Intent();
                    intent.putExtra("nom",ed_nom.getText().toString());
                    intent.putExtra("prenom",ed_prenom.getText().toString());
                    intent.putExtra("password",ed_password.getText().toString());
                    intent.putExtra("email",ed_email.getText().toString());
                    intent.putExtra("tele",ed_tele.getText().toString());
                    intent.putExtra("naissance",ed_date_naissance.getText().toString());
                    intent.putExtra("address",ed_address.getText().toString());

                    intent.setClass(InscriptionMainActivity.this,Inscription_pro_MainActivity.class);
                    startActivity(intent);
                });
            }
        });
    }

    protected void add_image(){
        ImageButton button_add = findViewById(R.id.ib_add);
        button_add.setOnClickListener(v->{
            Intent intent = new Intent(Intent.ACTION_PICK,null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
            startActivityForResult(intent,2);
        });
    }

    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == 2){
            Log.e(this.getClass().getName(),"Result:" + data.toString());
            if (data != null){
                Uri uri = data.getData();
                button_add.setImageURI(uri);
                Log.e(this.getClass().getName(),"Uri:"+String.valueOf(uri));
            }
        }
    }


}