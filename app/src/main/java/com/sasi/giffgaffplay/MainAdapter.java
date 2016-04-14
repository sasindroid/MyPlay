package com.sasi.giffgaffplay;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by sasikumarlakshmanan on 13/10/15.
 */
public class MainAdapter extends RecyclerView.Adapter<RowViewHolder> {

    Context context;
    ArrayList<RowItems> itemsList;

    public MainAdapter(Context context, ArrayList<RowItems> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
    }

    @Override
    public RowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.single_row, null);
        RowViewHolder viewHolder = new RowViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RowViewHolder holder, int position) {

//        Toast.makeText(context, position + " Size: " + getItemCount(), Toast.LENGTH_SHORT).show();

        RowItems rowItems = itemsList.get(position);
        holder.tvTitle.setText(rowItems.getTitle());

    }

    @Override
    public int getItemCount() {

        if(itemsList == null) {
            return 0;
        }
        else {
            return itemsList.size();
        }
    }


}
