package com.example.oscar.travelagent2;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class Information_Page_2 extends AppCompatActivity {

    private android.support.design.widget.TabLayout mTabs;
    private ViewPager mViewPager;
    private String userLocation;
    private String URL_TRAVEL,URL_HOTEL,URL_WIKI;
    private String title,detail,place;
    private DBHandler_places dbHandler_places;
    private App_Handler app_handler;
    private EditText date_info_title,date_info_note,date_info_place;
    private TextView date_info_date,date_info_time_start,date_info_time_end;
    private View view_weather,view_travel,view_video,view_date;
    private Calendar c;
    private int cyear,cmonth,cday,start_hour=0,start_min=0,end_hour=0,end_min=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information__page_2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("當地資訊一覽");
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Bundle bundle = getIntent().getExtras();
        userLocation = bundle.getString("place");
        c = Calendar.getInstance();
        cyear = c.get(Calendar.YEAR);
        cmonth = c.get(Calendar.MONTH) + 1;
        cday = c.get(Calendar.DAY_OF_MONTH);
        /***工具初始化***/
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        view_weather = layoutInflater.inflate(R.layout.weather_info,null);
        view_travel = layoutInflater.inflate(R.layout.travel_info,null);
        view_video = layoutInflater.inflate(R.layout.video_info,null);
        view_date = layoutInflater.inflate(R.layout.date_info,null);
        //view_weather
        TextView location_area = (TextView) view_weather.findViewById(R.id.location_area);
        TextView location_weather = (TextView) view_weather.findViewById(R.id.location_weather);
        TextView location_introduce = (TextView) view_weather.findViewById(R.id.local_introduce);
        TextView location_temp = (TextView) view_weather.findViewById(R.id.location_temp);
        //view_travel
        TextView location_attraction = (TextView)view_travel.findViewById(R.id.local_attraction);
        TextView location_hotel = (TextView)view_travel.findViewById(R.id.local_hotel);
        TextView location_restaruant = (TextView)view_travel.findViewById(R.id.local_restaruant);
        ImageView location_image = (ImageView)view_travel.findViewById(R.id.local_image);
        Button info_btn_travel = (Button)view_travel.findViewById(R.id.local_moreinfo_btn_1);
        Button info_btn_wiki = (Button)view_travel.findViewById(R.id.local_moreinfo_btn_2);
        Button info_btn_hotel = (Button)view_travel.findViewById(R.id.local_moreinfo_btn_3);
        info_btn_travel.setOnClickListener(travel_onclick);
        info_btn_wiki.setOnClickListener(wiki_onclick);
        info_btn_hotel.setOnClickListener(hotel_onclick);
        //view_video
        TextView video_info = (TextView)view_video.findViewById(R.id.video_info);
        //view_date
        Button date_info_btn = (Button)view_date.findViewById(R.id.date_info_btn);
        date_info_btn.setOnClickListener(date_info_onclick);
        Button time_info_btn1 = (Button)view_date.findViewById(R.id.time_info_btn1);
        time_info_btn1.setOnClickListener(time_info_start_onclick);
        Button time_info_btn2 = (Button)view_date.findViewById(R.id.time_info_btn2);
        time_info_btn2.setOnClickListener(time_info_end_onclick);
        Button date_start_btn = (Button)view_date.findViewById(R.id.date_info_startbtn);
        date_start_btn.setOnClickListener(date_start_onclick);
        date_info_date = (TextView)view_date.findViewById(R.id.date_info_date);
        date_info_date.setText(cyear+ "/" + cmonth + "/" +cday);
        date_info_time_start = (TextView)view_date.findViewById(R.id.date_info_time_start);
        date_info_time_end = (TextView)view_date.findViewById(R.id.date_info_time_end);
        date_info_title = (EditText)view_date.findViewById(R.id.date_info_title);
        date_info_title.setText("去"+userLocation+"旅行~!");
        date_info_note = (EditText)view_date.findViewById(R.id.date_info_note);
        date_info_place = (EditText)view_date.findViewById(R.id.date_info_place);
        /***CLASS處理+TAB處理***/
        app_handler = new App_Handler(Information_Page_2.this);
        dbHandler_places = new DBHandler_places(this,null,null,1);
        URL_TRAVEL = "https://www.tripadvisor.com.tw";
        URL_WIKI = "https://zh.wikipedia.org/wiki/"+userLocation;
        URL_HOTEL = "http://www.trivago.com.tw";
        mTabs = (android.support.design.widget.TabLayout) findViewById(R.id.info_tabs);
        mTabs.addTab(mTabs.newTab().setText("氣象資訊 & 當地簡介"));
        mTabs.addTab(mTabs.newTab().setText("旅遊資訊"));
        mTabs.addTab(mTabs.newTab().setText("相關影片"));
        mTabs.addTab(mTabs.newTab().setText("行程規劃"));
        mViewPager = (ViewPager) findViewById(R.id.info_viewPager);
        mViewPager.setAdapter(new SamplePagerAdapter());
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabs));
        /***開始抓取資料+PROGRESS BAR***/
        XML_Climber xmlClimber = new XML_Climber(Information_Page_2.this,userLocation, location_area, location_weather, location_temp);
        Web_Climber webClimber = new Web_Climber(Information_Page_2.this,userLocation,location_introduce,location_attraction,location_hotel,location_restaruant,location_image);
        Video_Climber video_climber = new Video_Climber(userLocation,video_info);
        xmlClimber.execute();
        webClimber.execute();
        video_climber.execute();
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.info_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.ADD_FAVORITE:
                if(app_handler.HaveInternet()) {
                    if (app_handler.isMember()) {
                        boolean successed;
                        Member favorite_location = new Member(MainActivity.User.get_name());
                        favorite_location.set_place(userLocation);
                        successed = dbHandler_places.addPlace(favorite_location);
                        if (!successed) {
                            Toast.makeText(Information_Page_2.this, "已有存在於最愛中的地點!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(Information_Page_2.this, "成功加入我的最愛!", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(Information_Page_2.this, "請先登入會員!", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(Information_Page_2.this, "請確認手機網路狀態!", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.DELETE_FAVORITE:
                if(app_handler.HaveInternet()) {
                    if (app_handler.isMember()) {
                        boolean successed;
                        successed = dbHandler_places.deletePlace(MainActivity.User.get_name(), userLocation);
                        if (successed) {
                            Toast.makeText(Information_Page_2.this, "成功刪除!", Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(Information_Page_2.this, "尚未加入我的最愛!", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(Information_Page_2.this, "請先登入會員!", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(Information_Page_2.this, "請確認手機網路狀態!", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.GENERATE_QRCODE:
                if(app_handler.HaveInternet()) {
                    ShowDialog();
                }else{
                    Toast.makeText(Information_Page_2.this, "請確認手機網路狀態!", Toast.LENGTH_LONG).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    /***QR_CODE顯示選擇視窗***/
    public void ShowDialog(){
        final CharSequence[] items = {"分享地點", "分享網站資訊"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("選擇形式");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("分享地點")) {
                    Intent go_QR_Generator_place = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("data", userLocation);
                    go_QR_Generator_place.putExtras(bundle);
                    go_QR_Generator_place.setClass(Information_Page_2.this, QRCode_Generator.class);
                    startActivity(go_QR_Generator_place);
                } else if (items[item].equals("分享網站資訊")) {
                    Intent go_QR_Generator_web = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("data", URL_TRAVEL);
                    go_QR_Generator_web.putExtras(bundle);
                    go_QR_Generator_web.setClass(Information_Page_2.this, QRCode_Generator.class);
                    startActivity(go_QR_Generator_web);
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    /***BUTTON CLICK處理***/
    private View.OnClickListener travel_onclick = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Uri uri = Uri.parse(URL_TRAVEL); // missing 'http://' will cause crashed
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    };
    private View.OnClickListener wiki_onclick = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Uri uri = Uri.parse(URL_WIKI); // missing 'http://' will cause crashed
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    };
    private View.OnClickListener hotel_onclick = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Uri uri = Uri.parse(URL_HOTEL); // missing 'http://' will cause crashed
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    };
    private View.OnClickListener date_info_onclick = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            new DatePickerDialog(Information_Page_2.this,

                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                            date_info_date.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                            cyear = year;
                            cmonth = monthOfYear + 1;
                            cday = dayOfMonth;
                        }
                    },c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        }
    };

    private View.OnClickListener time_info_start_onclick = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            new TimePickerDialog(Information_Page_2.this,
                    // 绑定监听器
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view,
                                              int hourOfDay, int minute) {

                            date_info_time_start.setText(hourOfDay + ":" + minute);
                            start_hour = hourOfDay;
                            start_min = minute;
                        }
                    }
                    , c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
                    // true表示采用24小时制
                    true).show();
        }

    };
    private View.OnClickListener time_info_end_onclick = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            new TimePickerDialog(Information_Page_2.this,
                    // 绑定监听器
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view,
                                              int hourOfDay, int minute) {

                            date_info_time_end.setText(hourOfDay+":"+minute);
                            end_hour = hourOfDay;
                            end_min = minute;
                        }
                    }
                    , c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
                    // true表示采用24小时制
                    true).show();
        }

    };
    private View.OnClickListener date_start_onclick = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            title = date_info_title.getText().toString();
            detail = date_info_note.getText().toString();
            place = date_info_place.getText().toString();
            if(start_hour==0 || start_min==0 || end_hour==0 || end_min==0){
                Toast.makeText(Information_Page_2.this, "請選擇日期與時間!", Toast.LENGTH_LONG).show();
            }else if(start_hour>end_hour){
                Toast.makeText(Information_Page_2.this, "起始時間早於結束時間!", Toast.LENGTH_LONG).show();
            }else if(start_hour==end_hour && start_min>end_min){
                Toast.makeText(Information_Page_2.this, "起始時間早於結束時間!", Toast.LENGTH_LONG).show();
            }else if(title.equals("")){
                Toast.makeText(Information_Page_2.this, "請填寫標題!", Toast.LENGTH_LONG).show();
            }else if(place.equals("")){
                Toast.makeText(Information_Page_2.this, "請填寫地點!", Toast.LENGTH_LONG).show();
            }else{
                InputCalendar(cyear,cmonth,cday,start_hour,start_min,end_hour,end_min,title,detail,place);
            }
        }
    };

    private void InputCalendar(int year,int month,int day,int s_hour,int s_min,int e_hour,int e_min,String title,String detail,String place){

        Calendar beginTime = Calendar.getInstance();
        beginTime.set(year, month, day, s_hour, s_min);

        Calendar endTime = Calendar.getInstance();
        endTime.set(year, month, day, s_hour, s_min);

        CalendarHelper calIntent = new CalendarHelper();

        calIntent.setTitle(title);
        calIntent.setDescription(detail);
        calIntent.setBeginTimeInMillis(beginTime.getTimeInMillis());
        calIntent.setEndTimeInMillis(endTime.getTimeInMillis());
        calIntent.setLocation(place);

        //全部設定好後就能夠取得 Intent
        Intent intent = calIntent.getIntentAfterSetting();

        //送出意圖
        startActivity(intent);
    }
    /***TAB切換處理***/
    class SamplePagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return 4;
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
                    if(view_weather.getParent()!=null){
                        container.removeAllViews();
                    }
                    container.addView(view_weather);
                    view =  view_weather;
                    break;
                case 1:
                    if(view_travel.getParent()!=null){
                        container.removeAllViews();
                    }
                    container.addView(view_travel);
                    view =  view_travel;
                    break;
                case 2:
                    if(view_video.getParent()!=null){
                        container.removeAllViews();
                    }
                    container.addView(view_video);
                    view =  view_video;
                    break;
                case 3:
                    if(view_date.getParent()!=null){
                        container.removeAllViews();
                    }
                    container.addView(view_date);
                    view =  view_date;
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
