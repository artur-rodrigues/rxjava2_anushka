package com.anushka.androidtutz.rxbindingdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


public class MainActivity extends AppCompatActivity {

    private EditText inputText;
    private TextView viewText;
    private Button clearButton;

    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputText = findViewById(R.id.etInputField);
        viewText = findViewById(R.id.tvInput);
        clearButton = findViewById(R.id.btnClear);

        disposable.add(
                RxTextView.textChanges(inputText)
                        .subscribe(consumer -> viewText.setText(consumer))
        );

        disposable.add(
                RxView.clicks(clearButton)
                .subscribe(consumer -> {
                    viewText.setText("");
                    inputText.setText("");
                })
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(! disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
