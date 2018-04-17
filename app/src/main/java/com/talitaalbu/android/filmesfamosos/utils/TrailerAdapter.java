package com.talitaalbu.android.filmesfamosos.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.talitaalbu.android.filmesfamosos.R;
import com.talitaalbu.android.filmesfamosos.model.Movie;
import com.talitaalbu.android.filmesfamosos.model.Trailer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by talita.a.de.araujo on 26/12/2017.
 */

public class TrailerAdapter extends RecyclerView.Adapter <TrailerAdapter.TrailerHolder> {
    private List<Trailer> trailerData;
    private final Context mContext;
    private boolean showLoadMore = false;

    private final TrailerAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface TrailerAdapterOnClickHandler {
        void onClick(Trailer selectedTrailer);
    }

    public TrailerAdapter(TrailerAdapterOnClickHandler clickHandler, Context context) {
        mClickHandler = clickHandler;
        mContext = context;
    }


    @Override
    public TrailerHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(R.layout.trailer_list_item, viewGroup, shouldAttachToParentImmediately);
        return new TrailerHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerHolder viewHolder, int position) {
        Trailer t = trailerData.get(position);
        viewHolder.mTrailerCounting.setText(mContext.getString(R.string.trailer_info) + " " + (position+1));
    }

    @Override
    public int getItemCount() {
        if (null == trailerData) return 0;
        return trailerData.size();
    }

    public void setData(List<Trailer> mTrailerData) {
        trailerData = mTrailerData;
        notifyDataSetChanged();
    }

    public Trailer getItem(int position)
    {
        return trailerData.get(position);
    }

    public boolean isLoadMore(int position) {
        return showLoadMore && (position == (getItemCount() - 1));
    }

    class TrailerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageButton mImage;
        private final TextView mTrailerCounting;

        public TrailerHolder(View view) {
            super(view);
            mImage = (ImageButton) view.findViewById(R.id.btn_play);
            mTrailerCounting = (TextView) view.findViewById(R.id.tv_trailer_info);
            view.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click.
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Trailer t = trailerData.get(adapterPosition);
            mClickHandler.onClick(t);
        }
    }

}

