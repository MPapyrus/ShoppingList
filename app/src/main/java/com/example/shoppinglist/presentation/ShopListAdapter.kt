package com.example.shoppinglist.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem

class ShopListAdapter : RecyclerView.Adapter<ShopListAdapter.ShopItemViewHolder>() {

    var count = 0
    var shopList = listOf<ShopItem>()
        set(value) {
            val callback = ShopListDiffCallBack(shopList, value)
            val diffResult = DiffUtil.calculateDiff(callback)
            diffResult.dispatchUpdatesTo(this)
            field = value
        }

    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null
    var onShopItemClickListener: ((ShopItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val layout = when (viewType) {
            VIEW_TYPE_ENABLED -> R.layout.item_shop_enabled
            VIEW_TYPE_DISABLED -> R.layout.item_shop_disabled
            else -> throw RuntimeException("Unknown view type: $viewType")
        }

        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)

        return ShopItemViewHolder(view)

    }

    override fun onBindViewHolder(viewHolder: ShopItemViewHolder, position: Int) {
        Log.d("onBindViewHolder", "Created view: ${++count}")
        val shopItem = shopList[position]

        viewHolder.itemView.setOnLongClickListener {
            onShopItemLongClickListener?.invoke(shopItem)
            true
        }

        viewHolder.itemView.setOnClickListener {
            onShopItemClickListener?.invoke(shopItem)
            true
        }

        viewHolder.tvName.text = shopItem.name
        viewHolder.tvCount.text = shopItem.count.toString()

    }

    override fun getItemViewType(position: Int): Int {
        val shopItem = shopList[position]
        return if (shopItem.enabled) {
            return VIEW_TYPE_ENABLED
        } else {
            return VIEW_TYPE_DISABLED
        }
    }

    override fun getItemCount(): Int {
        return shopList.size
    }


    class ShopItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName = view.findViewById<TextView>(R.id.tv_name)
        val tvCount = view.findViewById<TextView>(R.id.tv_count)
    }



    interface OnShopItemLongClickListener {
        fun onShopItemLongClick(shopItem: ShopItem)
    }

    interface OnShopItemClickListener {
        fun onShopItemClick(shopItem: ShopItem)
    }

    companion object {
        const val VIEW_TYPE_ENABLED = 100
        const val VIEW_TYPE_DISABLED = 101

        const val MAX_POOL_SIZE = 15
    }
}