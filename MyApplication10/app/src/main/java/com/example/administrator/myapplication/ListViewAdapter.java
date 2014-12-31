package com.example.administrator.myapplication;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import GreenDao.Person;

/**
 * Created by Administrator on 2014/12/29.
 */
public class ListViewAdapter extends BaseAdapter {

    private List<Person> data = new ArrayList<Person>();

    private LayoutInflater layoutInflater;

    public ListViewAdapter(Activity activity) {

        layoutInflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {

        return data.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Person person = data.get(position);
        String text = person.getName() + "     " + person.getAge();

        TextView textView = (TextView) layoutInflater.inflate(R.layout.listview_item, parent, false);
        textView.setText(text);

        return textView;
    }

    public List<Person> getData() {

        return data;
    }

    @Override
    public Object getItem(int position) {

        return null;
    }

    @Override
    public long getItemId(int position) {

        return 0;
    }

}