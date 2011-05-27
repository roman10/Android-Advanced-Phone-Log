package roman10reborn.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import roman10reborn.apl.main.R;

import android.content.ContentUris;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.PhoneLookup;

public class ContactsHelperStatic {
	 public InputStream openPhoto(Context _context, long contactId) {
	     Uri contactUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId);
	     Uri photoUri = Uri.withAppendedPath(contactUri, Contacts.Photo.CONTENT_DIRECTORY);
	     Cursor cursor = _context.getContentResolver().query(photoUri,
	          new String[] {Contacts.Photo.DATA1}, null, null, null);
	     if (cursor == null) {
	         return null;
	     }
	     try {
	         if (cursor.moveToFirst()) {
	             byte[] data = cursor.getBlob(0);
	             if (data != null) {
	                 return new ByteArrayInputStream(data);
	             }
	         }
	     } finally {
	         cursor.close();
	     }
	     return null;
	 }
	 
	 public static Bitmap getContactsPhotoBitmap(Context _context, long contactId) {
		 Uri contactUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId);
		 InputStream photoStream = Contacts.openContactPhotoInputStream(
				 _context.getContentResolver(),
				 contactUri);
		 Bitmap photoBitmap = BitmapFactory.decodeStream(photoStream);
		 return photoBitmap;
	 }
	 
	 public static Drawable getContactsPhotoDrawable(Context _context, long contactId) {
		 Bitmap l_bitmap = getContactsPhotoBitmap(_context, contactId);
		 if (l_bitmap == null) {
			 l_bitmap = BitmapFactory.decodeResource(_context.getResources(), R.drawable.ic_contact_list_picture);
		 }
		 l_bitmap = framePhoto(_context, l_bitmap);
		 l_bitmap = scaleToAppIconSize(_context, l_bitmap);
		 return new BitmapDrawable(l_bitmap);
	 }
	 
	 
	 public static Bitmap framePhoto(Context _context, Bitmap photo) {
	        final Resources r = _context.getResources();
	        final Drawable frame = r.getDrawable(R.drawable.photoframe4);

	        final int width = r.getDimensionPixelSize(R.dimen.contact_shortcut_frame_width);
	        final int height = r.getDimensionPixelSize(R.dimen.contact_shortcut_frame_height);

	        frame.setBounds(0, 0, width, height);

	        final Rect padding = new Rect();
	        frame.getPadding(padding);

	        final Rect source = new Rect(0, 0, photo.getWidth(), photo.getHeight());
	        final Rect destination = new Rect(padding.left, padding.top,
	                width - padding.right, height - padding.bottom);

	        final int d = Math.max(width, height);
	        final Bitmap b = Bitmap.createBitmap(d, d, Bitmap.Config.ARGB_8888);
	        final Canvas c = new Canvas(b);

	        c.translate((d - width) / 2.0f, (d - height) / 2.0f);
	        frame.draw(c);
	        c.drawBitmap(photo, source, destination, new Paint(Paint.FILTER_BITMAP_FLAG));

	        return b;
	    }
	 
	 
	   public static Bitmap scaleToAppIconSize(Context _context, Bitmap photo) {
		    int mIconSize = _context.getResources().getDimensionPixelSize(android.R.dimen.app_icon_size);
	        // Setup the drawing classes
	        Bitmap icon = Bitmap.createBitmap(mIconSize, mIconSize, Bitmap.Config.ARGB_8888);
	        Canvas canvas = new Canvas(icon);

	        // Copy in the photo
	        Paint photoPaint = new Paint();
	        photoPaint.setDither(true);
	        photoPaint.setFilterBitmap(true);
	        Rect src = new Rect(0,0, photo.getWidth(),photo.getHeight());
	        Rect dst = new Rect(0,0, mIconSize, mIconSize);
	        canvas.drawBitmap(photo, src, dst, photoPaint);

	        return icon;
	    }

	 
	 public static long getContactIdByPhoneNumber(Context _context, String _number) {
		 final String[] PHONES_PROJECTION = new String[] {
	   	         PhoneLookup._ID,
			};
		 final int COLUMN_INDEX_ID = 0;
    	 Uri phoneUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(_number));
    	 Cursor l_phoneCursor = _context.getContentResolver().query(phoneUri, 
    			PHONES_PROJECTION, null, null, null);
    	 if (l_phoneCursor != null && l_phoneCursor.moveToFirst()) {
    		 return l_phoneCursor.getLong(COLUMN_INDEX_ID);
    	 } else {
    		 return 0;
    	 }
	 }
}
