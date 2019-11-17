package com.example.lab21.Moxy;

import android.net.ConnectivityManager;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.example.lab21.MainActivity;

@InjectViewState
public class MainFragmentPresenter extends MvpPresenter<MainFragmentView> {
    ConnectivityManager cm;
    MainActivityPresenter parentPresenter;

    public void setParentPresenter(MainActivityPresenter parentPresenter) {
        this.parentPresenter = parentPresenter;
    }

    @StateStrategyType(AddToEndSingleStrategy.class)
    public void initConnectivityManager(ConnectivityManager cm){
        if (this.cm==null) this.cm=cm;
        getViewState().setConnectivityManager(this.cm);
    }

    @StateStrategyType(AddToEndSingleStrategy.class)
    public void giveMeParent(){
        getViewState().setParent(parentPresenter.getMainActivityInstance());
    }

    @StateStrategyType(SingleStateStrategy.class)
    public void loadInternetRecord(){
        getViewState().loadInternetRecord();
    }

    @StateStrategyType(SingleStateStrategy.class)
    public void loadInternetData(){
        getViewState().loadInternetData();
    }
}
