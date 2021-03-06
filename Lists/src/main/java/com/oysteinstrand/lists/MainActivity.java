package com.oysteinstrand.lists;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.oysteinstrand.listItems.R;
import com.oysteinstrand.lists.utils.Dialogs;

public class MainActivity extends Activity {

    ListsFragment mListsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListsFragment = new ListsFragment();

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, mListsFragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_new_category) {
            Dialogs.showInputDialog(this, getResources().getString(R.string.new_category_title), getResources().getString(R.string.list), new Dialogs.InputDialogListener() {
                @Override
                public void savedWithText(String text) {
                    mListsFragment.newCategory(text);
                }
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
