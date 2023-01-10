package software.ehsan.newsfeed.data.model

import software.ehsan.newsfeed.util.DigestUtil
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Article(
    @field:Json(name = "source") val source: Source,
    @field:Json(name = "author") val author: String?,
    @field:Json(name = "title") val title: String,
    @field:Json(name = "description") val description: String?,
    @field:Json(name = "url") val url: String,
    @field:Json(name = "urlToImage") val imageUrl: String?,
    @field:Json(name = "content") val content: String?,
    @field:Json(name = "publishedAt") val date: String,
    @field:Json(name = "mocked") val mocked:Boolean = false,
    var isSaved: Boolean = false,
    val id: String = DigestUtil.sha256(url + title)
) {
    constructor() : this(Source("", ""), "", "", "", "", "", "", "")
}
