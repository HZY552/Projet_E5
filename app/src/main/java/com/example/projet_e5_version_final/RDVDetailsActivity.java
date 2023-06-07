package com.example.projet_e5_version_final;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projet_e5_version_final.adapters.AdapterRDVDoctor;
import com.example.projet_e5_version_final.services.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

public class RDVDetailsActivity extends AppCompatActivity {
    Intent intent;
    String id,doctor_id;
    public static RDVDetailsActivity instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rdvdetails);
        RDVDetailsActivity.instance = this;
        intent = getIntent();

        id = intent.getStringExtra("id");


        try {
            load_details_rdv();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        Button button_delete = findViewById(R.id.button_delete);

        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(RDVDetailsActivity.this)
                        .setTitle("Confirmation")
                        .setMessage("Are you sure you want to proceed?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String values = "{id:" + id + "}";
                                ArrayList<String> listValues = new ArrayList<>(Arrays.asList("delete_rdv_Byid", "None", "Jiojio000608.", values));
                                Api api = new Api(listValues);
                                api.start();

                                try {
                                    api.join();
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }

                                String api_string = api.get_Values();

                                Toast.makeText(RDVDetailsActivity.this, api_string, Toast.LENGTH_SHORT).show();

                                RDVDetailsActivity.instance.finish();

                                Intent intent_rdv = new Intent();
                                intent_rdv.putExtra("id",doctor_id);
                                intent_rdv.setClass(RDVDetailsActivity.this,DoctorsRDVActivity.class);
                                startActivity(intent_rdv);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        Button button_contact_rdv = findViewById(R.id.button_contact);

        button_contact_rdv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_contact = new Intent();
                intent_contact.putExtra("id",doctor_id);
                intent_contact.putExtra("type","doctor");
                intent_contact.setClass(RDVDetailsActivity.this,SendMessageActivity.class);
                startActivity(intent_contact);
            }
        });
    }

    protected void load_details_rdv() throws InterruptedException, JSONException {
        String values = "{id:" + id + "}";
        ArrayList<String> listValues = new ArrayList<>(Arrays.asList("get_rdv_Byid", "None", "Jiojio000608.", values));
        Api api = new Api(listValues);
        api.start();

        api.join();

        String api_string = api.get_Values();

        JSONObject obj = new JSONObject(api_string);
        TextView tv_nom = findViewById(R.id.tv_rdv_name);
        TextView tv_mail = findViewById(R.id.tv_email_patientrdv);
        TextView tv_tele_rdv = findViewById(R.id.tv_tele_rdv);
        TextView tv_date_db = findViewById(R.id.tv_date_db);
        TextView tv_date_f = findViewById(R.id.tv_date_f);
        TextView tv_genre_rdv = findViewById(R.id.tv_genre_rdv);
        TextView tv_age_rdv = findViewById(R.id.tv_age_rdv);
        String patient_id = obj.getString("patient_id");

        Button button_contact_rdv = findViewById(R.id.button_contact);

        doctor_id = obj.getString("doctor_id");
        if (!patient_id.equals("") && !patient_id.equals("null")){
            String date = obj.getString("birthdate");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            LocalDate birthDate = LocalDate.parse(date, formatter);

            LocalDate currentDate = LocalDate.now();
            int age = Period.between(birthDate, currentDate).getYears();

            tv_nom.setText(obj.getString("first_name") + " " + obj.getString("last_name"));
            tv_mail.setText(obj.getString("email"));
            tv_tele_rdv.setText(obj.getString("phone_number"));
            tv_date_db.setText(obj.getString("available_from"));
            tv_date_f.setText(obj.getString("available_to"));
            tv_genre_rdv.setText(obj.getString("gender"));
            tv_age_rdv.setText(Integer.toString(age));
            button_contact_rdv.setEnabled(true);
        }else{
            tv_nom.setText("RDV Disponible");
            tv_mail.setText("RDV Disponible");
            tv_tele_rdv.setText("RDV Disponible");
            tv_date_db.setText(obj.getString("available_from"));
            tv_date_f.setText(obj.getString("available_to"));
            tv_genre_rdv.setText("RDV Disponible");
            tv_age_rdv.setText("RDV Disponible");
            button_contact_rdv.setEnabled(false);
        }
    }
}