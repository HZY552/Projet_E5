package com.example.projet_e5_version_final;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projet_e5_version_final.services.Api;
import com.example.projet_e5_version_final.tools.Regular_validation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class InscriptionActivity extends AppCompatActivity {
    public static InscriptionActivity instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        InscriptionActivity.instance = this;
        TextView tv_nom = findViewById(R.id.nom);
        set_Spinner();

        tv_nom.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                int width = tv_nom.getWidth();
                int height = tv_nom.getHeight();


                Spinner spinner = findViewById(R.id.genre);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
                layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
                layoutParams.bottomMargin = dpToPx(20);
                spinner.setLayoutParams(layoutParams);
                spinner.setDropDownWidth(-1);
                tv_nom.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        Button button_inscription = findViewById(R.id.button_inscription);

        button_inscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    inscription();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    protected void inscription() throws JSONException, InterruptedException {
        EditText ed_email = findViewById(R.id.edtext_email);
        EditText ed_password = findViewById(R.id.edtext_password);
        EditText ed_password_confirme = findViewById(R.id.confirmer_mot_de_passe);
        EditText ed_nom = findViewById(R.id.nom);
        EditText ed_prenom = findViewById(R.id.prenom);
        EditText ed_tele = findViewById(R.id.numero_de_tel);
        EditText ed_anniversaire = findViewById(R.id.datte_d_anniversaire);
        Spinner sp_genre = findViewById(R.id.genre);
        EditText ed_address = findViewById(R.id.adresse);
        CheckBox checkBox_doc = findViewById(R.id.checkBox_doc);

        String email = String.valueOf(ed_email.getText());
        String password = String.valueOf(ed_password.getText());
        String password_confirme = String.valueOf(ed_password_confirme.getText());
        String nom = String.valueOf(ed_nom.getText());
        String prenom = String.valueOf(ed_prenom.getText());
        String tele = String.valueOf(ed_tele.getText());
        String anniversaire = String.valueOf(ed_anniversaire.getText());
        String address = String.valueOf(ed_address.getText());
        String genre = sp_genre.getSelectedItem().toString();

        Boolean check_res = check_email();
        Boolean check_format_email = new Regular_validation().check_email(email);
        if (check_format_email){
            if (check_res){
                if (password.equals(password_confirme)){
                    if (checkBox_doc.isChecked()){

                        String values = "{email:" + email +
                                ",password:" + password +
                                ",first_name:" + nom +
                                ",last_name:" + prenom +
                                ",phone_number:" + tele +
                                ",office_address:" + address +
                                ",specialty:" + "null" +
                                ",type:" + "doctor"
                                + "}";

                        ArrayList<String> listValues = new ArrayList<>(Arrays.asList("inscription", "None", "Jiojio000608.", values));

                        Api api = new Api(listValues);

                        api.start();

                        api.join();

                        JSONObject res_obj = new JSONObject(api.get_Values());

                        if (res_obj.getString("res").equals("true")){
                            Intent intent = new Intent();
                            intent.setClass(InscriptionActivity.this,LoginActivity.class);
                            startActivity(intent);
                            InscriptionActivity.instance.finish();
                        }else{
                            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        String values = "{email:" + email +
                                ",password:" + password +
                                ",first_name:" + nom +
                                ",last_name:" + prenom +
                                ",phone_number:" + tele +
                                ",birthdate:" + anniversaire +
                                ",address:" + address +
                                ",genre:" + genre +
                                ",type:" + "patient"
                                + "}";
                        ArrayList<String> listValues = new ArrayList<>(Arrays.asList("inscription", "None", "Jiojio000608.", values));

                        Api api = new Api(listValues);

                        api.start();

                        api.join();

                        JSONObject res_obj = new JSONObject(api.get_Values());

                        if (res_obj.getString("res").equals("true")){
                            Intent intent = new Intent();
                            intent.setClass(InscriptionActivity.this,LoginActivity.class);
                            startActivity(intent);
                            InscriptionActivity.instance.finish();
                        }else{
                            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                        }

                    }
                }else{
                    Toast.makeText(this, "Password pas bon", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "l'utilisateur existe déjà!", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Email format pas bon", Toast.LENGTH_SHORT).show();
        }

    }

    protected boolean check_email() throws InterruptedException, JSONException {
        EditText ed_email = findViewById(R.id.edtext_email);
        String email = String.valueOf(ed_email.getText());

        String values = "{email:" + email  + "}";
        ArrayList<String> listValues = new ArrayList<>(Arrays.asList("check_user_double", "None", "Jiojio000608.", values));
        Api api = new Api(listValues);
        api.start();

        api.join();

        String json_string = api.get_Values();
        JSONObject json_obj = new JSONObject(json_string);

        if (json_obj.getString("user_existe").equals("true")){
            return false;
        }else{
            return true;
        }
    }

    protected void set_Spinner(){
        Spinner spinner = findViewById(R.id.genre);

        String[] items = new String[]{"M", "F"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

}