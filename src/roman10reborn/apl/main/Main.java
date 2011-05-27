package roman10reborn.apl.main;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import roman10reborn.apl.data2.AdvancedPhoneLogRecord;
import roman10reborn.apl.main.R;
import roman10reborn.dialogs.CallLogFilterDialog;
import roman10reborn.dialogs.FilterNameListDialog;
import roman10reborn.dialogs.FilterNumberListDialog;
import roman10reborn.dialogs.MainChoiceDialog;
import roman10reborn.expandableiconifiedlist.ExpandableThreeLinesIconifiedText;
import roman10reborn.expandableiconifiedlist.ExpandableThreeLinesIconifiedTextChildView;
import roman10reborn.expandableiconifiedlist.ExpandableThreeLinesIconifiedTextGroupView;
import roman10reborn.expandableiconifiedlist.ExpandableThreeLinesIconifiedTextListAdapter;
import roman10reborn.quickactionwindow.ActionItem;
import roman10reborn.quickactionwindow.QuickAction;
import roman10reborn.quickactionwindow.QuickAction2;
import roman10reborn.threelinesiconifiedlist.ThreeLinesIconifiedText;
import roman10reborn.utils.ConstantStatic;
import roman10reborn.utils.ContactsHelperStatic;
import roman10reborn.utils.SortingUtilsStatic;
import roman10reborn.utils.TimeUtilsStatic;
import android.R.color;
import android.app.AlertDialog;
import android.app.ExpandableListActivity;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.ClipboardManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.PopupWindow.OnDismissListener;

public class Main extends ExpandableListActivity implements ListView.OnScrollListener {
	private static final String TAG = "Main";
    private Button Btn_shortcut;
    private Button Btn_sort;
    private Button Btn_filter;
    private Button Btn_delete;
    private Context mContext;
    private TextView text_total, text_empty;
    
    private int lastListViewPos = 0;
    
    public static final int SHORTCUT_ALL = 0;
    public static final int SHORTCUT_ALL_MISSED = 1;
    public static final int SHORTCUT_ALL_INCOMING = 2;
    public static final int SHORTCUT_ALL_OUTGOING = 3;
    public static final int SHORTCUT_ALL_CURRENT_MONTH = 4;
    public static final int SHORTCUT_ALL_PREVIOUS_MONTH = 5;
    private int last_shortcut_option = SHORTCUT_ALL;
    
    public static final int SORT_DATE_DES = 0;
	public static final int SORT_DATE_ASC = 1;
	public static final int SORT_NAME_DES = 2;
	public static final int SORT_NAME_ASC = 3;
	public static final int SORT_DURATION_DES = 4;
	public static final int SORT_DURATION_ASC = 5;
	private int last_sort_option = SORT_DATE_DES;
	
	private static final int REQUEST_FILTER_OPTION = 1;
	private static final int REQUEST_ADD_TO_CONTACT = 2;
	private static final int REQUEST_MAIN_OPTIONS = 3;
	public static final String MAIN_OPTOINS_CHOICE = "main_option_result";
    
	public static int total_missCallNum = 0, total_outCallNum = 0, total_inCallNum = 0, total_outCallTime = 0, total_inCallTime = 0;
    
	//for making phone call and send out sms
	public static String currentPhoneNumber = "";
	public static String currentName = "";
	
	private static int sdk;
	
	private static int isFiltered = 0;
	
	public static Main self;
	
	public static ArrayList<AdvancedPhoneLogRecord> recordList = new ArrayList<AdvancedPhoneLogRecord>();
    private static ArrayList<AdvancedPhoneLogRecord> filteredList = new ArrayList<AdvancedPhoneLogRecord>();
    //private ArrayList<ThreeLinesIconifiedText> viewList = new ArrayList<ThreeLinesIconifiedText>();
    private ArrayList<ExpandableThreeLinesIconifiedText> viewGroupList = new ArrayList<ExpandableThreeLinesIconifiedText>();    
    private ArrayList<ArrayList<ExpandableThreeLinesIconifiedText>> viewList = new ArrayList<ArrayList<ExpandableThreeLinesIconifiedText>>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getApplicationContext();
        self = this;
        sdk = Integer.parseInt(Build.VERSION.SDK);
        //start background service
        //startAdvancedPhoneLogService();
        setContentView(R.layout.main);
    	text_total = (TextView) findViewById(R.id.apl_main_total_text);
    	//text_total.setBackgroundResource(android.R.drawable.title_bar);
    	text_total.setBackgroundColor(color.darker_gray);
    	text_total.setTextColor(Color.WHITE);
    	text_total.setText("Advanced Phone Log");
    	
        //set up button events and UI
        int screenWidth = this.getWindowManager().getDefaultDisplay().getWidth();
        Btn_shortcut = (Button) findViewById(R.id.apl_main_multi_btn_first);
        Btn_shortcut.setWidth(screenWidth/4);
        //Btn_shortcut.setBackgroundColor(R.color.custom_gray);
        Btn_shortcut.setOnClickListener(new View.OnClickListener() {	
			public void onClick(View v) {
				final CharSequence[] SHORTCUT_OPTIONS = {"All Logs", "All Missed Call",
						"All Incoming", "All Outgoing", "Current Month Logs", 
						"Previous Month Logs"
						};
				AlertDialog.Builder l_builder = new AlertDialog.Builder(Main.this);
				l_builder.setTitle("Choose a Shortcut");
				l_builder.setSingleChoiceItems(SHORTCUT_OPTIONS, last_shortcut_option, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						boolean l_typeFilter = false, l_startTimeFilter = false;
						boolean l_endTimeFilter = false;;
						boolean l_nameFilter = false, l_numberFilter = false;
						switch (which) {
						case SHORTCUT_ALL:
							isFiltered = 0;
							break;
						case SHORTCUT_ALL_MISSED:
							l_typeFilter = true;
							CallLogFilterDialog.filter_callType = CallLogFilterDialog.FILTER_CALLTYPE_MISSED;
							isFiltered = 1;
							break;
						case SHORTCUT_ALL_INCOMING:
							l_typeFilter = true;
							CallLogFilterDialog.filter_callType = CallLogFilterDialog.FILTER_CALLTYPE_IN;
							isFiltered = 1;
							break;
						case SHORTCUT_ALL_OUTGOING:
							l_typeFilter = true;
							CallLogFilterDialog.filter_callType = CallLogFilterDialog.FILTER_CALLTYPE_OUT;
							isFiltered = 1;
							break;
						case SHORTCUT_ALL_CURRENT_MONTH:
							l_startTimeFilter = true;
							final Calendar c = Calendar.getInstance();
					    	CallLogFilterDialog.startYear = c.get(Calendar.YEAR);
					    	CallLogFilterDialog.startMonth = c.get(Calendar.MONTH);
					    	CallLogFilterDialog.startDay = c.getActualMinimum(Calendar.DATE);
							isFiltered = 1;
							break;
						case SHORTCUT_ALL_PREVIOUS_MONTH:
							l_startTimeFilter = true;
							final Calendar l_c = Calendar.getInstance();
							CallLogFilterDialog.startMonth = l_c.get(Calendar.MONTH) - 1;
							if (CallLogFilterDialog.startMonth < 0) {
								CallLogFilterDialog.startMonth = 11;
								CallLogFilterDialog.startYear = l_c.get(Calendar.YEAR) - 1;
							} else {
								CallLogFilterDialog.startYear = l_c.get(Calendar.YEAR);
							}
					    	CallLogFilterDialog.startDay = l_c.getActualMinimum(Calendar.DATE);
							l_endTimeFilter = true;
							CallLogFilterDialog.endMonth = l_c.get(Calendar.MONTH) - 1;
							if (CallLogFilterDialog.endMonth < 0) {
								CallLogFilterDialog.endMonth = 11;
								CallLogFilterDialog.endYear = l_c.get(Calendar.YEAR) - 1;
							} else {
								CallLogFilterDialog.endYear = l_c.get(Calendar.YEAR);
							}
					    	CallLogFilterDialog.endDay = l_c.getActualMaximum(Calendar.DATE);
					    	isFiltered = 1;
							break;
						default:
							isFiltered = 0;
							break;
						}
						last_shortcut_option = which;
						dialog.dismiss();
						if (isFiltered == 0) {
		    				convertRecordToExpandableDisplayData(recordList);
		    			} else {
		    				filteredList = filterPhoneLogs(recordList, l_typeFilter, l_startTimeFilter,
		    						l_endTimeFilter, l_nameFilter, l_numberFilter);
		    				convertRecordToExpandableDisplayData(filteredList);
		    			}
						refreshUI();
					}
				});
				AlertDialog l_shortcut = l_builder.create();
				l_shortcut.show();
			}
		});
        Btn_sort = (Button) findViewById(R.id.apl_main_multi_btn_second);
        Btn_sort.setWidth(screenWidth/4);
        Btn_sort.setOnClickListener(new View.OnClickListener() {	
			public void onClick(View v) {
				final CharSequence[] SORT_OPTIONS = {"Sort by Date Des", "Sort by Date Asc", "Sort by Name Des", "Sort by Name Asc",
						"Sort by Talk Time Des", "Sort by Talk Time Asc" };
				AlertDialog.Builder l_builder = new AlertDialog.Builder(Main.this);
				l_builder.setTitle("Choose an Option");
				l_builder.setSingleChoiceItems(SORT_OPTIONS, last_sort_option, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
		    			switch (which) {
		    			case SORT_DATE_DES:
		    				if (isFiltered == 0) {
		    					SortingUtilsStatic.sortRecordsByDateDesc(recordList);
		    				} else {
		    					SortingUtilsStatic.sortRecordsByDateDesc(filteredList);
		    				}
		    				break;
		    			case SORT_DATE_ASC:
		    				if (isFiltered==0) {
		    					SortingUtilsStatic.sortRecordsByDateAsc(recordList);
		    				} else {
		    					SortingUtilsStatic.sortRecordsByDateAsc(filteredList);
		    				}
		    				break;
		    			case SORT_NAME_DES:
		    				if (isFiltered==0) {
		    					SortingUtilsStatic.sortRecordsByNameDesc(recordList);
		    				} else {
		    					SortingUtilsStatic.sortRecordsByNameDesc(filteredList);
		    				}
		    				break;
		    			case SORT_NAME_ASC:
		    				if (isFiltered==0) {
		    					SortingUtilsStatic.sortRecordsByNameAsc(recordList);
		    				} else {
		    					SortingUtilsStatic.sortRecordsByNameAsc(filteredList);
		    				}
		    				break;
		    			case SORT_DURATION_DES:
		    				if (isFiltered==0) {
		    					SortingUtilsStatic.sortRecordsByDurationDesc(recordList);
		    				} else {
		    					SortingUtilsStatic.sortRecordsByDurationDesc(filteredList);
		    				}
		    				break;
		    			case SORT_DURATION_ASC:
		    				if (isFiltered==0) {
		    					SortingUtilsStatic.sortRecordsByDurationAsc(recordList);
		    				} else {
		    					SortingUtilsStatic.sortRecordsByDurationAsc(filteredList);
		    				}
		    				break;
		    			default:
		    				if (isFiltered==0) {
		    					SortingUtilsStatic.sortRecordsByDateDesc(recordList);
		    				} else {
		    					SortingUtilsStatic.sortRecordsByDateDesc(filteredList);
		    				}
		    				break;
		    			}
		    			last_sort_option = which;
		    			dialog.dismiss(); 
		    			//viewList = convertRecordToDisplayData(recordList);
		    			if (isFiltered == 0) {
		    				convertRecordToExpandableDisplayData(recordList);
		    			} else {
		    				convertRecordToExpandableDisplayData(filteredList);
		    			}
		    	        refreshUI();
		    		}
				});
				AlertDialog l_option = l_builder.create();
				l_option.show();
			}
		});
        Btn_filter = (Button) findViewById(R.id.apl_main_multi_btn_third);
        Btn_filter.setWidth(screenWidth/4);
        Btn_filter.setOnClickListener(new View.OnClickListener() {	
			public void onClick(View v) {
				Intent l_intent = new Intent();
				l_intent.setClass(mContext, roman10reborn.dialogs.CallLogFilterDialog.class);
				startActivityForResult(l_intent, REQUEST_FILTER_OPTION);
			}
		});
        Btn_delete = (Button) findViewById(R.id.apl_main_multi_btn_forth);
        Btn_delete.setWidth(screenWidth/4);
        Btn_delete.setOnClickListener(new View.OnClickListener() {	
			public void onClick(View v) {
				//allow more options
				createAdditionalMenu(v);
			}
		});
        
        Btn_shortcut.setVisibility(View.GONE);
	    Btn_sort.setVisibility(View.GONE);
	    Btn_filter.setVisibility(View.GONE);
	    Btn_delete.setVisibility(View.GONE);
	    
        this.getExpandableListView().setOnGroupClickListener(new OnGroupClickListener(){
			public boolean onGroupClick(ExpandableListView arg0, View arg1,
					int groupPosition, long arg3) {
				groupOrChildClick = 0;
				currentGroupPos = groupPosition;
				if (getExpandableListAdapter().getChildrenCount(groupPosition)==0) {
					askForAnOption();
				}
//				if (getExpandableListAdapter().getChildrenCount(groupPosition)==0) {
//		    		currentPhoneNumber = viewGroupList.get(groupPosition).getTextLine2();
//		    		currentName = viewGroupList.get(groupPosition).getTextLine1Text1();
//		    		if ((currentPhoneNumber!=null) && (currentPhoneNumber.compareTo("-1")!=0) && (!currentPhoneNumber.contains(ConstantStatic.NOTAVAIL))) {
//		    			popupQuickAction(arg1);
//		    		}
//		    	} 
				return false;
			}
			
		});
        
        this.getExpandableListView().setLongClickable(true);
        this.getExpandableListView().setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				//askForAnOption();
				return false;
			}
        });
        
        this.getExpandableListView().setOnGroupExpandListener(new OnGroupExpandListener() {
			public void onGroupExpand(int groupPosition) {
				//
				
			}
        });
        
        this.getExpandableListView().setOnGroupCollapseListener(new OnGroupCollapseListener() {
			public void onGroupCollapse(int groupPosition) {
				//
				
			}
        });
        
        recordList = getAllCallLogs();
        //viewList = convertRecordToDisplayData(recordList);
        isFiltered = 0;
        convertRecordToExpandableDisplayData(recordList);
        refreshUI();
    }
    
    private void askForAnOption() {
    	Intent l_intent = new Intent();
    	l_intent.setClass(mContext, roman10reborn.dialogs.MainChoiceDialog.class);
    	startActivityForResult(l_intent, REQUEST_MAIN_OPTIONS);
    }
    
    private void refreshData() {
    	recordList = getAllCallLogs();
    	isFiltered = 0;
    	convertRecordToExpandableDisplayData(recordList);
    }
    
    private void refreshUI() {
    	//set up the list view 
        //ThreeLinesIconifiedTextListAdapter adapter = new ThreeLinesIconifiedTextListAdapter(this);
        //ExpandableThreeLinesIconifiedTextListAdapter adapter = new ExpandableThreeLinesIconifiedTextListAdapter(this);
        SlowAdapter adapter = new SlowAdapter(this);
    	StringBuilder l_builder = new StringBuilder();
        //l_builder.append("Advanced Phone Log\n");
        l_builder.append("Miss: ").append(total_missCallNum).append("; ");
        l_builder.append("Out: ").append(total_outCallNum).append("; ");
        l_builder.append("In: ").append(total_inCallNum).append("\n");
        String l_talkTime = String.format("%02d:%02d:%02d", total_outCallTime/3600, (total_outCallTime%3600)/60,(total_outCallTime%3600)%60);
        l_builder.append("OT: ").append(l_talkTime).append("; ");
        l_talkTime = String.format("%02d:%02d:%02d", total_inCallTime/3600, 
        		(total_inCallTime%3600)/60,(total_inCallTime%3600)%60);
        l_builder.append("IT: ").append(l_talkTime).append("; ");
        l_talkTime = String.format("%02d:%02d:%02d", 
        		(total_inCallTime+total_outCallTime)/3600, 
        		((total_inCallTime+total_outCallTime)%3600)/60,
        		((total_inCallTime+total_outCallTime)%3600)%60);
        l_builder.append("TT: ").append(l_talkTime).append("\n");
        
        text_total.setText(l_builder.toString());
        
        text_empty = (TextView) findViewById(R.id.apl_main_empty_text);
        if ((viewList.size()==0) && (viewGroupList.size()==0)) {
        	text_empty.setTextColor(Color.WHITE);
        	text_empty.setVisibility(View.VISIBLE);
        	text_empty.setText("Phone Log is Empty!");
        } else {
        	text_empty.setVisibility(View.GONE);
        }
        
        adapter.setListItems(viewList);
        adapter.setGroups(viewGroupList);
        if (viewGroupList.size() > 30) {
        	getExpandableListView().setFastScrollEnabled(true);
        } else {
        	getExpandableListView().setFastScrollEnabled(false);
        }
        this.getExpandableListView().setGroupIndicator(null);
        this.setListAdapter(adapter);
        this.getExpandableListView().setOnScrollListener(this);
        if (lastListViewPos < adapter.getGroupCount()) {
        	this.getExpandableListView().setSelection(lastListViewPos);
        } else {
        	this.getExpandableListView().setSelection(adapter.getGroupCount()-1);
        }
    }
  
    
    public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		lastListViewPos = firstVisibleItem;
	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {
		switch (scrollState) {
		case OnScrollListener.SCROLL_STATE_IDLE:
			int l_first = view.getFirstVisiblePosition();
			int l_count = view.getChildCount();
			for (int i = 0; i < l_count; ++i) {
				AttachedTag l_at = (AttachedTag)view.getChildAt(i).getTag();
				if (l_at.groupOrChild==0) {
					ExpandableThreeLinesIconifiedTextGroupView l_groupView = 
						(ExpandableThreeLinesIconifiedTextGroupView)view.getChildAt(i);
					if (l_at.photoLoaded==0) {
						Drawable l_photo = getPhotoDrawable(viewGroupList.get(l_first+i).getTextLine2());
						//l_groupView.getContent().setLeftIcon(l_photo);
						viewGroupList.get(l_first+i).setLeftIcon(l_photo);
						int l_size = viewList.get(l_first+i).size();
						for (int j = 0; j < l_size; ++j) {
							viewList.get(l_first+i).get(j).setLeftIcon(l_photo);
						}
						l_groupView.setLeftIcon(l_photo);
						l_at.photoLoaded = 1;
						l_groupView.setTag(l_at);
					}
				} 
			}
			break;
		case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
			break;
		case OnScrollListener.SCROLL_STATE_FLING:
			break;
		}
	}
	
	private class AttachedTag {
		public int groupOrChild;	//0 for group, 1 for child
		public int photoLoaded;
		public AttachedTag(int _groupOrChild, int _photoLoaded){
			groupOrChild = _groupOrChild;
			photoLoaded = _photoLoaded;
		}
	}
	
    /**
     * 
     * SlowAdapter
     */
    /**
     * Will not bind views while the list is scrolling
     * 
     */
    private class SlowAdapter extends BaseExpandableListAdapter {
    	private Context mContext;

    	private ArrayList<ArrayList<ExpandableThreeLinesIconifiedText>> mItems = new ArrayList<ArrayList<ExpandableThreeLinesIconifiedText>>();
    	private ArrayList<ExpandableThreeLinesIconifiedText> mGroups = new ArrayList<ExpandableThreeLinesIconifiedText>();
    	
    	public SlowAdapter(Context context) {
    		mContext = context;
    	}

    	public void setListItems(ArrayList<ArrayList<ExpandableThreeLinesIconifiedText>> lit) 
    	{ mItems = lit; }
    	
    	public void setGroups(ArrayList<ExpandableThreeLinesIconifiedText> _groups) {
    		mGroups = _groups;
    	}


    	public Object getChild(int arg0, int arg1) {
    		return mItems.get(arg0).get(arg1);
    	}

    	public long getChildId(int arg0, int arg1) {
    		long id = 0;
    		for (int i = 0; i < arg0; ++i) {
    			id += mItems.get(i).size();
    		}
    		return id + arg1;
    	}

    	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
    			ViewGroup parent) {
    		ExpandableThreeLinesIconifiedTextChildView btv;
    		int l_photoLoaded = 0;
    		if (convertView == null) {
    			btv = new ExpandableThreeLinesIconifiedTextChildView(mContext, mItems.get(groupPosition).get(childPosition));
    		} else { //Reuse/Overwrite the View passed
    			// We are assuming(!) that it is castable! 
    			btv = (ExpandableThreeLinesIconifiedTextChildView) convertView;		
    			btv.setTextLine1Text1(mItems.get(groupPosition).get(childPosition).getTextLine1Text1());
    			btv.setTextLine1Text2("");
    			btv.setTextLine2(mItems.get(groupPosition).get(childPosition).getTextLine2());
    			/*set week of day as bold to separate the time*/
    			SpannableStringBuilder lSpanStr = new SpannableStringBuilder(mItems.get(groupPosition).get(childPosition).getTextLine3());
    			lSpanStr.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 10, 13, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
    			btv.setTextLine3(lSpanStr, TextView.BufferType.SPANNABLE);
    			
    			btv.setLeftIconText(mItems.get(groupPosition).get(childPosition).getLeftIconText());
    			btv.setRightIconText(mItems.get(groupPosition).get(childPosition).getRightIconText());
    			btv.setRightIcon(mItems.get(groupPosition).get(childPosition).getRightIcon());
    		}
    		btv.mRightIcon.setTag(groupPosition + " " + childPosition);
    		btv.mRightIcon.setOnClickListener(new View.OnClickListener() {
    			public void onClick(View v) {
    				//call the number
    				String l_pos = (String)v.getTag();
    				int l_emptyPos = l_pos.indexOf(" ");
    				int l_groupPos = Integer.parseInt(l_pos.substring(0, l_emptyPos));
    				int l_childPos = Integer.parseInt(l_pos.substring(l_emptyPos + 1, l_pos.length()));
    				makeACall(mItems.get(l_groupPos).get(l_childPos).getTextLine2());
    			}
    		});
    		Drawable l_leftIcon = mItems.get(groupPosition).get(childPosition).getLeftIcon();
    		if (l_leftIcon==null) {
    			btv.setLeftIcon(R.drawable.ic_contact_list_picture);
    			l_photoLoaded = 0;
    		} else {
    			btv.setLeftIcon(l_leftIcon);
    			l_photoLoaded = 1;
    		}
    		btv.mLeftIcon.setTag(groupPosition + " " + childPosition);
	    	btv.mLeftIcon.setOnClickListener(new View.OnClickListener() {
	    			public void onClick(View v) {
	    				String l_pos = (String)v.getTag();
	    				int l_emptyPos = l_pos.indexOf(" ");
	    				int l_groupPos = Integer.parseInt(l_pos.substring(0, l_emptyPos));
	    				int l_childPos = Integer.parseInt(l_pos.substring(l_emptyPos + 1, l_pos.length()));
	    				Main.currentPhoneNumber = mItems.get(l_groupPos).get(l_childPos).getTextLine2();
	    		    	Main.currentName = mGroups.get(l_groupPos).getTextLine1Text1();
	    				if ((Main.currentPhoneNumber!=null) && (Main.currentPhoneNumber.compareTo("-1")!=0) && (!Main.currentPhoneNumber.contains(ConstantStatic.NOTAVAIL))) {
	    					popupQuickAction(v);
	    				}
	    			}
	    		});
    		btv.setTag(new AttachedTag(1, l_photoLoaded));
    		return btv;
    	}

    	public int getChildrenCount(int groupPosition) {
    		return mItems.get(groupPosition).size();
    	}

    	public Object getGroup(int groupPosition) {
    		return mItems.get(groupPosition);
    	}

    	public int getGroupCount() {
    		return mItems.size();
    	}

    	public long getGroupId(int groupPosition) {
    		return groupPosition;
    	}

    	public View getGroupView(int groupPosition, boolean isExpanded,
    			View convertView, ViewGroup parent) {
    		ExpandableThreeLinesIconifiedTextGroupView btv;
    		int l_photoLoaded = 0;
    		if (convertView == null) {
    			btv = new ExpandableThreeLinesIconifiedTextGroupView(mContext, mGroups.get(groupPosition));
    			if(getChildrenCount(groupPosition)==0) {
    				btv.setFillerWidth(1);
    			} else {
    				btv.setFillerWidth(2);
    			}
    		} else { //Reuse/Overwrite the View passed
    			// We are assuming(!) that it is castable! 
    			btv = (ExpandableThreeLinesIconifiedTextGroupView) convertView;
    			btv.setTextLine1Text1(mGroups.get(groupPosition).getTextLine1Text1());
    			btv.setTextLine1Text2(mGroups.get(groupPosition).getTextLine1Text2());
    			btv.setTextLine2(mGroups.get(groupPosition).getTextLine2());
    			
    			SpannableStringBuilder lSpanStr = new SpannableStringBuilder(mGroups.get(groupPosition).getTextLine3());
    			lSpanStr.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 10, 13, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
    			btv.setTextLine3(lSpanStr, TextView.BufferType.SPANNABLE);
    			
    			//btv.setTextLine3(mGroups.get(groupPosition).getTextLine3());
    			//btv.setLeftIconText(mGroups.get(groupPosition).getLeftIconText());
    			btv.setRightIconText(mGroups.get(groupPosition).getRightIconText());
    			if (isExpanded) {
    				btv.setRightIcon(mGroups.get(groupPosition).getRightIcon2());
    			} else {
    				btv.setRightIcon(mGroups.get(groupPosition).getRightIcon1());
    			}
    			if(getChildrenCount(groupPosition)==0) {
    				btv.setFillerWidth(1);
    			} else {
    				btv.setFillerWidth(2);
    			}
    		}
    		btv.mRightIcon.setTag(groupPosition);
    		btv.mRightIcon.setOnClickListener(new View.OnClickListener() {
    			public void onClick(View v) {
    				//call the number
    				int l_groupPos = (Integer)v.getTag();
    				makeACall(mGroups.get(l_groupPos).getTextLine2());
    			}
    		});
        	//photo is not loaded yet
    		Drawable l_leftIcon = mGroups.get(groupPosition).getLeftIcon();
    		if (l_leftIcon==null) {
    			btv.setLeftIcon(R.drawable.ic_contact_list_picture);
    			l_photoLoaded = 0;
    		} else {
    			btv.setLeftIcon(l_leftIcon);
    			l_photoLoaded = 1;
    		}
    		btv.mLeftIcon.setTag(groupPosition);
        	btv.mLeftIcon.setOnClickListener(new View.OnClickListener() {
        			public void onClick(View v) {
        				int l_groupPos = (Integer)v.getTag();
        				Main.currentPhoneNumber = mGroups.get(l_groupPos).getTextLine2();
        		    	Main.currentName = mGroups.get(l_groupPos).getTextLine1Text1();
        				if ((Main.currentPhoneNumber!=null) && (Main.currentPhoneNumber.compareTo("-1")!=0) && (!Main.currentPhoneNumber.contains(ConstantStatic.NOTAVAIL))) {
        					popupQuickAction(v);
        				}
        			}
        		});
    		btv.setTag(new AttachedTag(0, l_photoLoaded));
    		return btv;
    	}
    	
    	private void popupQuickAction(View v) {
        	final QuickAction mQuickAction = new QuickAction(v);
        	final ActionItem phoneAction = new ActionItem();
    		phoneAction.setTitle("Call");
    		phoneAction.setIcon(mContext.getResources().getDrawable(R.drawable.phonecall1));
    		final ActionItem smsAction = new ActionItem();
    		smsAction.setTitle("SMS");
    		smsAction.setIcon(mContext.getResources().getDrawable(R.drawable.sms1));
    		final ActionItem contactAction = new ActionItem();
    		if (!Main.currentName.contains(ConstantStatic.UNKNOWN)) {
    			contactAction.setTitle("Contact");
    			contactAction.setIcon(mContext.getResources().getDrawable(R.drawable.viewcontact2));
    			contactAction.setOnClickListener(new View.OnClickListener() {
    				public void onClick(View v) {
    					//view contact			
    					viewContact(Main.currentPhoneNumber);
    					mQuickAction.dismiss();
    				}
    			});
    		} else {
    			contactAction.setTitle("Contact");
    			contactAction.setIcon(mContext.getResources().getDrawable(R.drawable.addcontact));
    			contactAction.setOnClickListener(new View.OnClickListener() {
    				public void onClick(View v) {
    					//add phone number to contact
    					Main.addToContact(Main.currentPhoneNumber);
    					mQuickAction.dismiss();
    				}
    			});
    		}
    		
    		phoneAction.setOnClickListener(new View.OnClickListener() {
    			public void onClick(View v) {
    				Main.makeAPhoneCall(Main.currentPhoneNumber);
    				mQuickAction.dismiss();
    			}
    		});
    		smsAction.setOnClickListener(new View.OnClickListener() {
    			public void onClick(View v) {
    				Main.sendOutSms(Main.currentPhoneNumber);
    				mQuickAction.dismiss();
    			}
    		});
    		mQuickAction.addActionItem(phoneAction);
    		mQuickAction.addActionItem(smsAction);
    		if (Integer.parseInt(Build.VERSION.SDK) >= Build.VERSION_CODES.ECLAIR) {
    			mQuickAction.addActionItem(contactAction);
    		}
    		//mQuickAction.addActionItem(deleteAction);
    		mQuickAction.setAnimStyle(QuickAction.ANIM_GROW_FROM_RIGHT);
    		mQuickAction.setOnDismissListener(new OnDismissListener(){
    			public void onDismiss() {
    				//do nothing
    			}
    		});
    		mQuickAction.show();
        }
    	
    	//sdk dependent
        private void viewContact(String _number) {
        	ContactAccessor.getInstance().viewContact(mContext, _number);
        }
    	
    	private void makeACall(String _number) {
    	    try {
    	        Intent callIntent = new Intent(Intent.ACTION_CALL);
    	        callIntent.setData(Uri.parse("tel:" + _number));
    	        mContext.startActivity(callIntent);
    	    } catch (ActivityNotFoundException e) {
    	        //
    	    }
    	}

    	public boolean hasStableIds() {
    		return true;
    	}

    	public boolean isChildSelectable(int groupPosition, int childPosition) {
    		return mItems.get(groupPosition).get(childPosition).isSelectable();
    	}
    }
    
    
    private void createAdditionalMenu(View _v) {
    	final ActionItem first = new ActionItem();
		
		first.setTitle("Share this App");
		//first.setIcon(getResources().getDrawable(R.drawable.ic_accept));
		first.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				shareTheApp();
				//Toast.makeText(Main.this, "Share this App" , Toast.LENGTH_SHORT).show();
			}
		});
		
		
		final ActionItem second = new ActionItem();
		second.setTitle("Request a Feature");
		//second.setIcon(getResources().getDrawable(R.drawable.ic_add));
		second.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				sendEmailToRequestAFeature();
				//Toast.makeText(Main.this, "Request a feature", Toast.LENGTH_SHORT).show();
			}
		});
		
		final ActionItem third = new ActionItem();
		third.setTitle("Report a Bug");
		third.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				sendEmailToReportABug();
				//Toast.makeText(Main.this, "Request ", Toast.LENGTH_SHORT).show();
			}
		});
		
		final ActionItem forth = new ActionItem();
		forth.setTitle("Get TS II");
		forth.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				getTS2();
			}
		});
		
		final QuickAction2 l_qa = new QuickAction2(_v);
		l_qa.addActionItem(first);
		l_qa.addActionItem(second);
		l_qa.addActionItem(third);
		l_qa.addActionItem(forth);
		l_qa.show();
    }
    
    private Drawable getPhotoDrawable(String _number) {
    	Drawable l_drawable;
    	long l_contactId = ContactsHelperStatic.getContactIdByPhoneNumber(mContext, _number);
    	l_drawable = ContactsHelperStatic.getContactsPhotoDrawable(mContext, l_contactId);
    	if (l_drawable == null) {
    		return mContext.getResources().getDrawable(R.drawable.ic_contact_list_picture);
    	} else {
    		return l_drawable;
    	}
    }
    private static final int INIT_NUM_OF_PHOTOS = 8;
    private void convertRecordToExpandableDisplayData(ArrayList<AdvancedPhoneLogRecord> _recordList) {
    	viewList.clear(); viewGroupList.clear(); 
    	total_missCallNum = 0; total_outCallNum = 0; total_inCallNum = 0;
    	total_outCallTime = 0; total_inCallTime = 0;
    	ArrayList<ExpandableThreeLinesIconifiedText> l_aGroup = null;
    	Drawable l_defaultGroupIndicator1 = mContext.getResources().getDrawable(R.drawable.arrowleft);
    	Drawable l_defaultGroupIndicator2 = mContext.getResources().getDrawable(R.drawable.arrowdown);
    	int l_recordSize = _recordList.size();
    	AdvancedPhoneLogRecord l_record = null;
    	Drawable l_photoIcon = null;
    	int l_numOfPhotos = 0;
    	String l_lastNumber = "";
    	for (int i = 0; i < l_recordSize; ++i) {
    		l_record = _recordList.get(i); 
	    	String l_line1 = ((l_record.aplr_name==null) || (l_record.aplr_name.compareTo("") == 0)) ? ConstantStatic.UNKNOWN : l_record.aplr_name;
	    	String l_line2 = l_record.aplr_number.compareTo("-1")==0?ConstantStatic.NOTAVAIL : l_record.aplr_number;
	    	String l_line3 = TimeUtilsStatic.dateToStringFormat2(l_record.aplr_time);
	    	String l_iconText = String.format("%02d:%02d", l_record.aplr_duration/60, l_record.aplr_duration%60);
	    	Drawable l_drawable = null;
	    	switch (l_record.aplr_type) {
	    	case android.provider.CallLog.Calls.INCOMING_TYPE:
	    		l_drawable = this.getResources().getDrawable(android.R.drawable.sym_call_incoming);
	    		++total_inCallNum;	
	    		total_inCallTime += l_record.aplr_duration;
	    		break;
	    	case android.provider.CallLog.Calls.OUTGOING_TYPE:
	    		l_drawable = this.getResources().getDrawable(android.R.drawable.sym_call_outgoing);
	    		++total_outCallNum;
	    		total_outCallTime += l_record.aplr_duration;
	    		break;
	    	case android.provider.CallLog.Calls.MISSED_TYPE:
	    		l_drawable = this.getResources().getDrawable(android.R.drawable.sym_call_missed);
	    		++total_missCallNum;	    		
	    		break;
	    	}
	    	//photo icon only needs to change when there is a new number
	    	if (l_record.aplr_number.compareTo(l_lastNumber)!=0) {
	    		l_lastNumber = l_record.aplr_number;
		    	if (l_numOfPhotos < INIT_NUM_OF_PHOTOS) {
		    		l_photoIcon = getPhotoDrawable(l_record.aplr_number);
		    		++l_numOfPhotos;
		    	} else {
		    		l_photoIcon = null;
		    	}
	    	}
	    	
	    	//l_anItem = new ExpandableThreeLinesIconifiedText(l_iconText, l_iconText, l_line1, "", l_line2, l_line3, l_drawable, l_record.aplr_number.compareTo("-1")==0?null:l_callIcon);
	    	ExpandableThreeLinesIconifiedText l_anItem = new ExpandableThreeLinesIconifiedText("", l_iconText, l_line1,
	    			"", l_line2, l_line3, l_photoIcon, l_drawable, l_record.aplr_id, 
	    			l_record);
	    	//first record, create new group
	    	if (i == 0) {
	    		l_aGroup = new ArrayList<ExpandableThreeLinesIconifiedText>();
	    		l_aGroup.add(l_anItem);
	    		continue;
	    	}
	    	if (l_record.isSameContact(_recordList.get(i-1))) {
	    		//same as last record, do not create new group
	    		l_aGroup.add(l_anItem);
	    	} else {
	    		//a new record, add old group to viewList, create new group
	    		int l_size = l_aGroup.size(); 
	    		if (l_size==1) {
	    			//[workaround]empty list, so it won't expand
	    			viewList.add(new ArrayList<ExpandableThreeLinesIconifiedText>());
	    		} else {
	    		    viewList.add(l_aGroup);
	    		}
	    		ExpandableThreeLinesIconifiedText l_topRecord = l_aGroup.get(0);
	    		viewGroupList.add(new ExpandableThreeLinesIconifiedText(
	    				l_topRecord.getLeftIconText(),
	    				l_topRecord.getRightIconText(),
	    				l_topRecord.getTextLine1Text1(), l_size==1?"":String.valueOf(l_size), 
	    				l_topRecord.getTextLine2(), l_topRecord.getTextLine3(), 
	    				l_topRecord.getLeftIcon(), 
	    				l_size==1?l_topRecord.getRightIcon():l_defaultGroupIndicator1,
	    				l_size==1?l_topRecord.getRightIcon():l_defaultGroupIndicator2,
	    				l_topRecord.getId(), 
	    				l_topRecord.getAPL()));
	    		
	    		l_aGroup = new ArrayList<ExpandableThreeLinesIconifiedText>();
	    		l_aGroup.add(l_anItem);
	    	}
    	}
    	//add the last group and its children
    	if (l_recordSize > 0) {
	    	int l_size = l_aGroup.size(); 
			if (l_size==1) {
				//[workaround]empty list, so it won't expand
				viewList.add(new ArrayList<ExpandableThreeLinesIconifiedText>());
			} else {
			    viewList.add(l_aGroup);
			}
			ExpandableThreeLinesIconifiedText l_topRecord = l_aGroup.get(0);
			viewGroupList.add(new ExpandableThreeLinesIconifiedText(
					l_topRecord.getLeftIconText(),
					l_topRecord.getRightIconText(),
					l_topRecord.getTextLine1Text1(), 
					l_size==1?"":String.valueOf(l_size), 
					l_topRecord.getTextLine2(), l_topRecord.getTextLine3(), 
					l_topRecord.getLeftIcon(),
					l_size==1?l_topRecord.getRightIcon():l_defaultGroupIndicator1, 
					l_size==1?l_topRecord.getRightIcon():l_defaultGroupIndicator2,
					l_topRecord.getId(),
					l_topRecord.getAPL()));
    	}
    }
    
    //get all the call logs sorted by date
    private ArrayList<AdvancedPhoneLogRecord> getAllCallLogs() {
    	ArrayList<AdvancedPhoneLogRecord> l_list = new ArrayList<AdvancedPhoneLogRecord>();
    	//query all all call logs
    	Cursor l_cur = mContext.getContentResolver().query(android.provider.CallLog.Calls.CONTENT_URI,
    			null, null, null, android.provider.CallLog.Calls.DATE + " DESC");
    	this.startManagingCursor(l_cur);
    	
    	//retrieve the information: number, name, time, type
    	int l_idCol = l_cur.getColumnIndex(android.provider.CallLog.Calls._ID);
    	int l_numberCol = l_cur.getColumnIndex(android.provider.CallLog.Calls.NUMBER);
    	int l_nameCol = l_cur.getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME);
    	int l_timeCol = l_cur.getColumnIndex(android.provider.CallLog.Calls.DATE);
    	int l_typeCol = l_cur.getColumnIndex(android.provider.CallLog.Calls.TYPE);
    	int l_durationCol = l_cur.getColumnIndex(android.provider.CallLog.Calls.DURATION);
    	int l_logCount = 0;
    	if (l_cur.moveToFirst()) {
    		do {
    			String l_number = l_cur.getString(l_numberCol);
    			String l_name = l_cur.getString(l_nameCol);
    			Date l_time = new Date(l_cur.getLong(l_timeCol));
    			int l_type = l_cur.getInt(l_typeCol);
    			int l_duration = l_cur.getInt(l_durationCol);
    			int l_id = l_cur.getInt(l_idCol);
    			l_list.add(new AdvancedPhoneLogRecord(l_number, l_name, l_time, 
    					l_type, l_duration, l_id, l_logCount));
    			++l_logCount;
    		} while (l_cur.moveToNext());
    	}
    	
    	return l_list;
    }
    
    private ArrayList<AdvancedPhoneLogRecord> filterPhoneLogs(ArrayList<AdvancedPhoneLogRecord> _records,
    		boolean _typeFilter, boolean _startTimeFilter, boolean _endTimeFilter, 
    		boolean _nameFilter, boolean _numberFilter) {
    	ArrayList<AdvancedPhoneLogRecord> l_list = new ArrayList<AdvancedPhoneLogRecord>();
    	for (int i = 0; i < _records.size(); ++i) {
    		AdvancedPhoneLogRecord l_aRecord = _records.get(i);
    		//call type
    		if (_typeFilter) {
	    		if ((CallLogFilterDialog.filter_callType!=CallLogFilterDialog.FILTER_CALLTYPE_ALL) && 
	    			(l_aRecord.aplr_type != CallLogFilterDialog.filter_callType)) {
	    			continue;
	    		}
    		}
    		//start time and end time
    		if (_startTimeFilter) {
	    		Date l_startTime = new Date(CallLogFilterDialog.startYear-1900, CallLogFilterDialog.startMonth, CallLogFilterDialog.startDay, 0, 0, 0);
	    		if (l_aRecord.aplr_time.before(l_startTime)) {
	    			continue;
	    		}
    		}
    		if (_endTimeFilter) {
	    		Date l_endTime = new Date(CallLogFilterDialog.endYear-1900, CallLogFilterDialog.endMonth, CallLogFilterDialog.endDay, 23, 59, 59);
	    		if (l_aRecord.aplr_time.after(l_endTime)) {
	    			continue;
	    		}
    		}
    		int l_cnt, j;
    		//name
    		if (_nameFilter) {
	    		l_cnt = FilterNameListDialog.selectedNamesList.size();
	    		j = 0;
	    		for (; j < l_cnt; ++j) {
	    			if (l_aRecord.aplr_name == null) {
	    				j = l_cnt-1; continue;
	    			}
	    			if (FilterNameListDialog.selectedNamesList.get(j).compareToIgnoreCase(l_aRecord.aplr_name)==0) {
	    				break;
	    			}
	    		}
	    		if ((l_cnt!=0) && (j==l_cnt))
	    			continue;
    		}
    		//number
    		if (_numberFilter) {
	    		l_cnt = FilterNumberListDialog.selectedNumbersList.size();
	    		for (j = 0; j < l_cnt; ++j) {
	    			if (FilterNumberListDialog.selectedNumbersList.get(j).compareToIgnoreCase(l_aRecord.aplr_number)==0) {
	    				break;
	    			}
	    		}
	    		if ((l_cnt!=0) && (j==l_cnt))
	    			continue;
    		}
    		//passed all the test
    		l_list.add(l_aRecord);
    	}
    	return l_list;
    }
    
    public static void makeAPhoneCall(String _number) {
    	//
    	if (_number.contains(ConstantStatic.NOTAVAIL)) {
    		return;
    	} else {
    		String l_url = "tel:" + _number;
    		Intent l_intent = new Intent(Intent.ACTION_DIAL, Uri.parse(l_url));
    		self.startActivity(l_intent);
    	}
    }
    
    public static void sendOutSms(String _number) {
    	//
    	if (_number.contains(ConstantStatic.NOTAVAIL)) {
    		return;
    	} else {
    		String l_url = "smsto:" + _number;
    		Intent l_intent = new Intent(Intent.ACTION_VIEW, Uri.parse(l_url));
    		l_intent.setType("vnd.android-dir/mms-sms");
    		l_intent.putExtra("address", _number);
    		self.startActivity(l_intent);
    	}
    }
      
    
    //sdk dependent
    private void viewContact(String _number) {
    	ContactAccessor.getInstance().viewContact(mContext, _number);
    }
    //sdk dependent
    public static void addToContact(String _number) {
    	 Intent l_intent = ContactAccessor.getInstance().getAddToContactIntent(_number);
    	 self.startActivityForResult(l_intent, REQUEST_ADD_TO_CONTACT);
    }
    
    private void popupQuickAction(View v) {
    	final QuickAction mQuickAction = new QuickAction(v);
    	final ActionItem phoneAction = new ActionItem();
		phoneAction.setTitle("Call");
		phoneAction.setIcon(mContext.getResources().getDrawable(R.drawable.phonecall1));
		final ActionItem smsAction = new ActionItem();
		smsAction.setTitle("SMS");
		smsAction.setIcon(mContext.getResources().getDrawable(R.drawable.sms1));
		final ActionItem contactAction = new ActionItem();
		if (!currentName.contains(ConstantStatic.UNKNOWN)) {
			contactAction.setTitle("Contact");
			contactAction.setIcon(mContext.getResources().getDrawable(R.drawable.viewcontact2));
			contactAction.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					//view contact
					viewContact(currentPhoneNumber);
					mQuickAction.dismiss();
				}
			});
		} else {
			contactAction.setTitle("Contact");
			contactAction.setIcon(mContext.getResources().getDrawable(R.drawable.addcontact));
			contactAction.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					//add phone number to contact
					addToContact(currentPhoneNumber);
					mQuickAction.dismiss();
				}
			});
		}
//		final ActionItem deleteAction = new ActionItem();
//		deleteAction.setTitle("Delete");
//		deleteAction.setIcon(mContext.getResources().getDrawable(R.drawable.delete));
//		deleteAction.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//				//delete the phone log
//				
//			}
//		});
		
		phoneAction.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				makeAPhoneCall(currentPhoneNumber);
				mQuickAction.dismiss();
			}
		});
		smsAction.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				sendOutSms(currentPhoneNumber);
				mQuickAction.dismiss();
			}
		});
		mQuickAction.addActionItem(phoneAction);
		mQuickAction.addActionItem(smsAction);
		if (sdk >= Build.VERSION_CODES.ECLAIR) {
			mQuickAction.addActionItem(contactAction);
		}
		//mQuickAction.addActionItem(deleteAction);
		mQuickAction.setAnimStyle(QuickAction.ANIM_GROW_FROM_RIGHT);
		mQuickAction.setOnDismissListener(new OnDismissListener(){
			public void onDismiss() {
				//do nothing
			}
		});
		mQuickAction.show();
    }
    
    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
    	//ExpandableThreeLinesIconifiedTextChildView childView = (ExpandableThreeLinesIconifiedTextChildView)v;
    	//currentPhoneNumber = childView.getContent().getTextLine2();
//    	currentPhoneNumber = viewList.get(groupPosition).get(childPosition).getTextLine2();
//    	currentName = viewGroupList.get(groupPosition).getTextLine1Text1();
//    	if ((currentPhoneNumber!=null) && (currentPhoneNumber.compareTo("-1")!=0) && (!currentPhoneNumber.contains(ConstantStatic.NOTAVAIL))) {
//    		popupQuickAction(v);
//    	}
    	groupOrChildClick = 1;
    	currentGroupPos = groupPosition;
    	currentChildPos = childPosition;
    	askForAnOption();
    	return false; 	
    }

	
	private static boolean menu_shown = false;
	private void toggleVisibilityOfMenuButtons() {
		if (menu_shown) {
			menu_shown = false;
			Btn_shortcut.setVisibility(View.GONE);
		    Btn_sort.setVisibility(View.GONE);
		    Btn_filter.setVisibility(View.GONE);
		    Btn_delete.setVisibility(View.GONE);
		} else {
			menu_shown = true;
			Btn_shortcut.setVisibility(View.VISIBLE);
		    Btn_sort.setVisibility(View.VISIBLE);
		    Btn_filter.setVisibility(View.VISIBLE);
		    Btn_delete.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
        	toggleVisibilityOfMenuButtons();
        }

        return super.onKeyDown(keyCode, event);
    }
	
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent i) {
    	super.onActivityResult(requestCode, resultCode, i);
    	switch (requestCode) {
    	case REQUEST_FILTER_OPTION:
    		//filtering options
    		if (resultCode == RESULT_OK) {
    			isFiltered = 1;
    			filteredList = filterPhoneLogs(recordList, true, CallLogFilterDialog.ifStartTimeEnabled, CallLogFilterDialog.ifEndTimeEnabled, true, true);
    			convertRecordToExpandableDisplayData(filteredList);
    			refreshUI();
    		}
    		break;
    	case REQUEST_ADD_TO_CONTACT:
    		if (resultCode == RESULT_OK) {
    			refreshData();
    			refreshUI();
    		}
    		break;
    	case REQUEST_MAIN_OPTIONS:
    		if (resultCode == RESULT_OK) {
    			int option = i.getIntExtra(Main.MAIN_OPTOINS_CHOICE, -1);
    			switch (option) {
    			case MainChoiceDialog.OPTION_DELETE_ONE:
    				int l_id = 0;
    				if (groupOrChildClick == 0) {
    					l_id = viewGroupList.get(currentGroupPos).getId();
    				} else if (groupOrChildClick == 1) {
    					l_id = viewList.get(currentGroupPos).get(currentChildPos).getId();
    				}
    				deletePhoneLogById(l_id);
    				refreshData();
    				refreshUI();
    				break;
    			case MainChoiceDialog.OPTION_DELETE_FROM_CONTACT:
    				String l_number = "";
    				if (groupOrChildClick == 0) {
    					l_number = viewGroupList.get(currentGroupPos).getTextLine2();
    				} else if (groupOrChildClick == 1) {
    					l_number = viewList.get(currentGroupPos).get(currentChildPos).getTextLine2();
    				}
    				deletePhoneLogByNumber(l_number);
    				refreshData();
    				refreshUI();
    				break;
    			case MainChoiceDialog.OPTION_ADD_TO_CAL:
    				addLogToCalendar();
    				break;
    			case MainChoiceDialog.OPTION_SCH_CAL:
    				scheduleACalendar();
    				break;
    			case MainChoiceDialog.OPTION_COPY:
    				String l_phoneNumber = "";
    				if (groupOrChildClick == 0) {
    					l_phoneNumber = viewGroupList.get(currentGroupPos).getTextLine2();
    				} else if (groupOrChildClick == 1) {
    					l_phoneNumber = viewList.get(currentGroupPos).get(currentChildPos).getTextLine2();
    				}
    				copyTextToClipboard(l_phoneNumber);
    				break;
    			default:
    				break;
    			}
    		}
    		break;
    	}
	}
	
	
	/**
	 * This part of the code deals with main options actions
	 */
	private static int groupOrChildClick = 0;	//0 for group, 1 for child
	private static int currentGroupPos = 0;
	private static int currentChildPos = 0;
	//delete call logs for a certain call number
    private int deletePhoneLogByNumber(String _number) {
    	String l_queryString = android.provider.CallLog.Calls.NUMBER + "='" +  _number + "'";
    	Log.d(TAG, "deletePhoneLogByNumber: " + _number);
    	int l_num_deleted = mContext.getContentResolver().delete(
    			android.provider.CallLog.Calls.CONTENT_URI, l_queryString, null);
    	if (l_num_deleted > 0) {
    		Log.d(TAG, "deletePhoneLogByNumber: " + l_num_deleted + " entries deleted!");
    		Toast.makeText(mContext, l_num_deleted + " entries deleted!", Toast.LENGTH_SHORT).show();
    	} else {
    		Log.d(TAG, "deletePhoneLogByNumber: " + l_num_deleted + " entries deleted!");
    		Toast.makeText(mContext, "phone log not found!", Toast.LENGTH_SHORT).show();
    	}
    	return l_num_deleted;
    }
    
    private void deletePhoneLogById(int _id) {
    	String l_queryString = android.provider.CallLog.Calls._ID + "=" + _id;
    	int l_num_deleted = mContext.getContentResolver().delete(
    			android.provider.CallLog.Calls.CONTENT_URI, l_queryString, null);
    	if (l_num_deleted > 0) {
    		Toast.makeText(mContext, l_num_deleted + " entries deleted!", Toast.LENGTH_SHORT).show();
    	} else {
    		Toast.makeText(mContext, "phone log delete failed!", Toast.LENGTH_SHORT).show();
    	}
    }
    
    private void scheduleACalendar() {
    	  String l_phoneNumber = "";
    	  if (groupOrChildClick == 0) {
			  l_phoneNumber = viewGroupList.get(currentGroupPos).getTextLine2();
		  } else if (groupOrChildClick == 1) {
			  l_phoneNumber = viewList.get(currentGroupPos).get(currentChildPos).getTextLine2();
		  }
    	  String l_name = "";
    	  if (groupOrChildClick == 0) {
    		  l_name = viewGroupList.get(currentGroupPos).getTextLine1Text1();
    	  } else if (groupOrChildClick == 1) {
    		  l_name = viewList.get(currentGroupPos).get(currentChildPos).getTextLine1Text1();
    	  }
    	  Intent intent = new Intent(Intent.ACTION_EDIT);  
    	  intent.setType("vnd.android.cursor.item/event");
    	  intent.putExtra("title", "Event with " + l_name);
    	  intent.putExtra("description", l_phoneNumber);
    	  intent.putExtra("beginTime", System.currentTimeMillis());
    	  intent.putExtra("endTime", System.currentTimeMillis() + 1800*1000);
    	  intent.putExtra("allDay", 0);
    	  intent.putExtra("hasAlarm", 1);
    	  try {
    		  startActivity(intent);
    	  } catch (Exception e) {
    		  Toast.makeText(mContext, "Sorry, no compatible calendar is found!", Toast.LENGTH_LONG).show();
    	  }
    }
    //add to calendar directly
    private void addLogToCalendar() {
      AdvancedPhoneLogRecord l_record = null;
  	  if (groupOrChildClick == 0) {
  		  l_record = viewGroupList.get(currentGroupPos).getAPL();
  	  } else if (groupOrChildClick == 1) {
  		  l_record = viewList.get(currentGroupPos).get(currentChildPos).getAPL();
	  }
  	  Intent intent = new Intent(Intent.ACTION_EDIT);  
  	  intent.setType("vnd.android.cursor.item/event");
  	  //title according to call type
  	  String title = "";
  	  switch (l_record.aplr_type) {
    	case android.provider.CallLog.Calls.INCOMING_TYPE:
    		title = "Incoming Call from ";
    		break;
    	case android.provider.CallLog.Calls.OUTGOING_TYPE:
    		title = "Outgoing Call with ";
    		break;
    	case android.provider.CallLog.Calls.MISSED_TYPE:
    		title = "Miss Call from ";	
    		break;
      }
  	  intent.putExtra("title", title + l_record.aplr_name);
  	  intent.putExtra("description", l_record.aplr_number);
  	  intent.putExtra("beginTime", l_record.aplr_time.getTime());
  	  intent.putExtra("endTime", l_record.aplr_time.getTime() + l_record.aplr_duration*1000);
  	  intent.putExtra("allDay", 0);
  	  intent.putExtra("hasAlarm", 0);
  	  try {
  		  startActivity(intent);
  	  } catch (Exception e) {
  		Toast.makeText(mContext, "Sorry, no compatible calendar is found!", Toast.LENGTH_LONG).show();
  	  }
    }
    
    private void copyTextToClipboard(String _text) {
		ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
		clipboard.setText(_text);
		Toast.makeText(mContext, _text + " copied to clipboard", Toast.LENGTH_SHORT).show();
	}
	
    /**
     * 
     */
	private void sendEmailToRequestAFeature() {
		final Intent l_emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		l_emailIntent.setType("plain/text");
		String l_content = "Please describe the feature you want to see in future release " +
				"as detailed as possible. Your help to improve this app is deeply appreciated!";
		l_emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"liuf0005@gmail.com"});
		l_emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "APL: Request a Feature");
		l_emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, l_content);
		startActivity(l_emailIntent);
	}
	
	private void sendEmailToReportABug() {
		final Intent l_emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		l_emailIntent.setType("plain/text");
		String l_content = "Please describe how this bug occurred as detailed as possible. Your help to improve" +
				" this app is deeply appreciated!";
		l_emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"liuf0005@gmail.com"});
		l_emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "APL: Report a Bug");
		l_emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, l_content);
		startActivity(l_emailIntent);
	}
	
	private void shareTheApp() {
		final Intent l_shareIntent = new Intent(android.content.Intent.ACTION_SEND);
		l_shareIntent.setType("text/plain");
		String l_title = "Advanced Phone Log, take a look";
		String l_content = "Take a look at the app: Advanced Phone Log. It allows you " +
				"to view and manage your phone call on Android more conveniently.";
		l_shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, l_title);
		l_shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, l_content);
		Intent.createChooser(l_shareIntent, "Select a way to share");
		startActivity(l_shareIntent);
	}
	
	private void getTS2() {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pname:roman10reborn.topsecret.main"));
		startActivity(intent);
	}
}