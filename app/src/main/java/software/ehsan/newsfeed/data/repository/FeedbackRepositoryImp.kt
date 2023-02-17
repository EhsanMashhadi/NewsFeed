package software.ehsan.newsfeed.data.repository

import com.google.android.gms.tasks.Task
import software.ehsan.newsfeed.data.model.Feedback
import software.ehsan.newsfeed.data.source.database.firebase.FirebaseRealTimeDatabase

class FeedbackRepositoryImp(private val firebaseRealTimeDatabase: FirebaseRealTimeDatabase): FeedbackRepository, BaseRepository() {

    override fun sendFeedback(feedback: Feedback): Task<Void> {
        return firebaseRealTimeDatabase.insertFeedback(feedback = feedback)
    }
}