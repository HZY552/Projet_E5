package com.example.projet_e5_version_final.services;

import android.os.Build;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Api extends Thread{

    private String url_api,url_server = "https://www.zeyu.site/api/api.php";

    private String Token = "S8jlgW4doHq1Lh9mfqSu6Q";

    private String Key_AES = "bUYJ3nTV6VBasdJF";
    private ArrayList values;

    private String res_final;

    public Api(ArrayList<String> listValues) {
        this.values = listValues;
    }

    public void run() {
        set_Url_Api(this.values);
        open_http_connection();
    }

    public Boolean open_http_connection(){
        String result = "",line;
        BufferedReader reader_in;
        int code_reponse;
        HttpURLConnection connection;

        try{
            URL url = new URL(this.url_api);
            System.out.println(this.url_api);
            connection = (HttpURLConnection) url.openConnection();
            // Default settings
            connection.setRequestProperty("accept","*/*");
            connection.setRequestProperty("connection","Keep-Alive");
            connection.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setRequestProperty("Expires","0");

            connection.connect();
            code_reponse = connection.getResponseCode();
            if (code_reponse == 200){
                reader_in = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()
                ));

                while ((line = reader_in.readLine()) != null){
                    result += line;
                }
                this.res_final = result;

                return true;
            }else {
                return false;
            }

        }catch (Exception e){

            System.out.println("error connection :" + e);
            return false;
        }
    }

    protected void set_Url_Api(ArrayList Values){
        String Key_AES = "bUYJ3nTV6VBasdJF";
        this.url_api = url_server + "?" + "jx7iCyX9c7wthGiqmlrWrQ=" + encrypt(Values.get(0).toString(),Key_AES) +
                "&exwX6tEWLJzXl6j9S7ZLaA=" + encrypt(Values.get(1).toString(),Key_AES) +
                "&wqLlE//YIwi2fOkmQa/6Eg=" + encrypt(Values.get(2).toString(),Key_AES) +
                "&BPDlAkeq+/jKaJUX2Ez1rg=" + encrypt(Values.get(3).toString(),Key_AES)
        ;
    }

    public String get_Values(){
        return this.res_final;
    }

    protected static String encrypt(String input, String key) {
        byte[] crypted = null;
        try {

            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skey);
            crypted = cipher.doFinal(input.getBytes());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        Base64.Encoder encoder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            encoder = Base64.getEncoder();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new String(encoder.encodeToString(crypted));
        }else{
            return "null";
        }
    }

    protected static String decrypt(String input, String key) {
        byte[] output = null;
        try {
            Base64.Decoder decoder = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                decoder = Base64.getDecoder();
            }
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skey);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                output = cipher.doFinal(decoder.decode(input));
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return new String(output);
    }
}
