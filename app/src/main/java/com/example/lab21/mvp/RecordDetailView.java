package com.example.lab21.mvp;

import com.example.lab21.Record;

import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndStrategy;
import moxy.viewstate.strategy.SkipStrategy;
import moxy.viewstate.strategy.StateStrategyType;

public interface RecordDetailView extends MvpView {
    @StateStrategyType(AddToEndStrategy.class)
    void loadData(Record record);
}
