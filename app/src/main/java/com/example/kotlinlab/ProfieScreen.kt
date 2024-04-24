package com.example.kotlinlab

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.kotlinlab.ui.theme.KotlinLabTheme


@Composable
private fun OutlinedTextFieldWithError(propVal: MutableState<String>,
                                       label: String, errorMessage: String,
                                       keyboardType: KeyboardType,
                                       validation: (input: MutableState<String>) -> Boolean) {
    val isError = validation.invoke(propVal)
    val supportingText = if(isError) errorMessage else ""

    Box{
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
}

@Composable
private fun ProfileImageWithPicker(profileImageUri: Uri?,
                                   selectImageOnClick: () -> Unit) {
    Box {
        if (profileImageUri != null){
            AsyncImage(
                model = profileImageUri,
                contentDescription = "Profile image",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .align(Alignment.Center),
                contentScale = ContentScale.Crop,
            )
        }
        else{
            Image(
                //wymaga dodania ikony w katalogu /res/drawable
                //(prawy przycisk | New | Vector asset)
                painter = painterResource(
                    id = R.drawable.ic_baseline_question_mark_24),
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
                    id = R.drawable.ic_baseline_image_search_24),
                contentDescription = "Search a photo",
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun ProfileScreen() {
    val name = rememberSaveable { mutableStateOf("") }
    val email = rememberSaveable { mutableStateOf("") }
    val numberOfColors = rememberSaveable { mutableStateOf("") }

    val profileImageUri = rememberSaveable { mutableStateOf<Uri?>(null) }
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { selectedUri ->
            if (selectedUri != null) {
                profileImageUri.value = selectedUri
            }
        })

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
            modifier = Modifier.padding(bottom = 48.dp)
        )
        ProfileImageWithPicker(profileImageUri = profileImageUri.value, selectImageOnClick = {
            imagePicker.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        })
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextFieldWithError(name, "Enter name", "Name can't be empty",
            KeyboardType.Text
        ) { input -> input.value.isEmpty() }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextFieldWithError(email, "Enter email", "Email must be in proper format",
            KeyboardType.Email
        ) { input ->
            val emailRegex = Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")
            !input.value.matches(emailRegex)
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextFieldWithError(numberOfColors, "Enter number of colors",
            "Number of colors must be between 5 and 10", KeyboardType.Number
        ) { input -> (input.value.toFloatOrNull() ?: 0f) !in 5f..10f }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /*TODO*/ },
            modifier =  Modifier
                .fillMaxWidth()
        ) {
            Text("Next")
        }
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    KotlinLabTheme {
        ProfileScreen()
    }
}