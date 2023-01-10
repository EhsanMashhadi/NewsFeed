package software.ehsan.newsfeed.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Source(
    @field:Json(name = "id")
    val id: String?,
    @field:Json(name = "name")
    val name: String
){
    constructor() : this("","")
}
