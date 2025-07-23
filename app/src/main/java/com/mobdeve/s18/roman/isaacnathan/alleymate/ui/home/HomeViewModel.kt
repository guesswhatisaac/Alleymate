package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.local.AlleyMateDatabase
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.EventStatus
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.repository.CatalogueRepository
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.repository.EventRepository
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.repository.ReportsRepository
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events.EventUiModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import java.util.*

private const val LOW_STOCK_THRESHOLD = 5 // Threshold for flagging low-stock items

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel(application: Application) : AndroidViewModel(application) {

    // Repositories for accessing app data
    private val eventRepository: EventRepository
    private val reportsRepository: ReportsRepository
    private val catalogueRepository: CatalogueRepository

    // UI state exposed as flows
    val liveEvent: StateFlow<EventUiModel?>           // Ongoing event
    val upcomingEvents: StateFlow<List<EventUiModel>> // Future events
    val overviewStats: StateFlow<HomeOverviewStats>   // Dashboard summary

    init {
        // Get DAOs from the database
        val database = AlleyMateDatabase.getDatabase(application)
        eventRepository = EventRepository(database.eventDao(), database.catalogueDao(), database.transactionDao())
        reportsRepository = ReportsRepository(database.transactionDao(), database.eventDao())
        catalogueRepository = CatalogueRepository(database.catalogueDao())

        // --- Events Section ---

        // Shared source of hydrated events
        val allHydratedEventsFlow = eventRepository.getHydratedEvents()

        // Live event: the first with status LIVE
        liveEvent = allHydratedEventsFlow
            .map { events -> events.firstOrNull { it.status == EventStatus.LIVE } }
            .stateIn(viewModelScope, SharingStarted.Eagerly, null)

        // Upcoming events: all with status UPCOMING
        upcomingEvents = allHydratedEventsFlow
            .map { events -> events.filter { it.status == EventStatus.UPCOMING } }
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

        // --- Overview Stats Section ---

        // All event IDs needed for filtering sales data
        val allEventIdsFlow = eventRepository.getAllEvents().map { events -> events.map { it.eventId } }

        // Start of the last 30-day window (in ms)
        val last30DaysStart = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -30) }.timeInMillis

        // Total revenue in the last 30 days
        val last30DaysRevenueFlow = allEventIdsFlow.flatMapLatest { ids ->
            reportsRepository.getSalesData(ids, last30DaysStart)
        }.map { salesData -> salesData.sumOf { it.totalRevenueInCents } }

        // Top-selling item (all-time)
        val bestSellerFlow = allEventIdsFlow.flatMapLatest { ids ->
            reportsRepository.getSalesData(ids, 0L)
        }.map { salesData -> salesData.firstOrNull()?.name ?: "N/A" }

        // Number of low-stock items
        val lowStockItemsFlow = catalogueRepository.getLowStockItemsCount(LOW_STOCK_THRESHOLD)

        // Total number of catalogue items
        val catalogueCountFlow = catalogueRepository.getAllItems().map { it.size }

        // Combine all flows into a unified dashboard summary
        overviewStats = combine(
            last30DaysRevenueFlow,
            bestSellerFlow,
            lowStockItemsFlow,
            catalogueCountFlow
        ) { revenue, bestSeller, lowStockCount, catalogueItemsCount ->
            HomeOverviewStats(
                last30DaysRevenueInCents = revenue,
                bestSellerName = bestSeller,
                lowStockItemsCount = lowStockCount,
                catalogueItemsCount = catalogueItemsCount
            )
        }.stateIn(viewModelScope, SharingStarted.Eagerly, HomeOverviewStats())
    }
}
