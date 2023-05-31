package software.ehsan.newsfeed.data.model

data class Feedback(
    val email: String?,
    val feedback: String,
    val userAppProperties: UserAppProperties,
)
