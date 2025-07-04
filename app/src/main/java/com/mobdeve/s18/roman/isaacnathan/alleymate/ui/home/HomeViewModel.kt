package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.local.AlleyMateDatabase
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.repository.CatalogueRepository
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val catalogueRepository: CatalogueRepository

    init {
        val database = AlleyMateDatabase.getDatabase(application)
        catalogueRepository = CatalogueRepository(database.catalogueDao(), database)
    }

    fun deleteAllCatalogueItems() {
        viewModelScope.launch {
            catalogueRepository.deleteAllItems()
        }
    }

    fun restartDatabase() {
        viewModelScope.launch {
            catalogueRepository.clearEntireDatabase()
        }
    }
}