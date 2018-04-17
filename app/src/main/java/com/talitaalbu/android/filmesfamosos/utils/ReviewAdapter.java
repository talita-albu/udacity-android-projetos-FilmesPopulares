package com.talitaalbu.android.filmesfamosos.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.talitaalbu.android.filmesfamosos.R;
import com.talitaalbu.android.filmesfamosos.model.Review;
import com.talitaalbu.android.filmesfamosos.model.Trailer;

import java.util.List;

/**
 * Created by talita.a.de.araujo on 26/12/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter <ReviewAdapter.ReviewHolder> {
    private List<Review> reviewData;
    private final Context mContext;
    private boolean showLoadMore = false;

    public ReviewAdapter(Context context) {
        mContext = context;
    }


    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(R.layout.review_list_item, viewGroup, shouldAttachToParentImmediately);
        return new ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewHolder viewHolder, int position) {
        Review r = reviewData.get(position);
        viewHolder.mUserName.setText(r.getAuthor());
        viewHolder.mUserReview.setText(r.getReview());
    }

    @Override
    public int getItemCount() {
        if (null == reviewData) return 0;
        return reviewData.size();
    }

    public void setData(List<Review> mReviewData) {
        reviewData = mReviewData;
        notifyDataSetChanged();
    }

    public Review getItem(int position)
    {
        return reviewData.get(position);
    }

    public boolean isLoadMore(int position) {
        return showLoadMore && (position == (getItemCount() - 1));
    }

    class ReviewHolder extends RecyclerView.ViewHolder {
        private final TextView mUserName;
        private final TextView mUserReview;

        public ReviewHolder(View view) {
            super(view);
            mUserName = (TextView) view.findViewById(R.id.tv_name_user);
            mUserReview = (TextView) view.findViewById(R.id.tv_user_review);
        }
    }

}

