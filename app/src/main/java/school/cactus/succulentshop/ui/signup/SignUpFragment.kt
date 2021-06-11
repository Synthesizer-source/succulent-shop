package school.cactus.succulentshop.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import school.cactus.succulentshop.R
import school.cactus.succulentshop.auth.JwtStore
import school.cactus.succulentshop.databinding.FragmentSignUpBinding
import school.cactus.succulentshop.infra.BaseFragment
import school.cactus.succulentshop.infra.keyboard.KeyboardObserver
import school.cactus.succulentshop.repository.SignUpRepository

class SignUpFragment : BaseFragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private val keyboardObserver = KeyboardObserver()

    override val viewModel: SignUpViewModel by viewModels {
        SignUpViewModelFactory(
            store = JwtStore(requireContext()),
            repository = SignUpRepository()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().title = getString(R.string.sign_up)
        keyboardObserver.observe(viewModel.keyboardFlag, view, viewLifecycleOwner)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}