package roman10reborn.dialogs;

import roman10reborn.apl.main.R;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SingleChoiceForSorting extends ListActivity {
	private static final String[] sortingOptionStrings = new String[] {
		"Sort by Date Des", "Sort by Date Asc", "Sort by Name Des", "Sort by Name Asc",
		"Sort by Talk Time Des", "Sort by Talk Time Asc" 
	};
	
	public static final int SORT_DATE_DES = 1;
	public static final int SORT_DATE_ASC = 2;
	public static final int SORT_NAME_DES = 3;
	public static final int SORT_NAME_ASC = 4;
	public static final int SORT_DURATION_DES = 5;
	public static final int SORT_DURATION_ASC = 6;
	
	public static final String SORT_OPTION = "SORT_OPTION";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.single_choice_for_sorting);
        this.setTitle("Choose One Option");
        
        setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice, sortingOptionStrings));
        
        final ListView listView = getListView();

        listView.setItemsCanFocus(false);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id){
		super.onListItemClick(l, v, position, id);
		Intent l_intent = new Intent();
		l_intent.putExtra(SORT_OPTION, position + 1);
		setResult(RESULT_OK, l_intent);
		finish();
	}
}
