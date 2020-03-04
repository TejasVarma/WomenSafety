package com.example.womensafety;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;
import android.os.Handler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HomeActivity extends AppCompatActivity {

    MediaPlayer player=new MediaPlayer();
    SharedPreferences sharedPreferences;
//    public static final String API_KEY= "K7H1+jtQWNo-zc6duTdUgsw6ZSb6xVJf5EGoZOOPQq";
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    String phoneNo="";
    String message="";
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    String latitude, longitude;
    StringBuffer smsBody = new StringBuffer();
    boolean isRedClick=false;
    boolean isGreenClick=false;
    boolean isYellowClick=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sharedPreferences=getSharedPreferences("myPreference", Context.MODE_PRIVATE);
        ActivityCompat.requestPermissions( this,
                new String[] {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.SEND_SMS}, REQUEST_LOCATION);
//        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
    }

    public void redClick(final View view) {
        if(!getSender().equalsIgnoreCase("invalid")) {
            message=getRedMsg()+" Name:"+getSender()+" Cell:"+getContact()+getMapLink();
            isRedClick=true;
            isGreenClick=false;
            isYellowClick=false;
            sendOneByOne();
            Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(isRedClick && !isGreenClick && !isYellowClick) {
                        redClick(view);
                    }
                    else if(isGreenClick)
                        finish();
                }
            },10000);
        }
        else{
            Toast.makeText(this, "Invalid Sender Cell Number", Toast.LENGTH_SHORT).show();
        }
    }

    private String getMapLink(){
        getMapData();
        String uri = "https://maps.google.com/maps?q="+latitude +","+longitude;
        StringBuffer smsBody = new StringBuffer();
        smsBody.append("  Map Link : ");
        smsBody.append(Uri.parse(uri));
        return smsBody.toString();
    }

    private void getMapData(){
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        } else {
            getLocation();
        }
    }

    private String getSender() {
        if(sharedPreferences.contains("name"))
            return sharedPreferences.getString("name","");
        else
            return "invalid";
    }

    private String getContact() {
        if(sharedPreferences.contains("contact"))
            return sharedPreferences.getString("contact","");
        else
            return "invalid";
    }

    private String getAddress() {
        if(sharedPreferences.contains("address"))
            return sharedPreferences.getString("address","");
        else
            return "invalid";
    }

    public void yellowClick(final View view) {
        if(!getSender().equalsIgnoreCase("invalid")) {
            message=getYellowMsg()+" Name:"+getSender()+" Cell:"+getContact()+getMapLink();
            sendOneByOne();
            isRedClick=false;
            isGreenClick=false;
            isYellowClick=true;
            Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(isYellowClick && !isGreenClick && !isRedClick) {
                        yellowClick(view);
                    }
                    else if(isGreenClick)
                        finish();
                }
            },10000);
        }
        else{
            Toast.makeText(this, "Invalid Sender Cell Number", Toast.LENGTH_SHORT).show();
        }
    }

    public void greenClick(View view) {
        if(!getSender().equalsIgnoreCase("invalid")) {
            message=getGreenMsg()+" Name:"+getSender()+" Cell:"+getContact()+getMapLink();
            sendOneByOne();
            isYellowClick=false;
            isRedClick=false;
            isGreenClick=true;
            finish();
        }
        else{
            Toast.makeText(this, "Invalid Sender Cell Number", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendOneByOne(){
        String p1=sharedPreferences.contains("p1")?sharedPreferences.getString("p1",""):"invalid";
        Log.d("sendOneByOne p1 be ", "sendOneByOne: p1="+p1);
        if(Patterns.PHONE.matcher(p1).matches()) {
            phoneNo=p1;
            Log.d("sendOneByOne phone1 ", "sendOneByOne: phone1="+phoneNo);
            sendSMSBySMSManager();
        }
        String p2=sharedPreferences.contains("p2")?sharedPreferences.getString("p2",""):"invalid";
        Log.d("sendOneByOne p2 be ", "sendOneByOne: p2="+p2);
        if(Patterns.PHONE.matcher(p2).matches()) {
            phoneNo=p2;
            Log.d("sendOneByOne phone2 ", "sendOneByOne: phone2="+phoneNo);
            sendSMSBySMSManager();
        }
        String p3=sharedPreferences.contains("p3")?sharedPreferences.getString("p3",""):"invalid";
        Log.d("sendOneByOne p3 be ", "sendOneByOne: p3="+p3);
        if(Patterns.PHONE.matcher(p3).matches()) {
            phoneNo=p3;
            Log.d("sendOneByOne phone3 ", "sendOneByOne: phone3="+phoneNo);
            sendSMSBySMSManager();
        }
        String p4=sharedPreferences.contains("p4")?sharedPreferences.getString("p4",""):"invalid";
        Log.d("sendOneByOne p4 be ", "sendOneByOne: p4="+p4);
        if(Patterns.PHONE.matcher(p4).matches()) {
            phoneNo=p4;
            Log.d("sendOneByOne phone4 ", "sendOneByOne: phone4="+phoneNo);
            sendSMSBySMSManager();
        }
        String p5=sharedPreferences.contains("p5")?sharedPreferences.getString("p5",""):"invalid";
        Log.d("sendOneByOne p5 be ", "sendOneByOne: p5="+p5);
        if(Patterns.PHONE.matcher(p5).matches()) {
            phoneNo=p5;
            Log.d("sendOneByOne phone5 ", "sendOneByOne: phone5="+phoneNo);
            sendSMSBySMSManager();
        }
        phoneNo="";
        message="";
    }

    private void sendSMSBySMSManager(){
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNo, null, message, null, null);
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.SEND_SMS)
//                != PackageManager.PERMISSION_GRANTED) {
//            Log.d("sent: ", "sendSMSBySMSManager: ");
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.SEND_SMS)) {
//                Log.d("22", "sendSMSBySMSManager: 22");
//            } else {
//                Log.d("333", "sendSMSBySMSManager: 333");
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.SEND_SMS},
//                        MY_PERMISSIONS_REQUEST_SEND_SMS);
//            }
//        }
    }

    public void soundClick(View view) {
        if(player.isPlaying()){
            player.stop();
        }
        else {
            player=MediaPlayer.create(this,R.raw.siren);
            player.start();
            player.setLooping(true);
        }
    }

    public void smsClick(View view) {
    }

    public void settingClick(View view) {
        Intent intent=new Intent(HomeActivity.this,SettingActivity.class);
        startActivity(intent);
    }

    public void newsClick(View view) {
    }

//    private void sendSMS(String msg,String cellNo){
//        try {
//            Toast.makeText(this, ""+API_KEY+" "+msg+" "+getSender()+" "+cellNo, Toast.LENGTH_SHORT).show();
//            Log.d("API = ", "API: "+API_KEY);
//            Log.d("msg = ", "msg: "+msg);
//            Log.d("sender = ", "sender: "+getSender());
//            Log.d("cellNo = ", "cellNo: "+cellNo);
//            // Construct data
//            String apiKey = "apikey=" + API_KEY;
//            String message = "&message=" + msg;
//            String sender = "&sender=" + getSender();
//            String numbers = "&numbers=" + cellNo;
//
//            // Send data
//            HttpURLConnection conn = (HttpURLConnection) new URL("https://api.txtlocal.com/send/?").openConnection();
//            String data = apiKey + numbers + message + sender;
//            conn.setDoOutput(true);
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
//            conn.getOutputStream().write(data.getBytes("UTF-8"));
//            final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            final StringBuffer stringBuffer = new StringBuffer();
//            String line;
//            while ((line = rd.readLine()) != null) {
//                Toast.makeText(this, ""+line, Toast.LENGTH_SHORT).show();
//                Log.d("line = ", "sendSMS: "+line);
//            }
//            rd.close();
//
//        } catch (Exception e) {
//            Toast.makeText(this,"Error SMS "+e,Toast.LENGTH_SHORT).show();
//            Log.d("error = ", "sendSMS: "+e.getMessage());
//            Log.d("error = ", "sendSMS code: "+e);
//        }
//    }

    private String getRedMsg(){
        if(sharedPreferences.contains("redMsg"))
            return sharedPreferences.getString("redMsg","");
        else
            return "Hi, I am in trouble. Please help..!!!";
    }

    private String getYellowMsg(){
        if(sharedPreferences.contains("yellowMsg"))
            return sharedPreferences.getString("yellowMsg","");
        else
            return "Hi, just being cautious..!!!";
    }

    private String getGreenMsg(){
        if(sharedPreferences.contains("greenMsg"))
            return sharedPreferences.getString("greenMsg","");
        else
            return "Hi, sending a status update..!!!";
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    SmsManager smsManager = SmsManager.getDefault();
//                    Log.d("Result", "onRequestPermissionsResult: "+phoneNo+" "+message);
//                    smsManager.sendTextMessage(phoneNo, null, message, null, null);
//                    Toast.makeText(getApplicationContext(), "SMS sent.",
//                            Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(getApplicationContext(),
//                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
//                    return;
//                }
//            }
//            break;
//        }
//
//    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                HomeActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                HomeActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);
                Log.d("getLocation", "getLocation: lat = "+latitude+" lon = "+longitude);
            } else {
                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void policeClick(View view) {
    }
}
