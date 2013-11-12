package com.oysteinstrand.lists;

public class ListItem {
    public String text;
    public ItemList category;
    public boolean completed = false;

    public ListItem(String text, ItemList category) {
        this.text = text;
        this.category = category;

    }
}
