package com.example.justdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.justdemo.databinding.ActivityMainBinding
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

//    private val greeting = arrayOf("Hello A", "Hello B", "Hello C", "Hello D")
    private lateinit var observable: Observable<String>
    private lateinit var observer: DisposableObserver<String>

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setUpObservers()
    }

    private fun setUpObservers() {
        observable = Observable.just("Hello A", "Hello B", "Hello C", "Hello D")

        disposable.add(
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getObserver())
        )
    }

    private fun getObserver(): DisposableObserver<String> {
        observer = object : DisposableObserver<String>() {
            override fun onComplete() {
                log("onComplete()")
            }

            override fun onNext(t: String) {
                log("onNext()")
            }

            override fun onError(e: Throwable) {
                log("onError()")
            }

        }

        return observer
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
    }
}
