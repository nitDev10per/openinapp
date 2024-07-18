package com.example.openinapp.fragment.linkFragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.openinapp.R
import com.example.openinapp.dataClass.RecentLink

class CustomListAdapter2(private val context: Context, private var items: List<RecentLink>) :
    BaseAdapter() {

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): Any = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            var item = getItem(position) as RecentLink
            val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
            val titleTextView: TextView = view.findViewById(R.id.item_name)
            val imageView: ImageView = view.findViewById(R.id.item_image)

            titleTextView.text = item.title
            Glide.with(context).load(item.original_image).into(imageView)


        return view
    }

    fun updateItemsRL(newItems: List<RecentLink>) {
        items = newItems
        notifyDataSetChanged()
    }
}