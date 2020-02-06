package com.androidtutz.anushka.createdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "myApp";
    private Observable<Student> myObservable;
    private DisposableObserver<Student> myObserver;


    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myObservable = Observable.create(emitter -> {
            for (Student student : getStudents()) {
                emitter.onNext(student);
            }

            emitter.onComplete();
        });

        compositeDisposable.add(
                myObservable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(getObserver())
        );

    }

    private DisposableObserver getObserver() {

        myObserver = new DisposableObserver<Student>() {
            @Override
            public void onNext(Student s) {
                Log.i(TAG, " onNext invoked with " + s.getEmail());
            }

            @Override
            public void onError(Throwable e)

            {
                Log.i(TAG, " onError invoked");
            }

            @Override
            public void onComplete() {

                Log.i(TAG, " onComplete invoked");
            }
        };

        return myObserver;
    }


    private ArrayList<Student> getStudents() {

        ArrayList<Student> students = new ArrayList<>();

        for(int i = 1; i <= 5; i++) {
            int finalI = i;
            students.add(new Student() {{
                setName(" student " + finalI);
                setEmail(" student" + finalI + "@gmail.com ");
                setAge(20 + finalI);
            }});
        }

        return students;


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }


}
