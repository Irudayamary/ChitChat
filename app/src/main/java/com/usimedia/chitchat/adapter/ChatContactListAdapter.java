package com.usimedia.chitchat.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.usimedia.chitchat.R;
import com.usimedia.chitchat.model.ChatContacts;

import java.text.SimpleDateFormat;

/**
 * Created by USI IT on 6/3/2016.
 */
public class ChatContactListAdapter extends ArrayAdapter<ChatContacts> {

    //creates an id for the model
    private static final int ChatContactLayoutId= R.layout.chat_contact_list_item;

    private ChatContacts[] contacts;
    private Activity context;
    private static final SimpleDateFormat DATE_UI_FORMAT = new SimpleDateFormat("dd/MM/yyyy hh:mm");
    //constructor
     public ChatContactListAdapter(Activity activity, ChatContacts[] objects) {
        super(activity, ChatContactLayoutId, objects);
         contacts = objects;
         context = activity;
    }

    //this function will map each field to a particular view in the layout
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View chatContactView;
        if (convertView == null)
        {
            chatContactView = context.getLayoutInflater().inflate(ChatContactLayoutId, parent, false);
        }
        else
        {
            chatContactView = convertView;
        }
        ChatContacts currentContact = contacts[position];

        TextView nameTextView = (TextView) chatContactView.findViewById(R.id.chatcontact_name);
        TextView statusTextView = (TextView) chatContactView.findViewById(R.id.chatcontact_status);
        TextView lastseenTextView = (TextView) chatContactView.findViewById(R.id.chatcontact_lastseen);

        nameTextView.setText(currentContact.getName());
        statusTextView.setText(currentContact.getStatus());
        lastseenTextView.setText(DATE_UI_FORMAT.format(currentContact.getLastSeen()));

        return chatContactView;
    }
}
