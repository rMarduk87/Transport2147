package rpt.games.transport2147.ui.intro

import android.content.Intent
import android.os.Bundle
import android.view.View
import rpt.com.base.BaseFragment
import rpt.com.base.navigation.safeNavController
import rpt.com.base.navigation.safeNavigate
import rpt.games.transport2147.MainMenuActivity
import rpt.games.transport2147.R
import rpt.games.transport2147.databinding.FragmentIntroBinding
import rpt.games.transport2147.utils.managers.DialogManager
import rpt.games.transport2147.utils.managers.SharedPreferencesManager

class IntroFragment : BaseFragment<FragmentIntroBinding>(
    FragmentIntroBinding::inflate,true) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(SharedPreferencesManager.showIntro){
            SharedPreferencesManager.showIntro = false
        }
        else{
            val intent = Intent(requireContext(), MainMenuActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
        loadIntro()
    }

    private fun loadIntro() {
        DialogManager.showFastStartDialog(requireContext())
    }


}