package com.androidtutz.anushka.moviesapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.androidtutz.anushka.moviesapp.model.Movie;
import com.androidtutz.anushka.moviesapp.model.MovieRepository;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    private MovieRepository repository;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        repository = new MovieRepository(application);
    }

    public LiveData<List<Movie>> getAllMovies() {
        return repository.getMovieLiveData();
    }



    @Override
    protected void onCleared() {
        super.onCleared();

        repository.clear();
    }
}
