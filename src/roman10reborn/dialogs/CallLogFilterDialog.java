package roman10reborn.dialogs;

import java.util.Calendar;

import roman10reborn.apl.main.R;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

public class CallLogFilterDialog extends Activity {
	private Spinner spinner_callType;
	//private TextView text_callType, text_startTime, text_endTime;
	private EditText edit_startTime, edit_endTime;
	private Button btn_startTime, btn_endTime;
	//private TextView text_name;
	private EditText edit_name;
	//private TextView text_number;
	private EditText edit_number;
	
	private Button btn_ok, btn_cancel, btn_save, btn_reset;
	
	private Context mContext;
	
	private static final String[] callTypeStrings = new String[] {
		"All", "Missed", "Incoming", "Outgoing" 
	};
	private static int l_filter_callType = 0;
	public static int FILTER_CALLTYPE_ALL = 0;
	public static int FILTER_CALLTYPE_MISSED = android.provider.CallLog.Calls.MISSED_TYPE;
	public static int FILTER_CALLTYPE_IN = android.provider.CallLog.Calls.INCOMING_TYPE;
	public static int FILTER_CALLTYPE_OUT = android.provider.CallLog.Calls.OUTGOING_TYPE;
	public static int filter_callType = FILTER_CALLTYPE_ALL;
	
	public static boolean ifStartTimeEnabled = false;
    public static int startYear = 0;
    public static int startMonth = 0;
    public static int startDay = 0;
    
    public static boolean ifEndTimeEnabled = false;
    public static int endYear = 0;
    public static int endMonth = 0;
    public static int endDay = 0;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getApplicationContext();
        //set up GUI 
        this.setContentView(R.layout.calllog_filter_dialog);
        this.setTitle("Specify a Phone Log Filter");
        	//spinner
        //text_callType = (TextView) findViewById(R.id.calllog_filter_dialog_text2);
        spinner_callType = (Spinner) findViewById(R.id.calllog_filter_dialog_spin1);
    	ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(mContext, android.R.layout.simple_spinner_item, callTypeStrings);
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	spinner_callType.setAdapter(adapter);
    	spinner_callType.setSelection(l_filter_callType);
    	spinner_callType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				l_filter_callType = (int) arg3;
				switch (l_filter_callType) {
				case 0:
					filter_callType = FILTER_CALLTYPE_ALL;
					break;
				case 1:
					filter_callType = FILTER_CALLTYPE_MISSED;
					break;
				case 2:
					filter_callType = FILTER_CALLTYPE_IN;
					break;
				case 3:
					filter_callType = FILTER_CALLTYPE_OUT;
					break;
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {	
			}
		});
    		//start time
    	//text_startTime = (TextView) findViewById(R.id.calllog_filter_dialog_text2);
    	edit_startTime = (EditText) findViewById(R.id.calllog_filter_dialog_edit2);
    	btn_startTime = (Button) findViewById(R.id.calllog_filter_dialog_btn_start_time);
    	btn_startTime.setText("Enable");
    	edit_startTime.setEnabled(false);
    	btn_startTime.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (ifStartTimeEnabled) {
					showDialog(DATE_DIALOG_ID1);
				} else {
					ifStartTimeEnabled = true;
					initStartTimeFields();
				}
			}
		});
    		//end time
    	//text_endTime = (TextView) findViewById(R.id.calllog_filter_dialog_text3);
    	edit_endTime = (EditText) findViewById(R.id.calllog_filter_dialog_edit3);
    	btn_endTime = (Button) findViewById(R.id.calllog_filter_dialog_btn_end_time);
        edit_endTime.setEnabled(false);
    	btn_endTime.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (ifEndTimeEnabled) {
					showDialog(DATE_DIALOG_ID2);
				} else {
					ifEndTimeEnabled = true;
					initEndTimeFields();
				}
			}
		});
        if (ifStartTimeEnabled) {
			initStartTimeFields();
		} else {
			resetStartTimeFields();
		}
		if (ifEndTimeEnabled) {
			initEndTimeFields();
		} else {
			resetEndTimeFields();
		}
        	//name
        edit_name = (EditText) findViewById(R.id.calllog_filter_dialog_edit4);
        edit_name.setInputType(InputType.TYPE_CLASS_TEXT);
        edit_name.setHint("Any, Long Press for Names");
        setNameEdit();
        edit_name.setOnLongClickListener(new View.OnLongClickListener() {
			public boolean onLongClick(View v) {
				//list out all names in the logs
				Intent l_intent = new Intent();
				l_intent.setClass(mContext, roman10reborn.dialogs.FilterNameListDialog.class);
				startActivityForResult(l_intent, REQUEST_FILTER_NAMES);
				return true;
			}
		});
        	//number
        edit_number = (EditText) findViewById(R.id.calllog_filter_dialog_edit5);
        edit_number.setHint("Any, Long Press for Num");
        setNumberEdit();
        edit_number.setInputType(InputType.TYPE_CLASS_PHONE);    
        edit_number.setOnLongClickListener(new View.OnLongClickListener() {
			public boolean onLongClick(View v) {
				//list out all numbers available
				Intent l_intent = new Intent();
				l_intent.setClass(mContext, roman10reborn.dialogs.FilterNumberListDialog.class);
				startActivityForResult(l_intent, REQUEST_FILTER_NUMBERS);
				return true;
			}
		});
        	//buttons
        btn_ok = (Button) findViewById(R.id.calllog_filter_dialog_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				setResult(RESULT_OK);
				finish();
			}
		});
        btn_cancel = (Button) findViewById(R.id.calllog_filter_dialog_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
        btn_save = (Button) findViewById(R.id.calllog_filter_dialog_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//save to short cut, pop up a window to explain the 
			}
		});
        btn_reset = (Button) findViewById(R.id.calllog_filter_dialog_reset);
        btn_reset.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//reset to default settings
				resetFilter();
			}
		});
        
        int screenWidth = this.getWindowManager().getDefaultDisplay().getWidth()/6*5;
		btn_ok.setWidth(screenWidth/4);
		btn_cancel.setWidth(screenWidth/4);
		btn_save.setWidth(screenWidth/4);
		btn_reset.setWidth(screenWidth/4);
	}
	
	private void resetFilter() {
		l_filter_callType = 0;
		filter_callType = FILTER_CALLTYPE_ALL;
		spinner_callType.setSelection(0);
		ifStartTimeEnabled = false;
		resetStartTimeFields();
		ifEndTimeEnabled = false;
		resetEndTimeFields();
		edit_name.setText("");
		FilterNameListDialog.selectedNamesList.clear();
		int l_size = FilterNameListDialog.ifSelected.size();
		for (int i = 0; i < l_size; ++i){
			FilterNameListDialog.ifSelected.set(i, false);
		}
		edit_number.setText("");
		l_size = FilterNumberListDialog.ifSelected.size();
		FilterNumberListDialog.selectedNumbersList.clear();
		for (int i = 0; i < l_size; ++i){
			FilterNameListDialog.ifSelected.set(i, false);
		}
	}
	
	private void resetStartTimeFields() {
		edit_startTime.setText("");
		edit_startTime.setKeyListener(null);
		btn_startTime.setText("Enable");
	}
	
	private void resetEndTimeFields() {
		edit_endTime.setText("");
		edit_endTime.setKeyListener(null);
		btn_endTime.setText("Enable");
	}
	
	private void initStartTimeFields() {
		if ((startYear == 0) && (startMonth == 0) && (startDay == 0)) {
			final Calendar c = Calendar.getInstance();
	    	startYear = c.get(Calendar.YEAR);
	        startMonth = c.get(Calendar.MONTH);
	        startDay = c.get(Calendar.DAY_OF_MONTH);
		} 
		edit_startTime.setKeyListener(null);
    	edit_startTime.setText( new StringBuilder()
        .append(startMonth + 1).append("-")
        .append(startDay).append("-")
        .append(startYear).append(" ")
        );
    	btn_startTime.setText("Set");
	}
	
	private void initEndTimeFields() {
		if ((endYear == 0) && (endMonth == 0) && (endDay == 0)) {
			final Calendar c = Calendar.getInstance();
	    	endYear = c.get(Calendar.YEAR);
	        endMonth = c.get(Calendar.MONTH);
	        endDay = c.get(Calendar.DAY_OF_MONTH);
		}
        edit_endTime.setKeyListener(null);
    	edit_endTime.setText( new StringBuilder()
    	                .append(endMonth + 1).append("-")
    	                .append(endDay).append("-")
    	                .append(endYear).append(" ")
    	                );
    	btn_endTime.setText("Set");
	}
	
    private static final int DATE_DIALOG_ID1 = 1;
    private static final int DATE_DIALOG_ID2 = 2;
	 @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
             case DATE_DIALOG_ID1:
                return new DatePickerDialog(this,
                		mDateSetListener1,
                            startYear, startMonth, startDay);
             case DATE_DIALOG_ID2:
                 return new DatePickerDialog(this,
                		 mDateSetListener2,
                             endYear, endMonth, endDay);
        }
        return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
            case DATE_DIALOG_ID1:
                ((DatePickerDialog) dialog).updateDate(startYear, startMonth, startDay);
                break;
            case DATE_DIALOG_ID2:
                ((DatePickerDialog) dialog).updateDate(endYear, endMonth, endDay);
                break;
        }
    }    

    private DatePickerDialog.OnDateSetListener mDateSetListener1 =
        new DatePickerDialog.OnDateSetListener() {
			public void onDateSet(DatePicker view, int year,
					int monthOfYear, int dayOfMonth) {
				startYear = year;
				startMonth = monthOfYear;
				startDay = dayOfMonth;
				updateDate1();
			}
        };
    private DatePickerDialog.OnDateSetListener mDateSetListener2 =
        new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear,
                    int dayOfMonth) {
                endYear = year;
                endMonth = monthOfYear;
                endDay = dayOfMonth;
                updateDate2();
            }
        };
        
    private void updateDate1() {
		edit_startTime.setText(
        new StringBuilder()
                // Month is 0 based so add 1
                .append(startMonth + 1).append("-")
                .append(startDay).append("-")
                .append(startYear).append(" ")
                );
    }
    
    private void updateDate2() {
		edit_endTime.setText(
        new StringBuilder()
                // Month is 0 based so add 1
                .append(endMonth + 1).append("-")
                .append(endDay).append("-")
                .append(endYear).append(" ")
                );
    }
    
    private void setNameEdit() {
    	int l_size = FilterNameListDialog.selectedNamesList.size();
		String l_text = "";
		if (l_size == 0) {
			//do nothing
		} else if (l_size == 1) {
			l_text = FilterNameListDialog.selectedNamesList.get(0);
		} 
		else {
			int j = 0;
			int l_cnt = FilterNameListDialog.selectedNamesList.size(); 
			for (; j <  l_cnt&& j < 3; ++j) {
				if (j != 0)
					l_text += ", ";
				l_text += FilterNameListDialog.selectedNamesList.get(j);
			}
			if (j < l_cnt)
				l_text += " ...";
		}
		edit_name.setText(l_text);

    }
    
    private void setNumberEdit() {
    	int l_size = FilterNumberListDialog.selectedNumbersList.size();
		String l_text = "";
		if (l_size == 0) {
			//do nothing
		} else if (l_size == 1) {
			l_text = FilterNumberListDialog.selectedNumbersList.get(0);
		} 
		else {
			int j = 0;
			int l_cnt = FilterNumberListDialog.selectedNumbersList.size(); 
			for (; j <  l_cnt&& j < 3; ++j) {
				if (j != 0)
					l_text += ", ";
				l_text += FilterNumberListDialog.selectedNumbersList.get(j);
			}
			if (j < l_cnt)
				l_text += " ...";
		}
		edit_number.setText(l_text);
    }
    
    private static final int REQUEST_FILTER_NAMES = 1;
    private static final int REQUEST_FILTER_NUMBERS = 2;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent i) {
    	super.onActivityResult(requestCode, resultCode, i);
    	switch (requestCode) {
    	case REQUEST_FILTER_NAMES:
    		if (resultCode == RESULT_OK) {
    			setNameEdit();
    		}
    		break;
    	case REQUEST_FILTER_NUMBERS:
    		if (resultCode == RESULT_OK) {
    			setNumberEdit();
    		}
    		break;
    	}
    	
	}
}
