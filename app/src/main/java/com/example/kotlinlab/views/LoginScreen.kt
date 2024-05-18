package com.example.kotlinlab.views

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.kotlinlab.AppViewModelProvider
import com.example.kotlinlab.R
import com.example.kotlinlab.ui.theme.KotlinLabTheme
import com.example.kotlinlab.viewmodels.LoginViewModel
import kotlinx.coroutines.launch


@Composable
private fun outlinedTextFieldWithError(
    propVal: MutableState<String>,
    label: String, errorMessage: String,
    keyboardType: KeyboardType,
    validation: (input: MutableState<String>) -> Boolean
): Boolean {
    val isError = validation.invoke(propVal)
    val supportingText = if (isError) errorMessage else ""

    Box {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = propVal.value,
            onValueChange = { propVal.value = it },
            label = { Text(label) },
            singleLine = true,
            trailingIcon = {
                if (isError) {
                    Image(
                        painter = painterResource(
                            id = R.drawable.ic_baseline_error_24
                        ),
                        contentDescription = "Error occurred",
                        contentScale = ContentScale.Crop
                    )
                }
            },
            isError = isError,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            supportingText = { Text(supportingText) }
        )
    }

    return isError
}

@Composable
private fun ProfileImageWithPicker(
    profileImageUri: Uri?,
    selectImageOnClick: () -> Unit
) {
    Box {
        if (profileImageUri != null) {
            AsyncImage(
                model = profileImageUri,
                contentDescription = "Profile image",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .align(Alignment.Center),
                contentScale = ContentScale.Crop,
            )
        } else {
            Image(
                //wymaga dodania ikony w katalogu /res/drawable
                //(prawy przycisk | New | Vector asset)
                painter = painterResource(
                    id = R.drawable.ic_baseline_question_mark_24
                ),
                contentDescription = "Profile photo",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .align(Alignment.Center),
                contentScale = ContentScale.Crop
            )
        }
        IconButton(
            onClick = selectImageOnClick,
            modifier = Modifier
                .fillMaxSize()
                .padding(150.dp, 0.dp, 0.dp, 0.dp)
        ) {
            Image(
                painter = painterResource(
                    id = R.drawable.ic_baseline_image_search_24
                ),
                contentDescription = "Search a photo",
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun LoginScreen(
    onStartGameClicked: (Int, Long) -> Unit,
    clearForm: Boolean,
    viewModel: LoginViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    val name = rememberSaveable { mutableStateOf("") }
    val email = rememberSaveable { mutableStateOf("") }
    val numberOfColors = rememberSaveable { mutableStateOf("") }

    if (clearForm) {
        name.value = ""
        email.value = ""
        numberOfColors.value = ""
    }

    val nameErr = remember { mutableStateOf(false) }
    val emailErr = remember { mutableStateOf(false) }
    val numberOfColorsErr = remember { mutableStateOf(false) }

    val profileImageUri = rememberSaveable { mutableStateOf<Uri?>(null) }
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { selectedUri ->
            if (selectedUri != null) {
                profileImageUri.value = selectedUri
            }
        })

    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse)
    )

    LaunchedEffect(name.value, email.value) {
        viewModel.name.value = name.value
        viewModel.email.value = email.value
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "MasterAnd",
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier
                .padding(bottom = 48.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    transformOrigin = TransformOrigin.Center
                }
        )
        ProfileImageWithPicker(profileImageUri = profileImageUri.value, selectImageOnClick = {
            imagePicker.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        })
        Spacer(modifier = Modifier.height(16.dp))
        nameErr.value = outlinedTextFieldWithError(
            name, "Enter name", "Name can't be empty",
            KeyboardType.Text
        ) { input -> input.value.isEmpty() }
        Spacer(modifier = Modifier.height(16.dp))
        emailErr.value = outlinedTextFieldWithError(
            email, "Enter email", "Email must be in proper format",
            KeyboardType.Email
        ) { input ->
            val emailRegex = Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{2,})")
            !input.value.matches(emailRegex)
        }
        Spacer(modifier = Modifier.height(16.dp))
        numberOfColorsErr.value = outlinedTextFieldWithError(
            numberOfColors, "Enter number of colors",
            "Number of colors must be between 5 and 10", KeyboardType.Number
        ) { input -> (input.value.toIntOrNull() ?: 0) !in 5..10 }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (!(nameErr.value || emailErr.value || numberOfColorsErr.value)) {
                    coroutineScope.launch {
                        viewModel.savePlayer()
                        onStartGameClicked(numberOfColors.value.toInt(), viewModel.playerId.value)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text("Next")
        }
    }
}

fun onStartGameClickedPreview(): (Int, Long) -> Unit {
    return { i: Int, l: Long -> {}}
}

@Preview
@Composable
fun LoginScreenPreview() {
    KotlinLabTheme {
        LoginScreen(onStartGameClicked = onStartGameClickedPreview(), clearForm = false)
    }
}
