package com.example.oscar.travelagent2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class User_Information extends AppCompatActivity {

    private TextView user_name,user_email,user_password,user_gender,user_country;
    private String name="";
    private String email="";
    private String password="";
    private String gender="";
    private String country="";
    private App_Handler app_handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__information);
        app_handler = new App_Handler(User_Information.this);
        user_name = (TextView)findViewById(R.id.user_name);
        user_email = (TextView)findViewById(R.id.user_email);
        user_password = (TextView)findViewById(R.id.user_password);
        user_gender = (TextView)findViewById(R.id.user_gender);
        user_country = (TextView)findViewById(R.id.user_country);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        GetInformation();
        ShowInformation();
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.userinfo_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.UPDATE_INFO:
                if(app_handler.HaveInternet()) {
                    Intent go_update = new Intent();
                    go_update.setClass(User_Information.this,User_Update_Page.class);
                    startActivity(go_update);
                }else{
                    Toast.makeText(User_Information.this, "請確認手機網路狀態!", Toast.LENGTH_LONG).show();
                }
                break;
        }



        return super.onOptionsItemSelected(item);
    }


    private void GetInformation(){
        Member member = (Member)getIntent().getSerializableExtra("get_info");
        if(member==null){
            Toast.makeText(User_Information.this, "未知的錯誤!", Toast.LENGTH_LONG).show();
        }else{
            name = member.get_name();
            email = member.get_email();
            password = member.get_password();
            gender = member.get_gender();
            country = member.get_country();
        }
    }
    private void ShowInformation(){
        user_name.setText(name);
        user_email.setText(email);
        user_password.setText(password);
        user_gender.setText(gender);
        user_country.setText(country);
    }
}
