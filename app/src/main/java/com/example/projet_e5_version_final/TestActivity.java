package com.example.projet_e5_version_final;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.projet_e5_version_final.services.Api;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        try {
            test();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected void test() throws InterruptedException {
        String values = "{id:12}";
        ArrayList<String> listValues = new ArrayList<>(Arrays.asList("find_user_By_id", "None", "Jiojio000608.", values));
        Api api = new Api(listValues);
        api.start();

        api.join();
        String api_string = api.get_Values();

        System.out.println(api_string);

    }

}