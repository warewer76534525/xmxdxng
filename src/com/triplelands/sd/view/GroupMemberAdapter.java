package com.triplelands.sd.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.triplelands.sd.activitypage.R;
import com.triplelands.sd.sms.Contact;

public class GroupMemberAdapter extends BaseAdapter {

	private List<Contact> listContact;
	private List<Contact> listSelectedContact;
	private LayoutInflater inflater;

	public GroupMemberAdapter(Context ctx, List<Contact> listContact) {
		this.listContact = listContact;
		listSelectedContact = new ArrayList<Contact>();
		inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return listContact.size();
	}

	public Object getItem(int position) {
		return listContact.get(position);
	}

	public long getItemId(int position) {
		return position;
	}
	
	public List<Contact> getSelectedContacts(){
		return listSelectedContact;
	}

	public View getView(final int position, View convertView, ViewGroup viewGroup) {
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.contact_member_row, null);
			holder.txtName = (TextView) convertView.findViewById(R.id.txtContactName);
			holder.txtNumber = (TextView) convertView.findViewById(R.id.txtContactNumber);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		holder.txtName.setText(((Contact)listContact.get(position)).getName());
		holder.txtNumber.setText(((Contact)listContact.get(position)).getNumber());
		
		return convertView;
	}
	
	public static class ViewHolder {
		public TextView txtName;
		public TextView txtNumber;
	}

}
