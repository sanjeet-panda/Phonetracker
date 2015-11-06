package com.phonetracker;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.widget.TextView;
import android.widget.Toast;

public class MyService extends Service implements SensorEventListener, LocationListener {

	double mx,ny,oz;
    private float mLastX, mLastY,mLastZ ;
    float avgx;
	 float avgy;
	 float avgz;
	 float sdx;
	 float sdy;
	 float sdz;
	 
	 long actualtime;
    
	 
    
    MediaPlayer mp,mxl;
    private long lastupdate;

    TextView tvX,tvY,tvZ;
    String abc="", def = "", ghi = "";
    String cba = "0.0";
    int count=0;
    //String lat;
    double sigma=0.5,th=10,th1=5,th2=2;
    static int BUFF_SIZE=50;
    public static String curr_state,prev_state;
    public int i=0;
    static public double[] window = new double[BUFF_SIZE];
    public double a_norm;
    
    String a,b,c;
    String id1,lat,s1,sa;
    String named,keyj;
    String stadev,avgxyz,sdsx,sdsy,sdsz;
    SharedPreferences pre;
    
    private SensorManager sensorManager = null;
    private Sensor sensor = null;
    public static float x, y, z;
    
    double lati,longii;
  //  Location location;
    AppLocationService gps;
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		
			
			float x = event.values[0];
			float y = event.values[1];
			float z = event.values[2];
			
				float deltaX = Math.abs(mLastX - x);
				float deltaY = Math.abs(mLastY - y);
				float deltaZ = Math.abs(mLastZ - z);
				
				mLastX = x;
				mLastY = y;
				mLastZ = z;
				
				
				
				 a = Float.toString(deltaX);
				 b = Float.toString(deltaY);
				 c = Float.toString(deltaZ);
				 
				 if(Float.parseFloat(a)>9.0 && Float.parseFloat(b)>9.0)
				 {
					 mp.start();
					 double latitude = gps.getLatitude();
			        	double longitude = gps.getLongitude();
					 
			        	
					 DownloadWebPageTask task = new DownloadWebPageTask();
					 task.execute(new String[] { "http://yajna.orgfree.com/phone/save.php?c1="+Double.toString(latitude)+"&c2="+Double.toString(longitude)+"&c3="+s1 });
				 }
				 else
				 {
					 double latitude = gps.getLatitude();
			        	double longitude = gps.getLongitude();
			        
			        	
					 DownloadWebPageTask task = new DownloadWebPageTask();
					 task.execute(new String[] { "http://yajna.orgfree.com/phone/save.php?c1="+Double.toString(latitude)+"&c2="+Double.toString(longitude)+"&c3="+s1 });
				 }
				 
				
			
	}
	
	
	
	@Override
	   public void onDestroy() {
	      super.onDestroy();
	      mp.stop();
	      mxl.stop();
	      sensorManager.unregisterListener(this);
	      Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
	   }

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	   @Override
	   public int onStartCommand(Intent intent, int flags, int startId) {
	      // Let it continue running until it is stopped.
	      Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
	    
	      gps =  new AppLocationService(MyService.this);
	      if(gps.canGetLocation()){
	        	
	        	double latitude = gps.getLatitude();
	        	double longitude = gps.getLongitude();
	        		
	        }else{
	        	// can't get location
	        	// GPS or Network is not enabled
	        	// Ask user to enable GPS/network in settings
	        	gps.showSettingsAlert();
	        }
	      
	      pre = getSharedPreferences("pref", 0);
  		
  		// Getting stored latitude if exists else return 10
  		s1 = pre.getString("savedDatasd", "10");
  		
  		if(!s1.equals("10"))
  		{
  			Toast.makeText(this, "yes", 30).show();
  		}
  		else
  		{
  			Toast.makeText(getApplicationContext(), s1, 30).show();
  		}
	      
	      mp = MediaPlayer.create(this, R.drawable.bt);
			mxl = MediaPlayer.create(this, R.drawable.alm);
			id1="a";
			for(i=0;i<BUFF_SIZE;i++){
		    	 window[i]=0;
		     }
			 prev_state="none";
			 curr_state="none";
			 lastupdate = System.currentTimeMillis();
	      sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
	        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	        sensorManager.registerListener(this, sensor,
	                    SensorManager.SENSOR_DELAY_NORMAL);
	      return START_STICKY;
	   }

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
	
	private class DownloadWebPageTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {
			String response = "";
			for (String url : urls) {
				DefaultHttpClient client = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(url);
				try {
					HttpResponse execute = client.execute(httpGet);
					InputStream content = execute.getEntity().getContent();

					BufferedReader buffer = new BufferedReader(
							new InputStreamReader(content));
					String s = "";
					while ((s = buffer.readLine()) != null) {
						response += s;
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return response;
		}
		
		@Override
		protected void onPostExecute(String result) 
		{
			if(result.contains("success"))
			{
				
			}
		}
	}
	
	
    protected void onPause() {
        
 
        //save the data
        pre = getSharedPreferences("pref", 0);
        //SharedPreferences.Editor editor = preferences.edit();
         
        //"savedData" is the key that we will use in onCreate to get the saved data
        //mDataString is the string we want to save
      //  editor.putString("savedDatasd", sa);
        
 
        // commit the edits
       // editor.commit();
        
 
    }

}
