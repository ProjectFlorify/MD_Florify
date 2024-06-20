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
import com.example.florify.api.data.ForumDataItem
import com.example.florify.ui.forum.ForumDetail
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ForumAdapter(private val forumList: List<ForumDataItem>) :
    RecyclerView.Adapter<ForumAdapter.ForumViewHolder>() {
    class ForumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imageForum: ImageView = itemView.findViewById(R.id.image_forum)
        private val user: TextView = itemView.findViewById(R.id.user)
        private val title: TextView = itemView.findViewById(R.id.result_prediction)
        private val timeForum: TextView = itemView.findViewById(R.id.time_forum)
        private val plant: TextView = itemView.findViewById(R.id.plant)
        private val caption: TextView = itemView.findViewById(R.id.comment)

        fun bind(forumItem: ForumDataItem) {
            Glide.with(itemView.context)
                .load(forumItem.imagePrediction)
                .into(imageForum)
            val time = formatDate(forumItem.timestamp)
            user.text = forumItem.userName
            timeForum.text = time
            plant.text = forumItem.plantPrediction
            title.text = forumItem.resultPrediction
            caption.text = forumItem.caption

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, ForumDetail::class.java)
                intent.putExtra("ForumItem", forumItem)
                itemView.context.startActivity(intent)
            }
        }

        private fun formatDate(timestamp: com.example.florify.api.data.Timestamp?): CharSequence {
            return timestamp?.let {
                val date = Date(it.seconds?.times(1000L) ?: 0L)
                val dateFormat = SimpleDateFormat("dd MMMM yyyy HH:mm", Locale("en", "EN"))
                dateFormat.format(date)
            } ?: ""
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForumViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_forum, parent, false)
        return ForumViewHolder(itemView)
    }

    override fun getItemCount(): Int = forumList.size

    override fun onBindViewHolder(holder: ForumViewHolder, position: Int) {
        val animation: Animation =
            AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.slide_in_left)
        holder.itemView.startAnimation(animation)
        val currentItem = forumList[position]
        holder.bind(currentItem)
    }
}
