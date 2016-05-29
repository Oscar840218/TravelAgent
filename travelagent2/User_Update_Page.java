package com.example.oscar.travelagent2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class User_Update_Page extends AppCompatActivity {

    private App_Handler app_handler;
    private DBHandler dbHandler;
    private Spinner update_spinner;
    private TextView user_update_og;
    private TextView user_update_oglabel;
    private EditText update_edit;
    private Button update_btn;
    private String update_choose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__update__page);
        app_handler = new App_Handler(User_Update_Page.this);
        dbHandler = new DBHandler(this,null,null,1);
        update_spinner = (Spinner)findViewById(R.id.update_spinner);
        update_spinner.setOnItemSelectedListener(Item_selected);
        user_update_oglabel = (TextView)findViewById(R.id.user_update_oglabel);
        user_update_og = (TextView)findViewById(R.id.user_update_og);
        update_edit = (EditText)findViewById(R.id.update_edit);
        update_btn = (Button)findViewById(R.id.user_update_btn);
        update_btn.setOnClickListener(btn_onclick);
        ItemVisible(0);
    }
    private void ItemVisible(int n) {
        if(n == 0){
            user_update_oglabel.setVisibility(View.INVISIBLE);
            user_update_og.setVisibility(View.INVISIBLE);
            update_edit.setVisibility(View.INVISIBLE);
        }else if(n==1){
            user_update_oglabel.setVisibility(View.VISIBLE);
            user_update_og.setVisibility(View.VISIBLE);
            update_edit.setVisibility(View.VISIBLE);
        }
    }
    private AdapterView.OnItemSelectedListener Item_selected = new AdapterView.OnItemSelectedListener(){

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String choose = parent.getSelectedItem().toString();
            update_choose = parent.getSelectedItem().toString();
            switch (choose){
                case "信箱":
                    ItemVisible(0);
                    ItemVisible(1);
                    user_update_og.setText(MainActivity.User.get_email());
                    break;
                case "密碼":
                    ItemVisible(0);
                    ItemVisible(1);
                    user_update_og.setText(MainActivity.User.get_password());
                    break;
                case "國籍":
                    ItemVisible(0);
                    ItemVisible(1);
                    user_update_og.setText(MainActivity.User.get_country());
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            ItemVisible(0);
        }
    };
    private View.OnClickListener btn_onclick = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            String text = update_edit.getText().toString();
            switch(update_choose){
                case "信箱":
                    if(!app_handler.ok_email(text) && !text.equals(MainActivity.User.get_email())){
                        Toast.makeText(User_Update_Page.this, "請輸入完整或輸入有誤!", Toast.LENGTH_LONG).show();
                    }else{
                        dbHandler.update_Member("Email",text,MainActivity.User.get_name());
                        Toast.makeText(User_Update_Page.this, "更新成功!", Toast.LENGTH_LONG).show();
                        update_edit.setText("");
                    }
                    break;
                case "密碼":
                    if(!app_handler.just(text) && !text.equals(MainActivity.User.get_password())){
                        Toast.makeText(User_Update_Page.this, "請輸入完整或輸入有誤!", Toast.LENGTH_LONG).show();
                    }else{
                        dbHandler.update_Member("Password",text,MainActivity.User.get_name());
                        Toast.makeText(User_Update_Page.this, "更新成功!", Toast.LENGTH_LONG).show();
                        update_edit.setText("");
                    }
                    break;
                case "國籍":
                    if(!app_handler.just(text) && !text.equals(MainActivity.User.get_country())){
                        Toast.makeText(User_Update_Page.this, "請輸入完整或輸入有誤!", Toast.LENGTH_LONG).show();
                    }else{
                        dbHandler.update_Member("Country",text,MainActivity.User.get_name());
                        Toast.makeText(User_Update_Page.this, "更新成功!", Toast.LENGTH_LONG).show();
                        update_edit.setText("");
                    }
                    break;
            }
        }
    };
}
