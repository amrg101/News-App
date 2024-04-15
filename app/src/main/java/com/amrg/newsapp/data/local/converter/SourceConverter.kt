package com.amrg.newsapp.data.local.converter

import androidx.room.TypeConverter
import com.amrg.newsapp.data.remote.models.responses.SourceApi

class SourceConverter {
    @TypeConverter
    fun fromSource(source: SourceApi): String {
        return source.name ?: ""
    }

    @TypeConverter
    fun toSource(name: String): SourceApi {
        return SourceApi(name, name)
    }
}