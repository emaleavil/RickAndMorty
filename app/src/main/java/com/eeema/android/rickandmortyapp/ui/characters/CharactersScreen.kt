package com.eeema.android.rickandmortyapp.ui.characters

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.eeema.android.data.model.Character
import com.eeema.android.data.model.Gender
import com.eeema.android.data.model.Status
import com.eeema.android.rickandmortyapp.R
import com.eeema.android.rickandmortyapp.ui.components.RickAndMortyScreenScaffold
import com.eeema.android.rickandmortyapp.ui.extensions.clickableSingle
import com.eeema.android.rickandmortyapp.ui.extensions.isFirstItemVisible
import com.eeema.android.rickandmortyapp.ui.extensions.isLastItemVisible
import com.eeema.android.rickandmortyapp.ui.model.Route
import com.eeema.android.rickandmortyapp.ui.model.ScrollContext
import com.eeema.android.rickandmortyapp.ui.theme.RickAndMortyTheme
import com.eeema.android.rickandmortyapp.ui.utils.toImageResource

@Composable
fun CharactersScreen(
    viewModel: CharactersViewModel = hiltViewModel(),
    navigate: (String) -> Unit = {}
) {
    RickAndMortyScreenScaffold {
        val state: CharactersState by viewModel.state.collectAsState()
        when (state) {
            is CharactersState.Initial -> EmptyScreen(viewModel::retry)
            is CharactersState.Loading -> LoaderScreen()
            is CharactersState.Failed -> ErrorScreen(viewModel::retry)
            is CharactersState.Success ->
                ListScreen(
                    state as CharactersState.Success,
                    viewModel::loadItems,
                    navigate
                )
        }
    }
}

@Composable
fun ListScreen(
    state: CharactersState.Success,
    loadNewPage: (Int?) -> Unit = {},
    navigate: (String) -> Unit = {}
) {
    val listState = rememberLazyListState()
    val scrollContext = rememberScrollContext(listState)

    LazyColumn(state = listState) {
        items(items = state.data) { character -> ItemContent(character, navigate) }
    }

    if (scrollContext.isBottom) {
        loadNewPage(state.nextPage)
    }
}

@Composable
fun rememberScrollContext(listState: LazyListState): ScrollContext {
    val scrollContext by remember {
        derivedStateOf {
            ScrollContext(
                isTop = listState.isFirstItemVisible,
                isBottom = listState.isLastItemVisible
            )
        }
    }
    return scrollContext
}

@Composable
fun ItemContent(
    character: Character,
    navigate: (String) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickableSingle { navigate(Route.Details.route.plus("/${character.id}")) },
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
        modifier = Modifier.fillMaxSize().padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
    ) {
        Column(
            Modifier.weight(0.70f).align(Alignment.CenterVertically)
        ) {
            Text(
                text = title,
                style = typography.h6,
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
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(url)
                .crossfade(true)
                .build(),
            loading = { CircularProgressIndicator() },
            contentDescription = stringResource(R.string.characters_image_content_description),
            contentScale = ContentScale.Crop,
            modifier = Modifier.padding(16.dp).clip(CircleShape).size(148.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CharactersScreenPreview() {
    val state = CharactersState.Success(
        listOf(
            Character(1, "Rick", Status.Alived, "Human", Gender.Male, "", ""),
            Character(2, "Morty", Status.Dead, "Human", Gender.Male, "", ""),
            Character(3, "Homer", Status.Unknown, "Human", Gender.Male, "", ""),
            Character(3, "Marge", Status.Alived, "Human", Gender.Female, "", "")
        ),
        1
    )

    RickAndMortyTheme {
        RickAndMortyScreenScaffold { ListScreen(state) }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun CharactersDarkScreenPreview() {
    val state = CharactersState.Success(
        listOf(
            Character(1, "Rick", Status.Alived, "Human", Gender.Male, "", ""),
            Character(2, "Morty", Status.Dead, "Human", Gender.Male, "", ""),
            Character(3, "Homer", Status.Unknown, "Human", Gender.Male, "", ""),
            Character(3, "Marge", Status.Alived, "Human", Gender.Female, "", "")
        ),
        1
    )

    RickAndMortyTheme {
        RickAndMortyScreenScaffold { ListScreen(state) }
    }
}
