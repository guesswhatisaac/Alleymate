package com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.CatalogueItem
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.SaleTransaction
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.SaleTransactionItem

data class TransactionItemWithDetails(
    @Embedded
    val saleTransactionItem: SaleTransactionItem,

    @Relation(
        parentColumn = "itemId",
        entityColumn = "itemId"
    )
    val catalogueItem: CatalogueItem
)


data class TransactionWithItems(
    @Embedded
    val transaction: SaleTransaction,

    @Relation(
        entity = SaleTransactionItem::class,
        parentColumn = "transactionId",
        entityColumn = "transactionId"
    )
    val items: List<TransactionItemWithDetails>
)