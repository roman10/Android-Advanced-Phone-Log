package roman10reborn.expandableiconifiedlist;

import java.util.ArrayList;
import java.util.List;

import roman10reborn.apl.data2.BooleanArrayList;
import roman10reborn.apl.main.ContactAccessor;
import roman10reborn.apl.main.Main;
import roman10reborn.apl.main.R;
import roman10reborn.quickactionwindow.ActionItem;
import roman10reborn.quickactionwindow.QuickAction;
import roman10reborn.utils.ConstantStatic;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.PopupWindow.OnDismissListener;

public class ExpandableThreeLinesIconifiedTextListAdapter extends BaseExpandableListAdapter {
	private Context mContext;

	private ArrayList<ArrayList<ExpandableThreeLinesIconifiedText>> mItems = new ArrayList<ArrayList<ExpandableThreeLinesIconifiedText>>();
	private ArrayList<ExpandableThreeLinesIconifiedText> mGroups = new ArrayList<ExpandableThreeLinesIconifiedText>();
	
	public ExpandableThreeLinesIconifiedTextListAdapter(Context context) {
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
		if (convertView == null) {
			btv = new ExpandableThreeLinesIconifiedTextChildView(mContext, mItems.get(groupPosition).get(childPosition));
		} else { //Reuse/Overwrite the View passed
			// We are assuming(!) that it is castable! 
			btv = (ExpandableThreeLinesIconifiedTextChildView) convertView;		
			btv.setTextLine1Text1(mItems.get(groupPosition).get(childPosition).getTextLine1Text1());
			btv.setTextLine1Text2("");
			btv.setTextLine2(mItems.get(groupPosition).get(childPosition).getTextLine2());
			btv.setTextLine3(mItems.get(groupPosition).get(childPosition).getTextLine3());
			btv.setLeftIconText(mItems.get(groupPosition).get(childPosition).getLeftIconText());
			btv.setRightIconText(mItems.get(groupPosition).get(childPosition).getRightIconText());
			btv.setLeftIcon(mItems.get(groupPosition).get(childPosition).getLeftIcon());
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
			btv.setTextLine3(mGroups.get(groupPosition).getTextLine3());
			//btv.setLeftIconText(mGroups.get(groupPosition).getLeftIconText());
			btv.setRightIconText(mGroups.get(groupPosition).getRightIconText());
			btv.setLeftIcon(mGroups.get(groupPosition).getLeftIcon());
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