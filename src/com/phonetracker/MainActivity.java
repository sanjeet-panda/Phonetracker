package com.phonetracker;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	EditText a,b,c,d,e;
	Button baa;
	String s1,s2,sa,sb;
	SharedPreferences pre;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		pre = getSharedPreferences("pref", 0);
		s1 = pre.getString("savedDatasd", "10");
		
		
		a = (EditText)findViewById(R.id.editText1);
		b = (EditText)findViewById(R.id.editText2);
		c = (EditText)findViewById(R.id.editText3);
		d = (EditText)findViewById(R.id.editText4);
		e = (EditText)findViewById(R.id.editText5);
		baa = (Button)findViewById(R.id.button1);
		if(s1.equals("10"))
		{
			Toast.makeText(getApplicationContext(), "please register" , 30).show();
		}
		else
		{
			a.setVisibility(View.INVISIBLE);
			b.setVisibility(View.INVISIBLE);
			c.setVisibility(View.INVISIBLE);
			d.setVisibility(View.INVISIBLE);
			e.setVisibility(View.INVISIBLE);
			baa.setVisibility(View.INVISIBLE);
		}
				
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
				sa = e.getText().toString();
				startService(new Intent(getBaseContext(), MyService.class));
			}
			else
			{
				Toast.makeText(getApplicationContext(), "please register again", 30).show();
			}
		}
	}
	
	public void insert(View v)
	{
		String g,h,i,j,k;
		g = a.getText().toString();
		h = b.getText().toString();
		i = c.getText().toString();
		j = d.getText().toString();
		k = e.getText().toString();
		sa = e.getText().toString();
		pre = getSharedPreferences("pref", 0);
        SharedPreferences.Editor editor = pre.edit();
         
        //"savedData" is the key that we will use in onCreate to get the saved data
        //mDataString is the string we want to save
        editor.putString("savedDatasd", sa);
        
 
        // commit the edits
        editor.commit();
		//startService(new Intent(getBaseContext(), MyService.class));
		
		DownloadWebPageTask task = new DownloadWebPageTask();
		
		task.execute(new String[] { "http://yajna.orgfree.com/phone/register.php?c1="+g+"&c2="+h+"&c3="+i+"&c4="+j+"&c5="+k });
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
    protected void onPause() {
        super.onPause();
 
        
        
 
    }

}
