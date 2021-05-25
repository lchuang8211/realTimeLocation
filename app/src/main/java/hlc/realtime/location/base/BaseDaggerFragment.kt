package hlc.realtime.location.base

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseDaggerFragment : DaggerFragment(){


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected abstract val viewModel: ViewModel

    protected abstract val binding: ViewDataBinding

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.lifecycleOwner = this.viewLifecycleOwner
    }

}