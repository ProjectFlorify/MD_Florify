package com.example.florify.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.florify.R
import com.example.florify.api.data.CommentsItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CommentAdapter(private val forumList: List<CommentsItem>): RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {
    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val username = itemView.findViewById<TextView>(R.id.username)
        private val comment = itemView.findViewById<TextView>(R.id.comment)
        private val time = itemView.findViewById<TextView>(R.id.time)

        fun bind(commentsItem: CommentsItem) {
            username.text = commentsItem.userName
            comment.text = commentsItem.comment
            time.text = formatDate(commentsItem.timestamp)
        }
        private fun formatDate(timestamp: com.example.florify.api.data.Timestamp?): CharSequence {
            return timestamp?.let {
                val date = Date(it.seconds?.times(1000L) ?: 0L)
                val dateFormat = SimpleDateFormat("dd MMMM yyyy HH:mm", Locale("en", "ID"))
                dateFormat.format(date)
            } ?: ""
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun getItemCount(): Int = forumList.size

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val currentItem = forumList[position]
        holder.bind(currentItem)
    }

}