package com.example.layout.assig20_2;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Pri on 10/23/2017.
 */

public class ContactsHelper {
    public static boolean insertContact(ContentResolver contactAdder, String firstName, String mobileNumber) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI).withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null).withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());

        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0).withValue(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE).withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,firstName).build());

        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0).withValue(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE).withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,mobileNumber).withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build());
        try {
            contactAdder.applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (Exception e) {
            Log.d("Errors", e.getMessage());
            return false;
        }
        return true;
    }

    public static int updateContact(ContentResolver contactUpdate, String name, String mobileNumber) throws RemoteException, OperationApplicationException {
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        String rawContactId = getRawContactID(contactUpdate, name);
//updates the contact and  debugs the contact
        Log.d("Raw Contact ID: ", rawContactId);

        String where = ContactsContract.Data.RAW_CONTACT_ID + " = ? AND mimetype_id=5 AND  " + ContactsContract.Data.MIMETYPE + " = ?" ;

        String[] params = new String[] {rawContactId, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE};

        ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                .withSelection(where, params)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,mobileNumber)
                .build());


        ContentProviderResult[] contentProviderResult = contactUpdate.applyBatch(ContactsContract.AUTHORITY, ops);

        return contentProviderResult[0].count;
        //COUNT THE NO.OF CONTENTS
    }


    private static String getRawContactID(ContentResolver contactHelper,String name) {
        Uri uri = ContactsContract.Data.CONTENT_URI;
        String[] projection = new String[] { ContactsContract.Data.RAW_CONTACT_ID };
        String selection = ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME + " = ?";
        String[] selectionArguments = { name };
        Cursor cursor = contactHelper.query(uri, projection, selection, selectionArguments, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                //curdor will move to next and returns the string
                return cursor.getString(0);
            }
        }
        return "-1";
        //returns -1
    }
}
/**
 * contentProviderOperations-Represents a single operation to be performed as part of a batch of operations.
 *  contentProviderOperations are set into an arraylist
 *  newUpdate-Create a ContentProviderOperation.Builder suitable for building an updateContentProviderOperation.
 Parameters
 uri	Uri: The Uri that is the target of the insert.
 ContactsContract.RawContacts.CONTENT_URI-Constants for the raw contacts table, which contains one row of contact information for each person in each synced account.
 Sync adapters and contact management apps are the primary consumers of this API.
 withValue()-A value to insert or update. This value may be overwritten by the corresponding value specified by withValueBackReference(String, int). This can only be used with builders of type insert, update, or assert.
 Parameters
 key	String: the name of this value
 value	Object: the value itself. the type must be acceptable for insertion by put(String, byte[])
 withValueBackReference-Add a ContentValues back reference. A column value from the back references takes precedence over a value specified in withValues(ContentValues). This can only be used with builders of type insert, update, or assert.
 Parameters
 key	String
 previousResult	int
 ContactsContract.CommonDataKinds.Structured-A data kind representing the contact's proper name. You can use all columns defined for ContactsContract.Data as well as the following aliases.
 ContactsContract.RawContacts-Constants for the raw contacts table, which contains one row of contact information for each person in each synced account. Sync adapters and contact management apps are the primary consumers of this API.
 getApplicationContext-Return the context of the single, global Application object of the current process.
 getContentResolver-Return a ContentResolver instance for your application's package.
 apply()-Commit your preferences changes back from this Editor to the SharedPreferences object it is editing.
 ContactsContract.AUTHORITY-The authority for the contacts provider
 ContactsContract.Data.MIMETYPE-The MIME type of the item represented by this row
 ContactsContract.Data.CONTENT_URI:The content:// style URI for this table, which requests a directory of data rows matching the selection criteria.
 CursorQuery the given URI, returning a Cursor over the result set.
 Parameters
 uri	Uri: The URI, using the content:// scheme, for the content to retrieve.
 This value must never be null.

 projection	String: A list of which columns to return. Passing null will return all columns, which is inefficient.
 selection	String: A filter declaring which rows to return, formatted as an SQL WHERE clause (excluding the WHERE itself). Passing null will return all rows for the given URI.
 selectionArgs	String: You may include ?s in selection, which will be replaced by the values from selectionArgs, in the order that they appear in the selection. The values will be bound as Strings.
 This value may be null.

 sortOrder	String: How to order the rows, formatted as an SQL ORDER BY clause (excluding the ORDER BY itself). Passing null will use the default sort order, which may be unordered.
 Returns
 Cursor	A Cursor object, which is positioned before the first entry, or null

 */
