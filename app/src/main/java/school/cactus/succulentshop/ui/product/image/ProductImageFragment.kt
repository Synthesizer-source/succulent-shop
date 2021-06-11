package school.cactus.succulentshop.ui.product.image

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import school.cactus.succulentshop.databinding.FragmentProductImageBinding

class ProductImageFragment : DialogFragment() {

    private var _binding: FragmentProductImageBinding? = null
    private val binding get() = _binding!!

    private val args: ProductImageFragmentArgs by navArgs()

    private val viewModel: ProductImageViewModel by viewModels {
        ProductImageViewModelFactory(args.imageUrl)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductImageBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}