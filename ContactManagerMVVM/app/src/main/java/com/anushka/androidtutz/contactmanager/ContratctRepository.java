package com.anushka.androidtutz.contactmanager;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Room;
import android.widget.Toast;

import com.anushka.androidtutz.contactmanager.db.ContactsAppDatabase;
import com.anushka.androidtutz.contactmanager.db.entity.Contact;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

public class ContratctRepository {

    private Application application;
    private ContactsAppDatabase contactsAppDatabase;
    private CompositeDisposable disposable=new CompositeDisposable();
    private MutableLiveData<List<Contact>> contractLiveData = new MutableLiveData<>();
    private long rowIdOfTheItemInserted;

    public ContratctRepository(Application application) {
        this.application = application;

        contactsAppDatabase= Room.databaseBuilder(application, ContactsAppDatabase.class,"ContactDB").build();

        disposable.add(contactsAppDatabase.getContactDAO().getContacts()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contacts -> contractLiveData.postValue(contacts), throwable -> {})
        );
    }

    public void createContact(final String name, final String email) {
        disposable.add(Completable.fromAction(() -> rowIdOfTheItemInserted = contactsAppDatabase.getContactDAO().addContact(new Contact(0,name, email)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        Toast.makeText(application," contact added successfully "+rowIdOfTheItemInserted, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(application," error occurred ", Toast.LENGTH_LONG).show();
                    }
                }));
    }

    public void updateContact(final Contact contact) {
        disposable.add( Completable.fromAction(() -> contactsAppDatabase.getContactDAO().updateContact(contact))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        Toast.makeText(application," contact updated successfully ", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(application," error occurred ", Toast.LENGTH_LONG).show();
                    }
                }));
    }

    public void deleteContact(final Contact contact) {
        disposable.add( Completable.fromAction(() -> contactsAppDatabase.getContactDAO().deleteContact(contact))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        Toast.makeText(application," contact deleted successfully ", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(application," error occurred ", Toast.LENGTH_LONG).show();
                    }
                }));
    }

    public void clear() {
        if (! disposable.isDisposed()) {
            disposable.clear();
        }
    }

    public MutableLiveData<List<Contact>> getContractLiveData() {
        return contractLiveData;
    }
}