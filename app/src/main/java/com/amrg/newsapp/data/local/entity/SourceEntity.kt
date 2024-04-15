package com.amrg.newsapp.data.local.entity

import androidx.room.ColumnInfo

data class SourceEntity(
    @ColumnInfo("sourceId")
    val id: String?,

    @ColumnInfo("sourceName")
    val name: String?
)

