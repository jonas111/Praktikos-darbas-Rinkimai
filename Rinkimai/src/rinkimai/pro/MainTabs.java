package rinkimai.pro;

import java.util.ArrayList;

import android.R.color;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;



@SuppressWarnings("deprecation")
public class MainTabs extends TabActivity implements OnTabChangeListener
{

	/** Called when the activity is first created. */
    public static TabHost tabHost;
    
    // aplikacijai uzsidarant reik uzdaryt db
     @Override
    protected void onDestroy() {
    	super.onDestroy();
    	SQLiteCommandCenter.kill();
    }
     
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
         
        // Get TabHost Refference
        tabHost = getTabHost();
         
        // Set TabChangeListener called when tab changed
        tabHost.setOnTabChangedListener((OnTabChangeListener) this);
     
        TabHost.TabSpec spec;
        Intent intent;
   
         /************* TAB1 ************/
        // Create  Intents to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, Tab_Home.class);
        spec = tabHost.newTabSpec("Home").setIndicator("Home")
                      .setContent(intent);
         
        //Add intent to tab
        tabHost.addTab(spec);
   
        /************* TAB2 ************/
        intent = new Intent().setClass(this, Tab_Vote.class);
        spec = tabHost.newTabSpec("Vote").setIndicator("Vote")
                      .setContent(intent);  
        tabHost.addTab(spec);
   
        /************* TAB3 ************/
        intent = new Intent().setClass(this, Tab_Stats.class);
        spec = tabHost.newTabSpec("Stats").setIndicator("Stats")
                      .setContent(intent);
        tabHost.addTab(spec);
                
        /************* TAB5 ************/
        intent = new Intent().setClass(this, Tab_Me.class);
        spec = tabHost.newTabSpec("Info").setIndicator("Info")
                      .setContent(intent);
        tabHost.addTab(spec);
    
        // Set drawable images to tab
        tabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.main_tab_button1);
        tabHost.getTabWidget().getChildAt(2).setBackgroundResource(R.drawable.main_tab_button1);
        tabHost.getTabWidget().getChildAt(3).setBackgroundResource(R.drawable.main_tab_button1);
        //tabHost.getTabWidget().getChildAt(4).setBackgroundResource(color.white);
           
        // Set Tab1 as Default tab and change image   
        tabHost.getTabWidget().setCurrentTab(0);
        tabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.main_tab_button2);
         
   
     }
  @Override
  public void onTabChanged(String tabId) {
       
      /************ Called when tab changed *************/
       
      //********* Check current selected tab and change according images *******/
       
      for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
      {
          if(i==0)
              tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.main_tab_button1);
          else if(i==1){
        	 
        	  tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.main_tab_button1);
        	 
          }
          else if(i==2)
              tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.main_tab_button1);
          else if(i==3)
              tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.main_tab_button1);
      }
       
       
      Log.i("tabs", "CurrentTab: "+tabHost.getCurrentTab());
       
  if(tabHost.getCurrentTab()==0)
      tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundResource(R.drawable.main_tab_button2);
  else if(tabHost.getCurrentTab()==1)
      tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundResource(R.drawable.main_tab_button2);
  else if(tabHost.getCurrentTab()==2)
      tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundResource(R.drawable.main_tab_button2);
  else if(tabHost.getCurrentTab()==3)
      tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundResource(R.drawable.main_tab_button2);
  }


	
}
