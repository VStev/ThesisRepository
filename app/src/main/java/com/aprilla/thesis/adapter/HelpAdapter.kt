package com.aprilla.thesis.adapter

import android.content.res.TypedArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aprilla.thesis.R
import com.aprilla.thesis.databinding.ItemHelpBinding
import com.bumptech.glide.Glide

class HelpAdapter(): RecyclerView.Adapter<HelpAdapter.CardViewHolder>() {

    private val messagesData = ArrayList<String>()
    private val imagesData = ArrayList<Int>()

    fun setData(items: TypedArray, images: TypedArray){
        messagesData.clear()
        imagesData.clear()
        for (i in 1..(items.length()-2)){
            messagesData.add(items.getString(i)?:"")
            imagesData.add(images.getResourceId(i, 0))
        }
        notifyDataSetChanged()
    }

    inner class CardViewHolder(items: View): RecyclerView.ViewHolder(items){
        private val binding = ItemHelpBinding.bind(itemView)
        fun bind(position: Int){
            binding.txtMessage.text = messagesData[position]
            Glide.with(binding.root)
                .load(imagesData[position])
                .placeholder(R.drawable.placeholder_item_picture)
                .into(binding.cardImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_help, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = messagesData.size
}