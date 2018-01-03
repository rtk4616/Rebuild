package rejasupotaro.rebuild.activities

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.squareup.otto.Subscribe
import kotlinx.android.synthetic.main.activity_episode_detail.*
import rejasupotaro.rebuild.R
import rejasupotaro.rebuild.data.models.Episode
import rejasupotaro.rebuild.events.BusProvider
import rejasupotaro.rebuild.events.DownloadEpisodeCompleteEvent
import rejasupotaro.rebuild.fragments.EpisodeDetailFragment
import rejasupotaro.rebuild.fragments.EpisodeMediaFragment
import rejasupotaro.rebuild.tools.MainThreadExecutor
import rejasupotaro.rebuild.utils.IntentUtils

class EpisodeDetailActivity : AppCompatActivity() {

    private var episode: Episode? = null
    private var episodeMediaFragment: EpisodeMediaFragment? = null
    private var episodeDetailFragment: EpisodeDetailFragment? = null
    private val mainThreadExecutor = MainThreadExecutor()

    companion object {
        private const val EXTRA_EPISODE_ID = "extra_episode_id"

        fun createIntent(context: Context, episodeId: String): Intent {
            return Intent(context, EpisodeDetailActivity::class.java).apply {
                putExtra(EXTRA_EPISODE_ID, episodeId)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_episode_detail)
        BusProvider.getInstance().register(this)

        val episodeId = intent.getStringExtra(EXTRA_EPISODE_ID)
        episode = Episode.findById(episodeId)

        setupActionBar()

        episodeMediaFragment = fragmentManager.findFragmentById(R.id.fragment_episode_media) as? EpisodeMediaFragment
        episodeMediaFragment?.setup(episode)

        episodeDetailFragment = fragmentManager.findFragmentById(R.id.fragment_episode_detail) as? EpisodeDetailFragment
        episodeDetailFragment?.setup(episode)
    }

    public override fun onDestroy() {
        BusProvider.getInstance().unregister(this)
        super.onDestroy()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        close()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.episode_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                close()
                true
            }
            R.id.action_settings -> {
                startActivity(SettingsActivity.createIntent(this))
                true
            }
            R.id.action_share -> {
                IntentUtils.shareEpisode(this, episode)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @Subscribe
    fun onEpisodeDownloadComplete(event: DownloadEpisodeCompleteEvent) {
        val episode = Episode.findById(event.episodeId)
        if (this.episode?.isSameEpisode(episode) != true) {
            return
        }

        this.episode = episode

        mainThreadExecutor.execute { episodeMediaFragment?.setup(this@EpisodeDetailActivity.episode) }
    }

    private fun setupActionBar() {
        title = ""

        setSupportActionBar(toolbar)

        //        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener { _ -> close() }

        val colorDrawable = ColorDrawable(ContextCompat.getColor(this, R.color.dark_gray))
        colorDrawable.alpha = 0
        toolbar.background = colorDrawable

        toolbar_title.text = episode?.title
        toolbar_title.alpha = 0f

        scroll_view.scrollEvent.subscribe({ scrollPosition ->
            val alpha: Int
            val y = scrollPosition.current.y
            alpha = when {
                y < 0 -> 0
                y > 500 -> 255
                else -> (y / 500.0 * 255).toInt()
            }
            colorDrawable.alpha = alpha
            toolbar.background = colorDrawable

            toolbar_title.alpha = alpha / 255f
        })
    }

    private fun close() {
        finish()
        overridePendingTransition(R.anim.zoom_in, R.anim.slide_down_exit)
    }
}