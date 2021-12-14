package com.artisticent.collegespace.presentation.ui.users

import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.artisticent.collegespace.databinding.FragmentUserEditBinding
import com.artisticent.collegespace.presentation.LoadingScreen
import com.artisticent.collegespace.presentation.ui.utils.toast
import com.artisticent.collegespace.presentation.viewmodels.UserViewModel
import com.artisticent.collegespace.util.Util
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class UserEditFragment : Fragment() {
    lateinit var binding: FragmentUserEditBinding
    val viewModel : UserViewModel by viewModels()
    private var imageUri = mutableStateOf<Uri?>(null)
    private var userImageChanged = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                var isLoading by rememberSaveable { mutableStateOf(false) }
                val currentUserData by viewModel.currentUserData.observeAsState()
                val coroutineScope = rememberCoroutineScope()
                LoadingScreen(inProgress = isLoading, message = "Updating Profile"){

                    UserEditScreen(
                        onUpdateDetails = lambda@ { name, description ->
                            if(name.isBlank()){
                                Util.toast(requireContext(), "Name cannot be empty")
                                return@lambda
                            }
                            isLoading = true
                            val updateName = if(name != currentUserData?.name) name else null
                            val updateDescription = if(name != currentUserData?.name) description else null
                            val updateUsrImage =
                                if(userImageChanged){
                                    val input = requireActivity().contentResolver.openInputStream(imageUri.value!!)
                                    val bitmap = BitmapFactory.decodeStream(input)
                                    input?.close()
                                    bitmap
                                }
                                else null
                            coroutineScope.launch {
                                try {
                                    viewModel.updateUserDetails(
                                        name = updateName,
                                        about = updateDescription,
                                        imageBitmap = updateUsrImage
                                    )
                                    findNavController().navigate(UserEditFragmentDirections.actionUserEditFragmentToUserFragment())
                                }
                                catch (e: Exception){
                                    Util.toast(requireContext(), e.message)
                                    isLoading = false
                                }
                            }
                        },
                        onImageClick = {
                            try{
                                getImageContent.launch("image/*")
                            }catch (e: Exception){
                                toast(requireContext(), e.message)
                            }
                        },
                        userImageUrl = currentUserData?.userImg,
                        localImageUri =
                            if(imageUri.value != null) getRealPathFromURI(imageUri.value!! )
                            else null
                    )
                }
            }
        }
    }

    private val getImageContent = registerForActivityResult(ActivityResultContracts.GetContent()) {
        imageUri.value = it
        println(getRealPathFromURI(imageUri.value!!))
        userImageChanged = true
    }
    private fun getRealPathFromURI(contentURI: Uri): String? {
        val result: String?
        val cursor: Cursor? = requireActivity().contentResolver.query(contentURI, null, null, null, null)
        if (cursor == null) {
            result = contentURI.path
        } else {
            cursor.moveToFirst()
            val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }
}