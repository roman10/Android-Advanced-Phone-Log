package roman10reborn.apl.main;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import roman10reborn.apl.main.ContactAccessorOldApi;
import roman10reborn.apl.main.ContactAccessorNewApi;

public abstract class ContactAccessor {
	private static ContactAccessor sInstance;
	public static ContactAccessor getInstance() {
		if (sInstance == null) {
			String className;
			int sdkVersion = Integer.parseInt(Build.VERSION.SDK);
			if (sdkVersion < Build.VERSION_CODES.ECLAIR) {
				className = "ContactAccessorOldApi";
			} else {
				className = "ContactAccessorNewApi";
			}
			try {
				Class<? extends ContactAccessor> clazz =
					  Class.forName("roman10reborn.apl.main." + className).asSubclass(ContactAccessor.class);
//                    Class.forName(ContactAccessor.class.getPackage() + "." + className)
//                            .asSubclass(ContactAccessor.class);
				sInstance = clazz.newInstance();
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		}
		return sInstance;
	}
	public abstract Intent getAddToContactIntent(String number);
	public abstract Intent getViewContactIntent();
	public abstract void viewContact(Context mContext, String _number);
}
