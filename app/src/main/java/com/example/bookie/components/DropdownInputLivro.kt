package com.example.bookie.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.bookie.R
import com.example.bookie.models.Livro

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownInputLivro(
    selectedValue: Livro?,
    options: List<Livro>,
    onValueChangedEvent: (Livro) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.menuAnchor()
        ) {
//            Image(
//                painter = painterResource(id = R.drawable.capa_bunny),
//                contentDescription = stringResource(id = R.string.capa_livro),
//                modifier = Modifier
//                    .height(120.dp)
//                    .width(86.dp),
//            )
            if (selectedValue == null || selectedValue.getCapa().isEmpty()) {
                Image(
                    painter = painterResource(id = R.drawable.capa_bunny),
                    contentDescription = stringResource(id = R.string.capa_livro),
                    modifier = Modifier.height(120.dp).width(86.dp),
                )
            } else {
                AsyncImage(
                    model = selectedValue.getCapa(),
                    contentDescription = null,
                    modifier = Modifier.height(120.dp).width(86.dp)
                )
            }
            if (selectedValue == null) {
                Text(text = "Seu Livro\n (Clique Aqui)", style = MaterialTheme.typography.labelMedium, textAlign = TextAlign.Center, modifier = Modifier.width(86.dp))
            } else {
                selectedValue.volumeInfo?.nome?.let { Text(text = it, style = MaterialTheme.typography.labelMedium, modifier = Modifier.width(86.dp)) }
            }
        }
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier.width(256.dp)) {
            options.forEach { option: Livro ->
                DropdownMenuItem(
                    text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    modifier = Modifier.height(40.dp).fillMaxWidth()
                                ) {
                                    if (option.getCapa().isEmpty()) {
                                        Image(
                                            painter = painterResource(id = R.drawable.capa_bunny),
                                            contentDescription = stringResource(id = R.string.capa_livro),
                                            modifier = Modifier.height(33.dp).width(24.dp),
                                        )
                                    } else {
                                        AsyncImage(
                                            model = option.getCapa(),
                                            contentDescription = null,
                                            modifier = Modifier.height(33.dp).width(24.dp)
                                        )
                                    }
                                    option.volumeInfo?.nome?.let { Text(text = it) }
                                }
                            },
                    onClick = {
                        expanded = false
                        onValueChangedEvent(option)
                    },
                    modifier = Modifier.width(256.dp).height(40.dp).fillMaxWidth()
                )
            }
        }
    }
}