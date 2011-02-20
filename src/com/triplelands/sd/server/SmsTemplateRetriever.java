package com.triplelands.sd.server;

import java.lang.reflect.Type;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.google.myjson.reflect.TypeToken;
import com.triplelands.sd.rest.HttpClientUtils;
import com.triplelands.sd.smstemplate.SmsCategory;
import com.triplelands.sd.smstemplate.SmsTemplate;
import com.triplelands.sd.storage.SmsTemplateRepository;

public class SmsTemplateRetriever {
	private static final String SMS_TEMPLATE_URI = "http://smsdong.triplelands.com/catalog/";
	private static final String SMS_CATEGORY_URI = "http://smsdong.triplelands.com/catalog/category/";

	private SmsTemplateRepository _smsTemplateRepository;

	public SmsTemplateRetriever(Context context) {
		_smsTemplateRepository = new SmsTemplateRepository(context);
	}

	public void initializeForTheFirstTimeUse() {
		_smsTemplateRepository.open();

		retrieveAllCategories();
		retrieveSmsTemplate();

		_smsTemplateRepository.close();
	}

	private void retrieveSmsTemplate() {
		for (int i = 1; i <= 850; i++) {
			SmsTemplate template = HttpClientUtils.get(SMS_TEMPLATE_URI + i,
					SmsTemplate.class);
			Log.i("SmsTemplate", "" + i);
			Log.i("SmsTemplate", template.getTitle());
			Log.i("SmsTemplate", template.getIsi());
			Log.i("SmsTemplate", "" + template.getCategory());
			_smsTemplateRepository.save(template);
		}
	}

	private void retrieveAllCategories() {
		Type listType = new TypeToken<List<SmsCategory>>() {
		}.getType();

		List<SmsCategory> categories = HttpClientUtils.getList2(
				SMS_CATEGORY_URI, listType);

		for (SmsCategory smsCategory : categories) {
			Log.i("SmsCategory", "" + smsCategory.getId());
			Log.i("SmsCategory", smsCategory.getName());
			_smsTemplateRepository.save(smsCategory);
		}
	}
}
