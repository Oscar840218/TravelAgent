package com.example.oscar.travelagent2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Route_Request extends AppCompatActivity {

    private EditText start,end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route__request);
        start = (EditText)findViewById(R.id.rd_start);
        end = (EditText)findViewById(R.id.rd_destination);
        Button start_btn = (Button)findViewById(R.id.rd_start_btn);
        Button location_btn = (Button)findViewById(R.id.rd_location_btn);
        start_btn.setOnClickListener(btn_onClick);
        location_btn.setOnClickListener(location_btn_onClick);
    }
    private View.OnClickListener location_btn_onClick = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            Bundle bundle = getIntent().getExtras();
            String location = bundle.getString("location");
            if(location!=null){
                start.setText(location);
            }
        }
    };
    private View.OnClickListener btn_onClick = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            String start_place = start.getText().toString();
            String end_place = end.getText().toString();
            if(start_place.equals("") || end_place.equals("")){
                Toast.makeText(Route_Request.this, "請輸入完整!", Toast.LENGTH_LONG).show();
            }else{
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("start",start_place);
                bundle.putString("end",end_place);
                intent.putExtras(bundle);
                intent.setClass(Route_Request.this, SearchMainPage.class);
                startActivity(intent);
            }
        }
    };
}
