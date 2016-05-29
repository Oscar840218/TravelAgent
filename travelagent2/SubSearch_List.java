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
import android.widget.Toast;

public class SubSearch_List extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_search__list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final App_Handler app_handler = new App_Handler(SubSearch_List.this);
        ListView myListView = (ListView)findViewById(R.id.subsearch_listView);
        Bundle bundle = getIntent().getExtras();
        String area = bundle.getString("area");
        ListAdapter myList=null;
        if(area!=null){
            switch(area){
                case "亞洲":
                    String[] Asia = {
                            "東京","大阪","首爾","關島","曼谷","雅加達",
                            "吉隆坡","新加坡","馬尼拉","加德滿都","胡志明市",
                            "河內","新德里","伊斯坦堡","莫斯科","海參威","伯力"
                    };
                    myList = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,Asia);
                    break;
                case "亞洲-中國":
                    String[] China = {
                            "廣州","香港","福州","昆明","重慶","武漢","南昌",
                            "杭州","上海","南京","青島","北京","開封","西安",
                            "瀋陽","蘭州","海口"
                    };
                    myList = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,China);
                    break;
                case "歐洲":
                    String[] Europe = {
                            "奧斯陸","馬德里","哥本哈根","赫爾辛基","法蘭克福",
                            "柏林","日內瓦","布魯塞爾","倫敦","巴黎","維也納","羅馬",
                            "威尼斯","布達佩斯","雅典","華沙","布拉格","阿姆斯特丹",
                            "斯德哥爾摩","里斯本"
                    };
                    myList = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,Europe);
                    break;
                case "美洲":
                    String[] America = {
                            "檀香山","洛杉磯","拉斯維加斯","舊金山","西雅圖","紐約",
                            "華盛頓特區","芝加哥","邁阿密","多倫多","溫哥華","蒙特婁",
                            "墨西哥城","里約","聖地牙哥","利瑪","布宜諾斯艾利斯"
                    };
                    myList = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,America);
                    break;
                case "非洲":
                    String[] Africa = {
                            "開羅","約翰尼斯堡"
                    };
                    myList = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,Africa);
                    break;
                case "澳洲":
                    String[] Oceania = {
                            "雪梨","布里斯班","墨爾本","伯斯","奧克蘭","威靈頓"
                    };
                    myList = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,Oceania);
                    break;
                default:
                    break;
            }
            myListView.setAdapter(myList);
            myListView.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if(app_handler.HaveInternet()) {
                                String place = String.valueOf(parent.getItemAtPosition(position));
                                Intent go_info_page = new Intent();
                                Bundle location = new Bundle();
                                location.putString("place",place);
                                go_info_page.putExtras(location);
                                go_info_page.setClass(SubSearch_List.this,SearchMainPage.class);
                                startActivity(go_info_page);
                            }else{
                                Toast.makeText(SubSearch_List.this, "請確認網路狀態!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
            }else{
                Toast.makeText(SubSearch_List.this, "發生不明錯誤", Toast.LENGTH_LONG).show();
        }



    }

}
