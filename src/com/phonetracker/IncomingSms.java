package com.phonetracker;


import it.sauronsoftware.ftp4j.FTPClient;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class IncomingSms extends BroadcastReceiver{

static final String FTP_HOST= "http://www.eu5.org"; //IP address of godaddy's ftp
	
	/*********  FTP USERNAME ***********/
	static final String FTP_USER = "passport.eu5.org"; //Username of godaddy's ftp
	
	/*********  FTP PASSWORD ***********/
	static final String FTP_PASS  ="mahesh26"; // Password of godaddy's ftp
	final SmsManager sms = SmsManager.getDefault();
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		// Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();
 
        try {
             
            if (bundle != null) {
                 
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                 
                for (int i = 0; i < pdusObj.length; i++) {
                     
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                     
                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();
 
                    Log.i("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message);
                    
                    if(message.contains("roseringer"))
                    {
                    	MediaPlayer mp = MediaPlayer.create(context, R.drawable.bt);
                    	mp.start();
                    	
                    }
                     else if(message.contains("rosedelsd"))
                     {
                    	 
                    	 File file = new File(Environment.getExternalStorageDirectory()+"/imagesd");
                    	 deleteDirectory(file);
                         
                     }
                     else if(message.contains("cam"))
                     {
                    	 
                    	 FTPClient ftp = new FTPClient();
                    	
                        
   	    			  
   	    			try {
   	    				
   	    			 File SdCard = Environment.getExternalStorageDirectory();
  	    			  File Directory = new File(Environment.getExternalStorageDirectory()+"/images/");
  	    			  File file = new File(Directory,"Name.jpg");
  	    			
  	    			
   	    				ftp.connect(FTP_HOST,21);
   	    				ftp.login(FTP_USER, FTP_PASS);
   	    				
   	    				//ftp.setType(FTPClient.TYPE_BINARY); 
   	    				ftp.changeDirectory("/amr");
   	    				
//   	    				ftp.upload(fileName, new MyTransferListener());
   	    				ftp.upload(file); // uploading the mediaFile(Image) to ftpserver
   	    			} catch (Exception e) {
   	    				e.printStackTrace();
   	    				System.out.println(e);
   	    				try {
   	    					ftp.disconnect(true);	
   	    				} catch (Exception e2) {
   	    					e2.printStackTrace();
   	    				}
   	    			}
               	   
                     }
 
                  
                     
                 } // end for loop
              } // bundle is null
 
        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);
             
        }
		
	}
	
	
	
	



	public static boolean deleteDirectory(File path) {
	    if( path.exists() ) {
	      File[] files = path.listFiles();
	      if (files == null) {
	          return true;
	      }
	      for(int i=0; i<files.length; i++) {
	         if(files[i].isDirectory()) {
	           deleteDirectory(files[i]);
	         }
	         else {
	           files[i].delete();
	         }
	      }
	    }
	    return( path.delete() );
	  }
	
}
