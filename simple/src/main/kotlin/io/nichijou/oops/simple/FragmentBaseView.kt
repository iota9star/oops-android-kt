package io.nichijou.oops.simple

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import io.nichijou.oops.Oops
import kotlinx.android.synthetic.main.fragment_base_view.*

class FragmentBaseView : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_base_view, container, false)
    }

    companion object {
        fun newInstance() = FragmentBaseView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButton()
        initEvent()
    }

    private fun initEvent() {
        switch_dark.isChecked = Oops.immed().isDark
        switch_dark.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Oops.bulk {
                    theme = R.style.AppThemeDark
                    isDark = true
                    windowBackground = Color.BLACK
                }
            } else {
                Oops.bulk {
                    theme = R.style.AppTheme
                    isDark = false
                    windowBackground = Color.WHITE
                }
            }
        }
    }

    private fun initButton() {
        button1.setOnClickListener {
            AlertDialog.Builder(context!!)
                .setTitle("This is a test dialog...")
                .setMessage("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut tincidunt mattis eros. Suspendisse potenti. Nulla vel.")
                .setPositiveButton("Positive") { _, _ -> }
                .setNegativeButton("Negative") { _, _ -> }
                .setNeutralButton("Neutral") { _, _ -> }
                .show()
        }

    }
}