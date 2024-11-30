package com.example.myapplication.ui.fragments.FragmentFactories

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.example.myapplication.ui.fragments.JokesListFragment

class CustomFragmentFactory : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment =
        when (loadFragmentClass(classLoader, className)) {
            JokesListFragment::class.java -> JokesListFragment()
            else -> super.instantiate(classLoader, className)
        }
}