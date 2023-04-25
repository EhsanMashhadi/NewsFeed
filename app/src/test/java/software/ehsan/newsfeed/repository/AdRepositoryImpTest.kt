package software.ehsan.newsfeed.repository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import software.ehsan.newsfeed.data.repository.AdRepositoryImp
import software.ehsan.newsfeed.data.source.shared_preference.SharedPreferenceInterface

class AdRepositoryImpTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testSaveAndGetTime() = runTest{
        val adRepositoryImp = AdRepositoryImp(MockSharedPreference())
        val time = System.currentTimeMillis().toDouble()
        adRepositoryImp.insertAdShownTime()
        val savedTime = adRepositoryImp.getLastAdShownTime()!!.toDouble()
        Assert.assertEquals(savedTime,time,1000.0)
    }


    class MockSharedPreference():SharedPreferenceInterface{

        private val map = mutableMapOf<String,String>()

        override suspend fun insertStringSync(key: String, value: String): Boolean {
            map[key] = value
            return true;
        }

        override suspend fun insertStringAsync(key: String, value: String) {
            map[key] = value
        }

        override suspend fun getString(key: String): String? {
            return map[key]
        }
    }
}