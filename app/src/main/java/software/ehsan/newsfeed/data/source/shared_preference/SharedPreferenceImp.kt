package software.ehsan.newsfeed.data.source.shared_preference

import android.content.Context
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SharedPreferenceImp(
    private val context: Context,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : SharedPreferenceInterface {

    companion object {
        const val SHARED_PREFERENCE_NAME = "stored_shared_pref"
    }

    private val sharedPreference =
        context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
    private val sharedPreferenceEditor = sharedPreference.edit()

    override suspend fun insertStringSync(key: String, value: String) =
        withContext(dispatcher) {
            sharedPreferenceEditor.putString(key, value).commit()
        }

    override suspend fun insertStringAsync(key: String, value: String) = withContext(dispatcher) {
        sharedPreferenceEditor.putString(key, value).apply()
    }

    override suspend fun getString(key: String) = withContext(dispatcher) {
            sharedPreference.getString(key, null)
    }
}