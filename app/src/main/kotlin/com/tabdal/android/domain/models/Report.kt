package com.tabdal.android.domain.models

data class Report(
    val id: String = "",
    val targetId: String,
    val targetType: ReportTarget,
    val reporterId: String,
    val reason: ReportReason,
    val details: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

enum class ReportTarget { LISTING, MESSAGE, USER }

enum class ReportReason(val labelFr: String) {
    ILLEGAL_CONTENT("Contenu illicite"),
    FRAUD("Fraude"),
    DUPLICATE("Doublon"),
    MISLEADING("Informations trompeuses"),
    OTHER("Autre")
}
