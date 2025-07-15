package com.mobdeve.s18.roman.isaacnathan.alleymate.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

object AllocationStateHolder {

    private val _itemIdsToAllocate = MutableStateFlow<Set<Int>>(emptySet())
    val itemIdsToAllocate = _itemIdsToAllocate.asStateFlow()

    val allocationCount = _itemIdsToAllocate.asStateFlow().map { it.size }

    fun getAllocationCount(): Int {
        return _itemIdsToAllocate.value.size
    }

    fun addItemIds(itemIds: Set<Int>) {
        val currentIds = _itemIdsToAllocate.value.toMutableSet()
        currentIds.addAll(itemIds)
        _itemIdsToAllocate.value = currentIds
    }

    fun removeItem(itemId: Int) {
        _itemIdsToAllocate.value = _itemIdsToAllocate.value.toMutableSet().apply {
            remove(itemId)
        }
    }

    fun clear() {
        _itemIdsToAllocate.value = emptySet()
    }
}