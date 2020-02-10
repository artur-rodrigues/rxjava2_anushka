package com.anushka.androidtutz.contactmanager.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.anushka.androidtutz.contactmanager.db.entity.Contact;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface ContactDAO {

    @Insert
    Long addContact(Contact contact);

    @Update
    void updateContact(Contact contact);

    @Delete
    void deleteContact(Contact contact);

    @Query("select * from contacts")
    Flowable<List<Contact>> getContacts();

    /*@Query("select * from contacts")
    List<Contact> getContacts();*/

    /*@Query("select * from contacts where contact_id ==:contactId")
    Contact getContact(long contactId);*/

}
