package com.example.lab21;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.lab21.ui.main.MainFragment;
import com.example.lab21.ui.main.RecordDetailsFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String TAG="MainActivity";
    ConnectivityManager cm;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_activity);
        //подключаем connectivity manager
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance(cm,this))
                    .addToBackStack(MainFragment.ID)
                    .commit();
        }
        Log.i(TAG, "onCreate: ");
    }

    public void changeFragment(Record record){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, RecordDetailsFragment.newInstance(record))
                .addToBackStack(RecordDetailsFragment.ID)
                .commit();
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
        Log.i(TAG, "onDestroy: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");
    }
}
