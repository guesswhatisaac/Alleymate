package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.reports

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.local.AlleyMateDatabase
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.Event
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.repository.EventRepository
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.repository.ReportsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import java.util.Calendar

@OptIn(ExperimentalCoroutinesApi::class)
class ReportsViewModel(application: Application) : AndroidViewModel(application) {

    // Initialize repositories
    private val reportsRepository: ReportsRepository
    private val eventRepository: EventRepository

    // Backing state for current filter selection
    private val _filterState = MutableStateFlow(ReportsFilterState(selectedEvent = null))
    val filterState: StateFlow<ReportsFilterState> = _filterState.asStateFlow()

    // List of all events for filter dropdown
    val allEvents: StateFlow<List<Event>>

    // Summary report values (e.g., revenue, profit, sold count)
    val reportStats: StateFlow<ReportStats>

    // List of best-selling items
    val bestSellers: StateFlow<List<BestSeller>>

    init {
        // Initialize database and repositories
        val database = AlleyMateDatabase.getDatabase(application)
        reportsRepository = ReportsRepository(database.transactionDao(), database.eventDao())
        eventRepository = EventRepository(database.eventDao(), database.catalogueDao(), database.transactionDao())

        // Load all events and expose them as a StateFlow
        allEvents = eventRepository.getAllEvents()
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

        // Reactively build a combined data stream for sales and expenses
        val reportsDataFlow = _filterState.flatMapLatest { filters ->
            val eventIds = filters.selectedEvent?.eventId?.let { listOf(it) }
                ?: allEvents.value.map { it.eventId }

            val startTime = getStartTimeForFilter(filters.selectedTimeFilter)

            // If no event is selected, return empty flow
            if (eventIds.isEmpty()) {
                flowOf(Pair(emptyList(), 0L))
            } else {
                // Combine both revenue and expense flows
                combine(
                    reportsRepository.getSalesData(eventIds, startTime),
                    reportsRepository.getTotalExpenses(eventIds)
                ) { salesData, totalExpenses ->
                    Pair(salesData, totalExpenses ?: 0L)
                }
            }
        }

        // Map the combined flow into a single ReportStats object
        reportStats = reportsDataFlow.map { (salesData, totalExpenses) ->
            val totalRevenue = salesData.sumOf { it.totalRevenueInCents }
            ReportStats(
                totalRevenueInCents = totalRevenue,
                totalExpensesInCents = totalExpenses,
                netProfitInCents = totalRevenue - totalExpenses,
                itemsSoldCount = salesData.sumOf { it.quantitySold }
            )
        }.stateIn(viewModelScope, SharingStarted.Eagerly, ReportStats())

        // Generate ranked best sellers based on quantity sold
        bestSellers = reportsDataFlow.map { (salesData, _) ->
            salesData.mapIndexed { index, data ->
                BestSeller(
                    rank = index + 1,
                    name = data.name,
                    quantitySold = data.quantitySold,
                    totalRevenueInCents = data.totalRevenueInCents
                )
            }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    }

    // Updates the selected event and resets the time filter to ALL_TIME
    fun selectEvent(event: Event?) {
        _filterState.value = _filterState.value.copy(
            selectedEvent = event,
            selectedTimeFilter = TimeFilter.ALL_TIME
        )
    }

    // Updates only the selected time filter
    fun selectTimeFilter(timeFilter: TimeFilter) {
        _filterState.value = _filterState.value.copy(selectedTimeFilter = timeFilter)
    }

    // Converts a time filter enum into a Unix timestamp for querying
    private fun getStartTimeForFilter(timeFilter: TimeFilter): Long {
        val calendar = Calendar.getInstance()
        return when (timeFilter) {
            TimeFilter.ALL_TIME -> 0L
            TimeFilter.LAST_7_DAYS -> {
                calendar.add(Calendar.DAY_OF_YEAR, -7)
                calendar.timeInMillis
            }
            TimeFilter.LAST_30_DAYS -> {
                calendar.add(Calendar.DAY_OF_YEAR, -30)
                calendar.timeInMillis
            }
            TimeFilter.THIS_MONTH -> {
                calendar.set(Calendar.DAY_OF_MONTH, 1)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.timeInMillis
            }
        }
    }
}
