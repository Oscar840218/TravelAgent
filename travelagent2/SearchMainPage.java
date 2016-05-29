package com.example.oscar.travelagent2;


import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class SearchMainPage extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,DirectionFinderListener {

    private GoogleMap map;
    private LatLng Location_USER,Location_PLACE;
    private TextView place;
    private Button infobtn;
    public String locationName,CountryName,Subname;
    private App_Handler app_handler;
    private String location;
    private static ArrayList<String> history_places = new ArrayList<>();
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private GoogleApiClient googleApiClient;
    private final int MICRO_REQUEST = 0;
    private final int PLACE_REQUEST = 1;
    private Geocoder gc;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_main_page);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_public_black_24dp);
        setSupportActionBar(toolbar);
        app_handler = new App_Handler(SearchMainPage.this);
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        infobtn = (Button)findViewById(R.id.information);
        place = (TextView)findViewById(R.id.where);
        infobtn.setOnClickListener(infobtnOnClick);
        gc = new Geocoder(this);
        googleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        try {
            ShowPlace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.MENU_HOT:
                if(app_handler.HaveInternet()) {
                    Intent go_Popularlist = new Intent();
                    go_Popularlist.setClass(SearchMainPage.this, Popular_Location_List.class);
                    startActivity(go_Popularlist);
                }else{
                    Toast.makeText(SearchMainPage.this, "請確認手機網路狀態!", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.MENU_LOVE:
                if(app_handler.HaveInternet()) {
                    if (app_handler.isMember()) {
                        Intent go_Favoritelist = new Intent();
                        go_Favoritelist.setClass(SearchMainPage.this, favorite_place_List.class);
                        startActivity(go_Favoritelist);
                    }else{
                        Toast.makeText(SearchMainPage.this, "請先登入會員!", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(SearchMainPage.this, "請確認手機網路狀態!", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.MENU_INPUT:
                if(app_handler.HaveInternet()) {
                    Intent goInput = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("history",history_places);
                    goInput.putExtras(bundle);
                    goInput.setClass(SearchMainPage.this, Search_By_Word2.class);
                    startActivity(goInput);
                }else{
                    Toast.makeText(SearchMainPage.this, "請確認手機網路狀態!", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.MENU_MICRO:
                if(app_handler.HaveInternet()) {
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "請說話..."); //語音辨識 Dialog 上要顯示的提示文字
                    startActivityForResult(intent, MICRO_REQUEST);
                }else{
                    Toast.makeText(SearchMainPage.this, "請確認手機網路狀態!", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.MENU_SEARCH:
                Intent go_search = new Intent();
                go_search.setClass(SearchMainPage.this,Search_List.class);
                startActivity(go_search);
                break;
            case R.id.MENU_ROUTE:
                if(app_handler.HaveInternet()) {
                    Intent go_route = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("location",location);
                    go_route.putExtras(bundle);
                    go_route.setClass(SearchMainPage.this,Route_Request.class);
                    startActivity(go_route);
                }else{
                    Toast.makeText(SearchMainPage.this, "請確認手機網路狀態!", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.MENU_LOCAL:
                if(app_handler.HaveInternet()) {
                    try {
                        UserLocation();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(SearchMainPage.this, "請確認手機網路狀態!", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.MENU_QR:
                if(app_handler.HaveInternet()) {
                    Intent go_scanner = new Intent();
                    go_scanner.setClass(SearchMainPage.this,QRCode_Scanner.class);
                    startActivity(go_scanner);
                }else{
                    Toast.makeText(SearchMainPage.this, "請確認手機網路狀態!", Toast.LENGTH_LONG).show();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private View.OnClickListener infobtnOnClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if(app_handler.HaveInternet()) {
                Intent goInfo = new Intent();
                Bundle bundle = new Bundle();
                if (locationName == null) {
                    location = app_handler.NameFix(location);
                    bundle.putString("place", location);
                } else {
                    bundle.putString("place", locationName);
                }
                goInfo.putExtras(bundle);
                goInfo.setClass(SearchMainPage.this, Information_Page_2.class);
                startActivity(goInfo);
            }else{
                Toast.makeText(SearchMainPage.this, "請確認手機網路狀態!", Toast.LENGTH_LONG).show();
            }
        }
    };
    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }
    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent().setClass(SearchMainPage.this, MainActivity.class));
    }
    /***抓取資料並決定下一步***/
    public void ShowPlace() throws IOException {
        Bundle bundle = getIntent().getExtras();
        if(bundle==null){
            UserLocation();
        }else{
            location = bundle.getString("place");
            String start_place = bundle.getString("start");
            String end_place = bundle.getString("end");
            if(start_place !=null && end_place !=null){
                new DirectionFinder(this, start_place, end_place).execute();
                infobtn.setVisibility(View.INVISIBLE);
            }else if(location.equals(" ") || location.equals("")){
                infobtn.setEnabled(false);
            }else{
                infobtn.setEnabled(true);
                history_places.add(location);
                List<Address> locationlist = gc.getFromLocationName(location, 1);
                if (locationlist.size()>0){
                    Address add = locationlist.get(0);
                    double lat = add.getLatitude();
                    double lng = add.getLongitude();
                    RunMap(lat, lng, Location_PLACE);
                    GetLocationName(lat, lng);
                }else{
                    NoLocation();
                }

            }
        }
    }
    /***操作地圖***/
    public void RunMap(double lat,double lng,LatLng place){
        CameraUpdate update;
        place = new LatLng(lat,lng);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        onDirectionFinderStart();
        destinationMarkers.add(map.addMarker(new MarkerOptions().position(place).title("可點選右下角取得詳細地圖資訊")));
        update = CameraUpdateFactory.newLatLngZoom(place, 15);
        map.animateCamera(update);
    }
    /***使用者位置***/
    public void UserLocation() throws IOException {
        infobtn.setVisibility(View.VISIBLE);
        GPSTracker GPS = new GPSTracker(SearchMainPage.this);
        if(GPS.canGetLocation()){
            double latitude = GPS.getLatitude();
            double longitude = GPS.getLongtitude();
            RunMap(latitude, longitude, Location_USER);
            GetLocationName(latitude, longitude);
            infobtn.setEnabled(true);
        }
    }

    private void NoLocation(){
        Toast.makeText(SearchMainPage.this,"查無此地區",Toast.LENGTH_LONG).show();
        infobtn.setEnabled(false);
    }
    /***控制輸出地點名稱***/
    private void GetLocationName(double lat,double lng) throws IOException {
        List<Address> namelist = gc.getFromLocation(lat, lng, 1);
        if (namelist.size() > 0) {
            Address addname = namelist.get(0);
            Subname = addname.getSubLocality();
            locationName = addname.getAdminArea();
            CountryName = addname.getCountryName();
            if(locationName!=null){
                locationName = app_handler.NameFix(locationName);
                location = locationName;
                if(Subname!=null){
                    place.setText(CountryName+" "+locationName+" "+Subname);
                }else{
                    if(CountryName.equals(locationName)){
                        place.setText(locationName);
                    }else{
                        place.setText(CountryName+" "+locationName);
                    }
                }
            }else{
                if(location.equals(CountryName)){
                    place.setText(location);
                }else{
                    place.setText(CountryName+" "+location);
                }

            }
        }else{
            NoLocation();
        }
    }
    /***清除標記***/
    public void onDirectionFinderStart() {
        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }
    public void onDirectionFinderSuccess(List<Route> routes) {
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 8));
            place.setText("花費時間"+route.duration.text+" 距離:"+route.distance.text);

            originMarkers.add(map.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(map.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(map.addPolyline(polylineOptions));
        }
    }

    /***語音辨識結果***/
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == MICRO_REQUEST){
            if (resultCode == RESULT_OK) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String match = result.get(0);
                history_places.add(match);

                //location = match;
                List<Address> list = null;

                try {
                    list = gc.getFromLocationName(match,1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(list!=null){
                    Address add = list.get(0);
                    double lat = add.getLatitude();
                    double lng = add.getLongitude();
                    try {
                        GetLocationName(lat,lng);
                        RunMap(lat, lng, Location_PLACE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    NoLocation();
                }

            }
        }else if(requestCode == PLACE_REQUEST){
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }


}
