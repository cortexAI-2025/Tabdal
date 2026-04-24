package com.tabdal.android.domain.usecases.listing

import android.net.Uri
import com.tabdal.android.domain.models.Listing
import com.tabdal.android.domain.repository.ListingRepository
import com.tabdal.android.utils.Result
import javax.inject.Inject

class CreateListingUseCase @Inject constructor(private val repo: ListingRepository) {
    suspend operator fun invoke(listing: Listing, photoUris: List<Uri>): Result<Listing> {
        if (listing.title.isBlank()) return Result.Error("Titre requis")
        if (listing.price <= 0) return Result.Error("Prix invalide")
        if (listing.surface <= 0) return Result.Error("Surface invalide")
        if (listing.city.isBlank()) return Result.Error("Ville requise")
        return repo.createListing(listing, photoUris)
    }
}
