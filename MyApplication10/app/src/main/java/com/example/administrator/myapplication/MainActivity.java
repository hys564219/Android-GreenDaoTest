package com.example.administrator.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import GreenDao.DaoMaster;
import GreenDao.DaoSession;
import GreenDao.Person;
import GreenDao.PersonDao;


public class MainActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private SQLiteDatabase db;

    private PersonDao personDao;

    private ListViewAdapter listViewAdapter;

    private EditText etName;

    private EditText etAge;

    private EditText etNameToUpdate;

    private EditText etNameToQuery;

    private Person personToUpdate;

    private EditText etSelectedData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDao();
        initView();
        refreshListView();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnAdd:
                onBtnAddClick();
                break;
            case R.id.btnQuery:
                onBtnQueryClick();
                break;
            case R.id.btnUpdate:
                onBtnUpdateClick();
                break;
            case R.id.btnDelete:
                onBtnDeleteClick();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        personToUpdate = listViewAdapter.getData().get(position);
        etNameToUpdate.setText(personToUpdate.getName());
        etSelectedData.setText(personToUpdate.getName()+"  "+personToUpdate.getAge());

    }

    private void refreshListView() {

        List<Person> dataFromDb = getDataFromDb();
        List<Person> listViewData = listViewAdapter.getData();
        listViewData.clear();
        listViewData.addAll(dataFromDb);
        listViewAdapter.notifyDataSetChanged();
        personToUpdate = null;
        etNameToUpdate.setText("");
        etSelectedData.setText("");
    }

    private List<Person> getDataFromDb() {

        String nameToQuery = etNameToQuery.getText().toString();
        String sql = "select * from " + PersonDao.TABLENAME + " where " + PersonDao.Properties.Name.columnName + " like ?";
        String[] selectionArgs = new String[]{"%" + nameToQuery + "%"};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        List<Person> noteList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Long id = cursor.getLong(cursor.getColumnIndex(PersonDao.Properties.Id.columnName));
            String name = cursor.getString(cursor.getColumnIndex(PersonDao.Properties.Name.columnName));
            Integer age = cursor.getInt(cursor.getColumnIndex(PersonDao.Properties.Age.columnName));
            Person note = new Person(id, name, age);
            noteList.add(note);
        }

        return noteList;
    }


    private void initDao() {

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "person-db", null);
        db = helper.getWritableDatabase();

        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        personDao = daoSession.getPersonDao();
        personDao.createTable(db,true);
        //personDao.dropTable(db,false);
        //personDao.createTable(db,false);
    }

    private void initView() {

        etName = (EditText) findViewById(R.id.etName);
        etAge = (EditText) findViewById(R.id.etAge);
        etNameToUpdate = (EditText) findViewById(R.id.etNameForUpdate);
        etNameToQuery = (EditText) findViewById(R.id.etNameForQuery);
        etSelectedData = (EditText) findViewById(R.id.etSelectedData);
        findViewById(R.id.btnAdd).setOnClickListener(this);
        findViewById(R.id.btnQuery).setOnClickListener(this);
        findViewById(R.id.btnUpdate).setOnClickListener(this);
        findViewById(R.id.btnDelete).setOnClickListener(this);

        ListView listView = (ListView) findViewById(R.id.lvNote);
        listViewAdapter = new ListViewAdapter(this);
        listView.setAdapter(listViewAdapter);
        listView.setOnItemClickListener(this);
    }

    private void onBtnAddClick() {

        String name = etName.getText().toString();
        String age = etAge.getText().toString();
        Person person = new Person(null, name, Integer.valueOf(age));
        personDao.insert(person);

        refreshListView();
    }

    private void onBtnQueryClick() {

        refreshListView();
    }

    private void onBtnUpdateClick() {

        if(personToUpdate == null) {
            new  AlertDialog.Builder(this)
                    .setMessage("请点击某行选择要更改的数据" )
                    .setPositiveButton("确定",null)
                    .show();
            return;
        }
        String name = etNameToUpdate.getText().toString();
        personToUpdate.setName(name);
        personDao.update(personToUpdate);
        personToUpdate = null;
        etNameToUpdate.setText("");
        etSelectedData.setText("");
        refreshListView();
    }

    private void onBtnDeleteClick() {

        if(personToUpdate == null) {
            new  AlertDialog.Builder(this)
                    .setMessage("请点击某行选择要更改的数据" )
                    .setPositiveButton("确定",null)
                    .show();
            return;
        }
        personDao.delete(personToUpdate);
        personToUpdate = null;
        etNameToUpdate.setText("");
        etSelectedData.setText("");
        refreshListView();
    }
}