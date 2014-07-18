package rinkimai.pro;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;




import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;



public class variantai extends Activity {
	
	 	public String id;
	 	private ProgressDialog pDialog;
		private static final String URL = "http://rinkimai2014.coxslot.com/webservisas/variantai.php";

	    
	    private static final String POSTS = "posts";
	    private static final String ID = "id";
	    private static final String INFO = "info";
	    private static final String VARIANTAS = "variantas";
	    
	    private JSONArray variantai = null;
	    private ArrayList<HashMap<String, String>> variantuSarasas;

	
	 @Override
     public void onCreate(Bundle savedInstanceState) {         

        super.onCreate(savedInstanceState);    
        setContentView(R.layout.variantai);
        TextView tv = (TextView)findViewById(R.id.txt);
        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            id =(String) b.get("id");
            tv.setText((String) b.get("pavadinimas"));
        }
        new LoadBalsavimai().execute();
        backBtn();
    }
	 
	 public void CreateList(final String text, final String info, final String id){
			
			LinearLayout panele = (LinearLayout) findViewById(R.id.panele2);
			Button bt = new Button(getApplicationContext());
			
			bt.setText(text);
			bt.setBackgroundResource(R.drawable.variantai_button_sellector);
		    bt.setAlpha((float) 0.5);
			bt.setTextColor(Color.BLACK);
			
		    bt.setCompoundDrawablesWithIntrinsicBounds( R.drawable.user_files_icon, 0, 0, 0);
				
			bt.setOnClickListener(new View.OnClickListener() {
			    @Override
			    public void onClick(View v) {
			    	Intent intent = new Intent(variantai.this, info.class);
			    	intent.putExtra("pavadinimas", text);
			    	intent.putExtra("info", info);
			    	intent.putExtra("ats_id", id);
			    	startActivityForResult(intent, 0);
			    }
			});
			panele.addView(bt);
			
		}
	 public void updateJSONdata() {
	    	
	        variantuSarasas = new ArrayList<HashMap<String, String>>();
	        
	        if(Home.networkStateListener.isInternetOn())
	        {
	        JSONObject json = JSONParser.getJSONFromUrl(URL);
	        
	        try {
	        	SQLiteCommandCenter.variantaiJsonToSqlite(json);
	        	
	            variantai = json.getJSONArray(POSTS);

	            // looping through all posts according to the json object returned
	            for (int i = 0; i < variantai.length(); i++) {
	                JSONObject c = variantai.getJSONObject(i);

	                //gets the content of each tag
	                
	                String info = c.getString(INFO);
	                String variantas = c.getString(VARIANTAS);
	                String id = c.getString(ID);
	                String var_id = c.getString("var_id");
	                              
	                // creating new HashMap
	                HashMap<String, String> map = new HashMap<String, String>();
	                
	                map.put(INFO, info);
	                map.put(VARIANTAS, variantas);
	                map.put(ID, id);
	                map.put("var_id", var_id);
	             
	                // adding HashList to ArrayList
	                variantuSarasas.add(map);
	                
	                //annndddd, our JSON data is up to date same with our array list
	            }

	        } catch (JSONException e) {
	            e.printStackTrace();
	        }
	        
	        }else{
	        	variantuSarasas = SQLiteCommandCenter.getTableVariantai();
	        }
	    }
	 
	 public void getAll(){
		 for(int i = 0; i < variantuSarasas.size(); i++){
			 String a = variantuSarasas.get(i).get(VARIANTAS);
			 String b = variantuSarasas.get(i).get(INFO);
			 String c = variantuSarasas.get(i).get(ID);
			 String d = variantuSarasas.get(i).get("var_id");
			 if(c.equals(id)) CreateList(a, b, d);
		 }
	 }
	 
	 public class LoadBalsavimai extends AsyncTask<Void, Void, Boolean> {

	    	@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pDialog = new ProgressDialog(variantai.this);
				pDialog.setMessage("Loading List...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				pDialog.show();
			}
	        @Override
	        protected Boolean doInBackground(Void... arg0) {
	        	//we will develop this method in version 2
	            updateJSONdata();
	            return null;

	        }


	        @Override
	        protected void onPostExecute(Boolean result) {
	            super.onPostExecute(result);
	            pDialog.dismiss();
	          //we will develop this method in version 2
	            getAll();
	        }
	    }
	 
	 public void backBtn(){
		 Button a = (Button) findViewById(R.id.backB);
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
	        //    finish();

	    }

}
