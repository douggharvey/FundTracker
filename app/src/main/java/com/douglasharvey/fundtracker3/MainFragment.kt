package com.douglasharvey.fundtracker3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : androidx.fragment.app.Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Using type-safe arguments to decide to display the full list or just the favourites
        //https://medium.com/@shanmugasanthosh/android-navigation-architecture-component-part-3-f9cb9e9b642e

        fund_list_button.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToFundListFragment()
            action.setListType("1")
            Navigation.findNavController(activity!!, R.id.my_nav_host_fragment).navigate(action)
        }
        favourites_button.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToFundListFragment()
            action.setListType("2")
            Navigation.findNavController(activity!!, R.id.my_nav_host_fragment).navigate(action)
        }

        portfolio_button.setOnClickListener(
                Navigation.createNavigateOnClickListener(R.id.action_mainFragment_to_portfolioMainFragment, null)
        )
    }

}
