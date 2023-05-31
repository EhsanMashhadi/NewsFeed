package software.ehsan.newsfeed.ui.common

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import software.ehsan.newsfeed.domain.ad.GetAdLastShownTimeUseCase
import software.ehsan.newsfeed.domain.ad.SaveAdTimeUseCase
import software.ehsan.newsfeed.ui.base.BaseViewModel
import software.ehsan.newsfeed.util.Event
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val context: Application,
    private val getAddLastShownTimeUseCase: GetAdLastShownTimeUseCase,
    private val saveAdTimeUseCase: SaveAdTimeUseCase
) : BaseViewModel() {

    companion object {
        const val MIN_3 = 1000 * 1 * 12
    }

    private val _adLiveData = MutableLiveData<Event<MainEvent>>()
    val adLiveData: LiveData<Event<MainEvent>> = _adLiveData

    private var isLoading = false

    fun showAds() {
        viewModelScope.launch(Dispatchers.Main) {
            val lastTime = getAddLastShownTimeUseCase.invoke()
            if (!isLoading) {
                if (lastTime == null) {
                    loadAds()
                } else {
                    val lastTimeMillisecond = java.lang.Long.valueOf(lastTime)
                    val currentTimeMillisecond = System.currentTimeMillis()
                    if (currentTimeMillisecond >= lastTimeMillisecond + MIN_3) {
                        loadAds()
                    }
                }
            }
        }
    }

    private fun loadAds() {
        isLoading = true
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(context,
            "ca-app-pub-3940256099942544/1033173712",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Logger.d("Ad is loaded successfully.")
                    displayAds(interstitialAd = interstitialAd)
                    isLoading = false
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Logger.e(adError.toString())
                    isLoading = false
                }
            })
    }

    private fun displayAds(interstitialAd: InterstitialAd) {
        viewModelScope.launch {
            _adLiveData.value = Event(MainEvent.ShowAds(interstitialAd))
            saveAdTimeUseCase.invoke()
        }
    }
}

sealed class MainEvent {
    data class ShowAds(val interstitialAd: InterstitialAd) : MainEvent()
}