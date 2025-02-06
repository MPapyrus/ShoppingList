package com.example.shoppinglist.domain

class EditShopItemUseCase(private val shopListRepository: ShopListRepository) {

    suspend fun editShopItemUseCase(shopItem: ShopItem){
        shopListRepository.editShopItem(shopItem)
    }
}