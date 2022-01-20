package com.github.rahul_gill.collegespace.presentation.ui.contests

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.github.rahul_gill.collegespace.MainNavGraphDirections
import com.github.rahul_gill.collegespace.domain.models.ContestModel
import com.github.rahul_gill.collegespace.domain.models.ContestPlatform
import com.github.rahul_gill.collegespace.domain.models.Event
import com.github.rahul_gill.collegespace.domain.models.imageId
import com.github.rahul_gill.collegespace.presentation.theme.AppTheme
import com.github.rahul_gill.collegespace.presentation.theme.LocalExtendedColors
import com.github.rahul_gill.collegespace.presentation.viewmodels.ContestViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class ContestsFragment : Fragment() {
    private val viewModel: ContestViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply{

        setContent{

            val contestList = viewModel.contestList.observeAsState()
            val isLoading = viewModel.isLoadingEvents.observeAsState()

            LaunchedEffect(key1 = "LOAD_CONTESTS_FIRST_TIME"){
                if(contestList.value.isNullOrEmpty()){
                    viewModel.loadContestDataNew()
                    Timber.i("loading new data previous: ${contestList.value}")
                }
            }

            ContestsScreen(
                contests = contestList.value ?: listOf(),
                isRefreshing = isLoading.value == true,
                onRefresh = {
                    lifecycleScope.launch {
                        viewModel.loadContestDataNew()
                    }
                },
                onAddToCalendar = { event ->
                    findNavController().navigate(MainNavGraphDirections.toEventFragment(event))
                }
            )
        }
    }
}


@SuppressLint("MutableCollectionMutableState")
@Composable
fun ContestsScreen(
    contests: List<ContestModel> ,
    onRefresh: () -> Unit = {},
    isRefreshing: Boolean = false,
    onAddToCalendar: (Event) -> Unit
) = AppTheme {
    Timber.d("isLoading $isRefreshing contests: $contests")
    val items = EnumMap<ContestPlatform, Boolean>(ContestPlatform::class.java)
    var filer by remember {
        mutableStateOf(items)
    }
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = { onRefresh() },
        Modifier.background(MaterialTheme.colors.surface)
    ) {
        LazyColumn{
            item{ Spacer(modifier = Modifier.height(60.dp)) }
            var noFilters = true
            for(filter in filer.values) {
                if(filter == true) noFilters = false
            }
            itemsIndexed(if(noFilters) contests else contests.filter { filer[it.platform] == true }){ _, contest ->
                ContestItem(contest, onAddToCalendar = onAddToCalendar)
            }
        }
        ContestChips(
            selectedItems = filer,
            onSelectedCategoryChanged = {  platform ->
                val new = EnumMap(filer)
                new[platform] = !(new[platform] ?: false)
                filer = new
            }
        )
    }

}

@Composable
fun ContestItem(
    contestModel: ContestModel,
    onAddToCalendar: (Event) -> Unit = {}
) {
    if(contestModel.name != "ProjectEuler+") Box {
        Card(
            modifier = Modifier.padding(bottom = 8.dp).fillMaxWidth(),
            elevation = 2.dp,
            shape = RectangleShape
        ) {
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
//            val image = Util.loadLocalPicture(imageRes = contestModel.platform.imageId)
//            image?.let {
                Image(
//                    bitmap = it,
//                        FOR PREVIEW
                    painter = painterResource(id = contestModel.platform.imageId),
                    contentDescription = "",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface)
                )
//            }

                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = contestModel.name,
                        style = MaterialTheme.typography.body1.copy(fontSize = 14.sp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.widthIn(max = 250.dp)
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                    ) {
                        Text(
                            text = contestModel.timeString(),
                            style = MaterialTheme.typography.caption.copy(color = Color.Gray),
                        )
                        Text(
                            text = contestModel.status.status_string,
                            modifier = Modifier.padding(end = 2.dp),
                            style = MaterialTheme.typography.caption.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (contestModel.status.status_string == "running") MaterialTheme.colors.primary else Color.Unspecified
                            )
                        )
                    }

                }
            }
        }
        Icon(
            imageVector = Icons.Default.CalendarToday,
            contentDescription = "",
            modifier = Modifier
                .padding(8.dp)
                .clickable { onAddToCalendar(Event.from(contestModel)) }
                .align(Alignment.TopEnd)
        )
    }
}


@Composable
fun ContestChips(
    contestPlatforms: Array<ContestPlatform> = ContestPlatform.values(),
    selectedItems: EnumMap<ContestPlatform, Boolean>,
    onSelectedCategoryChanged: (ContestPlatform) -> Unit = {}
){
    Surface(
        shape = RectangleShape,
        color = LocalExtendedColors.current.transparentSurface,
        modifier = Modifier.heightIn(max = 65.dp)
    ) {
        Row(modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .padding(8.dp)
        ){
            for(platform in contestPlatforms) ContestChip(
                platform = platform,
                isSelected = selectedItems[platform] == true,
                onSelectedCategoryChanged = { onSelectedCategoryChanged(platform) }
            )
        }
    }
}


@Composable
fun ContestChip(
    platform: ContestPlatform,
    isSelected: Boolean = false,
    onSelectedCategoryChanged: (ContestPlatform) -> Unit = {}
) {
    Surface(
        modifier = Modifier.padding(end = 8.dp, bottom = 8.dp),
        elevation = 8.dp,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if(isSelected) MaterialTheme.colors.primary else Color.Unspecified
        )
    ) {
        Row(modifier = Modifier.toggleable(
            value = isSelected,
            onValueChange = { onSelectedCategoryChanged(platform) }
        )){
            Text(
                text = platform.title,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(8.dp),
                color = if(isSelected) MaterialTheme.colors.primary else Color.Unspecified
            )
        }
    }
}
