package com.example.dartstopplayers.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.dartstopplayers.R
import com.example.dartstopplayers.domain.Player

class RcViewPlayersAdapter :
    ListAdapter<Player, RcViewPlayersAdapter.ItemViewHolder>(DiffCallback()) {

    var onPlayerItemListenerLongClick: ((Player) -> Unit)? = null
    var onPlayerItemListenerShortClick: ((Player) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val maketView = when (viewType) {
            VIEW_TYPE_ACTIVE -> LayoutInflater.from(parent.context).inflate(R.layout.item_card_active_player, parent, false)
            VIEW_TYPE_FINISH -> LayoutInflater.from(parent.context).inflate(R.layout.item_card_finish_player, parent, false)
            else -> throw RuntimeException("Unknown view type: $viewType")
        }
        return ItemViewHolder(maketView)
    }

    override fun onBindViewHolder(itemViewHolder: ItemViewHolder, position: Int) {
        val item = getItem(position)

        itemViewHolder.maketView.setOnLongClickListener {
            onPlayerItemListenerLongClick?.invoke(item)
            true
        }

        itemViewHolder.maketView.setOnClickListener {
            onPlayerItemListenerShortClick?.invoke(item)
        }

        itemViewHolder.nicknamePlayer.text = item.nickname
    }


    override fun getItemViewType(position: Int): Int {
        val itemPlayer = getItem(position)
        return if (itemPlayer.status) {
            VIEW_TYPE_ACTIVE
        } else  {
            VIEW_TYPE_FINISH
        }
    }

    class ItemViewHolder(val maketView: View) : ViewHolder(maketView) {
        val nicknamePlayer = maketView.findViewById<TextView>(R.id.nicknamePlayer)
    }

    class DiffCallback : DiffUtil.ItemCallback<Player>() {
        override fun areItemsTheSame(oldItem: Player, newItem: Player): Boolean {
            return oldItem.playerId == newItem.playerId
        }

        override fun areContentsTheSame(oldItem: Player, newItem: Player): Boolean {
            return oldItem == newItem
        }
    }

    companion object {
        const val VIEW_TYPE_ACTIVE = 0
        const val VIEW_TYPE_FINISH = 1
        const val MAX_POOL_SIZE = 15
    }
}



