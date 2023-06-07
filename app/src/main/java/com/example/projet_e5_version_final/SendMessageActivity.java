package com.example.projet_e5_version_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projet_e5_version_final.services.Api;
import com.example.projet_e5_version_final.tools.Regular_validation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class SendMessageActivity extends AppCompatActivity {

    private String id,type;
    private EditText ed_dsmail,ed_obj,ed_contenu;
    public static SendMessageActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        SendMessageActivity.instance = this;

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        type = intent.getStringExtra("type");

        ed_dsmail = findViewById(R.id.ed_dsmail);
        ed_obj = findViewById(R.id.ed_obj);
        ed_contenu = findViewById(R.id.ed_contenu);

        Button button_send = findViewById(R.id.button_send);

        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean check = check();

                if (check){
                    try {
                        Boolean res = send_message();
                        if (res){
                            Toast.makeText(SendMessageActivity.this, "True", Toast.LENGTH_SHORT).show();
                            Intent intent_message = new Intent();
                            intent_message.putExtra("id",id);
                            intent_message.putExtra("type",type);
                            intent_message.setClass(SendMessageActivity.this,MessageActivity.class);
                            startActivity(intent_message);
                            SendMessageActivity.instance.finish();
                        }else{
                            Toast.makeText(SendMessageActivity.this, "False", Toast.LENGTH_SHORT).show();
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }else{
                    Toast.makeText(SendMessageActivity.this, "Error Email ou champs vid", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected boolean check(){
        String text_mail = String.valueOf(ed_dsmail.getText());
        String text_obj = String.valueOf(ed_obj.getText());
        String text_contenu = String.valueOf(ed_contenu.getText());
        Regular_validation re = new Regular_validation();

        if (TextUtils.isEmpty(text_mail) || TextUtils.isEmpty(text_obj) || TextUtils.isEmpty(text_contenu)){
            return false;
        }else{
            if (re.check_email(text_mail)){
                return true;
            }else{
                return false;
            }
        }
    }

    protected Boolean send_message() throws InterruptedException, JSONException {

        String text_mail = String.valueOf(ed_dsmail.getText());
        String text_obj = String.valueOf(ed_obj.getText());
        String text_contenu = String.valueOf(ed_contenu.getText());
        if (type.equals("patient")){
            String values = "{email:" + text_mail +
                    ",obj:" + text_obj +
                    ",contenu:" + text_contenu +
                    ",id_patient:" + id
                    + "}";

            ArrayList<String> listValues = new ArrayList<>(Arrays.asList("send_message_patient", "None", "Jiojio000608.", values));

            Api api = new Api(listValues);

            api.start();

            api.join();

            JSONObject res_obj = new JSONObject(api.get_Values());

            return res_obj.getBoolean("message");
        }else{
            String values = "{email:" + text_mail +
                    ",obj:" + text_obj +
                    ",contenu:" + text_contenu +
                    ",id_doctor:" + id
                    + "}";

            ArrayList<String> listValues = new ArrayList<>(Arrays.asList("send_message_doctor", "None", "Jiojio000608.", values));

            Api api = new Api(listValues);

            api.start();

            api.join();

            JSONObject res_obj = new JSONObject(api.get_Values());

            return res_obj.getBoolean("message");
        }


    }

}