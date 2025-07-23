package com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.CatalogueItem
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.SaleTransaction
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.SaleTransactionItem

/**
 * Represents a single transaction item with its corresponding catalogue item details.
 */
data class TransactionItemWithDetails(
    @Embedded
    val saleTransactionItem: SaleTransactionItem,

    @Relation(
        parentColumn = "itemId",     // SaleTransactionItem's FK
        entityColumn = "itemId"      // CatalogueItem's PK
    )
    val catalogueItem: CatalogueItem
)

/**
 * Represents a full sale transaction with all associated transaction items and their item details.
 */
data class TransactionWithItems(
    @Embedded
    val transaction: SaleTransaction,

    @Relation(
        entity = SaleTransactionItem::class,
        parentColumn = "transactionId",    // SaleTransaction's PK
        entityColumn = "transactionId"     // SaleTransactionItem's FK
    )
    val items: List<TransactionItemWithDetails>
)
