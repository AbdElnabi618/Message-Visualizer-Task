package com.abdelnabi.messagevisualizer.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MessageModel {
    @Expose
    @SerializedName("feed")
    var feed: FeedEntity? = null

    class FeedEntity {
        @Expose
        @SerializedName("entry")
        var entry: List<EntryEntity>? = null
    }

    class EntryEntity {
        @Expose
        @SerializedName("content")
        var content: ContentEntity? = null
    }

    class ContentEntity {
        @Expose
        @SerializedName("\$t")
        var messageBody: String? = null

        @Expose
        @SerializedName("type")
        var type: String? = null
    }
}