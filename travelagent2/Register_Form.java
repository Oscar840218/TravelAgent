package com.example.oscar.travelagent2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Register_Form extends AppCompatActivity {

    private DBHandler dbHandler;
    private App_Handler app_handler;
    private EditText rg_name,rg_email,rg_password,rg_country;
    private Spinner rg_gender;
    private Button rg_btn;
    private String gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register__form);
        rg_name = (EditText)findViewById(R.id.rg_name);
        rg_email = (EditText)findViewById(R.id.rg_email);
        rg_password = (EditText)findViewById(R.id.rg_password);
        rg_country = (EditText)findViewById(R.id.rg_country);
        rg_gender = (Spinner)findViewById(R.id.rg_spinner);
        rg_gender.setOnItemSelectedListener(Item_select);
        rg_btn = (Button)findViewById(R.id.rg_btn_ok);
        rg_btn.setOnClickListener(btn_click);
        app_handler = new App_Handler(Register_Form.this);
        dbHandler = new DBHandler(this,null,null,1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
    private AdapterView.OnItemSelectedListener Item_select = new AdapterView.OnItemSelectedListener(){

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            gender = parent.getSelectedItem().toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            gender = "男";
        }
    };


    private View.OnClickListener btn_click = new View.OnClickListener(){

        @Override
        public void onClick(View v) {

            String name = String.valueOf(rg_name.getText());
            String email = String.valueOf(rg_email.getText());
            String password = String.valueOf(rg_password.getText());
            String country = String.valueOf(rg_country.getText());

            if(!app_handler.just(name) || !app_handler.just(password) || !app_handler.just(country) || !app_handler.ok_email(email)){
                Toast.makeText(Register_Form.this,"請輸入完整或輸入有誤!", Toast.LENGTH_LONG).show();
            }else{
                Member member = new Member(name);
                member.set_email(email);
                member.set_password(password);
                member.set_country(country);
                member.set_gender(gender);
                dbHandler.addMember(member);
                Toast.makeText(Register_Form.this,"註冊成功!", Toast.LENGTH_LONG).show();
                Intent go_back_main = new Intent();
                go_back_main.setClass(Register_Form.this,MainActivity.class);
                startActivity(go_back_main);
            }
        }
    };




}
