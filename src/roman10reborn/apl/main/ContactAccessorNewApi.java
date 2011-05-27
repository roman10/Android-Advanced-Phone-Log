package roman10reborn.apl.main;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.ContactsContract.Intents.Insert;

public class ContactAccessorNewApi extends ContactAccessor{
	@Override
	public Intent getAddToContactIntent(String number) {
		Intent l_intent = new Intent(Intent.ACTION_INSERT_OR_EDIT);
		l_intent.setType(ContactsContract.Contacts.CONTENT_ITEM_TYPE);
   	 	l_intent.putExtra(Insert.PHONE, number);
   	 	return l_intent;
	}

	@Override
	public Intent getViewContactIntent() {
		return null;
	}
	
	public void viewContact(Context mContext, String _number) {
	 final String[] PHONES_PROJECTION = new String[] {
   	         PhoneLookup._ID,
   	         PhoneLookup.DISPLAY_NAME,
   	         PhoneLookup.TYPE,
   	         PhoneLookup.LABEL,
   	         PhoneLookup.NUMBER,
		};
	 final int COLUMN_INDEX_ID = 0;
    	Uri phoneUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(_number));
    	Cursor l_phoneCursor = mContext.getContentResolver().query(phoneUri, PHONES_PROJECTION, null, null, null);
    	Uri l_personUri =  null;
    	try {
    		if (l_phoneCursor != null && l_phoneCursor.moveToFirst()) {
    			long personId = l_phoneCursor.getLong(COLUMN_INDEX_ID);
    			l_personUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, personId);
    		} else {
    			
    		}
    	} finally {
    		if (l_phoneCursor != null) l_phoneCursor.close();
    	}
    	if (l_personUri!=null) {
	    	Intent l_intent = new Intent(Intent.ACTION_VIEW, l_personUri);
	    	l_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    	mContext.startActivity(l_intent);
    	}
	}


	
}
