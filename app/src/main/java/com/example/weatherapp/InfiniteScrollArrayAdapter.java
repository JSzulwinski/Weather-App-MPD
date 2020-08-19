//Jakub Szulwinski S1627131

package com.example.weatherapp;

import android.content.Context;
import android.widget.ArrayAdapter;


public class InfiniteScrollArrayAdapter<T> extends ArrayAdapter<T> {
    private T[] objects;
    public int mid;

    public InfiniteScrollArrayAdapter(Context context, int viewId, int view2Id, T[] objects) {
        super(context, viewId, view2Id, objects);
        this.objects = objects;
        mid = (Integer.MAX_VALUE/2)-((Integer.MAX_VALUE/2)%objects.length);
    }

    @Override
    public T getItem(int position) {
        return objects[position % objects.length];
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }
}
