package com.example.lab21.Moxy;

import com.arellomobile.mvp.MvpView;
import com.example.lab21.Record;

public interface RecordDetailView extends MvpView {
    void loadData(Record record);
}
