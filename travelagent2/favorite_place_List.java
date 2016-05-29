package com.example.oscar.travelagent2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

public class favorite_place_List extends AppCompatActivity {

    private String[] locations;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_place_list);
        final App_Handler app_handler = new App_Handler(favorite_place_List.this);
        GetLocations();
        ListAdapter myList = new Favorite_CustomAdapter(this,locations);
        ListView myListView = (ListView)findViewById(R.id.favorite_listView);
        myListView.setAdapter(myList);
        myListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(app_handler.HaveInternet()) {
                            String location = String.valueOf(parent.getItemAtPosition(position));
                            if(!location.equals("尚無我的最愛")){
                                Intent Back_To_MainPage = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putString("place", location);
                                Back_To_MainPage.putExtras(bundle);
                                Back_To_MainPage.setClass(favorite_place_List.this, Information_Page_2.class);
                                startActivity(Back_To_MainPage);
                            }
                        }else{
                            Toast.makeText(favorite_place_List.this, "請確認手機網路狀態!", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }
    private void GetLocations(){
        ArrayList<String> arrayList;
        DBHandler_places dbHandler_places = new DBHandler_places(this, null, null, 1);
        arrayList = dbHandler_places.getLocation(MainActivity.User.get_name());
        if(arrayList==null){
            locations = new String[1];
            locations[0] = "尚無我的最愛";
        }else{
            locations = new String[arrayList.size()];
            for(int i=0;i< arrayList.size();i++){
                locations[i] = arrayList.get(i);
            }
        }

    }

}
