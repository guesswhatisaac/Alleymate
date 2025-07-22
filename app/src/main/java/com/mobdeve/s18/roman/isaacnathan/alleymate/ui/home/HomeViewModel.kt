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

private const val LOW_STOCK_THRESHOLD = 5

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val eventRepository: EventRepository
    private val reportsRepository: ReportsRepository
    private val catalogueRepository: CatalogueRepository

    // --- UI STATE FLOWS ---
    val liveEvent: StateFlow<EventUiModel?>
    val upcomingEvents: StateFlow<List<EventUiModel>>
    val overviewStats: StateFlow<HomeOverviewStats>

    init {
        val database = AlleyMateDatabase.getDatabase(application)
        eventRepository = EventRepository(database.eventDao(), database.catalogueDao(), database.transactionDao())
        reportsRepository = ReportsRepository(database.transactionDao(), database.eventDao())
        catalogueRepository = CatalogueRepository(database.catalogueDao(), database)

        // --- Live and Upcoming Events ---
        val allHydratedEventsFlow = eventRepository.getHydratedEvents()
        liveEvent = allHydratedEventsFlow
            .map { events -> events.firstOrNull { it.status == EventStatus.LIVE } }
            .stateIn(viewModelScope, SharingStarted.Eagerly, null)
        upcomingEvents = allHydratedEventsFlow
            .map { events -> events.filter { it.status == EventStatus.UPCOMING } }
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

        // --- Overview Stats ---
        val allEventIdsFlow = eventRepository.getAllEvents().map { events -> events.map { it.eventId } }

        val last30DaysStart = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -30) }.timeInMillis
        val last30DaysRevenueFlow = allEventIdsFlow.flatMapLatest { ids ->
            reportsRepository.getSalesData(ids, last30DaysStart)
        }.map { salesData -> salesData.sumOf { it.totalRevenueInCents } }

        val bestSellerFlow = allEventIdsFlow.flatMapLatest { ids ->
            reportsRepository.getSalesData(ids, 0L) // 0L for all time
        }.map { salesData -> salesData.firstOrNull()?.name ?: "N/A" }

        val lowStockItemsFlow = catalogueRepository.getLowStockItemsCount(LOW_STOCK_THRESHOLD)

        val catalogueCountFlow = catalogueRepository.getAllItems().map { it.size }

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