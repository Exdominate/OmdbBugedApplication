package com.example.lab21.mvp;

import android.net.ConnectivityManager;

import com.example.lab21.MainActivity;
import com.example.lab21.Record;

import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.SingleStateStrategy;
import moxy.viewstate.strategy.StateStrategyType;

public interface MainFragmentView extends MvpView {
    @StateStrategyType(SingleStateStrategy.class)
    void loadInternetData();

    @StateStrategyType(SingleStateStrategy.class)
    void showInternetRecord(Record record);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setConnectivityManager(ConnectivityManager cm);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setParent(MainActivity parent);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showError(String message);
}
