package roman10reborn.dialogs;

import java.util.ArrayList;
import java.util.List;

import roman10reborn.apl.main.Main;
import roman10reborn.apl.main.R;
import roman10reborn.apl.main.R.color;
import roman10reborn.iconifiedlist.IconifiedText;
import roman10reborn.iconifiedlist.IconifiedTextListAdapter;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainChoiceDialog extends ListActivity {
	private static final String[] options = new String[] {
		"Delete this Log", "Delete All Logs of this Number", "Add to Calendar",
		"Schedule an Event","Copy Number to Clipboard"
	};
	public static final int OPTION_DELETE_ONE = 0;
	public static final int OPTION_DELETE_FROM_CONTACT = 1;
	public static final int OPTION_ADD_TO_CAL = 2;
	public static final int OPTION_SCH_CAL = 3;
	public static final int OPTION_COPY = 4;
	private List<IconifiedText> optionsList = new ArrayList<IconifiedText>();
	private Context mContext;
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main_choice_dialog);
        //this.getListView().setBackgroundColor(color.solid_white);
        mContext = this.getApplicationContext();
        //IconifiedTextListAdapter adapter = new IconifiedTextListAdapter(mContext);
        //convertOptionsToDisplayData();
        //adapter.setListItems(optionsList);
        //this.setListAdapter(adapter);
        setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice, options));
        
        final ListView listView = getListView();

        listView.setItemsCanFocus(false);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	}
	
	private void convertOptionsToDisplayData() {
		optionsList.clear();
		for (int i = 0; i < options.length; ++i) {
			IconifiedText oneOption = new IconifiedText(options[i]);
			optionsList.add(oneOption);
		}
	}
	
	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) 
    {    
		Intent l_intent = new Intent();
		l_intent.putExtra(Main.MAIN_OPTOINS_CHOICE, position);
		setResult(RESULT_OK, l_intent);
		finish();
    }
}
