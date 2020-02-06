package com.androidtutz.anushka.rxsubjectdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.AsyncSubject;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.ReplaySubject;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "myApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        asyncSubjectDemo1();
//        asyncSubjectDemo2();
//        behaviourSubjectDemo1();
//        behaviourSubjectDemo2();
//        publishSubjectDemo1();
//        publishSubjectDemo2();
//        replaySubjectDemo1();
        replaySubjectDemo2();
    }

    /**
     * Async emit o último valor para todos os observers
     */
    private void asyncSubjectDemo1() {
        AsyncSubject<String> subject = AsyncSubject.create();

        subject.subscribe(getObserver("First"));
        subject.subscribe(getObserver("Second"));
        subject.subscribe(getObserver("Third"));

        Observable.just("Java", "Kotlin", "XML", "JSON")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subject);
    }

    /**
     * Async emit o último valor antes do onComplete para todos os observers
     */
    private void asyncSubjectDemo2() {
        AsyncSubject<String> subject = AsyncSubject.create();

        subject.subscribe(getObserver("First"));

        subject.onNext("Java");
        subject.onNext("Kotlin");
        subject.onNext("XML");

        subject.subscribe(getObserver("Second"));

        subject.onNext("JSON");
        subject.onComplete();

        subject.subscribe(getObserver("Third"));
    }

    private void behaviourSubjectDemo1() {
        BehaviorSubject<String> subject = BehaviorSubject.create();

        subject.subscribe(getObserver("First"));
        subject.subscribe(getObserver("Second"));
        subject.subscribe(getObserver("Third"));

        Observable.just("Java", "Kotlin", "XML", "JSON")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subject);
    }

    /**
     * Devolve todos o último elemento antes da subscrição e todos os elementos até a chamada do
     * onComplete. Se hover alguma subscrição depois do onComplete não será emitido nenhum elemento
     */
    private void behaviourSubjectDemo2() {
        BehaviorSubject<String> subject = BehaviorSubject.create();

        subject.subscribe(getObserver("First"));

        subject.onNext("Java");
        subject.onNext("Kotlin");
        subject.onNext("XML");

        subject.subscribe(getObserver("Second"));

        subject.onNext("JSON");
        subject.onComplete();

        subject.subscribe(getObserver("Third"));
    }

    /**
     * Emite todos os elementos na ordem eu que foram criados.
     */
    private void publishSubjectDemo1() {
        PublishSubject<String> subject = PublishSubject.create();

        subject.subscribe(getObserver("First"));
        subject.subscribe(getObserver("Second"));
        subject.subscribe(getObserver("Third"));

        Observable.just("Java", "Kotlin", "XML", "JSON")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subject);
    }

    /**
     * Emite todos os elementos a partir da subscrição até o onComplete. Se a subscrição ocorrer
     * após o onComplete não será enviados nenhum valor.
     */
    private void publishSubjectDemo2() {
        PublishSubject<String> subject = PublishSubject.create();

        subject.subscribe(getObserver("First"));

        subject.onNext("Java");
        subject.onNext("Kotlin");
        subject.onNext("XML");

        subject.subscribe(getObserver("Second"));

        subject.onNext("JSON");
        subject.onComplete();

        subject.subscribe(getObserver("Third"));
    }

    private void replaySubjectDemo1() {
        ReplaySubject<String> subject = ReplaySubject.create();

        subject.subscribe(getObserver("First"));
        subject.subscribe(getObserver("Second"));
        subject.subscribe(getObserver("Third"));

        Observable.just("Java", "Kotlin", "XML", "JSON")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subject);
    }

    /**
     * Independentemente o do momento que ocorrer a subscrição o subject sempre vai emitir todos
     * os dados novamente.
     */
    private void replaySubjectDemo2() {
        ReplaySubject<String> subject = ReplaySubject.create();

        subject.subscribe(getObserver("First"));

        subject.onNext("Java");
        subject.onNext("Kotlin");
        subject.onNext("XML");

        subject.subscribe(getObserver("Second"));

        subject.onNext("JSON");
        subject.onComplete();

        subject.subscribe(getObserver("Third"));
    }

    private <T> Observer<T> getObserver(final String observer) {
        return new Observer<T>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.i(TAG, observer + " Observer onSubscribe ");
            }

            @Override
            public void onNext(T t) {
                Log.i(TAG, observer + " Observer Received " + t);
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, observer + " Observer onError ");
            }

            @Override
            public void onComplete() {
                Log.i(TAG, observer + " Observer onComplete ");
            }
        };
    }
}