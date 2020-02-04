package com.example.justdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.justdemo.databinding.ActivityMainBinding
import io.reactivex.Observable
import io.reactivex.observers.DisposableObserver

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val greeting = "HelloFrom RxJava"
    private lateinit var observable: Observable<String>
    private lateinit var observer: DisposableObserver<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setUpObservers()
    }

    private fun setUpObservers() {

    }

    override fun onDestroy() {
        super.onDestroy()
        if (!observer.isDisposed) {
            observer.dispose()
        }
    }
}
