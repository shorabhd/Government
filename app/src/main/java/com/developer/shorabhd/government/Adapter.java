package com.developer.shorabhd.government;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shorabhd on 3/31/17.
 */

public class Adapter extends RecyclerView.Adapter<ViewHolder> {
    private static final String TAG = "StockAdapter";
    private List<Official> mOfficicalList;
    private MainActivity mainAct;

    public Adapter(List<Official> mOfficicalList, MainActivity ma) {
        this.mOfficicalList = mOfficicalList;
        mainAct = ma;
    }

    public Adapter(ArrayList<Official> mOfficicalList) {
        this.mOfficicalList = mOfficicalList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                              int viewType) {
        Log.d(TAG, "onCreateViewHolder: MAKING NEW");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_item, parent, false);
        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);
        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Official official = mOfficicalList.get(position);
        holder.mRole.setText(official.getOffice());
        if(official.getParty().equals("Unknown"))
            holder.mName.setText(official.getName());
        else
            holder.mName.setText(official.getName()+" ("+official.getParty()+")");

    }

    @Override
    public int getItemCount() {
        return mOfficicalList.size();
    }
}
