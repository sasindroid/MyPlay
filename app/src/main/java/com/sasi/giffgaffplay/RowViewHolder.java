package com.sasi.giffgaffplay;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by sasikumarlakshmanan on 13/10/15.
 */
public class RowViewHolder extends RecyclerView.ViewHolder {
    TextView tvTitle;

    public RowViewHolder(View itemView) {
        super(itemView);

        this.tvTitle = (TextView) itemView.findViewById(R.id.tvTitle)
;    }
}
