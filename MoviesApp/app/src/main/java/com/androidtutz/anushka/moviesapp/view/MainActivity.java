package com.androidtutz.anushka.moviesapp.view;

import android.content.res.Configuration;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.androidtutz.anushka.moviesapp.R;
import com.androidtutz.anushka.moviesapp.adapter.MovieAdapter;
import com.androidtutz.anushka.moviesapp.model.Movie;
import com.androidtutz.anushka.moviesapp.model.MovieDBResponse;
import com.androidtutz.anushka.moviesapp.service.MoviesDataService;
import com.androidtutz.anushka.moviesapp.service.RetrofitInstance;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Movie> movies;
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private SwipeRefreshLayout swipeContainer;
    private Call<MovieDBResponse> call;
    private DisposableObserver<Movie> observable;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("TMDb Popular Movies Today");

        swipeContainer = findViewById(R.id.swipe_layout);
        swipeContainer.setColorSchemeResources(R.color.colorPrimary);
        swipeContainer.setOnRefreshListener(this::getPopularMoviesWithRx);

        getPopularMoviesWithRx();
    }

    /*public void getPopularMovies() {

        movies = new ArrayList<>();
        MoviesDataService getMoviesDataService = RetrofitInstance.getService();
        call = getMoviesDataService.getPopularMovies(this.getString(R.string.api_key));

        call.enqueue(new Callback<MovieDBResponse>() {
            @Override
            public void onResponse(Call<MovieDBResponse> call, Response<MovieDBResponse> response) {

                MovieDBResponse movieDBResponse = response.body();

                if (movieDBResponse != null && movieDBResponse.getMovies() != null) {


                    movies = (ArrayList<Movie>) movieDBResponse.getMovies();
                    init();


                }


            }

            @Override
            public void onFailure(Call<MovieDBResponse> call, Throwable t) {

                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(getApplicationContext(), "Socket Time out.", Toast.LENGTH_LONG).show();
                }


            }
        });


    }*/

    private void getPopularMoviesWithRx() {
        movies = new ArrayList<>();
        MoviesDataService getMoviesDataService = RetrofitInstance.getService();
        observable = getMoviesDataService.getPopularMoviesWithRx(this.getString(R.string.api_key))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(movieDBResponse ->
                        Observable.fromArray(movieDBResponse.getMovies().toArray(new Movie[0])))
                .filter(movie -> movie.getVoteAverage() > 7.0 )
                .subscribeWith(new DisposableObserver<Movie>() {

                    @Override
                    public void onNext(Movie movie) {
                        movies.add(movie);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        init();
                    }
                });

        compositeDisposable.add(observable);

        // Teste com map
        /*compositeDisposable.add(
                getMoviesDataService.getPopularMoviesWithRx(this.getString(R.string.api_key))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(MovieDBResponse::getMovies)
                        .subscribeWith(new DisposableObserver<List<Movie>>() {

                            @Override
                            public void onNext(List<Movie> movies) {
                                MainActivity.this.movies = (ArrayList<Movie>) movies;
                            }

                            @Override
                            public void onError(Throwable e) {
                            }

                            @Override
                            public void onComplete() {
                                init();
                            }
                        })
        );*/
    }

    public void init() {
        recyclerView = findViewById(R.id.rvMovies);
        movieAdapter = new MovieAdapter(this, movies);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(movieAdapter);
        movieAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (call != null) {
            if (call.isExecuted()) {
                call.cancel();
            }
        }

        if (! compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }
}


