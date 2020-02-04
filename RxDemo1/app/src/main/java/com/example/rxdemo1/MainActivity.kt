package com.example.rxdemo1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.rxdemo1.databinding.ActivityMainBinding
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private val greeting = "HelloFrom RxJava"
    lateinit var observable: Observable<String>
    lateinit var observer: DisposableObserver<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setUpBinding()
        setUpObservers()
    }

    private fun setUpObservers() {
        observer = object : DisposableObserver<String>() {
            override fun onComplete() {
                log("onComplete()")
            }

            override fun onNext(t: String) {
                log("onNext()")
                binding.label.text = t
            }

            override fun onError(e: Throwable) {
                log("onError()")
            }

        }

        observable = Observable.just(greeting)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        observable.subscribe(observer)
    }

    private fun setUpBinding() {

    }

    override fun onDestroy() {
        super.onDestroy()

        if (! observer.isDisposed) {
            observer.dispose()
        }
    }
}
