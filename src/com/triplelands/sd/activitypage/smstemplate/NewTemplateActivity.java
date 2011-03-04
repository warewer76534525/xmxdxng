package com.triplelands.sd.activitypage.smstemplate;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.triplelands.sd.activitypage.R;
import com.triplelands.sd.smstemplate.SmsTemplate;
import com.triplelands.sd.storage.SmsTemplateRepository;

public class NewTemplateActivity extends Activity {
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_template);
		setTitle("Template SMS");
		
		final int idCategory = getIntent().getExtras().getInt("idCategory");
		
		final int idTemplate = getIntent().getExtras().getInt("idTemplate");
		final String judulTemplate = getIntent().getExtras().getString("titleTemplate");
		final String isiTemplate = getIntent().getExtras().getString("contentTemplate");
		
		final SmsTemplateRepository smsRepo = new SmsTemplateRepository(this);
		
		final EditText txtTitle = (EditText) findViewById(R.id.txtJudulNewTemplate);
		final EditText txtContent = (EditText) findViewById(R.id.txtContentNewTemplate);
		if(judulTemplate != null) txtTitle.setText(judulTemplate);
		if(isiTemplate != null) txtContent.setText(isiTemplate);
		
		Button btnSubmit = (Button) findViewById(R.id.btnNewTemplate);
		btnSubmit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(txtTitle.getText().toString().trim().equals("")){
					Toast.makeText(NewTemplateActivity.this, "Judul masih kosong.", Toast.LENGTH_SHORT).show();
				}else if(txtContent.getText().toString().trim().equals("")){
					Toast.makeText(NewTemplateActivity.this, "Isi Template masih kosong.", Toast.LENGTH_SHORT).show();
				} else {
					smsRepo.open();
					if(String.valueOf(idCategory) != null){
						SmsTemplate template = new SmsTemplate();
						template.setCategory(idCategory);
						template.setTitle(txtTitle.getText().toString());
						template.setIsi(txtContent.getText().toString());
						smsRepo.save(template);
					} 
					if(judulTemplate != null) {
						smsRepo.updateTemplate(idTemplate, txtTitle.getText().toString(), txtContent.getText().toString());
						Toast.makeText(NewTemplateActivity.this, "Template sudah diperbaharui.", Toast.LENGTH_SHORT).show();
					}
					smsRepo.close();
					finish();
				}
			}
		});
	}
}
