package com.tabdal.android.domain.usecases.listing

import androidx.paging.PagingData
import com.tabdal.android.domain.models.Listing
import com.tabdal.android.domain.models.ListingFilter
import com.tabdal.android.domain.repository.ListingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetListingsUseCase @Inject constructor(private val repo: ListingRepository) {
    operator fun invoke(filter: ListingFilter): Flow<PagingData<Listing>> = repo.getListings(filter)
}
