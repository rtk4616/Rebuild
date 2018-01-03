package rejasupotaro.rebuild.activities

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import butterknife.ButterKnife
import com.squareup.otto.Subscribe
import kotlinx.android.synthetic.main.activity_episode_list.*
import rejasupotaro.rebuild.R
import rejasupotaro.rebuild.data.models.Episode
import rejasupotaro.rebuild.events.BusProvider
import rejasupotaro.rebuild.events.EpisodePlayStartEvent
import rejasupotaro.rebuild.fragments.EpisodeListFragment
import rejasupotaro.rebuild.media.PodcastPlayer
import rejasupotaro.rebuild.services.PodcastPlayerService
import rejasupotaro.rebuild.tools.MainThreadExecutor

class EpisodeListActivity : AppCompatActivity(), EpisodeListFragment.OnEpisodeSelectListener {

    private val mainThreadExecutor = MainThreadExecutor()

    companion object {

        private const val EXTRA_EPISODE_ID = "extra_episode_id"

        fun createIntent(context: Context, episodeId: String): Intent {
            return Intent(context, EpisodeListActivity::class.java).apply {
                putExtra(EXTRA_EPISODE_ID, episodeId)
            }
        }
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_episode_list)
        ButterKnife.bind(this)
        BusProvider.getInstance().register(this)

        setupActionBar()
        startServices()
        parseIntent(intent)
    }

    private fun setupActionBar() {
        supportActionBar?.also {
            it.title = ""
            val transparent = ContextCompat.getColor(this, android.R.color.transparent)
            it.setBackgroundDrawable(ColorDrawable(transparent))
            it.setIcon(ColorDrawable(transparent))
        }
    }

    public override fun onResume() {
        super.onResume()
        setupMediaBar(PodcastPlayer.getInstance().episode)
    }

    public override fun onDestroy() {
        BusProvider.getInstance().unregister(this)
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                startActivity(SettingsActivity.createIntent(this))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun parseIntent(intent: Intent?) {
        if (intent == null) {
            return
        }

        val episodeId = intent.getStringExtra(EXTRA_EPISODE_ID)
        if (TextUtils.isEmpty(episodeId)) {
            return
        }

        openEpisodeDetailActivity(episodeId)
    }

    private fun startServices() {
        startService(Intent(this, PodcastPlayerService::class.java))
    }

    private fun setupMediaBar(episode: Episode?) {
        media_bar?.setEpisode(episode) { e -> openEpisodeDetailActivity(e.episodeId) }
    }

    override fun onSelect(episode: Episode) {
        val episodeId = episode.episodeId
        if (TextUtils.isEmpty(episodeId)) {
            return
        }

        openEpisodeDetailActivity(episodeId)
    }

    private fun openEpisodeDetailActivity(episodeId: String) {
        startActivity(EpisodeDetailActivity.createIntent(this, episodeId))
        overridePendingTransition(R.anim.slide_up_enter, R.anim.zoom_out)
    }

    @Subscribe
    fun onEpisodePlayStart(event: EpisodePlayStartEvent) {
        val episode = Episode.findById(event.episodeId)
        mainThreadExecutor.execute { setupMediaBar(episode) }
    }
}