package com.example.projet_e5_version_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.util.DisplayMetrics;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projet_e5_version_final.adapters.AdapterHitorique;
import com.example.projet_e5_version_final.adapters.AdapterShowLogin;
import com.example.projet_e5_version_final.adapters.AdapterShowRDV;
import com.example.projet_e5_version_final.services.Api;
import com.google.android.material.internal.NavigationMenu;
import com.google.android.material.navigation.NavigationView;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private MenuItem menu_moncompte,menu_log_out,menu_message,menu_rdv,menu_setting;
    private String id,type;
    public static MainActivity instance;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity.instance = this;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int halfScreenWidth = screenWidth / 2;

        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ViewGroup.LayoutParams layoutParams = navigationView.getLayoutParams();
        layoutParams.width = halfScreenWidth;
        navigationView.setLayoutParams(layoutParams);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent info_login = getIntent();
        id = info_login.getStringExtra("id");
        type = info_login.getStringExtra("type");

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override

            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                drawerLayout.closeDrawer(GravityCompat.START);

                if(item.getTitle().equals("SE CONNECTER ")){
                    Intent intent_login = new Intent();
                    intent_login.setClass(MainActivity.this,LoginActivity.class);
                    startActivity(intent_login);
                }

                if(item.getTitle().equals("S'INSCRIRE")){
                    Intent intent_login = new Intent();
                    intent_login.setClass(MainActivity.this,InscriptionActivity.class);
                    startActivity(intent_login);
                }

                if(item.getTitle().equals("Mon Compte")){
                    Intent intent_mon_compte = new Intent();
                    intent_mon_compte.putExtra("id",id);
                    intent_mon_compte.putExtra("type",type);
                    intent_mon_compte.setClass(MainActivity.this,MonCompteActivity.class);
                    startActivity(intent_mon_compte);
                }

                if(item.getTitle().equals("RDV")){
                    if (type.equals("patient")){
                        Intent intent_rdv = new Intent();
                        intent_rdv.putExtra("id",id);
                        intent_rdv.putExtra("type",type);
                        intent_rdv.setClass(MainActivity.this,RDVActivity.class);
                        startActivity(intent_rdv);
                    }else{
                        Intent intent_rdv_doctor = new Intent();
                        intent_rdv_doctor.putExtra("id",id);
                        intent_rdv_doctor.putExtra("type",type);
                        intent_rdv_doctor.setClass(MainActivity.this,DoctorsRDVActivity.class);
                        startActivity(intent_rdv_doctor);
                    }

                }

                if(item.getTitle().equals("Messages")){
                    Intent intent_message = new Intent();
                    intent_message.putExtra("id",id);
                    intent_message.putExtra("type",type);
                    intent_message.setClass(MainActivity.this,MessageActivity.class);
                    startActivity(intent_message);
                }

                if (item.getTitle().equals("Log Out")){

                    //menu
                    NavigationView nv = findViewById(R.id.nav_view);
                    nv.getMenu().clear(); // 清除原有菜单
                    nv.inflateMenu(R.menu.nav_menu); // 加载新菜单

                    Intent intent = getIntent();
                    intent.replaceExtras(new Bundle());
                    finish();
                    startActivity(intent);


                    TextView tv_titie1 = findViewById(R.id.main_title1);
                    TextView tv_title2 = findViewById(R.id.main_title2);

                    tv_titie1.setText("Trouvez un rendez-vous avec");
                    tv_title2.setText("une médecin généraliste");
                }
                return true;
            }
        });

        if (id != null && type != null){
            LoginActivity.instance.finish();
            try {
                change_title(navigationView);
                if (type.equals("patient")){
                    show_historique();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }else{
            ArrayList list = new ArrayList<>();
            list.add(0,"");
            ListView list_View_historique_non = findViewById(R.id.list_historique);

            BaseAdapter adapterShowLogin = new AdapterShowLogin(this,list,this);
            list_View_historique_non.setAdapter(adapterShowLogin);

        }

        categorie();

        Button btn_rechercher = findViewById(R.id.button_rechercher);

        btn_rechercher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,RDVActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void show_historique() throws InterruptedException, JSONException {
        String values = "{id_patient:" + id + "}";
        ArrayList<String> listValues = new ArrayList<>(Arrays.asList("find_historique_By_patientId", "None", "Jiojio000608.", values));
        Api api = new Api(listValues);
        api.start();

        api.join();

        JSONArray jsonArray = new JSONArray(api.get_Values());

        if (jsonArray.length() > 0){
            ListView list_View_historique = findViewById(R.id.list_historique);
            if (jsonArray.length() > 2){
                BaseAdapter adapter_historique = new AdapterHitorique(this,jsonArray);
                list_View_historique.setAdapter(adapter_historique);
            }else{
                BaseAdapter adapter_historique = new AdapterHitorique(this,jsonArray);
                list_View_historique.setAdapter(adapter_historique);
            }



            list_View_historique.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    JSONObject obj = null;
                    try {
                        obj = new JSONObject(adapterView.getItemAtPosition(i).toString());
                        Toast.makeText(MainActivity.this, obj.getString("email"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            });
        }else{
            ArrayList list = new ArrayList<>();
            list.add(0,"");
            ListView list_View_historique_non = findViewById(R.id.list_historique);

            BaseAdapter adapterShowRDV = new AdapterShowRDV(this,list,this);
            list_View_historique_non.setAdapter(adapterShowRDV);
        }

    }

    protected void change_title(NavigationView navigationView) throws InterruptedException, JSONException {
        String values = "{id:" + id + "}";
        ArrayList<String> listValues = new ArrayList<>(Arrays.asList("find_user_By_id", "None", "Jiojio000608.", values));
        Api api = new Api(listValues);
        api.start();
        change_menu_user(navigationView);

        api.join();
        JSONObject api_json = new JSONObject(api.get_Values());

        TextView tv_titie1 = findViewById(R.id.main_title1);
        TextView tv_title2 = findViewById(R.id.main_title2);

        tv_titie1.setText("Bonjour !");
        tv_title2.setText(api_json.getString("first_name") + " " + api_json.getString("last_name"));
    }

    protected void change_menu_user(NavigationView navigationview){
        Menu menu = navigationview.getMenu();
        menu_moncompte = menu.add("Mon Compte");
        menu_moncompte.setIcon(ContextCompat.getDrawable(MainActivity.this,R.drawable.baseline_account_circle_24));
        menu_rdv = menu.add("RDV");
        menu_rdv.setIcon(ContextCompat.getDrawable(MainActivity.this,R.drawable.baseline_calendar_month_24));
        menu_message = menu.add("Messages");
        menu_message.setIcon(ContextCompat.getDrawable(MainActivity.this,R.drawable.baseline_comment_24));
        menu_setting = menu.add("PARAMETRES");
        menu_setting.setIcon(ContextCompat.getDrawable(MainActivity.this,R.drawable.baseline_miscellaneous_services_24));
        menu_log_out = menu.add("Log Out");
        menu_log_out.setIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.baseline_lock_24));
        menu.removeItem(R.id.menu_se_connecter);
        menu.removeItem(R.id.menu_inscription);
        menu.removeItem(R.id.menu_paramétres);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:

                DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void categorie(){
        Button btn_dentiste = findViewById(R.id.button_dentiste);
        Button btn_medecin = findViewById(R.id.button_medecin);
        Button btn_psychologie = findViewById(R.id.btn_psychologie);
        btn_dentiste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("key","Dentiste");
                intent.setClass(MainActivity.this,RDVActivity.class);
                startActivity(intent);
            }
        });

        btn_medecin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("key","Medecin générale");
                intent.setClass(MainActivity.this,RDVActivity.class);
                startActivity(intent);
            }
        });

        btn_psychologie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("key","psychologie");
                intent.setClass(MainActivity.this,RDVActivity.class);
                startActivity(intent);
            }
        });
    }

}
