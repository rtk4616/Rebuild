package rejasupotaro.rebuild.test.models;

import android.os.Bundle;

import rejasupotaro.rebuild.models.Episode;
import rejasupotaro.robotgirl.ActiveAndroidTestCase;
import rejasupotaro.robotgirl.Factory;
import rejasupotaro.robotgirl.RobotGirl;

public class EpisodeTest extends ActiveAndroidTestCase {

    @Override
    public void setUp() throws Exception {
        super.setUp();

        RobotGirl.define(new Factory("episode1", Episode.class) {
            @Override
            public Bundle set(Bundle bundle) {
                bundle.putInt("episode_id", 1);
                return bundle;
            }
        }).define(new Factory("episode2", Episode.class) {
            @Override
            public Bundle set(Bundle bundle) {
                bundle.putInt("episode_id", 1);
                return bundle;
            }
        }).define(new Factory("episode3", Episode.class) {
            @Override
            public Bundle set(Bundle bundle) {
                bundle.putInt("episode_id", 3);
                return bundle;
            }
        });
    }

    public void testIsSameId() {
        Episode episode1 = RobotGirl.build("episode1");
        Episode episode2 = RobotGirl.build("episode2");
        Episode episode3 = RobotGirl.build("episode3");

        assertTrue(episode1.isSameId(episode2));
        assertFalse(episode1.isSameId(episode3));
        assertFalse(episode2.isSameId(episode3));
    }
}