package rinkimai.pro;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class FragmentWebView extends Fragment {

	WebView webView;
	
	String [] links = {"http://rinkimai2014.coxslot.com/webservisas/prezidento_rinkimai.php","http://rinkimai2014.coxslot.com/webservisas/seimo_rinkimai.php","http://rinkimai2014.coxslot.com/webservisas/europarlamento_rinkimai.php"};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmentwebview, container, false);
		webView = (WebView) v.findViewById(R.id.webView1); //sukuriamas laukas, kuris atvaizduoja web puslapi.
		//webView.getSettings().setJavaScriptEnabled(true);
		WebSettings webSettings = webView.getSettings(); //igalina nustatymus WebView klasei.
		webSettings.setJavaScriptEnabled(true); //ijungia JavaScript.
		webView.setInitialScale(1); //sutalpina vaizda i ekrana.
		webSettings.setUseWideViewPort(true); //viewport support'as.
		webView.getSettings().setBuiltInZoomControls(true); //igalina priartinima.
        webView.loadUrl(links[0].toString());
        return v;
	}

	public void setNewPage(int i) 
	{
	//webView = (WebView) getView().findViewById(R.id.webView1);
		Log.d("webveiw", "called");
		webView.loadUrl(links[i].toString());
	}
}
