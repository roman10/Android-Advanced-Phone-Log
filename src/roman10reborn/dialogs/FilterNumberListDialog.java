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

public class FilterNumberListDialog extends ListActivity {
	private Button btn_ok, btn_cancel, btn_all, btn_none;
	private ArrayList<String> numbersList = new ArrayList<String>();
	
	public static BooleanArrayList ifSelected = new BooleanArrayList(50);
	public static ArrayList<String> selectedNumbersList = new ArrayList<String>();
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		this.setContentView(R.layout.calllog_filter_number_list_dialog);
		this.setTitle("Numbers of Logs Shown");
		btn_ok = (Button) findViewById(R.id.calllog_filter_number_list_multi_btn_second);
		btn_ok.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				selectedNumbersList.clear();
				int l_size = ifSelected.size();
				for (int i = 0; i < l_size; ++i) {
					if (ifSelected.get(i)) {
						selectedNumbersList.add(numbersList.get(i));
					}
				}
				setResult(RESULT_OK);
				finish();
			}
		});
		btn_cancel = (Button) findViewById(R.id.calllog_filter_number_list_multi_btn_third);
		btn_cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				selectedNumbersList.clear();
				finish();
			}
		});
		btn_all = (Button) findViewById(R.id.calllog_filter_number_list_multi_btn_first);
		btn_all.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				int l_size = FilterNumberListDialog.this.getListAdapter().getCount();
				for (int i = 0; i < l_size; ++i) {
					ifSelected.set(i, true);
					FilterNumberListDialog.this.getListView().setItemChecked(i, true);
				}
			}
		});
		btn_none = (Button) findViewById(R.id.calllog_filter_number_list_multi_btn_forth);
		btn_none.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				int l_size = FilterNumberListDialog.this.getListAdapter().getCount();
				for (int i = 0; i < l_size; ++i) {
					ifSelected.set(i, false);
					FilterNumberListDialog.this.getListView().setItemChecked(i, false);
				}
			}
		});
		int screenWidth = this.getWindowManager().getDefaultDisplay().getWidth()/6*5;
		btn_ok.setWidth(screenWidth/4);
		btn_cancel.setWidth(screenWidth/4);
		btn_all.setWidth(screenWidth/4);
		btn_none.setWidth(screenWidth/4);
		//set up the list view
		numbersList = getNumberListFromCurrentLogs();
		setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, numbersList));
        final ListView listView = getListView();
        listView.setItemsCanFocus(false);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        initChecked();
	}
	
	private void initChecked() {
		int l_size = ifSelected.size();
		for (int i = 0; i< l_size; ++i) {
			if (ifSelected.get(i)) {
				FilterNumberListDialog.this.getListView().setItemChecked(i, true);
			}
		}
	}
	
	private ArrayList<String> getNumberListFromCurrentLogs() {
		Set<String> l_set = new HashSet<String>();
		int l_size = Main.recordList.size();
		for (int i = 0; i < l_size; ++i) {
			String number = Main.recordList.get(i).aplr_number;
			l_set.add(number.compareTo("-1")==0 ? ConstantStatic.NOTAVAIL : number);
		}
		ArrayList<String> l_numbers = new ArrayList<String>(l_set);
		SortingUtilsStatic.sortStringAsc(l_numbers);
		for (int i = 0; i < l_numbers.size(); ++i) {
			ifSelected.add(false);
		}
		return l_numbers;
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		ifSelected.set(position, !ifSelected.get(position));
	}

}
