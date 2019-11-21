package com.example.lab21.mvp;

import android.net.ConnectivityManager;

import com.example.lab21.NetworkService;
import com.example.lab21.Room.AppDb;
import com.example.lab21.Room.OmdbDao;

import java.util.Arrays;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;


@InjectViewState
public class MainFragmentPresenter extends MvpPresenter<MainFragmentView> {

    ConnectivityManager cm;
    private NetworkService networkService;
    private CompositeDisposable disposibles = new CompositeDisposable();
    private AppDb moviesDb;

    private String API_KEY = "d862f73d";

    public void initConnectivityManager(ConnectivityManager cm){
        if (this.cm==null) this.cm=cm;
        getViewState().setConnectivityManager(this.cm);
    }

    public void setNetworkService(NetworkService service) {
        networkService = service;
    }

    public void setMoviesDb(AppDb db) {
        moviesDb = db;
    }

    public void loadInternetRecord(String strForSearch){
        if (disposibles.isDisposed()) disposibles = new CompositeDisposable();
        disposibles.add(networkService.getJSONapi()//new OmdbSingle()//
                .getByTitle(strForSearch, API_KEY)
                .subscribeOn(Schedulers.io())
                .map(record -> {
                    OmdbDao dao = moviesDb.omdbDao();
                    dao.insert(record);
                    return record;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(record -> {
                    getViewState().showInternetRecord(record);

                }, error -> {
                    getViewState().showError("ERROR OCCURED" + error.getMessage());
                }));
    }

    public void loadInternetData(){
        getViewState().loadInternetData();
    }
}
