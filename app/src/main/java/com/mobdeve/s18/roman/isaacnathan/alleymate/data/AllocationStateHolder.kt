package com.mobdeve.s18.roman.isaacnathan.alleymate.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Holds and manages the allocation state of item IDs globally.
 * Useful for tracking selected items across different screens or components.
 */
object AllocationStateHolder {

    // Backing state for currently allocated item IDs
    private val _itemIdsToAllocate = MutableStateFlow<Set<Int>>(emptySet())

    // Public read-only flow of allocated item IDs
    val itemIdsToAllocate = _itemIdsToAllocate.asStateFlow()

    /** Returns the current number of allocated items. */
    fun getAllocationCount(): Int {
        return _itemIdsToAllocate.value.size
    }

    /**
     * Adds a set of item IDs to the current allocation.
     * Prevents duplicates since the underlying data structure is a Set.
     */
    fun addItemIds(itemIds: Set<Int>) {
        val currentIds = _itemIdsToAllocate.value.toMutableSet()
        currentIds.addAll(itemIds)
        _itemIdsToAllocate.value = currentIds
    }

    /**
     * Removes a specific item ID from the allocation.
     */
    fun removeItem(itemId: Int) {
        _itemIdsToAllocate.value = _itemIdsToAllocate.value.toMutableSet().apply {
            remove(itemId)
        }
    }

    /** Clears all allocated item IDs. */
    fun clear() {
        _itemIdsToAllocate.value = emptySet()
    }
}
