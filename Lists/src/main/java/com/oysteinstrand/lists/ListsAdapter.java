package com.oysteinstrand.lists;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.oysteinstrand.listItems.R;
import com.oysteinstrand.lists.model.ItemList;
import com.oysteinstrand.lists.model.ListItem;
import com.oysteinstrand.lists.utils.Dialogs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListsAdapter extends BaseAdapter {

    ItemList handle = new ItemList("handle");
    ItemList jobb = new ItemList("jobb");

    List<ItemList> categories = Arrays.asList(handle, jobb);

    ArrayList<ListItem> listItems = new ArrayList<ListItem>();

    List items = new ArrayList();

    LayoutInflater mLayoutInflater;
    final Context mContext;

    public ListsAdapter(Context context) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        listItems.add(new ListItem("Melk", handle));
        listItems.add(new ListItem("Melk", handle));
        listItems.add(new ListItem("Melk", handle));
        listItems.add(new ListItem("Melk", jobb));
        listItems.add(new ListItem("Melk", jobb));
        listItems.add(new ListItem("Melk", jobb));

        for (ItemList category : categories) {
            items.add(category);
            for (ListItem listItem : listItems) {
                if (listItem.list == category) {
                    items.add(listItem);
                }
            }
        }
    }

    public int remove(Object object) {
        int pos = -1;
        for (Object item : items) {
            if (item == object) {
                pos = items.indexOf(object);
            }
        }
        items.remove(pos);
        notifyDataSetChanged();
        return pos;
    }

    public void insert(int pos, Object object) {
        items.add(pos, object);
        notifyDataSetChanged();
    }

    public void addTask(ListItem listItem, ItemList category) {
        int pos = -1;
        for (Object item : items) {
            if (item == category) {
                pos = items.indexOf(item);
            }
        }
        insert(pos + 1, listItem);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return Integer.MAX_VALUE - i;
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) instanceof ListItem ? 0 : 1;
    }

    @Override
    public boolean isEnabled(int position) {
        return items.get(position) instanceof ListItem;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    public void addCategory(String text) {
        items.add(0, new ItemList(text));
        notifyDataSetChanged();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    static class ViewHolderHeader {
        TextView headingTextView;
        ImageButton newTaskButton;
    }

    private static class ViewHolderTask {
        TextView text;
        CheckBox checkComplete;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        final Object item = items.get(i);

        if (item instanceof ItemList) {
            ViewHolderHeader viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolderHeader();
                convertView = mLayoutInflater.inflate(R.layout.list_header, parent, false);
                viewHolder.headingTextView = (TextView) convertView.findViewById(R.id.list_item_task_header_textview);
                viewHolder.newTaskButton = (ImageButton) convertView.findViewById(R.id.new_task_button);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolderHeader) convertView.getTag();
            }

            viewHolder.newTaskButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final ItemList category = (ItemList)item;
                    Dialogs.showInputDialog(
                            mContext,
                            mContext.getResources().getString(R.string.new_list_item_title, category.name),
                            mContext.getResources().getString(R.string.listItem), new Dialogs.InputDialogListener() {
                        @Override
                        public void savedWithText(String text) {

                            addTask(new ListItem(text, category), category);
                        }
                    });
                }
            });

            viewHolder.headingTextView.setText(((ItemList)item).name);

        } else if (item instanceof ListItem) {
            final ViewHolderTask viewHolder;
            final ListItem listItem = (ListItem) item;
            if (convertView == null) {
                viewHolder = new ViewHolderTask();
                convertView = mLayoutInflater.inflate(R.layout.list_item, null);
                viewHolder.text = (TextView) convertView.findViewById(R.id.text);
                viewHolder.checkComplete = (CheckBox) convertView.findViewById(R.id.check_complete);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolderTask) convertView.getTag();
            }
            viewHolder.text.setText(((ListItem) item).text);

            viewHolder.checkComplete.setChecked(listItem.completed);

            if (listItem.completed) {
                viewHolder.text.setPaintFlags(viewHolder.text.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                viewHolder.text.setPaintFlags(viewHolder.text.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }

            viewHolder.checkComplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View checkbox) {
                    final boolean isChecked = ((CheckBox) checkbox).isChecked();
                    listItem.completed = isChecked;
                    if (listItem.completed) {
                        viewHolder.text.setPaintFlags(viewHolder.text.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    } else {
                        viewHolder.text.setPaintFlags(viewHolder.text.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    }
                }
            });
        }

        return convertView;
    }
}
