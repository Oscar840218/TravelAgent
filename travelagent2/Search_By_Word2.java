package com.example.oscar.travelagent2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Search_By_Word2 extends AppCompatActivity {

    private App_Handler app_handler;
    private android.support.design.widget.TabLayout mTabs;
    private ViewPager mViewPager;
    private View view_search,view_history;
    private EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search__by__word2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle bundle = getIntent().getExtras();
        ArrayList<String> history_places = bundle.getStringArrayList("history");


        /***工具初始化***/
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        view_search = layoutInflater.inflate(R.layout.content_search__by__word,null);
        view_history = layoutInflater.inflate(R.layout.search_history,null);
        editText = (EditText)view_search.findViewById(R.id.editText);
        Button start_btn = (Button) view_search.findViewById(R.id.word_back_btn);
        start_btn.setOnClickListener(btnonClick);
        ListAdapter myList = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,history_places);
        ListView myListView = (ListView)view_history.findViewById(R.id.history_listView);
        myListView.setAdapter(myList);
        /***CLASS處理+TAB處理***/
        app_handler = new App_Handler(Search_By_Word2.this);
        mTabs = (android.support.design.widget.TabLayout) findViewById(R.id.search_tabs);
        mTabs.addTab(mTabs.newTab().setText("搜尋"));
        mTabs.addTab(mTabs.newTab().setText("歷史紀錄"));
        mViewPager = (ViewPager) findViewById(R.id.search_viewPager);
        mViewPager.setAdapter(new SamplePagerAdapter());
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabs));
        myListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (app_handler.HaveInternet()) {
                            String place = String.valueOf(parent.getItemAtPosition(position));
                            Intent go_info_page = new Intent();
                            Bundle location = new Bundle();
                            location.putString("place", place);
                            go_info_page.putExtras(location);
                            go_info_page.setClass(Search_By_Word2.this, SearchMainPage.class);
                            startActivity(go_info_page);
                        } else {
                            Toast.makeText(Search_By_Word2.this, "請確認網路狀態!", Toast.LENGTH_LONG).show();
                        }
                    }
                });



    }
    private View.OnClickListener btnonClick = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            if(app_handler.HaveInternet()) {
                String word = editText.getText().toString();
                if(CheckWordOK(word)) {
                    Intent Back_To_MainPage = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("place", word);
                    Back_To_MainPage.putExtras(bundle);
                    Back_To_MainPage.setClass(Search_By_Word2.this, SearchMainPage.class);
                    startActivity(Back_To_MainPage);
                }else{
                    Toast.makeText(Search_By_Word2.this, "輸入錯誤!", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(Search_By_Word2.this, "請確認手機網路狀態!", Toast.LENGTH_LONG).show();
            }
        }
    };
    private boolean CheckWordOK(String word){
        boolean pass = true;
        if(word.equals("")){
            pass = false;
        }else{
            String[] words = word.split("");
            for(String w:words){
                if(w.equals(" ")){
                    pass = false;
                    break;
                }
            }
        }

        return pass;
    }
    class SamplePagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return 2;
        }
        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = null;
            switch (position){
                case 0:
                    if(view_search.getParent()!=null){
                        container.removeAllViews();
                    }
                    container.addView(view_search);
                    view =  view_search;
                    break;
                case 1:
                    if(view_history.getParent()!=null){
                        container.removeAllViews();
                    }
                    container.addView(view_history);
                    view =  view_history;
                    break;
            }
            return view;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
