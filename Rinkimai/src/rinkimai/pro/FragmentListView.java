package rinkimai.pro;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FragmentListView extends Fragment implements AdapterView.OnItemClickListener
{
    ListView list;
	String [] websites = {"Prezidento Rinkimai","Seimo Rinkimai","Europarlamento Rinkimai"};
	
	
	OnSiteSelectedListener SiteListener;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragmentlistview, container, false);
        list = (ListView) v.findViewById(R.id.list);
        list.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_list_item_1,websites));
        list.setOnItemClickListener(this);
        return v;
	}

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
    {
        SiteListener.onSiteSelected(position);
    }

    // Container Activity must implement this interface
    public interface OnSiteSelectedListener {
        public void onSiteSelected(int i);
    }

    public void setRefrence(OnSiteSelectedListener siteListener)
    {
        this.SiteListener = siteListener;
    }

}

