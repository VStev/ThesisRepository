package com.aprilla.thesis.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aprilla.thesis.R
import com.aprilla.thesis.databinding.ItemNewsBinding
import com.aprilla.thesis.databinding.ItemNewsSavedBinding
import com.aprilla.thesis.models.ItemsRSS
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*

class SavedAdapter: RecyclerView.Adapter<SavedAdapter.CardViewHolder>() {

    private val rssData = ArrayList<ItemsRSS>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setData(items: List<ItemsRSS>){
        rssData.clear()
        rssData.addAll(items)
        notifyDataSetChanged()
    }

    fun updateData(position: Int): Boolean{
        notifyItemRemoved(position)
        return rssData.isEmpty()
    }

//    fun setSavedData(items: List<ItemsRSS>){
//        rssData.clear()
//        rssData.addAll(items)
//    }

    inner class CardViewHolder(items: View): RecyclerView.ViewHolder(items){
        private val binding = ItemNewsSavedBinding.bind(itemView)
        fun bind(item: ItemsRSS){
            val sdf = SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss Z")
            val format = SimpleDateFormat("MMMM dd")
            val date = sdf.parse(item.pubDate)
            val cal = Calendar.getInstance()
//            item.favourite = setSave(item)
            cal.time = date as Date
//            cal.add(Calendar.HOUR, 7)
            binding.txtRssTitle.apply{
                text = item.title
            }
            binding.txtRssDate.text = format.format(cal.time)
            binding.txtAuthor.text = item.author
            Glide.with(binding.root)
                .load(item.thumbnail)
                .placeholder(R.drawable.placeholder_item_picture)
                .into(binding.imageRss)
            binding.buttonDelete.setOnClickListener {
                rssData.removeAt(adapterPosition)
                onItemClickCallback.onItemSave(item, adapterPosition)
            }
            binding.cardNews.setOnClickListener{
                onItemClickCallback.onItemClicked(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_news_saved, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(rssData[position])
    }

    override fun getItemCount(): Int = rssData.size

//    fun setSave(item: ItemsRSS): Boolean{
//        Log.d("TAGb", "setSave: $savedData")
//        return savedData.contains(item)
//    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(article: ItemsRSS?)
        fun onItemSave(article: ItemsRSS?, position: Int)
    }
}