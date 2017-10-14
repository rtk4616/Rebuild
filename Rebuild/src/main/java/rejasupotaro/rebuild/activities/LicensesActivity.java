package rejasupotaro.rebuild.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.webkit.WebView;

import butterknife.BindView;
import butterknife.ButterKnife;
import rejasupotaro.rebuild.R;

public class LicensesActivity extends AppCompatActivity {

    private static final String LICENSES_FILE_PATH = "file:///android_asset/licenses.html";

    @BindView(R.id.licenses_view)
    WebView licensesView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_licenses);
        ButterKnife.bind(this);
        setupActionBar();
        setupLicensesView();
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    private void setupLicensesView() {
        licensesView.getSettings().setJavaScriptEnabled(false);
        licensesView.loadUrl(LICENSES_FILE_PATH);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_settings:
                startActivity(SettingsActivity.createIntent(this));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
