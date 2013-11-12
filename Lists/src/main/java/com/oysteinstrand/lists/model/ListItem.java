package com.oysteinstrand.lists.model;

public class ListItem {
    public long id;
    public long list_id;
    public String text;
    public ItemList list;
    public boolean completed = false;

    public ListItem() {
    }

    public ListItem(String text, ItemList list) {
        this.text = text;
        this.list = list;

    }
}
