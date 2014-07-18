package rinkimai.pro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class FragmentSupport extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_support);
        Intent intent = getIntent();
        int index = intent.getIntExtra("index",0);
        FragmentWebView web = (FragmentWebView) getFragmentManager().findFragmentById(R.id.fragment2);
        if (web != null)
            web.setNewPage(index);

    }
}
