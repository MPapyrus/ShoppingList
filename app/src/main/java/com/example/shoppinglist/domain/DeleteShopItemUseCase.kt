package com.example.shoppinglist.domain

class DeleteShopItemUseCase(private val shopListRepository: ShopListRepository) {

    suspend fun deleteShopItemUseCase(shopItem: ShopItem){
        shopListRepository.deleteShopItem(shopItem)
    }
}