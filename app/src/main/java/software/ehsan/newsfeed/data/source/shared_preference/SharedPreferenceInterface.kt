package software.ehsan.newsfeed.data.source.shared_preference

interface SharedPreferenceInterface {
    suspend fun insertStringSync(key:String, value:String):Boolean
    suspend fun insertStringAsync(key: String,value: String)
    suspend fun getString(key:String):String?
}