package com.zsl.android.mobilesafe.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.zsl.android.mobilesafe.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ContactActivity extends Activity {

    private ListView lvList;
    private ArrayList<Map<String, String>> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        mList = getContacts();

        lvList = (ListView) findViewById(R.id.lv_list);
        lvList.setAdapter(new SimpleAdapter(
                this, mList,
                R.layout.contact_item,
                new String[]{"name", "phone"},
                new int[]{R.id.tv_name, R.id.tv_phone}));
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                String phone = mList.get(position).get("phone");
                intent.putExtra("phone", phone);
                ContactActivity.this.setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    private ArrayList<Map<String,String>> getContacts() {
        ArrayList<Map<String, String>> contacts = new ArrayList<>();
        ContentResolver cr = getContentResolver();
        Cursor cursorContactId = cr.query(Uri.parse("content://com.android.contacts/raw_contacts"),
                new String[]{"contact_id"}, null, null, null);
        while(cursorContactId.moveToNext()){
            String contactId = cursorContactId.getString(0);
            Cursor cursorContactData = cr.query(Uri.parse("content://com.android.contacts/data"),
                    new String[] {"data1","mimetype"},
                    "contact_id=?",
                    new String[] {contactId} , null);
            HashMap<String, String> map = new HashMap<>();
            while(cursorContactData.moveToNext()){
                String data1 = cursorContactData.getString(0);
                String mimetype = cursorContactData.getString(1);
                if(mimetype.equals("vnd.android.cursor.item/phone_v2")){
                    map.put("phone", data1);
                }else if(mimetype.equals("vnd.android.cursor.item/name")){
                    map.put("name", data1);
                }
            }
            contacts.add(map);
            cursorContactData.close();
        }
        cursorContactId.close();
        return contacts;
    }

}
