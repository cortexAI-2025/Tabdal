package com.tabdal.android.presentation.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tabdal.android.domain.models.*
import com.tabdal.android.domain.repository.AuthRepository
import com.tabdal.android.domain.repository.ListingRepository
import com.tabdal.android.domain.usecases.listing.CreateListingUseCase
import com.tabdal.android.domain.usecases.listing.GetListingsUseCase
import com.tabdal.android.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ListingUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
    val listing: Listing? = null,
    val myListings: List<Listing> = emptyList(),
    val favorites: List<Listing> = emptyList()
)

@HiltViewModel
class ListingViewModel @Inject constructor(
    private val getListingsUseCase: GetListingsUseCase,
    private val createListingUseCase: CreateListingUseCase,
    private val listingRepository: ListingRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListingUiState())
    val uiState: StateFlow<ListingUiState> = _uiState.asStateFlow()

    private val _filter = MutableStateFlow(ListingFilter())
    val filter: StateFlow<ListingFilter> = _filter.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val listings: Flow<PagingData<Listing>> = _filter
        .flatMapLatest { getListingsUseCase(it) }
        .cachedIn(viewModelScope)

    val favorites: Flow<List<Listing>> = listingRepository.getFavorites()

    fun applyFilter(filter: ListingFilter) { _filter.value = filter }

    fun loadListingDetail(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = listingRepository.getListingById(id)) {
                is Result.Success -> _uiState.update { it.copy(isLoading = false, listing = result.data) }
                is Result.Error   -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                else -> {}
            }
        }
    }

    fun loadMyListings() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = listingRepository.getMyListings()) {
                is Result.Success -> _uiState.update { it.copy(isLoading = false, myListings = result.data) }
                is Result.Error   -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                else -> {}
            }
        }
    }

    fun createListing(listing: Listing, photoUris: List<Uri>) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = createListingUseCase(listing, photoUris)) {
                is Result.Success -> _uiState.update { it.copy(isLoading = false, success = true, listing = result.data) }
                is Result.Error   -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                else -> {}
            }
        }
    }

    fun updateListing(listing: Listing, newPhotoUris: List<Uri>) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = listingRepository.updateListing(listing, newPhotoUris)) {
                is Result.Success -> _uiState.update { it.copy(isLoading = false, success = true) }
                is Result.Error   -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                else -> {}
            }
        }
    }

    fun deleteListing(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = listingRepository.deleteListing(id)) {
                is Result.Success -> { _uiState.update { it.copy(isLoading = false, success = true) }; loadMyListings() }
                is Result.Error   -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                else -> {}
            }
        }
    }

    fun markAsSold(id: String) {
        viewModelScope.launch {
            when (val result = listingRepository.markAsSold(id)) {
                is Result.Success -> loadMyListings()
                is Result.Error   -> _uiState.update { it.copy(error = result.message) }
                else -> {}
            }
        }
    }

    fun toggleFavorite(listingId: String) {
        viewModelScope.launch { listingRepository.toggleFavorite(listingId) }
    }

    fun reportListing(report: Report) {
        viewModelScope.launch {
            when (val result = listingRepository.reportListing(report)) {
                is Result.Success -> _uiState.update { it.copy(success = true) }
                is Result.Error   -> _uiState.update { it.copy(error = result.message) }
                else -> {}
            }
        }
    }

    fun clearError()   { _uiState.update { it.copy(error = null) } }
    fun clearSuccess() { _uiState.update { it.copy(success = false) } }

    fun currentUserId() = authRepository.getCurrentUserId()
}
