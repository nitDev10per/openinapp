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
import com.example.openinapp.dataClass.TopLink

class CustomListAdapter(private val context: Context, private var items: List<TopLink>) :
    BaseAdapter() {

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): Any = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        var item = getItem(position) as TopLink
            val titleTextView: TextView = view.findViewById(R.id.item_name)
            val imageView: ImageView = view.findViewById(R.id.item_image)

            titleTextView.text = item.title
            Glide.with(context).load(item.original_image).into(imageView)

        // Bind data to the view


        return view
    }

    fun updateItemsTL(newItems: List<TopLink>) {
        items = newItems
        notifyDataSetChanged()
    }

}
