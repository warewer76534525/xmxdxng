package com.triplelands.sd.server;

import java.lang.reflect.Type;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.google.myjson.reflect.TypeToken;
import com.triplelands.sd.rest.HttpClientUtils;
import com.triplelands.sd.specialevent.SpecialEvent;
import com.triplelands.sd.storage.SpecialEventRepository;

public class SpecialEventRetriever {
	private static final String SPECIAL_EVENT_URI = "http://smsdong.triplelands.com/catalog/specialevent";

	private SpecialEventRepository _specialEventRepository;

	public SpecialEventRetriever(Context context) {
		_specialEventRepository = new SpecialEventRepository(context);
	}

	public void initializeForTheFirstTimeUse() {
		_specialEventRepository.open();
		retrieveAllEvent();
		_specialEventRepository.close();
	}

	private void retrieveAllEvent() {
		Type listType = new TypeToken<List<SpecialEvent>>() {
		}.getType();

		List<SpecialEvent> events = HttpClientUtils.getList2(
				SPECIAL_EVENT_URI, listType);

		for (SpecialEvent specialEvents : events) {
			Log.i("SpecialEvent", "" + specialEvents.getId());
			Log.i("SpecialEvent", specialEvents.getName());
			_specialEventRepository.save(specialEvents);
		}
	}
}
