package com.eeema.android.rickandmortyapp.ui.detail

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.eeema.android.data.model.Character
import com.eeema.android.data.model.Gender
import com.eeema.android.data.model.Status
import com.eeema.android.rickandmortyapp.R
import com.eeema.android.rickandmortyapp.ui.characters.ErrorScreen
import com.eeema.android.rickandmortyapp.ui.characters.LoaderScreen
import com.eeema.android.rickandmortyapp.ui.components.RickAndMortyScreenScaffold
import com.eeema.android.rickandmortyapp.ui.theme.RickAndMortyTheme
import com.eeema.android.rickandmortyapp.ui.utils.asStringResource

@Composable
fun DetailScreen(
    viewModel: DetailViewModel = hiltViewModel(),
    id: Int?
) {
    val state: DetailState by viewModel.state.collectAsState()
    when (state) {
        is DetailState.Loading -> LoaderScreen()
        is DetailState.Data -> DetailContent((state as DetailState.Data).data)
        is DetailState.Failed -> ErrorScreen()
    }

    viewModel.retrieveCharacter(id)
}

@Composable
fun DetailContent(character: Character) {
    RickAndMortyScreenScaffold {
        Column(
            modifier = Modifier.fillMaxSize().padding(vertical = 32.dp)
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(character.image)
                    .crossfade(true)
                    .build(),
                loading = { CircularProgressIndicator() },
                contentDescription = stringResource(R.string.characters_image_content_description),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(300.dp)
                    .fillMaxSize()
                    .padding(16.dp)
                    .clip(CircleShape)
                    .aspectRatio(1f),
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = character.name,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h4,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = MaterialTheme.colors.primaryVariant
            )
            Spacer(Modifier.height(32.dp))
            DetailRow(
                stringResource(R.string.detail_specie_title),
                character.species
            )
            Spacer(Modifier.height(8.dp))
            DetailRow(
                stringResource(R.string.detail_status_title),
                stringResource(character.status.asStringResource())
            )
            Spacer(Modifier.height(8.dp))
            DetailRow(
                stringResource(R.string.detail_gender_title),
                stringResource(character.gender.asStringResource())
            )
        }
    }
}

@Composable
fun DetailRow(title: String, value: String) {
    Row(modifier = Modifier.padding(horizontal = 8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            color = MaterialTheme.colors.primaryVariant
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Normal,
            maxLines = 1
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    RickAndMortyTheme {
        RickAndMortyScreenScaffold {
            DetailContent(Character(1, "Rick", Status.Alived, "Human", Gender.Male, "", ""))
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun DetailDarkScreenPreview() {
    RickAndMortyTheme {
        RickAndMortyScreenScaffold {
            DetailContent(Character(1, "Rick", Status.Alived, "Human", Gender.Male, "", ""))
        }
    }
}
