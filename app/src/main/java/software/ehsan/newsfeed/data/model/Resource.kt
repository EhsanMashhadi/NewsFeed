package software.ehsan.newsfeed.data.model


data class Resource<out T>(val status: Status, val data: T?, val exception: Exception?) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.Success, data, null)
        }

        fun <T> error(msg: String, data: T? = null): Resource<T> {
            return Resource(Status.Error, data, null)
        }

        fun <T> error(exception: Exception? = null): Resource<T> {
            return Resource(Status.Error, null, exception)
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(Status.Loading, data, null)
        }
    }
}

sealed class Status {
    object Success:Status()
    object Error:Status()
    object Loading:Status()
}