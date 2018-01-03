package rejasupotaro.rebuild.data.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

abstract class BindableAdapter<T>(context: Context, episodeList: List<T>) : ArrayAdapter<T>(context, -1, episodeList) {

    private val inflater by lazy {
        LayoutInflater.from(context)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val v = convertView ?: newView(inflater, position, parent)
        v ?: throw IllegalStateException("newView result must not be null.")
        bindView(getItem(position), position, v)
        return v
    }

    abstract fun newView(inflater: LayoutInflater, position: Int, container: ViewGroup?): View?

    abstract fun bindView(item: T?, position: Int, view: View)
}