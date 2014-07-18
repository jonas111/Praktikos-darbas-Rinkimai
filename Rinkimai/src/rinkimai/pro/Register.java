package rinkimai.pro;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends Activity implements OnClickListener{
	
	private EditText email, pass, name, surename;
	private Button  mRegister;
	
	 // Progress Dialog
    private ProgressDialog pDialog;
 
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
    
    //php register script
    
    
    //testing on Emulator:
    private static final String REGISTER_URL = "http://rinkimai2014.coxslot.com/webservisas/register.php";
       
    //ids
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		
		email = (EditText)findViewById(R.id.username);
		pass = (EditText)findViewById(R.id.password);
		name = (EditText)findViewById(R.id.name);
		surename = (EditText)findViewById(R.id.surename);
		

		mRegister = (Button)findViewById(R.id.register);
		mRegister.setAlpha((float)0.7);
		mRegister.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
				new CreateUser().execute();
		
	}
	
	class CreateUser extends AsyncTask<String, String, String> {

		
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Register.this);
            pDialog.setMessage("Creating User...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
		
		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			 // Check for success tag
            int success;
            String uemail = email.getText().toString();
            String password = pass.getText().toString();
            String uname = name.getText().toString();
            String usurename = surename.getText().toString();
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("email", uemail));
                params.add(new BasicNameValuePair("password", password));
                params.add(new BasicNameValuePair("name", uname));
                params.add(new BasicNameValuePair("sure_name", usurename));
 
                Log.d("request!", "starting");
                
                //Posting user data to script 
                JSONObject json = jsonParser.makeHttpRequest(
                       REGISTER_URL, "POST", params);
 
                // full json response
                Log.d("Registering attempt", json.toString());
 
                // json success element
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                	Log.d("User Created!", json.toString()); 
                	
                	VartotojoDuomenys.setEmail(uemail);
                	VartotojoDuomenys.setPassword(password);
                	VartotojoDuomenys.setName(uname);
                	VartotojoDuomenys.setSurename(usurename);
                	SQLiteCommandCenter.registerDataToSql();
                	
                	finish();
                	return json.getString(TAG_MESSAGE);
                }else{
                	Log.d("Registering Failure!", json.getString(TAG_MESSAGE));
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
            	Toast.makeText(Register.this, file_url, Toast.LENGTH_LONG).show();
            }
 
        }
		
	}
		 

}
