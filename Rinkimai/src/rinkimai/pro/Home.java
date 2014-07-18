package rinkimai.pro;

import java.util.ArrayList;
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
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Home extends Activity implements OnClickListener 
{
	private EditText email, pass;
	public static String thisUser;
	private Button mSubmit, mRegister;

	// Progress Dialog
	private ProgressDialog pDialog;

	// JSON parser class
	private final JSONParser jsonParser = new JSONParser();

	// php login script location:

	// testing on Emulator:
	private static final String LOGIN_URL = "http://rinkimai2014.coxslot.com/webservisas/login.php";
	private static final String GET_TO_MUCH_DATA = "http://rinkimai2014.coxslot.com/webservisas/vartotojai.php";
	// JSON element ids from repsonse of php script:
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";

	public final static NetworkStateListener networkStateListener = new NetworkStateListener();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_home);
		
		networkStateListener.startListening(getApplicationContext());
		
		SQLiteCommandCenter.initiateDb(getApplicationContext());	
		
		SQLiteCommandCenter.loadSqliteUserData();
		
		if(VartotojoDuomenys.isLoginNeeded())
		{
			// setup input fields
			email = (EditText) findViewById(R.id.username);
			pass = (EditText) findViewById(R.id.password);
	
			// setup buttons
			mSubmit = (Button) findViewById(R.id.login);
			mRegister = (Button) findViewById(R.id.register);
	
			// register listeners
			mSubmit.setOnClickListener(this);
			mRegister.setOnClickListener(this);
		}
		else {
			Intent i = new Intent(Home.this, MainTabs.class);
			finish();
			startActivity(i);
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.login:
			new AttemptLogin().execute();
			break;
		case R.id.register:
			Intent i = new Intent(this, Register.class);
			startActivity(i);
			break;

		default:
			break;
		}
	}

	class AttemptLogin extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Home.this);
			pDialog.setMessage("Attempting login...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			// Check for success tag
			int success;
			String username = email.getText().toString();
			String password = pass.getText().toString();
			if(NetworkStateListener.isInternetOn())
			{
			try {
				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("email", username));
				params.add(new BasicNameValuePair("password", password));

				Log.d("request!", "starting");
				// getting product details by making HTTP request
				JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST",
						params);

				// check your log for json response
				Log.d("Login attempt", json.toString());
//				Toast.makeText(getApplicationContext(), json.getString("user_id"), Toast.LENGTH_LONG).show();
				// json success tag
				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					Log.d("Login Successful!", json.toString());
					// save user data
					thisUser = username;

					VartotojoDuomenys.setEmail(username);
					VartotojoDuomenys.setPassword(password);
					SQLiteCommandCenter.loginDataToSql();
					
					// wtf ? --------------------------
					SharedPreferences sp = PreferenceManager
							.getDefaultSharedPreferences(Home.this);
					Editor edit = sp.edit();
					edit.putString("email", username);
					edit.commit();
					// --------------------------------
					Intent i = new Intent(Home.this, MainTabs.class);
					finish();
					startActivity(i);
					return json.getString(TAG_MESSAGE);
				} else {
					Log.d("Login Failure!", json.getString(TAG_MESSAGE));
					return json.getString(TAG_MESSAGE);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			}
			else {
				success = 1;
				Intent i = new Intent(Home.this, MainTabs.class);
				finish();
				startActivity(i);
			}
			return null;
		}

		protected void onPostExecute(String file_url) {
			// dismiss the dialog once product deleted
			pDialog.dismiss();
//			Toast.makeText(getApplicationContext(), Home.thisUser, Toast.LENGTH_LONG).show();
			if (file_url != null) {
				Toast.makeText(Home.this, file_url, Toast.LENGTH_LONG).show();
			}
		}
	}
}
