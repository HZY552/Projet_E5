package com.example.projet_e5_version_final.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projet_e5_version_final.R;
import com.example.projet_e5_version_final.RDVActivity;
import com.example.projet_e5_version_final.services.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class AdaptaterRDV extends BaseAdapter {

    private JSONArray jsonarray;

    private Context context;

    private LayoutInflater inflater;
    private String doctor_id;
    private String res_rdv;



    public AdaptaterRDV(Context context,JSONArray jsonarray){
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
            view = inflater.inflate(R.layout.list_rdv,viewGroup,false);
        }

        TextView tv_name_doctor = (TextView) view.findViewById(R.id.tv_name_rdv);
        TextView tv_specialty = (TextView) view.findViewById(R.id.specialty_rdv);
        TextView tv_email = (TextView) view.findViewById(R.id.rdv_email);
        TextView tv_tele = (TextView) view.findViewById(R.id.rdv_tele);
        TextView tv_address = (TextView) view.findViewById(R.id.rdv_address);
        ImageView img_doctor = (ImageView) view.findViewById(R.id.img_doctor);
        ImageView img_add = (ImageView) view.findViewById(R.id.img_add);


        JSONObject jsonobj = (JSONObject) getItem(i);

        this.doctor_id = jsonobj.optString("id");
        String name = jsonobj.optString("first_name") + " " + jsonobj.optString("last_name");
        String email = jsonobj.optString("email");
        String tele = jsonobj.optString("phone_number");
        String specialty = jsonobj.optString("specialty");
        String address = jsonobj.optString("office_address");


        if (jsonobj.optString("email").equals("null")){
            tv_name_doctor.setText(name);
            tv_specialty.setVisibility(View.GONE);
            img_add.setVisibility(View.GONE);
            img_doctor.setVisibility(View.GONE);

        }else{
            tv_name_doctor.setText(name);
            tv_address.setText("Address : " + address);
            tv_email.setText("Email : " + email);
            tv_specialty.setText("Specialty : " + specialty);
            tv_tele.setText("Téléphone : " + tele);
        }
        ListView list_main = view.findViewById(R.id.list_res);

        String values = "{doctor_email:" + email + "}";
        System.out.println(values);
        ArrayList<String> listValues = new ArrayList<>(Arrays.asList("get_rdv_By_doctor_email", "None", "Jiojio000608.", values));
        Api api = new Api(listValues);

        api.start();

        try {
            api.join();

            JSONArray json_array = new JSONArray(api.get_Values());
            ListView listview_date = view.findViewById(R.id.list_date);
            BaseAdapter adapter_date = new AdapterDateRDV(context,json_array);
            listview_date.setAdapter(adapter_date);

            tv_tele.setVisibility(View.GONE);
            tv_address.setVisibility(View.GONE);
            tv_email.setVisibility(View.GONE);
            listview_date.setVisibility(View.GONE);

            img_add.setOnClickListener(new View.OnClickListener() {
                private int num = 0;

                @Override
                public void onClick(View v_button) {
                    if (num%2 == 0){
                        listview_date.setVisibility(View.VISIBLE);
                        tv_tele.setVisibility(View.VISIBLE);
                        tv_address.setVisibility(View.VISIBLE);
                        tv_email.setVisibility(View.VISIBLE);
                    }else{
                        listview_date.setVisibility(View.GONE);
                        tv_tele.setVisibility(View.GONE);
                        tv_address.setVisibility(View.GONE);
                        tv_email.setVisibility(View.GONE);
                    }

                    num++;
                }
            });
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }





        return view;
    }
}
