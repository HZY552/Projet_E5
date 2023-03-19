package com.example.projet_e5;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        get_user_info();


        Button button_log_out = findViewById(R.id.button_deconnection);
        button_log_out.setOnClickListener(v-> log_out());
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String type = intent.getStringExtra("type");
        back_Home(id,type);
        if (type.equals("1")){
            delect_user("clients",id);
        }else {
            delect_user("docteurs",id);
        }

    }

    protected void log_out(){
        Intent intent_main = new Intent();
        intent_main.setClass(UserActivity.this,MainActivity.class);
        intent_main.putExtra("id","null");

        startActivity(intent_main);

    }

    @SuppressLint("SetTextI18n")
    protected void get_user_info(){
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String user_name = intent.getStringExtra("user_name");
        TextView t_user_name = findViewById(R.id.tv_nomprenom);
        t_user_name.setText(user_name);

        Thread_API api = new Thread_API();
        Intent intent_get = getIntent();
        String type = intent_get.getStringExtra("type");
        String id_user = intent_get.getStringExtra("id");
        if (type.equals("0")){
            String function_name = "get_By_value",
                    token = "Jiojio000608.",
                    table_name = "docteurs",
                    values_send = "id,"+id_user;
            ArrayList<String> list_values = new ArrayList<>();
            list_values.add(0,function_name);
            list_values.add(1,token);
            list_values.add(2,table_name);
            list_values.add(3,values_send);
            api.set_array_list(list_values);
        }else {
            String function_name = "get_By_value",
                    token = "Jiojio000608.",
                    table_name = "clients",
                    values_send = "id,"+id_user;
            ArrayList<String> list_values = new ArrayList<>();
            list_values.add(0,function_name);
            list_values.add(1,token);
            list_values.add(2,table_name);
            list_values.add(3,values_send);
            api.set_array_list(list_values);
        }

        api.start();
        try{
            api.join();
        }catch (Exception e){
            Show_notification(e.toString());
        }

        JSONObject res = api.get_Values();
        System.out.println(res);
        @SuppressLint("CutPasteId") TextView tv_np = findViewById(R.id.tv_nomprenom);
        @SuppressLint("CutPasteId") TextView tv_email = findViewById(R.id.tv_email);
        @SuppressLint("CutPasteId") TextView tv_naissance = findViewById(R.id.tv_naissance);
        @SuppressLint("CutPasteId") TextView tv_address = findViewById(R.id.tv_address);
        @SuppressLint("CutPasteId") TextView tv_tele = findViewById(R.id.tv_tele);
        try{
            tv_np.setText(res.getString("nom") + " " + res.getString("prenom"));
            tv_email.setText(res.getString("email"));
            //tv_naissance.setText(res.getString("date_naissance"));
            tv_address.setText(res.getString("address"));
            tv_tele.setText(res.getString("tele"));
        }catch (Exception e){
            System.out.println(e);
        }

    }

    protected void Show_notification(String info){
        Context context = getApplicationContext();
        CharSequence text = info;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context,text,duration);
        toast.show();
    }

    protected void back_Home(String id,String type){
        ImageButton button_home = findViewById(R.id.button_homeuserpage);

        button_home.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(UserActivity.this,MainActivity.class);
            intent.putExtra("id",id);
            intent.putExtra("type",type);
            startActivity(intent);
        });
    }

    protected void delect_user(String table_name,String id){
        Button button_del = findViewById(R.id.button_delete);
        button_del.setOnClickListener(v->{
            Thread_API api = new Thread_API();
            String function_name = "delete_user",
                    token = "Jiojio000608.",
                    t = table_name,
                    values_send = id;

            ArrayList list = new ArrayList<>();
            list.add(0,function_name);
            list.add(1,token);
            list.add(2,t);
            list.add(3,values_send);

            api.set_array_list(list);

            api.start();
            try {
                api.join();
            } catch (InterruptedException e) {
                Show_notification("error");
            }

            Intent intent = new Intent();
            intent.putExtra("id","null");
            intent.setClass(UserActivity.this,MainActivity.class);
            startActivity(intent);
        });
    }
}