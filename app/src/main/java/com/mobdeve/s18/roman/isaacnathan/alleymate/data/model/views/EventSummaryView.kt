package com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.views

import androidx.room.DatabaseView
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.Event


@DatabaseView(
    """
    SELECT
        e.eventId,
        e.title,
        e.location,
        e.startDate,
        e.endDate,
        COALESCE(inv.totalItemsAllocated, 0) AS totalItemsAllocated,
        COALESCE(inv.totalItemsSold, 0) AS totalItemsSold,
        COALESCE(inv.totalRevenueInCents, 0) AS totalRevenueInCents,
        COALESCE(exp.totalExpensesInCents, 0) AS totalExpensesInCents,
        COALESCE(inv.catalogueCount, 0) AS catalogueCount
    FROM events AS e
    LEFT JOIN (
        -- --- THIS ENTIRE SUBQUERY IS REWRITTEN ---
        SELECT
            i.eventId,
            SUM(i.allocatedQuantity) AS totalItemsAllocated,
            SUM(i.soldQuantity) AS totalItemsSold,
            COUNT(i.itemId) AS catalogueCount,
            -- The calculation is now simpler because we have access to the price via the JOIN
            SUM(i.soldQuantity * (c.price * 100)) AS totalRevenueInCents
        FROM event_inventory AS i
        -- We explicitly JOIN catalogue_items so Room's tracker can see it
        INNER JOIN catalogue_items AS c ON i.itemId = c.itemId
        GROUP BY i.eventId
    ) AS inv ON e.eventId = inv.eventId
    LEFT JOIN (
        -- This subquery was already correct
        SELECT
            eventId,
            SUM(amountInCents) AS totalExpensesInCents
        FROM event_expenses
        GROUP BY eventId
    ) AS exp ON e.eventId = exp.eventId
    """
)
data class EventSummaryView(
    val eventId: Int,
    val title: String,
    val location: String,
    val startDate: Long,
    val endDate: Long,
    val totalItemsAllocated: Int,
    val totalItemsSold: Int,
    val totalRevenueInCents: Long,
    val totalExpensesInCents: Long,
    val catalogueCount: Int
) {
    fun toEvent(): Event {
        return Event(
            eventId = this.eventId,
            title = this.title,
            location = this.location,
            startDate = this.startDate,
            endDate = this.endDate
        ).apply {
            this.totalItemsAllocated = this@EventSummaryView.totalItemsAllocated
            this.totalItemsSold = this@EventSummaryView.totalItemsSold
            this.totalRevenueInCents = this@EventSummaryView.totalRevenueInCents
            this.totalExpensesInCents = this@EventSummaryView.totalExpensesInCents
            this.catalogueCount = this@EventSummaryView.catalogueCount
            this.totalStockLeft = this@EventSummaryView.totalItemsAllocated - this@EventSummaryView.totalItemsSold
        }
    }
}