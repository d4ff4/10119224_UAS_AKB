package com.programmer.moviecatalogue;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.programmer.moviecatalogue.activity.MainActivity;
import com.programmer.moviecatalogue.api.ApiClient;
import com.programmer.moviecatalogue.api.ApiEndpoints;
import com.programmer.moviecatalogue.model.Movie;
import com.programmer.moviecatalogue.model.MovieResponse;

public class MainViewModel extends ViewModel {
	private MutableLiveData<ArrayList<Movie>> movieList = new MutableLiveData<>();
	private ApiEndpoints apiService = ApiClient.getClient().create(ApiEndpoints.class);

	public void setMovies(final String movieType) {
		Call<MovieResponse> call = apiService.getMovies(movieType);
		call.enqueue(new Callback<MovieResponse>() {
			@Override
			public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
				try {
					ArrayList<Movie> movies = response.body().getResults();
					for (Movie data : movies) {
						data.setMovieType(movieType);
					}
					movieList.postValue(movies);
				} catch (Exception e) {
					Log.d(MainActivity.class.getSimpleName(), e.getLocalizedMessage());
				}
			}

			@Override
			public void onFailure(Call<MovieResponse> call, Throwable t) {
				Log.d(MainActivity.class.getSimpleName(), t.getLocalizedMessage());
			}
		});
	}

	public void setFavMovie(ArrayList<Movie> movies) {
		movieList.postValue(movies);
	}

	public void searchMovie(String query) {
		Call<MovieResponse> call = apiService.searchMovies(query);
		call.enqueue(new Callback<MovieResponse>() {
			@Override
			public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
				try {
					ArrayList<Movie> movies = response.body().getResults();
					movieList.postValue(movies);
					Log.d(MainActivity.class.getSimpleName(), movies.toString());
				} catch (Exception e) {
					Log.d(MainActivity.class.getSimpleName(), e.getLocalizedMessage());
				}
			}

			@Override
			public void onFailure(Call<MovieResponse> call, Throwable t) {
				Log.d(MainActivity.class.getSimpleName(), t.getLocalizedMessage());
			}
		});
	}

	public LiveData<ArrayList<Movie>> getMovies() {
		return movieList;
	}

}
