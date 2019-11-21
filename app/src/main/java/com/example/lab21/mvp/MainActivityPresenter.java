package com.example.lab21.mvp;

import com.example.lab21.MainActivity;

import moxy.InjectViewState;
import moxy.MvpPresenter;

@InjectViewState
public class MainActivityPresenter extends MvpPresenter<MainActivityView> {
    MainActivity mainActivityInstance;

    public void setActualActivity(){
        getViewState().getActivityInstance();
    }

    public void setMainActivityInstance(MainActivity instance) {
        this.mainActivityInstance = instance;
    }

    public MainActivity getMainActivityInstance() {
        return mainActivityInstance;
    }
}
