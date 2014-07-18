package rinkimai.pro;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.color;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Tab_Home extends Activity {
	
 	private ProgressDialog pDialog;
	private static final String URL = "http://rinkimai2014.coxslot.com/webservisas/vartotojai.php";
	private static final String URL2 = "http://rinkimai2014.coxslot.com/webservisas/daysLeft.php";

    
    private static final String POSTS = "posts";
    private static final String NAME = "name";
    private static final String SURENAME = "sure_name";
    private static final String EMAIL = "email";
    private static final String ID = "user_id";
    private static final String ASM_KOD = "asm_kod";
    private static final String BIL_NR = "bil_nr";
    private static final String PAVADINIMAS = "pavadinimas";
    private static final String DAYS = "days";
    private static final String HOURS = "hours";
    public static String user_id;
    
    private JSONArray jsonTemp = null;
    private ArrayList<HashMap<String, String>> vartotojai;
    
    private JSONArray jsonTemp2 = null;
    public static ArrayList<HashMap<String, String>> liko;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_home);	
		new Hello().execute();
		
	}
	
	public class Hello extends AsyncTask<Void, Void, Boolean> {

    	@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Tab_Home.this);
			pDialog.setMessage("Loading...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
        @Override
        protected Boolean doInBackground(Void... arg0) {
            updateJSONdata();
            updateJSONdata2();
            return null;

        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            getUserData();
            getTimeLeft();
            checkUser();
        }
    }
	
	private void checkUser(){
		LinearLayout panele = (LinearLayout) findViewById(R.id.panele5);
		
		 for(int i = 0; i < vartotojai.size(); i++){
			 String a = vartotojai.get(i).get(BIL_NR);
			 String b = vartotojai.get(i).get(ASM_KOD);
			 String c = vartotojai.get(i).get(EMAIL);
			 if(VartotojoDuomenys.getEmail().equals(c)){
				 if(a.equals("0")){
					 TextView empty = new TextView(getApplicationContext());
					 empty.setText("");
					 empty.setHeight(25);
					 panele.addView(empty);
					 TextView tt = new TextView(getApplicationContext());
					 tt.setText("Noredami balsuoti papildykite savo duomenis.");
					 tt.setHeight(50);
					 tt.setGravity(Gravity.CENTER);
					 tt.setBackgroundColor(Color.RED);
					 tt.setTextSize(17);
					 MainTabs.tabHost.getTabWidget().getChildAt(1).setEnabled(false);
				     
				     panele.addView(tt);
				 }
			 }
		 }
		
	}
	
	public void updateJSONdata() {
    	
        vartotojai = new ArrayList<HashMap<String, String>>();
        
        JSONObject json = JSONParser.getJSONFromUrl(URL);
        
        try {
        	        	
            jsonTemp = json.getJSONArray(POSTS);

            // looping through all posts according to the json object returned
            for (int i = 0; i < jsonTemp.length(); i++) {
                JSONObject c = jsonTemp.getJSONObject(i);

                //gets the content of each tag
                
                String name = c.getString(NAME);
                String surename = c.getString(SURENAME);
                String email = c.getString(EMAIL);
                String asm = c.getString(ASM_KOD);
                String bil = c.getString(BIL_NR);
                String ids = c.getString(ID);
                

                
                // creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();
                
                map.put(NAME, name);
                map.put(SURENAME, surename);
                map.put(EMAIL, email);
                map.put(ASM_KOD, asm);
                map.put(BIL_NR, bil);
                map.put(ID, ids);
               
                vartotojai.add(map);
             
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
               
	}
	
public void updateJSONdata2() {
    	
        liko = new ArrayList<HashMap<String, String>>();
        
        JSONObject json = JSONParser.getJSONFromUrl(URL2);
        
        try {
        	        	
            jsonTemp2 = json.getJSONArray(POSTS);

            // looping through all posts according to the json object returned
            for (int i = 0; i < jsonTemp2.length(); i++) {
                JSONObject c = jsonTemp2.getJSONObject(i);

                //gets the content of each tag
                
                String pavadinimas = c.getString(PAVADINIMAS);
                String days = c.getString(DAYS);
                String hours = c.getString(HOURS);
                                             
                // creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();
                
                map.put(PAVADINIMAS, pavadinimas);
                map.put(DAYS, days);
                map.put(HOURS, hours);
               
                liko.add(map);
             
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
               
	}
	
	 private void SayHello(final String name, final String surename, final String email){
			
			LinearLayout panele = (LinearLayout) findViewById(R.id.panele5);
			TextView tt = (TextView) findViewById(R.id.pasisveikinimas);
			tt.setText("Sveiki "+name+" "+surename);
						
		}
	 
	 private void getUserData(){
		 for(int i = 0; i < vartotojai.size(); i++){
			 String a = vartotojai.get(i).get(NAME);
			 String b = vartotojai.get(i).get(SURENAME);
			 String c = vartotojai.get(i).get(EMAIL);
			 if(VartotojoDuomenys.getEmail().equals(c)){
				SayHello(a, b, c); 
			 	this.user_id = vartotojai.get(i).get(ID);
			 	VartotojoDuomenys.setAsm_kod(vartotojai.get(i).get(ASM_KOD));
			 	VartotojoDuomenys.setBil_nr(vartotojai.get(i).get(BIL_NR));
			 	VartotojoDuomenys.setEmail(vartotojai.get(i).get(EMAIL));
			 	VartotojoDuomenys.setName(vartotojai.get(i).get(NAME));
			 	VartotojoDuomenys.setSurename(vartotojai.get(i).get(SURENAME));
			 	VartotojoDuomenys.setUser_id(vartotojai.get(i).get(ID));
			 	SQLiteCommandCenter.naujiVartotojoDuomenys();
                SQLiteCommandCenter.ijungtiAutentifikacija();
			 } 
//			 if(Home.thisUser.equals(c)){
//					SayHello(a, b, c); 
//				 	this.user_id = vartotojai.get(i).get(ID);
//				 } 
			 
		 }
	 }
	 
	 public void getTimeLeft(){
		 for(int i = 0; i < liko.size(); i++){
			 String a = liko.get(i).get(PAVADINIMAS);
			 String b = liko.get(i).get(DAYS);
			 String c = liko.get(i).get(HOURS);
			 postList(a, b, c);
		 }
	 }
	 
	 private void postList(final String pav, final String days, final String hours){
		 LinearLayout panele = (LinearLayout) findViewById(R.id.panele5);
		 TextView tt = new TextView(getApplicationContext());
		 tt.setText("Iki "+pav+" liko "+days+ " dienos ir "+hours+" valandos.");
		 tt.setPadding(0, 0, 0, 10);
		 tt.setGravity(Gravity.CENTER);
		 tt.setTextColor(Color.GREEN);
		 tt.setTextSize(16);
		 panele.addView(tt);
	 }

}
