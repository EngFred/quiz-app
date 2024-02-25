package com.engineerfred.kotlin.ktor.data.remote.repository

import android.util.Log
import com.engineerfred.kotlin.ktor.domain.model.Question
import com.engineerfred.kotlin.ktor.domain.repository.QuestionsRepository
import com.engineerfred.kotlin.ktor.ui.model.Subject
import com.engineerfred.kotlin.ktor.util.Constants.QUESTIONS_COLLECTION
import com.engineerfred.kotlin.ktor.util.Response
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException
import javax.inject.Inject

class QuestionsRepoImpl @Inject constructor (
    private val db: FirebaseFirestore
) : QuestionsRepository {

    companion object {
        private val TAG = QuestionsRepoImpl::class.java.simpleName
    }

    override suspend fun setQuestion(question: Question): Response<Any> {
        return try {

            val result = db.collection( QUESTIONS_COLLECTION ).document( "${question.id}" ).set(question).await()
            Response.Success(result)

        } catch ( ex: Exception ) {
            if ( ex is CancellationException ) throw  ex
            Log.d(TAG, "Error while setting question! $ex")
            Response.Error("$ex")
        }
    }

    override suspend fun updateQuestion(question: Question): Response<Any> {
        return try {
            val result = db.collection( QUESTIONS_COLLECTION ).document( "${question.id}" ).set(question).await()
            Response.Success(result)
        } catch ( ex: Exception ) {
            if ( ex is CancellationException ) throw  ex
            Log.d(TAG, "Error while updating question! $ex")
            Response.Error("$ex")
        }
    }

    override suspend fun deleteQuestion(question: Question): Response<Any> {
        return try {
            val result = db.collection( QUESTIONS_COLLECTION ).document( "${question.id}" ).delete().await()
            Response.Success(result)
        } catch ( ex: Exception ) {
            if ( ex is CancellationException ) throw  ex
            Log.d(TAG, "Error while deleting question! $ex")
            Response.Error("$ex")
        }
    }

    override fun getAllQuestions( subject: String ): Flow<Response<List<Question>>> {
        return callbackFlow {
            val task = db.collection( QUESTIONS_COLLECTION )
                .whereEqualTo("subject", subject )
                .addSnapshotListener { value, error ->
                    if ( error != null ) {
                        Log.v(TAG, "Snapshot error: $error")
                        return@addSnapshotListener
                    }
                    if( value != null ) {
                        Log.v(TAG, "Received a question snapshot")
                        val questions = value.toObjects( Question::class.java)
                        trySend(Response.Success(questions))
                    }
            }
            awaitClose { task.remove() }
        }.catch {
            Log.d(TAG, "Error while getting $subject questions! $it")
            emit( Response.Success(emptyList()) )
        }.flowOn( Dispatchers.IO )
    }

    override fun getEnglishQuestions(level: String): Flow<Response<List<Question>>> {
        return callbackFlow {
            val task = db.collection( QUESTIONS_COLLECTION )
                .whereEqualTo("subject", Subject.English.name )
                .whereEqualTo("level", level)
                .addSnapshotListener { value, error ->
                    if ( error != null ) {
                        Log.v(TAG, "Snapshot error: $error")
                        return@addSnapshotListener
                    }
                    if( value != null ) {
                        Log.v(TAG, "Received an english question snapshot")
                        val mathQuestions = value.toObjects( Question::class.java)
                        trySend(Response.Success(mathQuestions))
                    }
                }
            awaitClose { task.remove() }
        }.catch {
            Log.d(TAG, "Error while getting english questions! $it")
            emit( Response.Success(emptyList()) )
        }.flowOn( Dispatchers.IO )
    }

    override fun getMathQuestions(level: String): Flow<Response<List<Question>>> {
        return callbackFlow {
            val task = db.collection( QUESTIONS_COLLECTION )
                .whereEqualTo("subject", Subject.Mathematics.name )
                .whereEqualTo("level", level )
                .addSnapshotListener { value, error ->
                    if ( error != null ) {
                        Log.v(TAG, "Snapshot error: $error")
                        return@addSnapshotListener
                    }
                    if( value != null ) {
                        Log.v(TAG, "Received a mathematics question snapshot")
                        val mathQuestions = value.toObjects( Question::class.java )
                        trySend(Response.Success(mathQuestions))
                    }
                }
            awaitClose { task.remove() }
        }.catch {
            Log.d(TAG, "Error while getting mathematics questions! $it")
            emit( Response.Success(emptyList()) )
        }.flowOn( Dispatchers.IO )
    }

    override fun getQuestionById(questionId: String): Flow<Response<Question>> {
        return flow {
            val docRef = db.collection( QUESTIONS_COLLECTION ).document( questionId ).get().await()
            if ( docRef.exists() ) {
                val question = docRef.toObject(Question::class.java)
                if ( question != null )
                    emit( Response.Success(question) )
                else
                    emit(Response.Error("Question not found!"))
            }
        }.catch {
            Log.d(TAG, "Error while getting question! $it")
            emit(Response.Error("$it"))
        }.flowOn( Dispatchers.IO )
    }

}