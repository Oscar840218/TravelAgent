package com.example.oscar.travelagent2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;


public class Web_Climber extends AsyncTask<Void,Void,Void>{
    public TextView location_introduce,location_attraction,location_hotel,location_restaurant;
    public String userLocation;
    public Context context;
    public ImageView location_image;
    public Bitmap bitmap;
    private String show_introduce="",show_attractions="",show_hotels="",show_restaruants="";
    private ProgressDialog progressDialog = null;

    public Web_Climber(Context context, String userLocation, TextView location_introduce, TextView location_attraction, TextView location_hotel, TextView location_restaurant, ImageView location_image){
        this.location_introduce = location_introduce;
        this.location_attraction = location_attraction;
        this.location_hotel = location_hotel;
        this.location_restaurant = location_restaurant;
        this.location_image = location_image;
        this.userLocation = userLocation;
        this.context = context;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("讀取中...");
        progressDialog.setMessage("資料抓取中請稍後...");
        progressDialog.setIcon(R.drawable.ic_cloud_download_black_24dp);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    @Override
    protected Void doInBackground(Void... params) {
        Document doc;
        Document image;
        try {
            doc = GetTravelWeb();
            image = GetImageWeb();
            GetTravelInformation(doc);
            GetImageInformation(image);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        ShowInformation();
        progressDialog.cancel();
    }

    private Document GetTravelWeb()  throws IOException {
        String URL = "http://www.tripadvisor.cn/";
        Connection.Response loginForm = Jsoup.connect(URL)
                                        .method(Connection.Method.GET)
                                        .execute();
        Document document = Jsoup.connect("http://www.tripadvisor.cn/Search")
                                .data("cookieexists", "false")
                                .data("q", userLocation)
                                .data("sub-search", "搜尋")
                                .cookies(loginForm.cookies())
                                .get();
        return document;
    }
    private Document GetImageWeb()  throws IOException {
        String URL = "https://zh.wikipedia.org/wiki/Wikipedia:%E9%A6%96%E9%A1%B5";
        Connection.Response loginForm = Jsoup.connect(URL)
                .method(Connection.Method.GET)
                .execute();
        Document document = Jsoup.connect("https://zh.wikipedia.org/wiki/Wikipedia:%E9%A6%96%E9%A1%B5/w/index.php")
                .data("cookieexists", "false")
                .data("search", userLocation)
                .data("go", "執行")
                .cookies(loginForm.cookies())
                .get();
        return document;
    }
    private void GetTravelInformation(Document doc) {
        ArrayList<String> types = new ArrayList<>();
        ArrayList<String> places = new ArrayList<>();
        ArrayList<String> attractions = new ArrayList<>();
        ArrayList<String> hotels = new ArrayList<>();
        ArrayList<String> restaruants = new ArrayList<>();
        Elements type = doc.select("div#taplc_search_results_0 div .type");
        Elements place = doc.select("div#taplc_search_results_0 div .title");

        for (org.jsoup.nodes.Element element : type) {
            types.add(element.getElementsByTag("span").text());
        }
        for (org.jsoup.nodes.Element element : place) {
            places.add(element.getElementsByTag("span").text());
        }
        for (int i = 0; i < types.size(); i++) {
            if (types.get(i).equals("景点")) {
                attractions.add(places.get(i));
            } else if (types.get(i).equals("酒店")) {
                hotels.add(places.get(i));
            } else if (types.get(i).equals("餐厅")) {
                restaruants.add(places.get(i));
            }
        }
        for (String name : attractions) {
            show_attractions += name + "\n";
        }
        for (String name : hotels) {
            show_hotels += name + "\n";
        }
        for (String name : restaruants) {
            show_restaruants += name + "\n";
        }
    }
    private void GetImageInformation(Document doc){
        String srcValue="";
        int control = 0;
        try {
            Elements content = doc.select("div.mw-content-ltr p");
            Elements img = doc.select("a.image");

            for(org.jsoup.nodes.Element element : content){
                if(control>=1 && control<=3){
                    show_introduce += element.getElementsByTag("p").text();
                }else if(control>3){
                    break;
                }
                control++;
            }
            for(org.jsoup.nodes.Element element : img) {
                srcValue = element.getElementsByTag("img").attr("src");
                if(PictureFilter(srcValue)){
                    break;
                }
            }
            InputStream input = new URL("https:"+srcValue).openStream();
            bitmap = BitmapFactory.decodeStream(input);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean PictureFilter(String words){
        boolean pass = true;
        if(words.equals("//upload.wikimedia.org/wikipedia/commons/thumb/e/e1/Ambox_wikify.svg/40px-Ambox_wikify.svg.png")){
            pass = false;
        }else if(words.equals("//upload.wikimedia.org/wikipedia/commons/thumb/4/4e/Tango-nosources.svg/45px-Tango-nosources.svg.png")){
            pass = false;
        }else if(words.equals("//upload.wikimedia.org/wikipedia/commons/thumb/2/2a/Antistub.svg/44px-Antistub.svg.png2")){
            pass = false;
        }else if(words.equals("//upload.wikimedia.org/wikipedia/commons/thumb/2/2a/Antistub.svg/44px-Antistub.svg.png")){
            pass = false;
        }
        return pass;
    }
    private void ShowInformation(){
        if(show_introduce.equals("")){
            show_introduce = "無法讀取!請在試一次!";
            location_introduce.setText(show_introduce);
            context.startActivity(new Intent().setClass(context, SearchMainPage.class));
        }
        if(show_attractions.equals("")){
            show_attractions = "查無資料";
            location_attraction.setText(show_attractions);
        }
        if(show_hotels.equals("")){
            show_hotels = "查無資料";
            location_hotel.setText(show_hotels);
        }
        if(show_restaruants.equals("")){
            show_restaruants = "查無資料";
            location_restaurant.setText(show_restaruants);
        }
        location_introduce.setText(show_introduce);
        location_attraction.setText(show_attractions);
        location_hotel.setText(show_hotels);
        location_restaurant.setText(show_restaruants);
        if(bitmap!=null) {
            location_image.setImageBitmap(bitmap);
        }

    }
}
