package com.example.oscar.travelagent2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Search_List extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search__list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String[] areas = {"亞洲","亞洲-中國","歐洲","美洲","非洲","澳洲"};
        ListAdapter myList = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,areas);
        ListView myListView = (ListView)findViewById(R.id.search_listView);
        myListView.setAdapter(myList);
        myListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent next = new Intent();
                        Bundle bundle = new Bundle();
                        String area = String.valueOf(parent.getItemAtPosition(position));
                        switch (area){
                            case "亞洲":
                                bundle.putString("area", area);
                                break;
                            case "亞洲-中國":
                                bundle.putString("area", area);
                                break;
                            case "歐洲":
                                bundle.putString("area", area);
                                break;
                            case "美洲":
                                bundle.putString("area", area);
                                break;
                            case "非洲":
                                bundle.putString("area", area);
                                break;
                            case "澳洲":
                                bundle.putString("area", area);
                                break;
                            default:
                                break;
                        }
                        next.putExtras(bundle);
                        next.setClass(Search_List.this,SubSearch_List.class);
                        startActivity(next);
                    }
                });
        }

    }
