package com.example.oscar.travelagent2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Popular_Location_List extends AppCompatActivity {
    private App_Handler app_handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular__location__list);
        app_handler = new App_Handler(Popular_Location_List.this);
        String[] locations = {  "東京","首爾","香港","臺北市","上海",
                                "新加坡","曼谷","洛杉磯","紐約","坎培拉",
                                "巴黎", "威尼斯","倫敦","里約熱內盧","北京"};
        ListAdapter myList = new Popular_CustomAdapter(this,locations);
        ListView myListView = (ListView)findViewById(R.id.popular_listView);
        myListView.setAdapter(myList);

        myListView.setOnItemClickListener(
            new AdapterView.OnItemClickListener(){

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(app_handler.HaveInternet()) {
                        String location = String.valueOf(parent.getItemAtPosition(position));
                        Intent Back_To_MainPage = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString("place", location);
                        Back_To_MainPage.putExtras(bundle);
                        Back_To_MainPage.setClass(Popular_Location_List.this, SearchMainPage.class);
                        startActivity(Back_To_MainPage);
                    }else{
                        Toast.makeText(Popular_Location_List.this, "請確認手機網路狀態!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        );
    }

}
