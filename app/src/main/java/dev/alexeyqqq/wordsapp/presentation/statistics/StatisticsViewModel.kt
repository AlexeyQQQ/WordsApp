package dev.alexeyqqq.wordsapp.presentation.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.alexeyqqq.wordsapp.domain.usecases.trainer.ShowStatisticsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class StatisticsViewModel @Inject constructor(
    private val showStatisticsUseCase: ShowStatisticsUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<StatisticsUiState> =
        MutableStateFlow(StatisticsUiState.Loading)
    val uiState get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val statistics = showStatisticsUseCase.invoke()
            _uiState.update { StatisticsUiState.ShowStatistics(statistics) }
        }
    }
}