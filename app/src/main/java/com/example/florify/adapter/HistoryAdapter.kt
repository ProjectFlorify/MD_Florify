package com.example.florify.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.florify.R
import com.example.florify.api.data.PredictionsItem
import java.text.SimpleDateFormat
import java.util.*

class HistoryAdapter(
    private val predictions: List<PredictionsItem>,
    private val onDeleteClickListener: OnDeleteClickListener
) : RecyclerView.Adapter<HistoryAdapter.PredictionsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PredictionsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return PredictionsViewHolder(view, onDeleteClickListener)
    }

    override fun onBindViewHolder(holder: PredictionsViewHolder, position: Int) {
        val animation: Animation = AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.slide_in_left)
        holder.itemView.startAnimation(animation)
        val predictionItem = predictions[position]
        holder.bind(predictionItem)
    }

    override fun getItemCount(): Int = predictions.size

    class PredictionsViewHolder(
        itemView: View,
        private val onDeleteClickListener: OnDeleteClickListener
    ) : RecyclerView.ViewHolder(itemView) {
        private val tvPredictions: TextView = itemView.findViewById(R.id.predictions_history)
        private val ivPlant: ImageView = itemView.findViewById(R.id.image_history)
        private val tvPlant: TextView = itemView.findViewById(R.id.plant_history)
        private val tvDate: TextView = itemView.findViewById(R.id.time_history)
        private val delete: ImageButton = itemView.findViewById(R.id.delete)

        fun bind(predictionsItem: PredictionsItem) {
            tvPredictions.text = predictionsItem.prediction
            tvPlant.text = predictionsItem.plant
            tvDate.text = formatDate(predictionsItem.timestamp)
            Glide.with(itemView.context).load(predictionsItem.imageUrl).into(ivPlant)

            delete.setOnClickListener {
                onDeleteClickListener.onDeleteClick(predictionsItem)
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
    interface OnDeleteClickListener {
        fun onDeleteClick(predictionItem: PredictionsItem)
    }
}