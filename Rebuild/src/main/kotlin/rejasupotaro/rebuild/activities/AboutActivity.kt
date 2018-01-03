package rejasupotaro.rebuild.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_about.*
import rejasupotaro.rebuild.R
import rejasupotaro.rebuild.data.adapters.AboutItemListAdapter
import rejasupotaro.rebuild.data.models.AboutItem
import rejasupotaro.rebuild.data.models.Developer
import rejasupotaro.rebuild.utils.ViewUtils
import rejasupotaro.rebuild.views.AppDescriptionView

class AboutActivity : AppCompatActivity() {

    companion object {

        fun createIntent(context: Context): Intent {
            return Intent(context, AboutActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        setupActionBar()
        setupAboutListItemView()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
    }

    private fun setupAboutListItemView() {
        ViewUtils.addHeaderView(about_item_list, AppDescriptionView(this))

        val aboutItemList = arrayListOf<AboutItem>().apply {
            add(AboutItem.AboutItemHeader(getString(R.string.about_item_header_developer)))
            add(AboutItem.AboutItemContent(Developer.REJASUPOTARO))
            add(AboutItem.AboutItemHeader(getString(R.string.about_item_header_contributors)))
            add(AboutItem.AboutItemContent(Developer.HOTCHEMI))
            add(AboutItem.AboutItemContent(Developer.MOOTOH))
            add(AboutItem.AboutItemContent(Developer.HAK))
        }

        about_item_list.adapter = AboutItemListAdapter(this, aboutItemList)
    }
}