package rinkimai.pro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.app.FragmentManager;

public class Tab_Stats extends Activity implements FragmentListView.OnSiteSelectedListener{

    FragmentWebView web;
    FragmentListView list;
    FragmentManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_stats);
     
        manager = getFragmentManager();
        list = (FragmentListView) manager.findFragmentById(R.id.fragment1);
        list.setRefrence(this);
        
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.layout.layout_home, menu);
        return true;
    }

    @Override
    public void onSiteSelected(int i) {

        web = (FragmentWebView) manager.findFragmentById(R.id.fragment2);
        // Check for landscape mode
        if (web!= null && web.isVisible())
        {
            web.setNewPage(i);
        }
        else
        {
            Intent intent = new Intent(this , FragmentSupport.class);
            intent.putExtra("index", i);
            startActivity(intent);
        }
    }

}
