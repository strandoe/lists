package com.oysteinstrand.lists;

import android.app.ListFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oysteinstrand.listItems.R;
import com.oysteinstrand.lists.model.ItemList;
import com.oysteinstrand.lists.model.ListItem;

import de.timroes.android.listview.EnhancedListView;

public class ListsFragment extends ListFragment {

    private ListsAdapter mTasksAdapter;


    public void newCategory(String text) {
        mTasksAdapter.addCategory(text);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTasksAdapter = new ListsAdapter(getActivity());
        setListAdapter(mTasksAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(
                R.layout.fragment_list_with_empty_container, container, false);
        root.setBackgroundColor(Color.WHITE);
        EnhancedListView listView = (EnhancedListView) root.findViewById(android.R.id.list);
        listView.setUndoHideDelay(0);
        listView.setUndoStyle(EnhancedListView.UndoStyle.MULTILEVEL_POPUP);

        listView.setDismissCallback(new EnhancedListView.OnDismissCallback() {

            @Override
            public EnhancedListView.Undoable onDismiss(EnhancedListView listView, final int position) {

                final Object item = mTasksAdapter.getItem(position);
                final int oldPos = mTasksAdapter.remove(item);

                // return an Undoable
                return new EnhancedListView.Undoable() {
                    // Reinsert the item to the adapter
                    @Override
                    public void undo() {
                        mTasksAdapter.insert(oldPos, item);
                    }

                    // Return a string for your item
                    @Override
                    public String getTitle() {
                        if (item instanceof ListItem) {
                            return "Deleted " + ((ListItem) item).text;
                        }
                        if (item instanceof ItemList) {
                            return "Deleted " + ((ItemList) item).name;
                        }
                        return "";
                    }

                    // Delete item completely from your persistent storage
                    @Override
                    public void discard() {

                    }
                };

            }

        });
        listView.enableSwipeToDismiss();
        return root;

    }
}