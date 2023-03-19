package com.example.projet_e5;

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

public class Thread_API extends Thread{

    public JSONObject values;
    private ArrayList arraylist;
    private String url_api,url_server = "https://www.zeyu.site/api/function/func.php";

    private String Token = "S8jlgW4doHq1Lh9mfqSu6Q";

    private String Key_AES = "bUYJ3nTV6VBasdJF";

    public JSONObject obj;

    public JSONArray obj_array;

    public boolean res;

    public void run() {
        Set_Url_Api(this.arraylist);
        try {
            this.res = open_http_connection();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    public Boolean open_http_connection() throws JSONException {
        String result = "",line;
        BufferedReader reader_in;
        int code_reponse;
        HttpURLConnection connection;

        try{
            URL url = new URL(this.url_api);

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

                if (String.valueOf(result.charAt(0)).equals("{")){
                    this.values = new JSONObject(result);
                    this.obj = this.values;
                }else{

                    this.obj_array = new JSONArray(result);

                }



                return true;
            }else {
                return false;
            }

        }catch (Exception e){

            System.out.println("error connection :" + e);
            //e.printStackTrace();
            return false;
        }

    }

    public JSONObject get_Values(){
        return this.values;
    }

    public JSONArray getObj_array(){
        return this.obj_array;
    }

    public void set_array_list(ArrayList list){
        this.arraylist = list;
    }

    public String get_URL_API(){
        return this.url_api;
    }

    public void Set_Url_Api(ArrayList Values){
        String Key_AES = "bUYJ3nTV6VBasdJF";
        this.url_api = url_server + "?" + "func=" + encrypt(Values.get(0).toString(),Key_AES) +
                "&token=" + encrypt(Values.get(1).toString(),Key_AES) +
                "&t=" + encrypt(Values.get(2).toString(),Key_AES) +
                "&values=" + encrypt(Values.get(3).toString(),Key_AES)
        ;
    }

    public static String encrypt(String input, String key) {
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

    public static String decrypt(String input, String key) {
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
