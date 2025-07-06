package com.mobdeve.s18.roman.isaacnathan.alleymate.data

import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.CatalogueItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

object AllocationStateHolder {

    private val _itemsMap = MutableStateFlow<Map<Int, CatalogueItem>>(emptyMap())

    val itemsToAllocate = _itemsMap.asStateFlow().map { it.values.toList() }

    val allocationCount = _itemsMap.asStateFlow().map { it.size }

    fun addItems(items: List<CatalogueItem>) {
        val currentMap = _itemsMap.value.toMutableMap()
        items.forEach { item ->
            currentMap[item.itemId] = item
        }
        _itemsMap.value = currentMap
    }

    fun removeItem(itemId: Int) {
        _itemsMap.value = _itemsMap.value.toMutableMap().apply {
            remove(itemId)
        }
    }

    fun clear() {
        _itemsMap.value = emptyMap()
    }
}