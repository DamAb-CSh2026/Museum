package com.example.museum

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment

class PageFragment : Fragment(R.layout.fragment_page) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = requireArguments().getString(ARG_TITLE)
        val text = requireArguments().getString(ARG_TEXT)
        view.findViewById<TextView>(R.id.textTitle).text = title
        view.findViewById<TextView>(R.id.textBody).text = text

        val host = requireActivity() as AppCompatActivity
        host.supportActionBar?.title = title
    }

    companion object {
        private const val ARG_TITLE = "title"
        private const val ARG_TEXT = "text"
        fun newInstance(title: String, text: String) = PageFragment().apply {
            arguments = bundleOf(ARG_TITLE to title, ARG_TEXT to text)
        }
    }
}