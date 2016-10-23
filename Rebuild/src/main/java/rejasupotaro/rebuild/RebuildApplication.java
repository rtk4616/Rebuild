package rejasupotaro.rebuild;

import android.app.Application;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;

import rejasupotaro.rebuild.api.RssFeedClient;
import rejasupotaro.rebuild.data.models.Episode;
import rejasupotaro.rebuild.data.models.Guest;
import rejasupotaro.rebuild.data.models.Tweet;
import rejasupotaro.rebuild.data.serializers.DateTypeSerializer;
import rejasupotaro.rebuild.data.serializers.UriTypeSerializer;
import rejasupotaro.rebuild.notifications.PodcastPlayerNotification;

import static rejasupotaro.rebuild.ActivityLifecycleObserver.OnActivityStoppedListener;

public class RebuildApplication extends Application {
    private static RebuildApplication instance;

    public RebuildApplication() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Configuration configuration = new Configuration.Builder(this)
                .setDatabaseVersion(4)
                .setDatabaseName("rebuild.db")
                .addModelClasses(Episode.class, Tweet.class, Guest.class)
                .addTypeSerializers(UriTypeSerializer.class, DateTypeSerializer.class)
                .create();
        ActiveAndroid.initialize(configuration);
        RssFeedClient.init(this);
        setupActivityLifecycleObserver();
    }

    @Override
    public void onTerminate() {
        ActiveAndroid.dispose();
        ActivityLifecycleObserver.terminate(this);
        super.onTerminate();
    }

    private void setupActivityLifecycleObserver() {
        ActivityLifecycleObserver.initialize(this, new OnActivityStoppedListener() {
            @Override
            public void onAllStop() {
                PodcastPlayerNotification.setIsInBackground(true);
            }
        });
    }

    public static RebuildApplication getInstance() {
        return instance;
    }
}
