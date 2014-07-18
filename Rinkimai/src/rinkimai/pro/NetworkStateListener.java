package rinkimai.pro;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

public class NetworkStateListener {

	private static boolean isInternetOn;
	
	private ConnectivityReciever connectivityReciever;
	
	private boolean klausymasis;
	
	private Context context;
	
	public static boolean isInternetOn()
	{
		return isInternetOn;
	}
	public NetworkStateListener()
	{
		connectivityReciever = new ConnectivityReciever();
	}
	/* manau kad synchronized reikalinagas nes servisui naudojams threadas
		reikalingas intent filteris betkokiam servisui, ir uzregistruot ji contexte
	*/
	public synchronized void startListening(Context context) {
		
		IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(connectivityReciever, filter);
        this.context = context;
        klausymasis = true;
	}
	// tai ga lir nebus naudojama, bet bent jau zinosim kad galim sustabdyt
	public synchronized void stopListening()
	{
		context.unregisterReceiver(connectivityReciever);
		klausymasis = false;
	}
	// klase kuri atlieka klausyma
	private class ConnectivityReciever extends BroadcastReceiver
	{
		
		
		@Override
		public void onReceive(Context context, Intent intent) {

			if(klausymasis == false)
			{
				return;
			}
			// manau sis istraukimas galimas visalaika
			if(intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false))
			{
				isInternetOn = false;
			}
			else
			{
				isInternetOn = true;
			}
			
			if(isInternetOn)
			{
				
				// klausytojam perduot busena
			}
		}
		
	}
}
