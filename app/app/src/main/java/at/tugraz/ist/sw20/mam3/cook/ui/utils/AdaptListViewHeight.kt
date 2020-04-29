package at.tugraz.ist.sw20.mam3.cook.ui.utils

import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.ListView


class AdaptListViewHeight {
    fun adaptListViewHeight(listView: ListView) {
        val adapter: ListAdapter = listView.getAdapter() ?: return
        val vg: ViewGroup = listView
        var totalHeight = 0
        for (i in 0 until adapter.getCount()) {
            val listItem: View = adapter.getView(i, null, vg)
            listItem.measure(0, 0)
            totalHeight += listItem.getMeasuredHeight()
        }
        val par: ViewGroup.LayoutParams = listView.getLayoutParams()
        par.height = totalHeight + listView.getDividerHeight() * (adapter.getCount() - 1)
        listView.setLayoutParams(par)
        listView.requestLayout()
    }
}