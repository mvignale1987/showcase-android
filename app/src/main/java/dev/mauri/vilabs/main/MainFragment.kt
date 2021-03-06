package dev.mauri.vilabs.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import coil.ImageLoader
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.mauri.vilabs.databinding.FragmentFirstBinding
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    @Inject
    lateinit var imageLoader: ImageLoader

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var adapter: UsersAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = UsersAdapter(imageLoader)
        _binding?.usersList?.adapter = adapter
        _binding?.pullToRefresh?.setOnRefreshListener {
            mainViewModel.fetchUsersInfo()
        }
        lifecycleScope.launch {
            mainViewModel.uiStateData
                .flowWithLifecycle(lifecycle)
                .collect(::onUiStateChanged)
        }
    }

    private fun onUiStateChanged(state: MainViewModel.MainUIState) {
        when (state) {
            is MainViewModel.MainUIState.UsersState -> {
                adapter.submitList(state.users)
                _binding?.pullToRefresh?.isRefreshing = false
            }
            is MainViewModel.MainUIState.ErrorState -> {
                Snackbar.make(requireView(), state.error, Snackbar.LENGTH_LONG).show()
            }
            else -> {}
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
