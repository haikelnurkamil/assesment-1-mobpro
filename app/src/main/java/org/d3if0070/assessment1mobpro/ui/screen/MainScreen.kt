package org.d3if0070.assessment1mobpro.ui.screen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if0070.assessment1mobpro.R
import org.d3if0070.assessment1mobpro.navigation.Screen
import org.d3if0070.assessment1mobpro.ui.theme.Assessment1MobProTheme
import kotlin.math.sqrt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {
                    IconButton(
                        onClick = {
                            navController.navigate(Screen.About.route)
                        }
                    ) {
                        Icon(imageVector = Icons.Outlined.Info,
                            contentDescription = stringResource(R.string.fitur_app),
                            tint = MaterialTheme.colorScheme.primary
                            )
                    }
                }
            )
        }
    ) { padding ->
        ScreenContent(Modifier.padding(padding))
    }
}

@SuppressLint("StringFormatMatches")
@Composable
fun ScreenContent(modifier: Modifier) {
    var alas by rememberSaveable { mutableStateOf("") }
    var alasError by rememberSaveable { mutableStateOf(false) }

    val radioOptions = listOf(
        stringResource(id = R.string.luas),
        stringResource(id = R.string.keliling)
    )

    var tinggi by rememberSaveable { mutableStateOf("") }
    var tinggiError by rememberSaveable { mutableStateOf(false) }

    var rumus by rememberSaveable { mutableStateOf(radioOptions[0]) }

    var hasil by rememberSaveable {
        mutableFloatStateOf(0f)
    }

    val context = LocalContext.current

    Column (
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = stringResource(id = R.string.intro_app),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = alas,
            onValueChange = { alas = it},
            label = { Text(text = stringResource(R.string.alas)) },
            isError = alasError,
            trailingIcon = { IconPicker(alasError, "cm") },
            supportingText = { ErrorHint(alasError) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = tinggi,
            onValueChange = { tinggi = it },
            label = { Text(text = stringResource(R.string.tinggi)) },
            isError = tinggiError,
            trailingIcon = { IconPicker(tinggiError, "cm") },
            supportingText = { ErrorHint(tinggiError) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier
                .padding(top = 6.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
        ) {
            radioOptions.forEach { text ->
               Option(
                    label = text,
                    isSelected = rumus == text,
                    modifier = Modifier
                        .selectable(
                            selected = rumus == text,
                            onClick = {
                                rumus = text
                            },
                            role = Role.RadioButton
                        )
                        .weight(1f)
                        .padding(16.dp)
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 64.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    alasError = (alas == "" || alas == "0")
                    tinggiError = (tinggi == "" || tinggi == "0")
                    if (alasError || tinggiError) return@Button

                    when (rumus) {
                        radioOptions[0] -> {
                            hasil = hitungLuasJajarGenjang(alas.toFloat(), tinggi.toFloat())
                        }

                        radioOptions[1] -> {
                            hasil = hitungKelilingJajarGenjang(
                                alas.toFloat(),
                                tinggi.toFloat()
                            ).toFloat()
                        }
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .width(IntrinsicSize.Min),
                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
            ) {
                Text(text = stringResource(R.string.hitung))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = {
                    alas = ""
                    tinggi = ""
                    rumus = radioOptions[0]
                    hasil = 0f
                    alasError = false
                    tinggiError = false
                },
                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
            ) {
                Text(text = stringResource(id = R.string.ulang))
            }
        }
        if (hasil != 0f) {
            Divider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 1.dp
            )
            Text(
                text = when (rumus) {
                    radioOptions[0] -> stringResource(id = R.string.hasil_luas, hasil)
                    radioOptions[1] -> stringResource(id = R.string.hasil_keliling, hasil)
                    else -> ""
                },
                style = MaterialTheme.typography.titleLarge
            )
            Button(
                onClick = {
                          shareData(
                              context = context,
                              message = context.getString(R.string.share_text,
                                alas, tinggi, rumus, hasil)
                          )
                },
                modifier = Modifier.padding(top = 8.dp),
                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
                ) {
                    Text(text = stringResource(R.string.share))
            }
        }
    }
}

@Composable
fun Option(label: String, isSelected: Boolean, modifier: Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = isSelected, onClick = null)
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 8.dp)
            )
    }
}

private fun shareData(context: Context, message: String) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, message)
    }
    if (shareIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(shareIntent)
    }
}

@Composable
fun IconPicker(isError: Boolean, unit: String) {
    if (isError) {
        Icon(imageVector = Icons.Filled.Warning, contentDescription = null)
    } else {
        Text(text = unit)
    }
}

@Composable
fun ErrorHint(isError: Boolean) {
    if (isError) {
        Text(text = stringResource(R.string.input_invalid))
    }
}

private fun hitungLuasJajarGenjang(alas: Float, tinggi: Float): Float {
    return alas * tinggi
}

private fun hitungKelilingJajarGenjang(alas: Float, tinggi: Float): Double {
    return 2 * sqrt((alas * alas + tinggi * tinggi).toDouble())
}



@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ScreenPreview() {
    Assessment1MobProTheme {
       MainScreen(rememberNavController())
    }
}