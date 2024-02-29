package com.engineerfred.kotlin.ktor.ui.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.engineerfred.kotlin.ktor.domain.usecase.questions.GetAllQuestionsUseCase
import com.engineerfred.kotlin.ktor.ui.screens.admin.home.QuestionsScreenState
import com.engineerfred.kotlin.ktor.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionsScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getAllQuestionsUseCase: GetAllQuestionsUseCase
) : ViewModel() {

    var uiState by mutableStateOf( QuestionsScreenState() )
        private set

    companion object {
        private val TAG = QuestionsScreenViewModel::class.java.simpleName
    }

    init {
        getQuestions()
    }

    private fun getQuestions() {
        viewModelScope.launch {
            savedStateHandle.getStateFlow("subject", "").collectLatest {
                Log.i(TAG, "The subject is $it")
                getAllQuestionsUseCase.invoke(it).collectLatest { response ->
                    when(response) {
                        is Response.Error -> {
                            uiState = uiState.copy(
                                errorMessage = response.errorMessage,
                                isLoading = false
                            )
                        }
                        Response.Undefined -> Unit
                        is Response.Success -> {
                            uiState = uiState.copy(
                                isLoading = false,
                                questions = response.data
                            )
                        }
                    }
                }
            }
        }
    }
}