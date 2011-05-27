package roman10reborn.dialogs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import roman10reborn.apl.data2.BooleanArrayList;
import roman10reborn.apl.main.Main;
import roman10reborn.apl.main.R;
import roman10reborn.utils.ConstantStatic;
import roman10reborn.utils.SortingUtilsStatic;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class FilterNameListDialog extends ListActivity {
	private Button btn_ok, btn_cancel, btn_all, btn_none;
	private ArrayList<String> namesList = new ArrayList<String>();
	
	public static BooleanArrayList ifSelected = new BooleanArrayList(50);
	public static ArrayList<String> selectedNamesList = new ArrayList<String>();
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		this.setContentView(R.layout.calllog_filter_name_list_dialog);
		this.setTitle("Names of Logs Shown");
		btn_ok = (Button) findViewById(R.id.calllog_filter_name_list_multi_btn_second);
		btn_ok.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				setResult(RESULT_OK);
				selectedNamesList.clear();
				int l_size = ifSelected.size();
				for (int i = 0; i < l_size; ++i) {
					if (ifSelected.get(i)) {
						selectedNamesList.add(namesList.get(i));
					}
				}
				finish();
			}
		});
		btn_cancel = (Button) findViewById(R.id.calllog_filter_name_list_multi_btn_third);
		btn_cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				selectedNamesList.clear();
				finish();
			}
		});
		btn_all = (Button) findViewById(R.id.calllog_filter_name_list_multi_btn_first);
		btn_all.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				int l_size = FilterNameListDialog.this.getListAdapter().getCount();
				for (int i = 0; i < l_size; ++i) {
					FilterNameListDialog.this.getListView().setItemChecked(i, true);
					ifSelected.set(i, true);
				}
			}
		});
		btn_none = (Button) findViewById(R.id.calllog_filter_name_list_multi_btn_forth);
		btn_none.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				int l_size = FilterNameListDialog.this.getListAdapter().getCount();
				for (int i = 0; i < l_size; ++i) {
					FilterNameListDialog.this.getListView().setItemChecked(i, false);
					ifSelected.set(i, false);
				}
				
			}
		});
		int screenWidth = this.getWindowManager().getDefaultDisplay().getWidth()/6*5;
		btn_ok.setWidth(screenWidth/4);
		btn_cancel.setWidth(screenWidth/4);
		btn_all.setWidth(screenWidth/4);
		btn_none.setWidth(screenWidth/4);
		//set up the list view
		namesList = getNameListFromCurrentLogs();
		setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, namesList));
        final ListView listView = getListView();
        listView.setItemsCanFocus(false);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        initChecked();
	}
	
	private void initChecked() {
		int l_size = ifSelected.size();
		for (int i = 0; i< l_size; ++i) {
			if (ifSelected.get(i)) {
				FilterNameListDialog.this.getListView().setItemChecked(i, true);
			}
		}
	}
	
	private ArrayList<String> getNameListFromCurrentLogs() {
		Set<String> l_set = new HashSet<String>();
		int l_size = Main.recordList.size();
		for (int i = 0; i < l_size; ++i) {
			String name = Main.recordList.get(i).aplr_name;
			l_set.add((name == null || name.compareTo("") == 0) ? ConstantStatic.UNKNOWN : name);
		}
		ArrayList<String> l_names = new ArrayList<String>(l_set);
		SortingUtilsStatic.sortStringAsc(l_names);
		//indicates all are not selected
		for (int i = 0; i < l_names.size(); ++i) {
			ifSelected.add(false);
		}
		return l_names;
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		//selectedNamesList.add(namesList.get(position));
		ifSelected.set(position, !ifSelected.get(position));
	}
}
