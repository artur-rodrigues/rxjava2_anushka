package com.example.rxdemo1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.rxdemo1.databinding.ActivityMainBinding
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    val greeting = "HelloFrom RxJava"
    lateinit var observable: Observable<String>
    lateinit var observer: Observer<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setUpBinding()
        setUpObservers()
    }

    private fun setUpObservers() {
        observer = object : Observer<String> {
            override fun onComplete() {
                log("onComplete()")
            }

            override fun onSubscribe(d: Disposable) {
                log("onSubscribe()")
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
        observable.subscribe(observer)
    }

    private fun setUpBinding() {

    }
}
