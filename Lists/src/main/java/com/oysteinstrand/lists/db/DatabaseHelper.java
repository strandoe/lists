package com.oysteinstrand.lists.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.oysteinstrand.lists.model.ItemList;
import com.oysteinstrand.lists.model.ListItem;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "listsDB";

    // Table Names
    private static final String TABLE_LIST_ITEM = "list_items";
    private static final String TABLE_LIST = "lists";
    private static final String TABLE_LIST_LIST_ITEM = "list_list_items";

    // Common column names
    private static final String KEY_ID = "id";

    // NOTES Table - column names
    private static final String KEY_LIST_ITEM_TEXT = "text";
    private static final String KEY_COMPLETED = "completed";

    // TAGS Table - column names
    private static final String KEY_LIST_NAME = "list_name";

    // NOTE_TAGS Table - column names
    private static final String KEY_LIST_ITEM_ID = "list_item_id";
    private static final String KEY_LIST_ID = "list_id";

    // Table Create Statements
    // Todo table create statement
    private static final String CREATE_TABLE_LIST_ITEM = "CREATE TABLE "
            + TABLE_LIST_ITEM + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_LIST_ITEM_TEXT
            + " TEXT,"  + KEY_COMPLETED + "INTEGER"
            + ")";

    // Tag table create statement
    private static final String CREATE_TABLE_LIST = "CREATE TABLE " + TABLE_LIST
            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_LIST_NAME + " TEXT"
            + ")";

    // todo_tag table create statement
    private static final String CREATE_TABLE_LIST_LIST_ITEM = "CREATE TABLE "
            + TABLE_LIST_LIST_ITEM + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_LIST_ITEM_ID + " INTEGER," + KEY_LIST_ID + " INTEGER"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_LIST_ITEM);
        db.execSQL(CREATE_TABLE_LIST);
        db.execSQL(CREATE_TABLE_LIST_LIST_ITEM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIST_ITEM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIST_LIST_ITEM);

        // create new tables
        onCreate(db);
    }

    public long createListItem(ListItem listItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LIST_ITEM_TEXT, listItem.text);
        values.put(KEY_COMPLETED, listItem.completed);

        // insert row
        long list_item_id = db.insert(TABLE_LIST_ITEM, null, values);

        createListListItem(list_item_id, listItem.list_id);

        return list_item_id;
    }
    public long createListListItem(long list_id, long list_item_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LIST_ITEM_ID, list_item_id);
        values.put(KEY_LIST_ID, list_id);

        long id = db.insert(TABLE_LIST_LIST_ITEM, null, values);

        return id;
    }

    public List<ListItem> getAllListItems() {
        List<ListItem> listItems = new ArrayList<ListItem>();
        String selectQuery = "SELECT  * FROM " + TABLE_LIST_ITEM;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                ListItem listItem = new ListItem();
                listItem.id = c.getLong(c.getColumnIndex(KEY_ID));
                listItem.text = (c.getString(c.getColumnIndex(KEY_LIST_ITEM_TEXT)));

                listItems.add(listItem);
            } while (c.moveToNext());
        }

        return listItems;
    }

    public long createList(ItemList itemList) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LIST_NAME, itemList.name);

        // insert row
        long tag_id = db.insert(TABLE_LIST, null, values);

        return tag_id;
    }

    public void deleteListItem(long list_item_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LIST_ITEM, KEY_ID + " = ?",
                new String[] { String.valueOf(list_item_id) });
    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}