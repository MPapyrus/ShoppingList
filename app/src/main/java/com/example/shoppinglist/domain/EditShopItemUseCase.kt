package com.example.shoppinglist.domain

import javax.inject.Inject

class EditShopItemUseCase @Inject constructor(private val shopListRepository: ShopListRepository) {

    suspend fun editShopItemUseCase(shopItem: ShopItem){
        shopListRepository.editShopItem(shopItem)
    }
}