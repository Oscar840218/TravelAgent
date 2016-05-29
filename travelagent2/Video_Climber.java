package com.example.oscar.travelagent2;


import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;
import android.widget.Toast;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;

public class Video_Climber extends AsyncTask<Void, Void, Void> {


    private String location;
    private TextView video_info;
    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<String> hrefs = new ArrayList<>();

    public Video_Climber(String location,TextView video_info){

        this.location = location;
        this.video_info = video_info;
    }


    protected void onPreExecute(){
        super.onPreExecute();
    }


    @Override
    protected Void doInBackground(Void... params) {
        try {
            Document doc = Jsoup.connect("https://www.youtube.com/results?search_query="+location+"旅遊") .data("query", "Java")
                    .userAgent("Mozilla")
                    .cookie("auth", "token")
                    .timeout(3000)
                    .post();
            GetInformation(doc);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        ShowInformation();
    }
    private void GetInformation(Document doc){

        Elements content = doc.select("ol.item-section li div.yt-lockup-content");
        for(org.jsoup.nodes.Element element : content) {
            titles.add(element.getElementsByTag("a").attr("title"));
            hrefs.add(element.getElementsByTag("a").attr("href"));
        }
    }
    private void ShowInformation(){
        String str = "";
        for(int i=0;i<titles.size();i++){
            str += "<a href=\"https://www.youtube.com/"+hrefs.get(i)+"\">"+titles.get(i)+"</a><br><br>";
        }
        video_info.setText(Html.fromHtml(str));
        video_info.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
