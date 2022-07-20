package com.aprilla.thesis.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aprilla.thesis.R
import com.aprilla.thesis.databinding.ItemNewsBinding
import com.aprilla.thesis.models.ItemsRSS
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*

class DetectAdapter(): RecyclerView.Adapter<DetectAdapter.CardViewHolder>() {

    private val rssData = ArrayList<ItemsRSS>()
    private val savedDataLinks = ArrayList<String>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setData(items: List<ItemsRSS>, cResult: String){
        rssData.clear()
        items.forEach {
            if (it.author.isNotBlank() || it.author.isNotEmpty()){
                it.category = cResult
                rssData.add(it)
            }
        }
        notifyDataSetChanged()
    }

    fun setSavedData(items: List<ItemsRSS>){
        savedDataLinks.clear()
        items.forEach {
            savedDataLinks.add(it.link)
        }
    }

    inner class CardViewHolder(items: View): RecyclerView.ViewHolder(items){
        private val binding = ItemNewsBinding.bind(itemView)
        fun bind(item: ItemsRSS){
            val sdf = SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss Z", Locale.ENGLISH)
            val format = SimpleDateFormat("MMMM dd yyyy")
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
            item.favourite = setSave(item.link)
            Log.d("TAG", "bind: ${item.favourite}")
            if (item.favourite){
                binding.buttonSave.setImageResource(R.drawable.ic_baseline_bookmark_24)
            }else{
                binding.buttonSave.setImageResource(R.drawable.ic_baseline_not_bookmarked_24)
            }
            binding.buttonSave.setOnClickListener {
                if (item.favourite){
                    binding.buttonSave.setImageResource(R.drawable.ic_baseline_not_bookmarked_24)
                    item.favourite = false
                }else{
                    binding.buttonSave.setImageResource(R.drawable.ic_baseline_bookmark_24)
                    item.favourite = true
                }
                onItemClickCallback.onItemSave(item, adapterPosition)
            }
            binding.cardNews.setOnClickListener{
                onItemClickCallback.onItemClicked(item)
            }
            binding.buttonMenu.setOnClickListener {
                onItemClickCallback.onMenuClicked(item, it)
            }
            binding.buttonShare.setOnClickListener {
                onItemClickCallback.onShareNews(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_detected_cat, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(rssData[position])
    }

    override fun getItemCount(): Int = rssData.size

    fun setSave(item: String): Boolean{
        return savedDataLinks.contains(item)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(article: ItemsRSS?)
        fun onItemSave(article: ItemsRSS?, position: Int)
        fun onMenuClicked(article: ItemsRSS?, cView: View)
        fun onShareNews(article: ItemsRSS?)
    }
}