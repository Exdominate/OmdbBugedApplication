package com.example.lab21.Moxy;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.example.lab21.Record;

@InjectViewState
public class RecordDetailPresenter extends MvpPresenter<RecordDetailView> {
    @StateStrategyType(SkipStrategy.class)
    public void showData(Record record){
        if (record!=null) {
            getViewState().loadData(record);
        }

    }
}
