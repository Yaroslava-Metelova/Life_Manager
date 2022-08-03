package com.life_manager.kotlin.life_manager

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class CryptoFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        activity?.setTitle(R.string.crypto)
        return inflater.inflate(R.layout.fragment_crypto, container, false)
    }
}