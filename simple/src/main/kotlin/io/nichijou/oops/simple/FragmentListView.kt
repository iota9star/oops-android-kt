package io.nichijou.oops.simple

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_list_view.*


class FragmentListView : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list_view, container, false)
    }

    companion object {
        fun newInstance() = FragmentListView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initScrollView()
        initNestedScrollView()
        initListView()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        recycler_view.layoutManager = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)
        recycler_view.adapter = RecyclerViewAdapter(getColor())
    }

    private fun initListView() {
        list_view.adapter = ListViewAdapter(context!!, getColor())
    }

    private fun initNestedScrollView() {
        val count = nested_scroll_view.childCount
        for (i in 0 until count) {
            nested_scroll_view.getChildAt(i).setBackgroundColor(randomColor())
        }
    }

    private fun initScrollView() {
        val count = scroll_view.childCount
        for (i in 0 until count) {
            scroll_view.getChildAt(i).setBackgroundColor(randomColor())
        }
    }

    private fun getColor(): ArrayList<Int> {
        val list = ArrayList<Int>()
        for (i in 0..30) {
            list.add(randomColor())
        }
        return list
    }
}

private class RecyclerViewAdapter(private val colors: List<Int>) : RecyclerView.Adapter<RecyclerViewAdapter.ColorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        return ColorViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_color, parent, false))
    }

    override fun getItemCount(): Int = colors.size

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        holder.itemView.setBackgroundColor(colors[position])
    }


    private class ColorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}

private class ListViewAdapter(private val context: Context, private val colors: ArrayList<Int>) : BaseAdapter() {

    override fun getCount() = colors.size

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItem(position: Int) = colors[position]

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewHolder: ColorViewHolder
        val view: View
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_color, parent, false)
            viewHolder = ColorViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ColorViewHolder
        }
        viewHolder.view.setBackgroundColor(colors[position])
        return view
    }

    private class ColorViewHolder(val view: View)
}
