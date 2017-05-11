package com.developer.shorabhd.government;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by shorabhd on 3/31/17.
 */

public class ViewHolder extends RecyclerView.ViewHolder {
    public TextView mRole;
    public TextView mName;


    public ViewHolder(View view) {
        super(view);
        mRole = (TextView) view.findViewById(R.id.role);
        mName = (TextView) view.findViewById(R.id.name);

    }
}
