package dev.mauri.vilabs.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import coil.ImageLoader
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import dagger.hilt.android.AndroidEntryPoint
import dev.mauri.vilabs.R
import dev.mauri.vilabs.databinding.FragmentSecondBinding
import dev.mauri.vilabs.network.User
import javax.inject.Inject

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
@AndroidEntryPoint
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    @Inject
    lateinit var imageLoader: ImageLoader

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val myUser = arguments?.getParcelable("detailUser") as? User
        view.findViewById<TextView>(R.id.user_name)?.text = "${myUser?.first_name} ${myUser?.last_name}"

        view.findViewById<TextView>(R.id.user_email)?.text = myUser?.email
        imageLoader.enqueue(
            ImageRequest.Builder(requireContext())
                .data(myUser?.avatar)
                .target(view.findViewById<ImageView>(R.id.user_avatar))
                .transformations(CircleCropTransformation())
                .build()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
