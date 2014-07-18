package rinkimai.pro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Tab_Me extends Activity {
	// cia saugosim balsavimo pavadinima ir varianta uz kuri balsuota
	
    HashMap<String, String> balsai = new HashMap<String, String>();
    JSONObject istrauktiDuomenys = null;
    ProgressDialog pDialog;
    
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_me);
		if(NetworkStateListener.isInternetOn())
		{
			new GetBalsavimai().execute();
		}
		
		
		balsai= SQLiteCommandCenter.metodasArturui(getApplicationContext());
		
		//tam kad is hashmap galetume istraukti visus duomenis naudosim mapset
		Set mapSet = (Set) balsai.entrySet();
		Iterator mapIterator = mapSet.iterator();
		// ViewListo adapteriams sukurti naudojama medziaga is array listo, taigi visus duomenis sukisim i array list
		ArrayList<String> list = new ArrayList<String>();
		
		while (mapIterator.hasNext()) {
             Map.Entry mapEntry = (Map.Entry) mapIterator.next();
             // getKey Method of HashMap access a key of map
             String keyValue = (String) mapEntry.getKey();
             //getValue method returns corresponding key's value
             String value = (String) mapEntry.getValue();
             
            // is Map set perduodame duomenis i array list 
             list.add( keyValue +" balsavote uz "+ value);
		}



		   ListView history = (ListView) findViewById(R.id.lv_history);
		   TextView name = (TextView) findViewById(R.id.tv_name);
		   TextView yourHistory = (TextView) findViewById(R.id.tv_history);
		   
		   Button userData = (Button)findViewById(R.id.button_edit_user_data);
		   
		   userData.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				new Input().show(getFragmentManager(), "ss");
			}
		});
		   final StableArrayAdapter adapter = new StableArrayAdapter(this, android.R.layout.simple_list_item_1, list);
		   history.setAdapter(adapter);
		   
		   if(VartotojoDuomenys.getName() == null){

			   name.setText("Sveiki Jusu duomenys dar nera pilnai uzpildyti, del to negalesite dalyvauti kaikuriuose balsavimuose");
			    
		   }
		   else{
			   name.setText("Sveiki "+ VartotojoDuomenys.getName());
		   }
		
		yourHistory.setText(R.string.balsavote_uz);
	}
	
	class GetBalsavimai extends AsyncTask<String, String, JSONArray> 
	{
		private static final String URL = "http://rinkimai2014.coxslot.com/webservisas/user_balsai.php";
		
		
		@Override
		protected JSONArray doInBackground(String... params) 
		{
			
			List<NameValuePair> params1 = new ArrayList<NameValuePair>();
			params1.add(new BasicNameValuePair("vart_id", VartotojoDuomenys.getUser_id()));
			istrauktiDuomenys = new JSONParser().makeHttpRequest(URL, "POST",params1);
			try {
				SQLiteCommandCenter.balsaiJsonToSqlite(istrauktiDuomenys);
				Log.d("$^%&*(", istrauktiDuomenys.getInt("success")+" ");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
			
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Tab_Me.this);
			pDialog.setMessage("Loading...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
	protected void onPostExecute(JSONArray result) {
		pDialog.dismiss();
		
	}
			
			
	
	}
	
	// adapterio sukurimas 
	  private class StableArrayAdapter extends ArrayAdapter<String> {

		    HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

		    public StableArrayAdapter(Context context, int textViewResourceId,
		        List<String> objects) {
		      super(context, textViewResourceId, objects);
		      for (int i = 0; i < objects.size(); ++i) {
		        mIdMap.put(objects.get(i), i);
		      }
		    }

		    @Override
		    public long getItemId(int position) {
		      String item = getItem(position);
		      return mIdMap.get(item);
		    }

		    @Override
		    public boolean hasStableIds() {
		      return true;
		    }

		  } 
	  @SuppressLint("ValidFragment")
	public class Input extends DialogFragment
	  {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {

			  AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			  
			  LayoutInflater inflater = getActivity().getLayoutInflater();
			  
			  View maView = inflater.inflate(R.layout.dialod_user_data_input, null);
			  
			   builder.setView(maView);
			  
//			   final EditText username = (EditText) maView.findViewById(R.id.username);
//			   username.setText(VartotojoDuomenys.getEmail());
//			   final EditText password = (EditText) maView.findViewById(R.id.password);
//			   password.setText(VartotojoDuomenys.getPassword());
//			   final EditText name = (EditText) maView.findViewById(R.id.name);
//			   name.setText(VartotojoDuomenys.getName());
//			   final EditText surename = (EditText) maView.findViewById(R.id.surename);
//			   surename.setText(VartotojoDuomenys.getSurename());
			   final EditText asmenskodas = (EditText) maView.findViewById(R.id.asmenskodas);
			   asmenskodas.setText(VartotojoDuomenys.getAsm_kod());
			   final EditText builetenio_nr = (EditText) maView.findViewById(R.id.builetenio_nr);
			   builetenio_nr.setText(VartotojoDuomenys.getBil_nr());
			   
			  builder.
			  setPositiveButton("Patvirtiniti", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
//					if(!username.getText().toString().isEmpty()){
//					VartotojoDuomenys.setEmail(username.getText().toString());
//					}
//					if(!password.getText().toString().isEmpty()){
//					VartotojoDuomenys.setPassword(password.getText().toString());
//					}
//					if(!name.getText().toString().isEmpty()){
//					VartotojoDuomenys.setName(name.getText().toString());
//					}
//					if(!surename.getText().toString().isEmpty()){
//					VartotojoDuomenys.setSurename(surename.getText().toString());
//					}
					if(!asmenskodas.getText().toString().isEmpty()){
					VartotojoDuomenys.setAsm_kod(asmenskodas.getText().toString());
					}
					if(!builetenio_nr.getText().toString().isEmpty()){
					VartotojoDuomenys.setBil_nr(builetenio_nr.getText().toString());
					}
					
					SQLiteCommandCenter.naujiVartotojoDuomenys();

						new PushToDb().execute();
				}
			}).setNegativeButton("Atsaukti", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Input.this.getDialog().cancel();
				}
			});
			  return builder.create();
		}
	  }
	  public class PushToDb extends AsyncTask<String, String, String>
	  {
			private static final String URL = "http://rinkimai2014.coxslot.com/webservisas/user_update.php";
		@Override
		protected String doInBackground(String... params) {
			if(NetworkStateListener.isInternetOn() && VartotojoDuomenys.arPiliDuomenys())
			{
			// Building Parameters
			List<NameValuePair> params1 = new ArrayList<NameValuePair>();
			params1.add(new BasicNameValuePair("user_id", VartotojoDuomenys.getUser_id()));
			params1.add(new BasicNameValuePair("asm_kod", VartotojoDuomenys.getAsm_kod()));
			params1.add(new BasicNameValuePair("bil_nr", VartotojoDuomenys.getBil_nr()));

			new JSONParser().makeHttpRequest(URL, "POST",params1);
			}
			return null;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			MainTabs.tabHost.getTabWidget().getChildAt(1).setEnabled(true);
		    
		}
		
	  }
}
