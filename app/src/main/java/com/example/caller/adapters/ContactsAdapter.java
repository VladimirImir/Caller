package com.example.caller.adapters;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caller.R;
import com.example.caller.adapters.ContactsAdapter.ContactHolder;


public class ContactsAdapter extends RecyclerView.Adapter<ContactHolder> {
    private Context context;
    private LayoutInflater inflater;
    private Cursor cursor;

    public ContactsAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        cursor = getContacts();
        cursorOut(cursor);
    }

    private Cursor getContacts() {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI,
                new String[]{
                        ContactsContract.Contacts._ID,
                        ContactsContract.Contacts.DISPLAY_NAME,
                        ContactsContract.Contacts.HAS_PHONE_NUMBER
                },
                null, null
        );
        return cursor;
    }

    private Cursor getPhones(String contactId) {
        Cursor cursor = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                new String[]{contactId},
                null
        );
        return cursor;
    }

    private void cursorOut(Cursor cursor) {
        while (cursor.moveToNext()) {
            Log.e("FF", "----------");
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                Log.e("FF", String.valueOf(cursor.getString(i)));
            }
        }
    }


    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.contact_item, parent, false);
        return new ContactHolder(view);
    }

    @SuppressLint("Range")
    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
        if (cursor.moveToPosition(position)) {
            holder.nameField.setText(String.valueOf(
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)))
            );
            Cursor phones = getPhones(
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)));
            if (phones.moveToFirst()) {
                holder.phoneField.setText(String.valueOf(
                        phones.getString(phones.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.DATA
                        ))
                ));
            }
        }
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public static class ContactHolder extends RecyclerView.ViewHolder {
        TextView nameField;
        TextView phoneField;

        public ContactHolder(@NonNull View itemView) {
            super(itemView);
            nameField = itemView.findViewById(R.id.nameField);
            phoneField = itemView.findViewById(R.id.phoneField);
        }
    }
}
