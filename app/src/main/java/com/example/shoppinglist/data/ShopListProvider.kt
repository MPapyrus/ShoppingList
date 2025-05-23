package com.example.shoppinglist.data

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.example.shoppinglist.di.DaggerApplicationComponent
import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.presentation.ShopListApp
import javax.inject.Inject

class ShopListProvider : ContentProvider() {

    private val component by lazy {
        (context as ShopListApp).component
    }

    @Inject
    lateinit var shopListDao: ShopListDao

    @Inject
    lateinit var mapper: ShopListMapper

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI("com.example.shoppinglist", "shop_items", GET_SHOP_ITEMS_QUERY)
        addURI("com.example.shoppinglist", "shop_items/#", GET_SHOP_ITEM_BY_ID_QUERY)
    }

    override fun onCreate(): Boolean {
        component.inject(this)
        return true
    }

    override fun query(
        p0: Uri,
        p1: Array<out String>?,
        p2: String?,
        p3: Array<out String>?,
        p4: String?
    ): Cursor? {
        val code = uriMatcher.match(p0)

        return when (code) {
            GET_SHOP_ITEMS_QUERY -> {
                shopListDao.getShopListCursor()
            }

            else -> {
                null
            }
        }

        Log.d("ShopListProvider", "query uri: $p0 code: $code")
    }

    override fun getType(p0: Uri): String? {
        TODO("Not yet implemented")
    }

    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        val code = uriMatcher.match(p0)

        when (code) {
            GET_SHOP_ITEMS_QUERY -> {
                if (p1 == null) return null

                val id = p1.getAsInteger("id")
                val name = p1.getAsString("name")
                val count = p1.getAsInteger("count")
                val enabled = p1.getAsBoolean("enabled")

                val shopItem = ShopItem(
                    id = id, name = name, count = count, enabled = enabled
                )

                shopListDao.addShopItemSync(mapper.mapEntityToDbModel(shopItem))
            }
        }

        return null
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        val code = uriMatcher.match(p0)

        when (code) {
            GET_SHOP_ITEMS_QUERY -> {
                val id = p2?.get(0)?.toInt() ?: -1

                return shopListDao.deleteShopItemSync(id)
            }
        }

        return 0
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        val code = uriMatcher.match(p0)

        when (code) {
            GET_SHOP_ITEM_BY_ID_QUERY -> {
                if (p1 == null) return 0

                val id = p0.lastPathSegment?.toIntOrNull() ?: return 0
                val name = p1.getAsString("name") ?: return 0
                val count = p1.getAsInteger("count") ?: return 0
                val enabled = p1.getAsBoolean("enabled") ?: true

                val shopItem = ShopItem(
                    id = id,
                    name = name,
                    count = count,
                    enabled = enabled
                )

                shopListDao.addShopItemSync(mapper.mapEntityToDbModel(shopItem))
                return 1
            }

        }
        return 0
    }

    companion object {

        const val GET_SHOP_ITEMS_QUERY = 100
        const val GET_SHOP_ITEM_BY_ID_QUERY = 101

    }


}