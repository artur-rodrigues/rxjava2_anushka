package com.androidtutz.anushka.moviesapp.model;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;

import com.androidtutz.anushka.moviesapp.R;
import com.androidtutz.anushka.moviesapp.service.MoviesDataService;
import com.androidtutz.anushka.moviesapp.service.RetrofitInstance;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MovieRepository {

    private Application application;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MutableLiveData<List<Movie>> movieLiveData = new MutableLiveData<>();
    private ArrayList<Movie> movies;
    private Observable<MovieDBResponse> observable;

    public MovieRepository(Application application) {
        this.application = application;

        movies = new ArrayList<>();
        MoviesDataService getMoviesDataService = RetrofitInstance.getService();
        observable = getMoviesDataService.getPopularMoviesWithRx(application.getString(R.string.api_key));

        compositeDisposable.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap((Function<MovieDBResponse, Observable<Movie>>) movieDBResponse -> Observable.fromArray(movieDBResponse.getMovies().toArray(new Movie[0])))
                .filter(movie -> movie.getVoteAverage()>7.0)
                .subscribeWith(new DisposableObserver<Movie>() {
                    @Override
                    public void onNext(Movie movie) {
                        movies.add(movie);
                    }

                    @Override
                    public void onError(Throwable e) {}

                    @Override
                    public void onComplete() {
                        movieLiveData.postValue(movies);
                    }
                }));
    }

    public MutableLiveData<List<Movie>> getMovieLiveData() {
        return movieLiveData;
    }

    public void clear() {
        if (! compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }
}
