package com.example.lab21.mvp;

import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.StateStrategyType;

public interface MainActivityView extends MvpView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void getActivityInstance();
}
