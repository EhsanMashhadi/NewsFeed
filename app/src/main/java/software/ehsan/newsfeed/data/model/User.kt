package software.ehsan.newsfeed.data.model

data class User(val email: String? = null, val id: String = "", val isAuthenticated:Boolean = false, val isEmailVerified:Boolean = false)
