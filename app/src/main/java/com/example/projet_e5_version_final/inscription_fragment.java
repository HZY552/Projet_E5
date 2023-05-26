package com.example.projet_e5_version_final;

import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.projet_e5_version_final.services.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;


public class inscription_fragment extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public inscription_fragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static inscription_fragment newInstance(String param1, String param2) {
        inscription_fragment fragment = new inscription_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_inscription_fragment, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            set_sp(view);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }
    public void set_sp(View view) throws JSONException, InterruptedException {

        String values = "{sp: null}";
        ArrayList<String> listValues = new ArrayList<>(Arrays.asList("get_specialty", "None", "Jiojio000608.", values));

        Api api = new Api(listValues);

        api.start();

        api.join();

        JSONArray res_obj = new JSONArray(api.get_Values());

        String[] specialties = new String[res_obj.length()];

        // Iterate over the JSON array
        for (int i = 0; i < res_obj.length(); i++) {
            // Get each JSON object in the array
            JSONObject jsonObject = res_obj.getJSONObject(i);

            // Extract the 'specialty' value and put it in the string array
            specialties[i] = jsonObject.getString("specialty");
        }

        // 创建 ArrayAdapter 并设置数据源
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, specialties);

        // 设置 Spinner 的适配器
        Spinner spinner = view.findViewById(R.id.sp_inscription);
        spinner.setAdapter(adapter);
    }


}