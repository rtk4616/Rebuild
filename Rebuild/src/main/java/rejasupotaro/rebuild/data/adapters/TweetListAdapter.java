package rejasupotaro.rebuild.data.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.data.models.Tweet;
import rejasupotaro.rebuild.utils.PicassoHelper;

public class TweetListAdapter extends BindableAdapter<Tweet> {
    public static class ViewHolder {
        @BindView(R.id.user_profile_image)
        ImageView userProfileImageView;
        @BindView(R.id.created_at_text)
        TextView createdAtTextView;
        @BindView(R.id.user_name_text)
        TextView userNameTextView;
        @BindView(R.id.tweet_text)
        TextView tweetTextView;
        @BindView(R.id.retweet_count_text)
        TextView retweetCountTextView;
        @BindView(R.id.favorite_count_text)
        TextView favoriteCountTextView;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public TweetListAdapter(Context context) {
        this(context, new ArrayList<Tweet>());
    }

    public TweetListAdapter(Context context,
                            List<Tweet> episodeList) {
        super(context, episodeList);
    }

    @Override
    public View newView(LayoutInflater inflater, int position, ViewGroup container) {
        View view = inflater.inflate(R.layout.list_item_tweet, null);
        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(Tweet item, int position, View view) {
        ViewHolder holder = (ViewHolder) view.getTag();

        PicassoHelper.loadAndCircleTransform(getContext(), holder.userProfileImageView,
                item.getUserImageUrl());
        holder.createdAtTextView.setText(item.getElapsedTimeText());
        holder.userNameTextView.setText(item.getUserName());
        holder.tweetTextView.setText(item.getText());
        holder.retweetCountTextView.setText(item.getRetweetCount() + " retweets");
        holder.favoriteCountTextView.setText(item.getFavoriteCount() + " favorites");
    }
}
