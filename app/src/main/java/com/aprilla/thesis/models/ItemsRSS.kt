package com.aprilla.thesis.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import org.simpleframework.xml.Element
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.Root

@Parcelize
@Entity(tableName = "saved")
@Root(strict = false, name = "item")
data class ItemsRSS @JvmOverloads constructor(
    @field:Element(name = "title")
    @param:Element(name = "title")
    @ColumnInfo(name = "title")
    var title: String = "",

    @field:Element(name = "pubDate")
    @param:Element(name = "pubDate")
    @ColumnInfo(name = "pubDate")
    var pubDate: String = "",

    @field:Element(name = "link")
    @param:Element(name = "link")
    @ColumnInfo(name = "link")
    @PrimaryKey
    var link: String = "",

//    @field:Element(name = "guid")
//    @param:Element(name = "guid")
//    @ColumnInfo(name = "guid")
//    var guid: String = "",

    @field:Element(name = "image", required = false)
    @param:Element(name = "image", required = false)
    @ColumnInfo(name = "thumbnail")
    var thumbnail: String = "",

//    @field:Element(name = "description")
//    @param:Element(name = "description")
//    @ColumnInfo(name = "description")
//    var description: String = "",

    @field:Namespace(prefix = "content", reference = "http://purl.org/rss/1.0/modules/content/")
    @param:Namespace(prefix = "content", reference = "http://purl.org/rss/1.0/modules/content/")
    @field:Element(name = "encoded", data = true)
    @param:Element(name = "encoded", data = true)
    @ColumnInfo(name = "content")
    var content: String = "",

    @field:Namespace(prefix = "dc", reference = "http://purl.org/dc/elements/1.1/")
    @param:Namespace(prefix = "dc", reference = "http://purl.org/dc/elements/1.1/")
    @field:Element(name = "creator", data = true, required = false)
    @param:Element(name = "creator", data = true, required = false)
    @ColumnInfo(name = "author")
    var author: String = "",

    @ColumnInfo(name = "isFav")
    var favourite: Boolean = false
) : Parcelable