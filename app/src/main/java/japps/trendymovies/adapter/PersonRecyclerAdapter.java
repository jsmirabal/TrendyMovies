package japps.trendymovies.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import japps.trendymovies.R;
import japps.trendymovies.data.MovieData;

/**
 * Created by Julio on 24/7/2016.
 */
public class PersonRecyclerAdapter extends RecyclerView.Adapter<PersonRecyclerAdapter.ViewHolder> {

    private Bundle mData;
    private String mPeopleType;
    public PersonRecyclerAdapter(Bundle data) {
        mData = data;
        mPeopleType = mData.getString(MovieData.PEOPLE_TYPE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_people_item, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ArrayList<String> nameList = mData.getStringArrayList(MovieData.CAST_NAME_PARAM);;
        ArrayList<String> occupationList;
        ArrayList<String> profileList = mData.getStringArrayList(MovieData.PROFILE_PATH_PARAM);
        switch (mPeopleType){
            case MovieData.CAST_PARAM:{
                occupationList = mData.getStringArrayList(MovieData.CAST_CHARACTER_PARAM);
                break;
            }
            case MovieData.CREW_PARAM:{
                occupationList = mData.getStringArrayList(MovieData.CREW_JOB_PARAM);
                break;
            }
            default:{
                occupationList = new ArrayList<>();
                occupationList.add("-");
            }

        }

        String name = nameList == null || nameList.get(position).equals("") ? "-" : nameList.get(position);
        String occupation = occupationList == null || occupationList.get(position).equals("") ? "-" : occupationList.get(position);
        String profile = profileList == null || profileList.get(position).equals("") ? "" : profileList.get(position);

        holder.mProfileNameView.setText(name);
        holder.mProfileJobView.setText(occupation);

        if (!profile.equals("")){
            Picasso.with(holder.getContext()).load(profile).into(holder.mProfileImageView);
        } else {
            Picasso.with(holder.getContext()).load(R.drawable.dummy_profile_image).into(holder.mProfileImageView);
        }

    }

    @Override
    public int getItemCount() {
        return mData.getInt(MovieData.PEOPLE_COUNT);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mProfileImageView;
        public TextView mProfileNameView, mProfileJobView;
        private Context mContext;
        public ViewHolder(View view) {
            super(view);
            mContext = view.getContext();
            mProfileImageView = (ImageView) view.findViewById(R.id.profile_image_view);
            mProfileNameView = (TextView) view.findViewById(R.id.profile_name_view);
            mProfileJobView = (TextView) view.findViewById(R.id.profile_job_view);
        }

        public Context getContext(){
            return mContext;
        }
    }
}
