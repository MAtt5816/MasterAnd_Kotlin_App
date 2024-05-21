package com.example.kotlinlab.views

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.kotlinlab.viewmodels.ProfileViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ProfileImage(profileImageUri: Uri?, modifier: Modifier) {
    if (profileImageUri != null) {
        AsyncImage(
            model = profileImageUri,
            contentDescription = "Profile image",
            modifier = modifier,
            contentScale = ContentScale.Crop,
        )
    } else {
        Image(
            painter = painterResource(
                id = R.drawable.ic_baseline_question_mark_24
            ),
            contentDescription = "Profile photo",
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun DescriptionButton(
    coroutineScope: CoroutineScope,
    viewModel: ProfileViewModel,
    modifier: Modifier,
    description: String,
    isDescriptionEditable: MutableState<Boolean>
) {
    if (isDescriptionEditable.value) {
        Button(
            onClick = {
                isDescriptionEditable.value = false
                coroutineScope.launch {
                    viewModel.description.value = description
                    viewModel.savePlayerDescription()
                }
            },
            modifier = modifier
        ) {
            Text("Save description")
        }
    }
    else {
        Button(
            onClick = {
                isDescriptionEditable.value = true
            },
            modifier = modifier
        ) {
            Text("Edit description")
        }
    }
}

@Composable
fun DescriptionField(
    description: MutableState<String>,
    isDescriptionEditable: Boolean
) {
    if (isDescriptionEditable) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = description.value,
            onValueChange = { description.value = it },
            label = { Text("Description") },
            singleLine = false,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
    }
    else {
        Text(
            text = description.value,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 48.dp)
        )
    }
}

@Composable
fun ProfileCard(
    playerId: Long,
    onStartGameClicked: () -> Unit,
    viewModel: ProfileViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    val name = rememberSaveable { mutableStateOf("") }
    val profileImageUri = rememberSaveable { mutableStateOf<Uri?>(null) }
    val description = rememberSaveable { mutableStateOf("") }

    val isDescriptionEditable = remember { mutableStateOf(false) }

    LaunchedEffect(name.value, profileImageUri.value) {
        coroutineScope.launch {
            viewModel.showPlayer(playerId)
            name.value = viewModel.name.value
            profileImageUri.value = viewModel.profileImageUri.value
            description.value = viewModel.description.value
        }
    }

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                ProfileImage(
                    profileImageUri = profileImageUri.value, modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .align(Alignment.Top)
                )
                Column(
                    modifier = Modifier
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = name.value,
                        style = MaterialTheme.typography.displayMedium,
                        modifier = Modifier.padding(bottom = 48.dp)
                    )
                    DescriptionField(description, isDescriptionEditable.value)
                }
            }
        }
        Column(modifier = Modifier.align(Alignment.BottomCenter)) {
            DescriptionButton(
                coroutineScope = coroutineScope,
                viewModel = viewModel,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                description = description.value,
                isDescriptionEditable = isDescriptionEditable
            )
            Button(
                onClick = { onStartGameClicked() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Start game")
            }
        }
    }
}

@Preview
@Composable
fun ProfileCardView() {
    KotlinLabTheme {
        ProfileCard(playerId = 0, {})
    }
}