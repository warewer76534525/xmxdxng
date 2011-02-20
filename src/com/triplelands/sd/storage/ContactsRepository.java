package com.triplelands.sd.storage;

import java.util.List;

import android.app.Activity;
import android.os.Build;

import com.triplelands.sd.sms.Contact;

public abstract class ContactsRepository {

	/**
	 * Static singleton instance of {@link ContactAccessor} holding the
	 * SDK-specific implementation of the class.
	 */
	
	private static ContactsRepository sInstance;

	public static ContactsRepository getInstance() {
		if (sInstance == null) {
			String className;

			int sdkVersion = Integer.parseInt(Build.VERSION.SDK);
			
			if (sdkVersion < Build.VERSION_CODES.ECLAIR) {
				className = "com.triplelands.sd.storage.ContactsOldRepository";
			} else {
				className = "com.triplelands.sd.storage.ContactsNewRepository";
			}

			try {
				Class<? extends ContactsRepository> clazz = Class.forName(
						className).asSubclass(ContactsRepository.class);
				sInstance = clazz.newInstance();
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		}

		return sInstance;
	}

	public abstract List<Contact> getContacts(Activity activity);
}
