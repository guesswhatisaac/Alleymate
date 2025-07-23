package com.mobdeve.s18.roman.isaacnathan.alleymate.data.repository

import com.mobdeve.s18.roman.isaacnathan.alleymate.data.local.EventDao
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.local.TransactionDao
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.query_results.SalesData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * Repository for generating report data such as sales summaries,
 * total expenses, and monthly revenue.
 */
class ReportsRepository(
    private val transactionDao: TransactionDao,
    private val eventDao: EventDao
) {

    /**
     * Returns sales data for a given list of event IDs starting from a specific date.
     * Returns an empty list if eventIds is empty.
     */
    fun getSalesData(eventIds: List<Int>, startDate: Long): Flow<List<SalesData>> {
        if (eventIds.isEmpty()) return flowOf(emptyList())
        return transactionDao.getSalesData(eventIds, startDate)
    }

    /**
     * Returns the total expenses for a list of event IDs.
     * Returns 0 if the list is empty.
     */
    fun getTotalExpenses(eventIds: List<Int>): Flow<Long?> {
        if (eventIds.isEmpty()) return flowOf(0L)
        return eventDao.getTotalExpensesForEvents(eventIds)
    }

}
