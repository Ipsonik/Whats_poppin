package com.example.whatspopin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.whatspopin.databinding.PopItemBinding
import com.example.whatspopin.models.PopItem
import com.squareup.picasso.Picasso

class FunkoListAdapter(val clickListener: PopListener) :
    ListAdapter<PopItem, FunkoListAdapter.FunkoListViewHolder>(DiffCallback) {

    inner class FunkoListViewHolder(private var binding: PopItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: PopListener, pop: PopItem) {
            binding.pop = pop
            binding.clickListener = clickListener
            binding.executePendingBindings()
            Picasso.get()
                .load(pop.img)
                .resize(320, 320)
                .into(binding.imageView)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FunkoListViewHolder {
        return FunkoListViewHolder(
            PopItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FunkoListViewHolder, position: Int) {
        val pop = getItem(position)
        holder.bind(clickListener, pop)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<PopItem>() {
        override fun areItemsTheSame(oldItem: PopItem, newItem: PopItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PopItem, newItem: PopItem): Boolean {
            return oldItem.name == newItem.name
        }

    }

}

class PopListener(val clickListener: (pop: PopItem) -> Unit) {
    fun onClick(pop: PopItem) = clickListener(pop)
}