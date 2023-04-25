package software.ehsan.newsfeed.domain

import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import software.ehsan.newsfeed.data.repository.AdRepository
import software.ehsan.newsfeed.domain.ad.GetAdLastShownTimeUseCase

@RunWith(MockitoJUnitRunner::class)
class GetAdLastShownTimeUseCaseTest {


    @Mock
    private lateinit var adRepository: AdRepository


    @Test
    fun test() = runTest {
        GetAdLastShownTimeUseCase(adRepository).invoke()
        Mockito.verify(adRepository).getLastAdShownTime()
    }

}