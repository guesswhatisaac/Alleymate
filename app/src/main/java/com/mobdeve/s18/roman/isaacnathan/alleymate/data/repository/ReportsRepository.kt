package com.mobdeve.s18.roman.isaacnathan.alleymate.data.repository

import com.mobdeve.s18.roman.isaacnathan.alleymate.data.local.EventDao
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.local.TransactionDao
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.query_results.MonthlyRevenue
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.query_results.SalesData
import kotlinx.coroutines.flow.Flow

class ReportsRepository(
    private val transactionDao: TransactionDao,
    private val eventDao: EventDao
) {
    fun getSalesData(eventIds: List<Int>, startDate: Long): Flow<List<SalesData>> {
        // Return empty flow if eventIds list is empty to prevent SQL errors
        if (eventIds.isEmpty()) return kotlinx.coroutines.flow.flowOf(emptyList())
        return transactionDao.getSalesData(eventIds, startDate)
    }

    fun getTotalExpenses(eventIds: List<Int>): Flow<Long?> {
        if (eventIds.isEmpty()) return kotlinx.coroutines.flow.flowOf(0L)
        return eventDao.getTotalExpensesForEvents(eventIds)
    }

    fun getMonthlyRevenue(startTime: Long): Flow<List<MonthlyRevenue>> {
        return transactionDao.getMonthlyRevenue(startTime)
    }
}