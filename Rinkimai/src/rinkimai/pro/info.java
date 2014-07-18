package rinkimai.pro;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class info extends Activity {
	
		Button vote;
		// langelis kuriame rodo kad kraunasi(pvz balsuojama...)
		private ProgressDialog pDialog;
		 
	    // JSON parser class
	    JSONParser jsonParser = new JSONParser();
	        
	    //linkas y php failiuka kuris perduoda duomenis y DB:
	    private static final String VOTE_URL = "http://rinkimai2014.coxslot.com/webservisas/vote.php";
	    //linkas patraukti vartotoju duomenis
		private static final String URL = "http://rinkimai2014.coxslot.com/webservisas/vartotojai.php";
		private static final String URL2 = "http://rinkimai2014.coxslot.com/webservisas/balsai.php";
	    
	    //Json tagai
	    private static final String TAG_SUCCESS = "success";
	    private static final String TAG_MESSAGE = "message";
	    /*Kintamasis isgauti esama vartotoja is Home klases (ten issaugojamas  
	     *statiniame kintamajame prisijungiant) Paspaudus vote keliauja y db*/
	    private String username = Home.thisUser;
	    private String var_id;
	    /*Kintamasis isgauti balsavima kuriame esu is Tab_Vote klases (ten issaugojamas  
	     *statiniame kintamajame paspaudus ant pacio balsavimo)Paspaudus vote keliauja y db*/
	    private String balsavimas = Tab_Vote.balsId;
	    /*Kintamasis isgauti kuri varianta pasirinkome(uzpildymas bus veliau)*/
	    private String variantas;
	    //private Button b = (Button) findViewById(R.id.vote);
	    
	    private JSONArray juserid = null;
	    private JSONArray balsai = null;
	    public ArrayList<HashMap<String, String>> pabalsuota = new ArrayList<HashMap<String, String>>();
	    
	    private static final String POSTS = "posts";
	    private TextView tevi;

	 public void onCreate(Bundle savedInstanceState) { 
		 
	        super.onCreate(savedInstanceState);    
	        setContentView(R.layout.info);
	        fillList();
	        
	    }
	public void fillList(){
		
		TextView tv = (TextView)findViewById(R.id.variantas);
        TextView inf = (TextView)findViewById(R.id.info);
        tevi = (TextView) findViewById(R.id.info2);
        Intent iin= getIntent();
      //* isgaunami duomenys siunciami is praejusio activicio
        Bundle b = iin.getExtras();
        
        if(b!=null)
        {
        	// isgaunamas varianto pavadinimas (jy parodau siame activity)
            tv.setText((String) b.get("pavadinimas"));
            // isgaunama informacija apie pasirinkta varianta (jy parodau siame activity)
            inf.setText((String) b.get("info"));
            // isgaunama pasirinkto varianto id. Paspaudus vote keliauja y db
	        variantas = (String) b.get("ats_id");
        }
        
        new CheckIfVoted().execute();
        
        
        backBtn();
        voteBtn();
	}
	 
	 public void backBtn(){
		 Button a = (Button) findViewById(R.id.back_b);
		 
		 a.setBackgroundResource(R.drawable.back_button_selector);
		 a.setAlpha((float) 0.8);
		 
		 a.setOnClickListener(new View.OnClickListener() {
			    @Override
			    public void onClick(View v) {
			    	onBackPressed();
			    }
			});
	 }
	 @Override
	    public void onBackPressed() {
	        super.onBackPressed();   
	 }
	 
	 	private void voteBtn(){
		 
	 		vote = (Button) findViewById(R.id.vote);
	 		vote.setBackgroundResource(R.drawable.back_button_selector);
		    vote.setOnClickListener(new View.OnClickListener() {
		    	@Override
		    	public void onClick(View v) {
		    		
		    		if(Home.networkStateListener.isInternetOn()){
		    			new Voting().execute();
		    			SQLiteCommandCenter.vidinisBalsavimas(balsavimas, variantas,getApplicationContext());
		    		}
		    		else{
		    			SQLiteCommandCenter.vidinisBalsavimas(balsavimas, variantas,getApplicationContext());
		    		}
				}
			}); 
	 }
	 
	 class Voting extends AsyncTask<String, String, String> {
			
	        @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            pDialog = new ProgressDialog(info.this);
	            pDialog.setMessage("Voting...");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(true);
	            pDialog.show();
	        }
			
			@Override
			protected String doInBackground(String... args) {
				//balsavimo funkcionalumas
	            int success;
	           
	            try {
	                // Building Parameters
	                List<NameValuePair> params = new ArrayList<NameValuePair>();
	                /* taip duomenys siunciami y php failiuka kuris nusius juos
	                 * y DB*/
	                params.add(new BasicNameValuePair("vart_id", var_id));
	                params.add(new BasicNameValuePair("bals_id", balsavimas));
	                params.add(new BasicNameValuePair("ats_id", variantas));
	                
	                Log.d("request!", "starting");
	                
	                //Posting user data to script 
	                JSONObject json = jsonParser.makeHttpRequest(
	                       VOTE_URL, "POST", params);
	 
	                // full json response
	                Log.d("Pabalsuota!", json.toString());
	 
	                // json success element
	                success = json.getInt(TAG_SUCCESS);
	                if (success == 1) {
	                	Log.d("Voted!", json.toString()); 
	                	
	                	return json.getString(TAG_MESSAGE);
	                }else{
	                	Log.d("Voting Fail!", json.getString(TAG_MESSAGE));
	                	return json.getString(TAG_MESSAGE);
	                	
	                }
	            } catch (JSONException e) {
	                e.printStackTrace();
	            }
	            return null;
			}
			
	        protected void onPostExecute(String file_url) {
	            // dismiss the dialog once product deleted
	            pDialog.dismiss();
	            if (file_url != null){
	            	Toast.makeText(info.this, file_url, Toast.LENGTH_LONG).show();
	            }
	            vote.setEnabled(false);
	        }		
		}
	 
	 public void updateJSONdata() {
	       	        
	        JSONObject json = JSONParser.getJSONFromUrl(URL);
	      
	        try {
	            
	            juserid = json.getJSONArray(POSTS);

	            // looping through all posts according to the json object returned
	            for (int i = 0; i < juserid.length(); i++) {
	                JSONObject c = juserid.getJSONObject(i);

	                if(c.getString("email").equals(username))
	                var_id = c.getString("user_id");          
	            }

	        } catch (JSONException e) {
	            e.printStackTrace();
	        }
	    }
	 
	 public void updateJSONdata2() {
	        
	        JSONObject json = JSONParser.getJSONFromUrl(URL2);
	      
	        try {
	            
	            balsai = json.getJSONArray(POSTS);

	            // looping through all posts according to the json object returned
	            for (int i = 0; i < balsai.length(); i++) {
	                JSONObject c = balsai.getJSONObject(i);
	                HashMap<String, String> map = new HashMap<String, String>();
	                map.put("var",c.getString("vart_id"));
	                map.put("ats",c.getString("ats_id"));
	                map.put("kl", c.getString("bals_id"));
	                pabalsuota.add(map);             
	            }

	        } catch (JSONException e) {
	            e.printStackTrace();
	        }
	    }

	 class CheckIfVoted extends AsyncTask<Void, Void, Boolean> {
			
		 @Override
			protected void onPreExecute() {
				super.onPreExecute();
				pDialog = new ProgressDialog(info.this);
				pDialog.setMessage("Checking...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				pDialog.show();
			}
	        @Override
	        protected Boolean doInBackground(Void... arg0) {
	        	//we will develop this method in version 2
	        	updateJSONdata();
	            updateJSONdata2();
	            return null;
	        }

	        @Override
	        protected void onPostExecute(Boolean result) {
	            super.onPostExecute(result);
	            pDialog.dismiss();
	            checking();
	        }
	    }
	 //patikrina ar galima balsuoti
	 private void checking(){
		 if(!Tab_Vote.pabaigosD.equals("0000-00-00 00:00:00")){
			 Calendar now = Calendar.getInstance();
			 //jei dabartine data yra veliau uz rinkimu pabaigos data
			 if(DateHelp.isFirstDateBigger(DateHelp.formatDate(now.getTime().toString()),Tab_Vote.pabaigosD)){
				 vote.setEnabled(false);
				 tevi.setText("Rinkimai pasibaige.");
			 }
			 //jei rinkimu pradzia dar neatejo
			 else if(DateHelp.isFirstDateBigger(Tab_Vote.ikelimoD, DateHelp.formatDate(now.getTime().toString()))){
				 vote.setEnabled(false);
				 tevi.setText("Rinkimai prasides nuo " + Tab_Vote.ikelimoD);
			 }else{
				 checkIfVoted();
			 }
			 
		 }else{
			 checkIfVoted();
		 
		 }
	 }
	 private void checkIfVoted(){
		 for(int i = 0; i < pabalsuota.size(); i++){
			 if(pabalsuota.get(i).get("kl").equals(balsavimas) && pabalsuota.get(i).get("var").equals(var_id)){
				 vote.setEnabled(false);
				 if(pabalsuota.get(i).get("ats").equals(variantas)){
					 vote.setText("Voted");
					 vote.setBackgroundColor(Color.GREEN);
					 tevi.setText("Pabalsuota uz si varianta.");
				 }else{
					 tevi.setText("Siuose rinkimuose jus jau balsavote.");
				 }
			 }
		 
		 }
	 }
}
