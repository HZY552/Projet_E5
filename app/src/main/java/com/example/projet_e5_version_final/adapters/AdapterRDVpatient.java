package com.example.projet_e5_version_final.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projet_e5_version_final.DoctorsRDVActivity;
import com.example.projet_e5_version_final.MainActivity;
import com.example.projet_e5_version_final.R;
import com.example.projet_e5_version_final.RDVDetailsActivity;
import com.example.projet_e5_version_final.RdvDetaileActivity;
import com.example.projet_e5_version_final.services.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class AdapterRDVpatient extends BaseAdapter {
    private JSONArray jsonarray;

    private Context context;

    private LayoutInflater inflater;
    private String id_patient,doctor_email;
    private TextView tv_date_from,tv_date_fin;


    public AdapterRDVpatient(Context context, JSONArray jsonarray,String id_patient,String doctor_email){
        this.doctor_email = doctor_email;
        this.id_patient = id_patient;
        this.context = context;
        this.jsonarray = jsonarray;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return jsonarray.length();
    }

    @Override
    public Object getItem(int i) {
        return jsonarray.optJSONObject(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null){
            view = inflater.inflate(R.layout.list_rdv_disponible,viewGroup,false);
        }

        tv_date_from = view.findViewById(R.id.tv_date_from);
        tv_date_fin = view.findViewById(R.id.tv_date_fin);

        JSONObject obj = (JSONObject) getItem(i);
        System.out.println(obj);
        tv_date_from.setText(obj.optString("available_from"));
        tv_date_fin.setText(obj.optString("available_to"));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(context)
                        .setTitle("Confirmation")
                        .setMessage("Are you sure you want to proceed?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String values = "{id_patient:" +
                                        id_patient + ",id_rdv:" + obj.optString("id") + "}";
                                System.out.println(values);
                                ArrayList<String> listValues = new ArrayList<>(Arrays.asList("update_rdv", "None", "Jiojio000608.", values));
                                Api api = new Api(listValues);
                                api.start();

                                try {
                                    api.join();
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }

                                String res = api.get_Values();

                                if (res.equals("true")){
                                    String contenu = tv_date_from.getText() + " / " + tv_date_fin.getText();

                                    String v = "{email:" + doctor_email +
                                            ",obj:" + "Rendez-vous confirmé" +
                                            ",contenu:" + contenu +
                                            ",id_patient:" + id_patient
                                            + "}";
                                    System.out.println(v);
                                    ArrayList<String> lv = new ArrayList<>(Arrays.asList("send_message_patient", "None", "Jiojio000608.", v));

                                    Api send_api = new Api(lv);

                                    send_api.start();

                                    try {
                                        send_api.join();
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }

                                    Toast.makeText(context, res, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent();
                                    intent.putExtra("id",id_patient);
                                    intent.putExtra("type","patient");
                                    intent.setClass(context, MainActivity.class);
                                    context.startActivity(intent);
                                }else {
                                    Toast.makeText(context, res, Toast.LENGTH_SHORT).show();
                                }

                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }

        });


        return view;
    }
}
