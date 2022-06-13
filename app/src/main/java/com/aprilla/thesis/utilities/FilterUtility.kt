package com.aprilla.thesis.utilities

import androidx.sqlite.db.SimpleSQLiteQuery

object FilterUtility {
    fun getFilteredQuery(filter: GroupingFilter): SimpleSQLiteQuery {
        val simpleQuery = StringBuilder().append("SELECT * FROM saved ")
        when (filter) {
            GroupingFilter.CAT_NEWS -> {
                simpleQuery.append("WHERE category = \'News\' ORDER BY pubDate DESC")
            }
            GroupingFilter.CAT_ECO -> {
                simpleQuery.append("WHERE category = \'Ekonomi\' ORDER BY pubDate DESC")
            }
            GroupingFilter.CAT_POL -> {
                simpleQuery.append("WHERE category = \'Politik\' ORDER BY pubDate DESC")
            }
            GroupingFilter.CAT_HEALTH -> {
                simpleQuery.append("WHERE category = \'Kesehatan\' ORDER BY pubDate DESC")
            }
            GroupingFilter.CAT_SPORT -> {
                simpleQuery.append("WHERE category = \'Olahraga\' ORDER BY pubDate DESC")
            }
            GroupingFilter.CAT_OTHER -> {
                simpleQuery.append("WHERE category = \'Lainnya\' ORDER BY pubDate DESC")
            }
            else -> {
                simpleQuery.append("ORDER BY pubDate DESC")
            }
        }
        return SimpleSQLiteQuery(simpleQuery.toString())
    }
}