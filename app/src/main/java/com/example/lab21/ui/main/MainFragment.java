package com.example.lab21.ui.main;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lab21.MainActivity;
import com.example.lab21.mvp.MainActivityPresenter;
import com.example.lab21.mvp.MainFragmentView;
import com.example.lab21.mvp.MainFragmentPresenter;
import com.example.lab21.MyRecycleViewAdapter;
import com.example.lab21.NetworkService;
import com.example.lab21.R;
import com.example.lab21.Record;
import com.example.lab21.RecycleViewOnClickListener;
import com.example.lab21.Room.AppDb;
import com.example.lab21.Room.DbSingleton;
import com.example.lab21.Room.OmdbDao;

import java.util.Arrays;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;

public class MainFragment extends MvpAppCompatFragment
        implements RecycleViewOnClickListener, MainFragmentView {
    @InjectPresenter
    MainFragmentPresenter presenter;

    public final static String ID = "VeryUniqueId";
    private String API_KEY = "d862f73d";
    private RecyclerView recyclerView;
    private MyRecycleViewAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private NetworkService networkService;
    private CompositeDisposable disposibles = new CompositeDisposable();
    private AppDb moviesDb;
    private ConnectivityManager cm;
    private MainActivity parent;


    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        networkService = NetworkService.getInstance();
        // 6 .подключаем бд
        moviesDb = DbSingleton.getInstance(getContext()).getAppDb();
        // 7. устанавливаем родительского презентера в наш презентер один раз
        //if (parentPresenter!=null) this.presenter.setParentPresenter(parentPresenter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.searchButton).setOnClickListener(this::clickedLoadInternetData);
        view.findViewById(R.id.findButton).setOnClickListener(this::clickedLoadInternetRecord);
        // 1. get recycle view
        recyclerView = view.findViewById(R.id.recordsRecycleView);
        // 2. use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        // 3. создаем адаптер
        mAdapter = new MyRecycleViewAdapter(view.getContext(),this);
        // 4. устанавливаем для списка адаптер
        recyclerView.setAdapter(mAdapter);
        // 5. set item animator to DefaultAnimator
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        ConnectivityManager tcm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        presenter.initConnectivityManager(tcm);
        presenter.setMoviesDb(moviesDb);
        presenter.setNetworkService(networkService);
    }

    @Override
    public void setParent(MainActivity parent){
    }

    public void clickedLoadInternetRecord(View v){
        String text = ((EditText) getView().findViewById(R.id.searchText)).getText().toString();
        presenter.loadInternetRecord(text);
    }

    public void clickedLoadInternetData(View v){
        presenter.loadInternetData();
    }

    @Override
    public void setConnectivityManager(ConnectivityManager cm) {
        this.cm=cm;
    }

    @Override
    public void showInternetRecord(Record record) {
        mAdapter.setRecords(Arrays.asList(record));
        mAdapter.notifyDataSetChanged();
        disposibles.dispose();
        hideKeyboardFrom(getContext(), getView());
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadInternetData() {
        String text = ((EditText) getView().findViewById(R.id.searchText)).getText().toString();
        if (text.length() == 0) return;
        if (disposibles.isDisposed()) disposibles = new CompositeDisposable();
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            disposibles.add(networkService.getJSONapi()//new OmdbSingle()//
                    .searchByTitle(text, API_KEY)
                    .subscribeOn(Schedulers.io())
                    .map(searchResponse -> {
                        OmdbDao dao = moviesDb.omdbDao();
                        for (Record record : searchResponse.getRecords()) {
                            dao.insert(record);
                        }
                        return searchResponse;
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(records -> {
                        mAdapter.setRecords(records.getRecords());
                        mAdapter.notifyDataSetChanged();
                        disposibles.dispose();
                        hideKeyboardFrom(getContext(), getView());
                    }, error -> {
                        error.printStackTrace();
                    }));
        } else {//try to load from db
            disposibles.add(moviesDb.omdbDao()
                    .getByTitle("%" + text + "%")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(records -> {
                        if (records != null && records.size() != 0) {
                            mAdapter.setRecords(records);
                            mAdapter.notifyDataSetChanged();
                            hideKeyboardFrom(getContext(), getView());
                        } else {
                            Toast toast = Toast.makeText(getContext(),
                                    "Запрашиваемые данные недоступны!", Toast.LENGTH_LONG);
                            toast.show();
                        }
                        disposibles.dispose();
                    }, error -> {
                    }));
        }
    }

    @Override
    public void onClick(Record clickedRecord) {
        ((MainActivity) getActivity()).changeFragment(clickedRecord);
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
