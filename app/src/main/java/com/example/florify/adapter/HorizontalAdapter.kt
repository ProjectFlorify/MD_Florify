package com.example.florify.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.florify.R
import com.example.florify.api.data.EncyclopediaItem
import com.example.florify.ui.ensiklopedia.EncyclopediaDetail

class HorizontalAdapter(private val itemList: List<EncyclopediaItem>) :
    RecyclerView.Adapter<HorizontalAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.title_home)
        private val imageView: ImageView = itemView.findViewById(R.id.image_home)
        fun bind(encyclopediaItem: EncyclopediaItem) {

            textView.text = encyclopediaItem.title
            Glide.with(itemView)
                .load(encyclopediaItem.image)
                .into(imageView)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, EncyclopediaDetail::class.java)
                intent.putExtra("EncyclopediaItem", encyclopediaItem)
                val optionCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    itemView.context as Activity,
                    Pair(imageView, "image_disease"),
                    Pair(textView, "title_disease")
                )
                itemView.context.startActivity(intent, optionCompat.toBundle())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val animation: Animation =
            AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.fade_in)
        holder.itemView.startAnimation(animation)
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size
}
