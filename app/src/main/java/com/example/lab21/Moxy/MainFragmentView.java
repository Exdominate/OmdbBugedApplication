package com.example.lab21.Moxy;

import android.net.ConnectivityManager;

import com.arellomobile.mvp.MvpView;
import com.example.lab21.MainActivity;

public interface MainFragmentView extends MvpView {
    void loadInternetData();

    void loadInternetRecord();

    void setConnectivityManager(ConnectivityManager cm);

    void setParent(MainActivity parent);
}
