package japps.trendymovies.adapter;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

import japps.trendymovies.R;
import japps.trendymovies.data.MovieData;

/**
 * Created by Julio on 25/7/2016.
 */
public class ReviewRecyclerAdapter extends RecyclerView.Adapter<ReviewRecyclerAdapter.ViewHolder> {
    private Bundle mData;

    public ReviewRecyclerAdapter(Bundle data) {
        mData = data;
    }

    @Override
    public ReviewRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_review_item, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ReviewRecyclerAdapter.ViewHolder holder, int position) {
        ArrayList<String> authorList = mData.getStringArrayList(MovieData.REVIEWS_AUTHOR_PARAM);
        ArrayList<String> contentList = mData.getStringArrayList(MovieData.REVIEWS_CONTENT_PARAM);
        if (authorList != null && contentList != null) {
            holder.mAuthor.setText(authorList.get(position));
            holder.mContent.setText(contentList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mData.getInt(MovieData.REVIEWS_COUNT);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mAuthor, mContent;

        public ViewHolder(View view) {
            super(view);
            mAuthor = (TextView) view.findViewById(R.id.review_author_value);
            mContent = (TextView) view.findViewById(R.id.review_content_value);
            ScrollView scrollView = (ScrollView) view.findViewById(R.id.scrollView);
            scrollView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });
        }
    }
}
