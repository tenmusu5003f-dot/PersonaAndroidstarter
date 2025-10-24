package com.persona.androidstarter.feature.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView

class HomeFragment : Fragment() {
    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        return TextView(requireContext()).apply { text = "🚀 Home" }
    }
}
