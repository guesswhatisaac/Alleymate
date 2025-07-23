package com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.views

import androidx.room.DatabaseView
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.EventStatus

/**
 * Database view that summarizes key metrics per event, including inventory stats and financials.
 */
@DatabaseView(
    """
    SELECT
        e.eventId,
        e.title,
        e.location,
        e.startDate,
        e.endDate,
        e.status,

        -- Inventory summary
        COALESCE(inv.totalItemsAllocated, 0) AS totalItemsAllocated,
        COALESCE(inv.totalItemsSold, 0) AS totalItemsSold,
        COALESCE(inv.totalRevenueInCents, 0) AS totalRevenueInCents,
        COALESCE(inv.catalogueCount, 0) AS catalogueCount,

        -- Expense summary
        COALESCE(exp.totalExpensesInCents, 0) AS totalExpensesInCents

    FROM events AS e

    -- Join with inventory summary per event
    LEFT JOIN (
        SELECT
            i.eventId,
            SUM(i.allocatedQuantity) AS totalItemsAllocated,
            SUM(i.soldQuantity) AS totalItemsSold,
            COUNT(i.itemId) AS catalogueCount,
            SUM(i.soldQuantity * (c.price * 100)) AS totalRevenueInCents
        FROM event_inventory AS i
        INNER JOIN catalogue_items AS c ON i.itemId = c.itemId
        GROUP BY i.eventId
    ) AS inv ON e.eventId = inv.eventId

    -- Join with expenses summary per event
    LEFT JOIN (
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
    val status: EventStatus,
    val totalItemsAllocated: Int,
    val totalItemsSold: Int,
    val totalRevenueInCents: Long,
    val totalExpensesInCents: Long,
    val catalogueCount: Int
)
