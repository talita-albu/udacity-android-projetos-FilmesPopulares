package com.talitaalbu.android.filmesfamosos.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.talitaalbu.android.filmesfamosos.R;
import com.talitaalbu.android.filmesfamosos.model.Movie;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by talita.a.de.araujo on 26/12/2017.
 */

public class MovieAdapter  extends RecyclerView.Adapter <MovieAdapter.MovieHolder> {
    private List<Movie> movieData;
    private final Context mContext;
    private boolean showLoadMore = false;

    private final MovieAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface MovieAdapterOnClickHandler {
        void onClick(Movie selectedMovie);
    }

    public MovieAdapter(MovieAdapterOnClickHandler clickHandler, Context context) {
        mClickHandler = clickHandler;
        mContext = context;
    }


    @Override
    public MovieHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(R.layout.movie_info_list_item, viewGroup, shouldAttachToParentImmediately);
        return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieHolder viewHolder, int position) {
        Movie movie = movieData.get(position);
        Picasso.with(mContext).load(movie.getPosterPath()).into(viewHolder.mImage);

        viewHolder.mMovieTitle.setText(movie.getOriginalTitle());
        String dataFormatada = "";
        try {
            String dataEmUmFormato = movie.getReleaseDate();
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
            Date data = formato.parse(dataEmUmFormato);
            formato.applyPattern("dd/MM/yyyy");
            dataFormatada = formato.format(data);
        }
        catch (ParseException ex)
        {
            Log.e("Conversão", ex.getMessage() );
        }

        viewHolder.mReleaseDate.setText(dataFormatada);
    }

    @Override
    public int getItemCount() {
        if (null == movieData) return 0;
        return movieData.size();
    }

    public void setData(List<Movie> mMovieData) {
        movieData = mMovieData;
        notifyDataSetChanged();
    }

    public boolean isLoadMore(int position) {
        return showLoadMore && (position == (getItemCount() - 1));
    }

    class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView mImage;
        private final TextView mMovieTitle;
        private final TextView mReleaseDate;

        public MovieHolder(View view) {
            super(view);
            mImage = (ImageView) view.findViewById(R.id.iv_poster_image);
            mMovieTitle = (TextView) view.findViewById(R.id.movie_item_title);
            mReleaseDate = (TextView) view.findViewById(R.id.movie_item_released);
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
            Movie movie = movieData.get(adapterPosition);
            mClickHandler.onClick(movie);
        }
    }

}

