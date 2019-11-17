package com.example.lab21;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.lab21.Moxy.MainActivityPresenter;
import com.example.lab21.Moxy.MainActivityView;
import com.example.lab21.ui.main.MainFragment;
import com.example.lab21.ui.main.RecordDetailsFragment;

public class MainActivity extends MvpAppCompatActivity implements MainActivityView {
    @InjectPresenter
    MainActivityPresenter presenter;
    private final String TAG="MainActivity";
    
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            MainFragment fragment=MainFragment.newInstance();
            presenter.setMainActivityInstance(this);
            presenter.setActualActivity();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container,fragment)
                    .addToBackStack(MainFragment.ID)
                    .commit();
            fragment.setParentPresenter(presenter);
        }
        Log.i(TAG, "onCreate: ");
    }

    @Override
    public void getActivityInstance() {
        presenter.setMainActivityInstance(this);
    }

    public void changeFragment(Record record){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, RecordDetailsFragment.newInstance(record))
                .addToBackStack(RecordDetailsFragment.ID)
                .commit();
    }

    //подключаем connectivity manager
    public ConnectivityManager getConnectivityManager(){
        return  (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: ");
    }
    
    @Override
    protected void onResume(){
        super.onResume();
        Log.i(TAG, "onResume: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.setMainActivityInstance(null);
        Log.i(TAG, "onDestroy: ");
    }
}
