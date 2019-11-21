package com.example.lab21.mvp;

import com.example.lab21.Record;

import moxy.InjectViewState;
import moxy.MvpPresenter;

@InjectViewState
public class RecordDetailPresenter extends MvpPresenter<RecordDetailView> {
    public void showData(Record record){
        if (record!=null) {
            getViewState().loadData(record);
        }

    }
}
