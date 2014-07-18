package rinkimai.pro;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class Vote extends Activity 
{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_vote);
		/*
		ArrayList<String> listux = new ArrayList<String>();
        ArrayAdapter<String> adaptas = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,listux);
        */
		
		ArrayAdapter<String> adaptas = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item);
		ListView sarasas = (ListView)findViewById(R.id.sarasas);
		
		sarasas.setAdapter(adaptas);
		
		for(int z = 0 ; z != 3 ; z++)
		{
			adaptas.add("Skaicius" + z);
		}
		
		sarasas.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				Toast.makeText(getApplicationContext(),String.valueOf( position + " pasirinkote" ), Toast.LENGTH_SHORT).show();
				
			}
		});
		
	}

	
}
