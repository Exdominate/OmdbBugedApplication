package com.example.lab21;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class MyRecycleViewAdapter extends RecyclerView.Adapter<MyRecycleViewAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private List<Record> records=new ArrayList<>();
    private Context parentContext;
    private RecycleViewOnClickListener clickHandler;
  //  private View.OnClickListener myClickListener;

    public Record getRecord(int position){
        return records.get(position);
    }

    public void setRecords(List<Record> records) {
        if (records!=null) this.records = records;
    }

    public MyRecycleViewAdapter(Context context, RecycleViewOnClickListener clickHandler) {
       // if (records!=null) this.records = records;
        this.parentContext = context;
        this.inflater = LayoutInflater.from(context);
        this.clickHandler=clickHandler;
        /*myClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Test");
            }
        };*/
    }

    @NonNull
    @Override
    public MyRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
     /*   view.setOnClickListener(myClickListener);*/
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecycleViewAdapter.ViewHolder holder, int position) {
        Record record = records.get(position);
        holder.titleView.setText(record.getTitle());
        holder.yearView.setText("Release year: "+record.getYear());
        holder.durationView.setText("Type: "+record.getType());
        holder.itemView.setOnClickListener(v -> clickHandler.onClick(record));
        //for image
        RequestOptions myOptions = new RequestOptions()
                .fitCenter();
        try {
            Glide.with(parentContext)
                    .asBitmap()
                    .apply(myOptions)
                    .load(record.getPosterURL())
                    .into(holder.imageView);
        }
        catch (Exception e){
            System.out.println("Exception::Glide::"+e.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        final ImageView imageView;
        final TextView titleView, yearView, durationView;

        ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.image);
            titleView = (TextView) view.findViewById(R.id.titleView);
            yearView = (TextView) view.findViewById(R.id.yearView);
            durationView = (TextView) view.findViewById(R.id.typeView);
        }
    }
}
