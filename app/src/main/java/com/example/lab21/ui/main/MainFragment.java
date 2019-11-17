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

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.lab21.MainActivity;
import com.example.lab21.Moxy.MainActivityPresenter;
import com.example.lab21.Moxy.MainFragmentView;
import com.example.lab21.Moxy.MainFragmentPresenter;
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

public class MainFragment extends MvpAppCompatFragment
        implements RecycleViewOnClickListener, MainFragmentView {
    @InjectPresenter
    MainFragmentPresenter presenter;
    MainActivityPresenter parentPresenter;

    public final static String ID = "VeryUniqueId";
    private String API_KEY = "d862f73d";
    private RecyclerView recyclerView;
    private MyRecycleViewAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private NetworkService networkService;
    private View rootView;
    private CompositeDisposable disposibles = new CompositeDisposable();
    private AppDb moviesDb;
    private Context appCntxt;
    private ConnectivityManager cm;
    private MainActivity parent;


    public static MainFragment newInstance() {
        return new MainFragment();
    }

    public MainFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        networkService = NetworkService.getInstance();
        rootView = inflater.inflate(R.layout.main_fragment, container, false);
        //on click listeners
        rootView.findViewById(R.id.searchButton).setOnClickListener(this::clickedLoadInternetData);
        rootView.findViewById(R.id.findButton).setOnClickListener(this::clickedLoadInternetRecord);
        // 1. get recycle view
        recyclerView = rootView.findViewById(R.id.recordsRecycleView);
        // 2. use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        // 3. создаем адаптер
        mAdapter = new MyRecycleViewAdapter(rootView.getContext(),this);
        // 4. устанавливаем для списка адаптер
        recyclerView.setAdapter(mAdapter);
        // 5. set item animator to DefaultAnimator
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // 6 .подключаем бд
        moviesDb = DbSingleton.getInstance(rootView.getContext()).getAppDb();
        // 7. устанавливаем родительского презентера в наш презентер один раз
        if (parentPresenter!=null) this.presenter.setParentPresenter(parentPresenter);
        appCntxt = rootView.getContext();
        return rootView;
    }

    @Override
    public void setParent(MainActivity parent){
        //когда всё таки получили родителя
        this.parent=parent;
        // 8. добавляем connectivityManager
        ConnectivityManager tcm=null;
        if (parent!=null) tcm = parent.getConnectivityManager();
        presenter.initConnectivityManager(tcm);
    }

    public void setParentPresenter(MainActivityPresenter _presenter){
        parentPresenter=_presenter;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
    }

    public void clickedLoadInternetRecord(View v){
        presenter.giveMeParent();
        if (parent!=null) presenter.loadInternetRecord();
    }

    public void clickedLoadInternetData(View v){
        presenter.giveMeParent();
        if (parent!=null) presenter.loadInternetData();
    }

    @Override
    public void setConnectivityManager(ConnectivityManager cm) {
        this.cm=cm;
    }

    public void loadInternetRecord() {
        String text = ((EditText) rootView.findViewById(R.id.searchText)).getText().toString();
        if (text.length() == 0) return;
        if (disposibles.isDisposed()) disposibles = new CompositeDisposable();
        disposibles.add(networkService.getJSONapi()//new OmdbSingle()//
                .getByTitle(text, API_KEY)
                .subscribeOn(Schedulers.io())
                .map(record -> {
                    OmdbDao dao = moviesDb.omdbDao();
                        dao.insert(record);
                    return record;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(record -> {
                    mAdapter.setRecords(Arrays.asList(record));
                    mAdapter.notifyDataSetChanged();
                    disposibles.dispose();
                    hideKeyboardFrom(appCntxt,rootView);
                }, error -> {
                    System.out.println("ERROR OCCURED" + error.getMessage());
                }));
    }

    public void loadInternetData() {
        String text = ((EditText) rootView.findViewById(R.id.searchText)).getText().toString();
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
                        hideKeyboardFrom(appCntxt,rootView);
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
                            hideKeyboardFrom(appCntxt,rootView);
                        } else {
                            Toast toast = Toast.makeText(appCntxt,
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
        presenter.giveMeParent();
        if (parent!=null) parent.changeFragment(clickedRecord);
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
