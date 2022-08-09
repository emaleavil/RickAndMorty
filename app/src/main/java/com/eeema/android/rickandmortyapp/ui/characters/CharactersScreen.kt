package com.eeema.android.rickandmortyapp.ui.characters

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.eeema.android.data.model.Character
import com.eeema.android.data.model.Gender
import com.eeema.android.data.model.Status
import com.eeema.android.rickandmortyapp.R
import com.eeema.android.rickandmortyapp.ui.components.RickAndMortyScreenScaffold
import com.eeema.android.rickandmortyapp.ui.model.Route
import com.eeema.android.rickandmortyapp.ui.theme.RickAndMortyTheme
import com.eeema.android.rickandmortyapp.ui.utils.toImageResource

@Composable
fun CharactersScreen(
    viewModel: CharactersViewModel = CharactersViewModel(),
    navigate: (Route) -> Unit = {}
) {
    RickAndMortyScreenScaffold {
        val state: ViewState by viewModel.state.collectAsState()
        when (state) {
            is ViewState.Initial -> EmptyScreen()
            is ViewState.Loading -> LoaderScreen()
            is ViewState.Failed -> ErrorScreen()
            is ViewState.Success -> ListScreen((state as ViewState.Success).data, navigate)
        }
    }
}

@Composable
fun ListScreen(
    data: List<Character>,
    navigate: (Route) -> Unit = {}
) {
    LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
        items(items = data) { character -> ItemContent(character, navigate) }
    }
}

@Composable
fun ItemContent(
    character: Character,
    navigate: (Route) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable { navigate(Route.Details) },
        elevation = 2.dp,
        backgroundColor = MaterialTheme.colors.primaryVariant,
        shape = RoundedCornerShape(corner = CornerSize(16.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CharacterImage(character.image)
            CharacterBody(character.name, character.species, character.status.toImageResource())
        }
    }
}

@Composable
fun CharacterBody(
    title: String,
    body: String,
    iconId: Int = R.drawable.tombstone
) {
    Row(
        modifier = Modifier.fillMaxSize().padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
    ) {
        Column(
            Modifier.weight(0.70f).align(Alignment.CenterVertically)
        ) {
            Text(
                text = title,
                style = typography.h4,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(text = body, style = typography.body2, maxLines = 1)
        }
        Spacer(Modifier.weight(0.05f))
        Image(
            painter = painterResource(iconId),
            modifier = Modifier.weight(0.25f).padding(16.dp)
                .align(Alignment.CenterVertically),
            contentDescription = stringResource(R.string.characters_status_description)
        )
    }
}

@Composable
fun CharacterImage(url: String) {
    Box(
        Modifier.fillMaxWidth().wrapContentHeight()
            .background(color = MaterialTheme.colors.primary),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(url)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.character_placeholder),
            contentDescription = stringResource(R.string.characters_image_content_description),
            contentScale = ContentScale.Crop,
            modifier = Modifier.padding(16.dp).clip(CircleShape).size(148.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CharactersScreenPreview() {
    RickAndMortyTheme {
        RickAndMortyScreenScaffold {
            ListScreen(
                listOf(
                    Character(1, "Rick", Status.Alived, "Human", Gender.Male, "", ""),
                    Character(2, "Morty", Status.Dead, "Human", Gender.Male, "", ""),
                    Character(3, "Homer", Status.Unknown, "Human", Gender.Male, "", ""),
                    Character(3, "Marge", Status.Alived, "Human", Gender.Female, "", "")
                )
            )
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun CharactersDarkScreenPreview() {
    RickAndMortyTheme {
        RickAndMortyScreenScaffold {
            ListScreen(
                listOf(
                    Character(1, "Rick", Status.Alived, "Human", Gender.Male, "", ""),
                    Character(2, "Morty", Status.Dead, "Human", Gender.Male, "", ""),
                    Character(3, "Homer", Status.Unknown, "Human", Gender.Male, "", ""),
                    Character(3, "Marge", Status.Alived, "Human", Gender.Female, "", "")
                )
            )
        }
    }
}
