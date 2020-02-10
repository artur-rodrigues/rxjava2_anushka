package com.anushka.androidtutz.contactmanager;

import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.anushka.androidtutz.contactmanager.adapter.ContactsAdapter;
import com.anushka.androidtutz.contactmanager.db.ContactsAppDatabase;
import com.anushka.androidtutz.contactmanager.db.entity.Contact;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private ContactsAdapter contactsAdapter;
    private ArrayList<Contact> contactArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ContactsAppDatabase contactsAppDatabase;
    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setTitle(" Contacts Manager ");

        recyclerView = findViewById(R.id.recycler_view_contacts);
        contactsAppDatabase= Room.databaseBuilder(getApplicationContext(),ContactsAppDatabase.class,"ContactDB").allowMainThreadQueries().build();

//        contactArrayList.addAll(contactsAppDatabase.getContactDAO().getContacts());

        disposable.add(contactsAppDatabase.getContactDAO().getContacts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contacts -> {
                    contactArrayList.clear();
                    contactArrayList.addAll(contacts);
                    contactsAdapter.notifyDataSetChanged();
                }, error -> {

                })
        );

        contactsAdapter = new ContactsAdapter(this, contactArrayList, MainActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(contactsAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> addAndEditContacts(false, null, -1));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addAndEditContacts(final boolean isUpdate, final Contact contact, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.layout_add_contact, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);

        TextView contactTitle = view.findViewById(R.id.new_contact_title);
        final EditText newContact = view.findViewById(R.id.name);
        final EditText contactEmail = view.findViewById(R.id.email);

        contactTitle.setText(!isUpdate ? "Add New Contact" : "Edit Contact");

        if (isUpdate && contact != null) {
            newContact.setText(contact.getName());
            contactEmail.setText(contact.getEmail());
        }

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(isUpdate ? "Update" : "Save", (dialogBox, id) -> {

                })
                .setNegativeButton("Delete", (dialogBox, id) -> {
                    if (isUpdate) {

                        deleteContact(contact, position);
                    } else {

                        dialogBox.cancel();

                    }
                });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            if (TextUtils.isEmpty(newContact.getText().toString())) {
                Toast.makeText(MainActivity.this, "Enter contact name!", Toast.LENGTH_SHORT).show();
                return;
            } else {
                alertDialog.dismiss();
            }

            if (isUpdate && contact != null) {
                updateContact(newContact.getText().toString(), contactEmail.getText().toString(), position);
            } else {
                createContact(newContact.getText().toString(), contactEmail.getText().toString());
            }
        });
    }

    private void deleteContact(Contact contact, int position) {
//        contactArrayList.remove(position);
        Completable.fromAction(() -> contactsAppDatabase.getContactDAO().deleteContact(contact))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
//        contactsAdapter.notifyDataSetChanged();
    }

    private void updateContact(String name, String email, int position) {
        Contact contact = contactArrayList.get(position);
        contact.setName(name);
        contact.setEmail(email);
        Completable.fromAction(() -> contactsAppDatabase.getContactDAO().updateContact(contact))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });

        /*disposable.add(

        );*/

//        contactArrayList.set(position, contact);
//        contactsAdapter.notifyDataSetChanged();
    }

    private void createContact(String name, String email) {
        disposable.add(
                Single.fromCallable(() -> contactsAppDatabase.getContactDAO().addContact(new Contact(0,name, email)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(row -> {

                }, error -> {

                })
        );

//        long id = contactsAppDatabase.getContactDAO().addContact(new Contact(0,name, email));
        /*Contact contact = contactsAppDatabase.getContactDAO().getContact(id);
        if (contact != null) {
            contactArrayList.add(0, contact);
            contactsAdapter.notifyDataSetChanged();
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (! disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}