package com.example.lab21.Moxy;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.example.lab21.MainActivity;
import com.example.lab21.ui.main.MainFragment;

@InjectViewState
public class MainActivityPresenter extends MvpPresenter<MainActivityView> {
    MainActivity mainActivityInstance;

    @StateStrategyType(AddToEndSingleStrategy.class)
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
