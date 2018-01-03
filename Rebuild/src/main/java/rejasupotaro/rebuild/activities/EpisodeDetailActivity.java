package rejasupotaro.rebuild.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.data.models.Episode;
import rejasupotaro.rebuild.events.BusProvider;
import rejasupotaro.rebuild.events.DownloadEpisodeCompleteEvent;
import rejasupotaro.rebuild.fragments.EpisodeDetailFragment;
import rejasupotaro.rebuild.fragments.EpisodeMediaFragment;
import rejasupotaro.rebuild.tools.MainThreadExecutor;
import rejasupotaro.rebuild.utils.IntentUtils;
import rejasupotaro.rebuild.views.ObservableScrollView;

public class EpisodeDetailActivity extends AppCompatActivity {

    private static final String EXTRA_EPISODE_ID = "extra_episode_id";

    @BindView(R.id.scroll_view)
    ObservableScrollView scrollView;

    private Episode episode;
    private EpisodeMediaFragment episodeMediaFragment;
    private EpisodeDetailFragment episodeDetailFragment;
    private MainThreadExecutor mainThreadExecutor = new MainThreadExecutor();

    public static Intent createIntent(Context context, String episodeId) {
        Intent intent = new Intent(context, EpisodeDetailActivity.class);
        intent.putExtra(EXTRA_EPISODE_ID, episodeId);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode_detail);
        ButterKnife.bind(this);
        BusProvider.getInstance().register(this);

        String episodeId = getIntent().getStringExtra(EXTRA_EPISODE_ID);
        episode = Episode.findById(episodeId);

        setupActionBar();

        episodeMediaFragment = (EpisodeMediaFragment) getFragmentManager().findFragmentById(R.id.fragment_episode_media);
        episodeMediaFragment.setup(episode);

        episodeDetailFragment = (EpisodeDetailFragment) getFragmentManager().findFragmentById(R.id.fragment_episode_detail);
        episodeDetailFragment.setup(episode);
    }

    @Override
    public void onDestroy() {
        BusProvider.getInstance().unregister(this);
        super.onDestroy();
    }

    private void setupActionBar() {
        setTitle("");

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(view -> close());

        final ColorDrawable colorDrawable = new ColorDrawable(
                getResources().getColor(R.color.dark_gray));
        colorDrawable.setAlpha(0);
        toolbar.setBackground(colorDrawable);

        final TextView titleTextView = findViewById(R.id.toolbar_title);
        titleTextView.setText(episode.getTitle());
        titleTextView.setAlpha(0);

        scrollView.getScrollEvent().subscribe(scrollPosition -> {
            int alpha;
            int y = scrollPosition.current.y;
            if (y < 0) {
                alpha = 0;
            } else if (y > 500) {
                alpha = 255;
            } else {
                alpha = (int) ((y / 500.0) * 255);
            }
            colorDrawable.setAlpha(alpha);
            toolbar.setBackground(colorDrawable);

            titleTextView.setAlpha(alpha / 255F);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.episode_detail, menu);
        return true;
    }

    public void close() {
        finish();
        overridePendingTransition(R.anim.zoom_in, R.anim.slide_down_exit);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                close();
                return true;
            case R.id.action_settings:
                startActivity(SettingsActivity.Companion.createIntent(this));
                return true;
            case R.id.action_share:
                IntentUtils.shareEpisode(this, episode);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Subscribe
    public void onEpisodeDownloadComplete(final DownloadEpisodeCompleteEvent event) {
        final Episode episode = Episode.findById(event.getEpisodeId());
        if (!this.episode.isSameEpisode(episode)) {
            return;
        }

        this.episode = episode;

        mainThreadExecutor.execute(() -> episodeMediaFragment.setup(EpisodeDetailActivity.this.episode));
    }
}
