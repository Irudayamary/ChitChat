package com.usimedia.chitchat;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArraySet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Contacts extends AppCompatActivity {

    private static final String CONTACTS_SERVICE_URL = "http:192.168.1.24:8000/api/contacts";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();

    private int numberOfContact;
    private Set<String> contactNumbers;
    private ListView contactListView;


    private class contactResolverTask extends AsyncTask<String, Void, List<String>> {
        @Override
        protected List<String> doInBackground(String... params) {
            List<String> phoneNumbers = Arrays.asList(params);

            try {
                return getContacts(phoneNumbers);
            } catch (JSONException e) {
                Log.d("Contacts", "Json parser exception");
                e.printStackTrace();
                return Collections.emptyList();
            } catch (IOException e) {
                Log.d("Contacts" , "Network Exception");
                e.printStackTrace();
                return Collections.emptyList();
            }

        }

        @Override
        protected void onPostExecute(List<String> names) {
            super.onPostExecute(names);
            setValuesToUiListView(names);
        }
    }

    private List<String> getContacts(List<String> phoneNumbers) throws JSONException, IOException {

        JSONArray jsonNumbers = new JSONArray(phoneNumbers);
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("numbers", jsonNumbers);

        String rawResult = post(CONTACTS_SERVICE_URL, jsonRequest.toString());

        JSONObject jsonResult = new JSONObject(rawResult);

        JSONArray jsonContacts = jsonResult.getJSONArray("contacts");

        Log.d("Contacts" , "Number of contacts received from backend = " + jsonContacts.length());

        final List<String> contactList = new ArrayList<>();

        String currentContactName;

        for (int i=0; i<jsonContacts.length(); i++){
            currentContactName = jsonContacts.getJSONObject(i).getString("name");
            contactList.add(currentContactName);
            Log.d("Contacts", "Current contact name being parsed = " + currentContactName);
        }

        return contactList;
    }

    private String post(String url, String json) throws IOException {

        RequestBody body = RequestBody.create(JSON, json);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();

        return response.body().string();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        contactListView = (ListView)findViewById(R.id.contacts_listView);

        ContentResolver contentResolver = getContentResolver();

        final Uri providerUri= ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        //second parameter
        String[] elementsrequired = new String[]{
                ContactsContract.Contacts._ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };
        //fifth parameter and to display the contacts in ascending order
        String ascendingorder =
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC";
        Cursor cursor = contentResolver.query(
                providerUri,
                elementsrequired,
                null, null, ascendingorder);

        //List<String> contactNames = new ArrayList<String>();

        Set<String> contactNumbers = new HashSet<>();



        String name;
        while(cursor.moveToNext())
        {
            //to display names, replace with the following code: cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            //to display phone numbers
            name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            //to remove space
            if(name != null)
            {
                contactNumbers.add(name.replaceAll("\\s+",""));
            }
        }
        final List<String> distinctNumbers = new ArrayList<>(contactNumbers);

        String[] numbersBuffer = new String[distinctNumbers.size()];

        new contactResolverTask().execute(distinctNumbers.toArray(numbersBuffer));
    }

    private void setValuesToUiListView(List<String> elements) {

        Log.d("Contact", "Number of contacts found from backend = "+elements.size());

        if(elements.size() == 0)
        {
            return;
        }

        final ArrayAdapter<String> contactListAdapter = new ArrayAdapter<>(
                Contacts.this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                elements
        );

        contactListView.setAdapter(contactListAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contacts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

