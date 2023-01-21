package software.ehsan.newsfeed.domain.profile

import com.google.android.gms.tasks.Task
import software.ehsan.newsfeed.data.model.Feedback
import software.ehsan.newsfeed.data.repository.FeedbackRepository
import javax.inject.Inject

class SendFeedbackUseCase @Inject constructor(private val feedbackRepository: FeedbackRepository) {

    operator fun invoke(feedback: Feedback): Task<Void> {
        return feedbackRepository.sendFeedback(feedback = feedback)
    }
}