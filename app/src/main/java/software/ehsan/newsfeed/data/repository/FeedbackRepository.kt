package software.ehsan.newsfeed.data.repository

import com.google.android.gms.tasks.Task
import software.ehsan.newsfeed.data.model.Feedback

interface FeedbackRepository {
    fun sendFeedback(feedback: Feedback): Task<Void>
}