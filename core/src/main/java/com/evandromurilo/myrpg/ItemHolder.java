package com.evandromurilo.myrpg;

public interface ItemHolder {
    public Item getItem();
    public String getName();
    public void hide();
    public void show();
    public boolean isVisible();
    public boolean isEmpty();
}
