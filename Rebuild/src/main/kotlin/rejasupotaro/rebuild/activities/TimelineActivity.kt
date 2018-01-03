package rejasupotaro.rebuild.activities

import android.app.LoaderManager
import android.content.Loader
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_timeline.*
import rejasupotaro.rebuild.R
import rejasupotaro.rebuild.data.adapter.TweetListAdapter
import rejasupotaro.rebuild.data.loaders.TweetLoader
import rejasupotaro.rebuild.data.models.Tweet
import rejasupotaro.rebuild.listener.MoreLoadListener
import rejasupotaro.rebuild.utils.IntentUtils
import rejasupotaro.rebuild.utils.StringUtils

class TimelineActivity : AppCompatActivity() {

    private lateinit var tweetListAdapter: TweetListAdapter
    private var isFirstRequest = true

    companion object {
        private const val REQUEST_TWEET_LIST = 1
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)

        setupActionBar()
        setupTweetListView()
        setupSwipeRefreshLayout()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.action_settings -> startActivity(SettingsActivity.createIntent(this))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
            this.title = StringUtils.capitalize(getString(R.string.label_timeline))
        }
    }

    private fun setupTweetListView() {
        tweet_list.setOnScrollListener(object : MoreLoadListener(this, tweet_list) {
            override fun onLoadMore() {
                requestTweetList()
            }
        })

        tweetListAdapter = TweetListAdapter(this)
        tweet_list.adapter = tweetListAdapter

        tweet_list.setOnItemClickListener { _, _, i, _ ->
            val item: Tweet? = tweetListAdapter.getItem(i)
            IntentUtils.openTwitter(this@TimelineActivity, item?.tweetId ?: -1, item?.userName)
        }

        requestTweetList()
    }

    private fun setupSwipeRefreshLayout() {
        swipe_refresh_layout.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light)
        swipe_refresh_layout.setOnRefreshListener { refresh() }
    }

    private fun refresh() {
        isFirstRequest = true
        requestTweetList()
    }

    private fun requestTweetList() {
        loaderManager.restartLoader(REQUEST_TWEET_LIST, Bundle(),
                object : LoaderManager.LoaderCallbacks<List<Tweet>> {
                    override fun onCreateLoader(i: Int, bundle: Bundle): Loader<List<Tweet>> {
                        return TweetLoader(this@TimelineActivity, isFirstRequest)
                    }

                    override fun onLoadFinished(listLoader: Loader<List<Tweet>>, tweetList: List<Tweet>) {
                        addTweetList(tweetList, isFirstRequest)
                        swipe_refresh_layout.isRefreshing = false
                        isFirstRequest = false
                    }

                    override fun onLoaderReset(listLoader: Loader<List<Tweet>>) {
                        // nothing to do
                    }
                }
        )
    }

    private fun addTweetList(tweetList: List<Tweet>?, isFirstRequest: Boolean) {
        if (tweetList == null || tweetList.isEmpty()) {
            state_frame_layout.showError()
            return
        }

        if (isFirstRequest) {
            tweetListAdapter.clear()
        }

        tweetListAdapter.addAll(tweetList)
    }
}
