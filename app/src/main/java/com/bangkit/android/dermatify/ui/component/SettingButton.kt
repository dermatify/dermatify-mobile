package com.bangkit.android.dermatify.ui.component

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bangkit.android.dermatify.R
import com.bangkit.android.dermatify.R.styleable.SettingButton
import com.bangkit.android.dermatify.R.styleable.SettingButton_menuIcon

class SettingButton(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs){

    init {
        inflate(context, R.layout.settings_button_layout,this)

        val customAttributeStyle = context.obtainStyledAttributes(
            attrs,
            SettingButton,
            0,
            0
        )

        val ivSettingIcon = findViewById<ImageView>(R.id.iv_icon)
        val tvSettingTitle = findViewById<TextView>(R.id.tv_setting_title)
        val tvSettingSubtitle = findViewById<TextView>(R.id.tv_setting_subtitle)

        try {
            ivSettingIcon.setImageDrawable(customAttributeStyle.getDrawable(SettingButton_menuIcon))
            tvSettingTitle.text = customAttributeStyle.getString(R.styleable.SettingButton_menuTitle)
            tvSettingSubtitle.text = customAttributeStyle.getString(R.styleable.SettingButton_menuSubtitle)
        } finally {
            invalidate()
            customAttributeStyle.recycle()
        }
    }

}