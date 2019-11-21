package com.example.lab21.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.lab21.mvp.RecordDetailPresenter;
import com.example.lab21.mvp.RecordDetailView;
import com.example.lab21.R;
import com.example.lab21.Record;

import java.util.Random;

import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;

public class RecordDetailsFragment extends MvpAppCompatFragment implements RecordDetailView {
    @InjectPresenter
    RecordDetailPresenter presenter;

    public final static String ID = String.valueOf(new Random().nextInt());
    private View rootView;
    private TextView title, year, released, duration, genre,
            Rated, director, imdbRating, imdbVotes, metascore, type;
    private ImageView poster;
    private Record record;

    public static RecordDetailsFragment newInstance(Record record) {
        return new RecordDetailsFragment(record);
    }

    public RecordDetailsFragment(Record record) {
        this.record = record;
    }

    public RecordDetailsFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.detailed_fragment, container, false);
        presenter.showData(record);
        return rootView;
    }

    @Override
    public void loadData(Record record){
        /*initialize elements*/
        poster = (ImageView) rootView.findViewById(R.id.detailedImage);
        title = (TextView) rootView.findViewById(R.id.title);
        year = (TextView) rootView.findViewById(R.id.year);
        released = (TextView) rootView.findViewById(R.id.released);
        duration = (TextView) rootView.findViewById(R.id.duration);
        genre = (TextView) rootView.findViewById(R.id.genre);
        Rated = (TextView) rootView.findViewById(R.id.rated);
        director = (TextView) rootView.findViewById(R.id.director);
        imdbRating = (TextView) rootView.findViewById(R.id.imdbRate);
        imdbVotes = (TextView) rootView.findViewById(R.id.imdbVotes);
        metascore = (TextView) rootView.findViewById(R.id.metascore);
        type = (TextView) rootView.findViewById(R.id.type);
        /*set text*/
        title.setText(title.getText() + nullToEmpty(record.getTitle()));
        year.setText(year.getText() + nullToEmpty(record.getYear()));
        released.setText(released.getText() + nullToEmpty(record.getReleased()));
        duration.setText(duration.getText() + nullToEmpty(record.getDuration()));
        genre.setText(genre.getText() + nullToEmpty(record.getGenre()));
        Rated.setText(Rated.getText() + nullToEmpty(record.getRated()));
        director.setText(director.getText() +nullToEmpty( record.getDirector()));
        imdbRating.setText(imdbRating.getText() +nullToEmpty(record.getImdbRating()));
        imdbVotes.setText(imdbVotes.getText() + nullToEmpty(record.getImdbVotes()));
        metascore.setText(metascore.getText() +nullToEmpty(record.getMetascore()));
        type.setText(type.getText() +nullToEmpty( record.getType()));
        /*image*/
        try {
            Glide.with(rootView.getContext())
                    .asBitmap()
                    .load(record.getPosterURL())
                    .into(poster);
        } catch (Exception e) {
            System.out.println("Exception::Glide::" + e.getMessage());
        }
    }

    private String nullToEmpty(String str){
        return (str!=null&&str.length()>0?str:"");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
