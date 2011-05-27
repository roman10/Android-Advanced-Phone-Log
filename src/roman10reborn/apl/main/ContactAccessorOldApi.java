package roman10reborn.apl.main;

import android.content.Context;
import android.content.Intent;

public class ContactAccessorOldApi extends ContactAccessor{	
	public Intent getViewContactIntent() {
		return new Intent();
	}

	@Override
	public void viewContact(Context mContext, String number) {
		//
	}

	@Override
	public Intent getAddToContactIntent(String number) {
		
		return null;
	}
}
