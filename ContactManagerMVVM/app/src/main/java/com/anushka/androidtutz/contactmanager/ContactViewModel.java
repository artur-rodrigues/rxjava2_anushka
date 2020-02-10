package com.anushka.androidtutz.contactmanager;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.anushka.androidtutz.contactmanager.db.entity.Contact;

import java.util.List;

public class ContactViewModel extends AndroidViewModel {

    private ContratctRepository repository;

    public ContactViewModel(@NonNull Application application) {
        super(application);

        repository = new ContratctRepository(application);
    }

    public LiveData<List<Contact>> getAllContacts() {
        return repository.getContractLiveData();
    }

    public void create(String name, String email) {
        repository.createContact(name, email);
    }

    public void update(Contact contact) {
        repository.updateContact(contact);
    }

    public void delete(Contact contact) {
        repository.deleteContact(contact);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        repository.clear();
    }
}
