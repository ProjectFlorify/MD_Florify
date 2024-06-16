package com.example.florify.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.florify.R
import com.example.florify.api.data.EncyclopediaItem
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import com.bumptech.glide.Glide
import androidx.core.util.Pair
import com.example.florify.ui.ensiklopedia.EncyclopediaDetail

class EncyclopediaAdapter(
    private val items: List<EncyclopediaItem>
) : RecyclerView.Adapter<EncyclopediaAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title_disease)
        val description: TextView = itemView.findViewById(R.id.desc_disease)
        val image: ImageView = itemView.findViewById(R.id.image_disease)
        fun bind(encyclopediaItem: EncyclopediaItem) {

            title.text = encyclopediaItem.title
            description.text = encyclopediaItem.description
            Glide.with(itemView)
                .load(encyclopediaItem.image)
                .into(image)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, EncyclopediaDetail::class.java)
                intent.putExtra("EncyclopediaItem", encyclopediaItem)
                val optionCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    itemView.context as Activity,
                    Pair(image, "image_disease"),
                    Pair(title, "title_disease"),
                    Pair(description, "desc_disease")
                )
                itemView.context.startActivity(intent, optionCompat.toBundle())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val animation: Animation =
            AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.slide_in_left)
        holder.itemView.startAnimation(animation)
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
