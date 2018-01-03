package rejasupotaro.rebuild.data.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import rejasupotaro.rebuild.R
import rejasupotaro.rebuild.data.models.Tweet
import rejasupotaro.rebuild.utils.PicassoHelper
import java.util.*

class TweetListAdapter constructor(context: Context, episodeList: List<Tweet> = ArrayList()) : BindableAdapter<Tweet>(context, episodeList) {

    private class ViewHolder(view: View) {
        var userProfileImageView: ImageView? = null
        var createdAtTextView: TextView? = null
        var userNameTextView: TextView? = null
        var tweetTextView: TextView? = null
        var retweetCountTextView: TextView? = null
        var favoriteCountTextView: TextView? = null

        init {
            userProfileImageView = view.findViewById(R.id.user_profile_image)
            createdAtTextView = view.findViewById(R.id.created_at_text)
            userNameTextView = view.findViewById(R.id.user_name_text)
            tweetTextView = view.findViewById(R.id.tweet_text)
            retweetCountTextView = view.findViewById(R.id.retweet_count_text)
            favoriteCountTextView = view.findViewById(R.id.favorite_count_text)
        }
    }

    override fun newView(inflater: LayoutInflater, position: Int, container: ViewGroup?): View? {
        val view = inflater.inflate(R.layout.list_item_tweet, null)
        val holder = ViewHolder(view)
        view.tag = holder
        return view
    }

    @SuppressLint("SetTextI18n")
    override fun bindView(item: Tweet?, position: Int, view: View) {
        val holder = view.tag as? ViewHolder
        holder?.let {
            PicassoHelper.loadAndCircleTransform(context, it.userProfileImageView, item?.userImageUrl)
            it.createdAtTextView?.text = item?.elapsedTimeText
            it.userNameTextView?.text = item?.userName
            it.tweetTextView?.text = item?.text
            it.retweetCountTextView?.text = item?.retweetCount.toString() + " retweets"
            it.favoriteCountTextView?.text = item?.favoriteCount.toString() + " favorites"
        }
    }
}