package com.tim.currencyexchanger.ui.currencyExchang.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tim.currencyexchanger.R
import com.tim.currencyexchanger.data.model.ExchangeRate
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeBuyCurrencyBottomSheet(
    allCurrencies: ImmutableList<ExchangeRate>,
    searchQuery: String,
    onCurrencySelected: (ExchangeRate) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    val modalBottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {

        SearchBar(
            query = searchQuery,
            onQueryChange = onSearchQueryChange,
            active = true,
            placeholder = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.search),
                )
            },
            colors = SearchBarDefaults.colors(containerColor = Color.Transparent),
            onActiveChange = {},
            onSearch = {}) {
            LazyColumn(Modifier.fillMaxSize()) {
                items(allCurrencies, key = ExchangeRate::currency) { currency ->
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .clickable {
                                onCurrencySelected(currency)
                                onDismiss()
                            }
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        text = currency.currency
                    )
                }
            }
        }
    }
}