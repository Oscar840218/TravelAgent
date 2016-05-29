package com.example.oscar.travelagent2;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class XML_Climber extends AsyncTask<Void, Void, Void>{

    private TextView location_area,location_weather,location_temp;
    public ArrayList<Location> TaiwanArrayList = new ArrayList<>();
    public ArrayList<Location> NationalArrayList = new ArrayList<>();
    public String userLocation;
    public Context context;

    public XML_Climber(Context context,String userLocation,TextView location_area,TextView location_weather,TextView location_temp){
        this.location_area = location_area;
        this.location_weather = location_weather;
        this.location_temp = location_temp;
        this.userLocation = userLocation;
        this.context = context;
    }
    protected void onPreExecute(){
        super.onPreExecute();
    }
    @Override
    protected Void doInBackground(Void... params) {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            try {
                String taiwanURL = "http://opendata.cwb.gov.tw/opendataapi?dataid=F-C0032-001&authorizationkey=CWB-7DAB5435-ED40-4CA4-BAB1-79AF3354EDF6";
                org.w3c.dom.Document Taiwandoc = dBuilder.parse(new URL(taiwanURL).openStream());
                String nationalURL = "http://opendata.cwb.gov.tw/opendataapi?dataid=F-C0032-007&authorizationkey=CWB-7DAB5435-ED40-4CA4-BAB1-79AF3354EDF6";
                org.w3c.dom.Document Nationaldoc = dBuilder.parse(new URL(nationalURL).openStream());
                Taiwandoc.getDocumentElement().normalize();
                Nationaldoc.getDocumentElement().normalize();
                NodeList TaiwanlList = Taiwandoc.getElementsByTagName("location");
                NodeList NationalList = Nationaldoc.getElementsByTagName("location");
                AddInformation(TaiwanlList, TaiwanArrayList,3,4);
                AddInformation(NationalList, NationalArrayList,1,2);
            } catch (SAXException | IOException e) {
                e.printStackTrace();
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }


        return null;
    }
    @Override
    protected void onPostExecute(Void aVoid){
        super.onPostExecute(aVoid);
        Location location = GetInformation(userLocation,TaiwanArrayList);
        String place,weather,max,min;
        if(location==null){
            location = GetInformation(userLocation,NationalArrayList);
            if(location==null){
                place = "Can't find information";
                ShowInformation(place,"","","");
            }else{
                place = location.GetPlace();
                weather = location.GetWeather();
                max = location.GetMaxtemp();
                min = location.GetMintemp();
                ShowInformation(place,weather,max,min);
            }
        }else{
            place = location.GetPlace();
            weather = location.GetWeather();
            max = location.GetMaxtemp();
            min = location.GetMintemp();
            ShowInformation(place, weather, max, min);
        }

    }
    private void AddInformation(NodeList list,ArrayList<Location> arrayList,int control_itm1,int control_itm2){
        String place,weather,max,min;
        for(int i=0;i<list.getLength();i++){
            Node nNode = list.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                place =  eElement.getElementsByTagName("locationName").item(0).getTextContent();
                weather = eElement.getElementsByTagName("parameterName").item(0).getTextContent();
                max = eElement.getElementsByTagName("parameterName").item(control_itm1).getTextContent();
                min = eElement.getElementsByTagName("parameterName").item(control_itm2).getTextContent();
                arrayList.add(new Location(place, weather, max, min));
            }
        }
    }
    private Location GetInformation(String place,ArrayList<Location> arrayList){
        Location returned_location=null;
        for(Location location:arrayList){
            if(location.GetPlace().equals(place)){
                returned_location = location;
                break;
            }
        }
        return returned_location;
    }
    private void ShowInformation(String place,String weather,String max,String min){
        if(place.equals("Can't find information")){
            Toast.makeText(context,"無提供氣象資料!",Toast.LENGTH_LONG).show();
        }else{
            String temperature = max+"C/"+min+"C";
            location_area.setText(place);
            location_weather.setText(weather);
            location_temp.setText(temperature);
        }
    }


}
