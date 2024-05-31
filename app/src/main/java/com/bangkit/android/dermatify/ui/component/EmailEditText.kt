package com.bangkit.android.dermatify.ui.component

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.bangkit.android.dermatify.R

class EmailEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs), View.OnFocusChangeListener {

    private var emailLogo: Drawable
    private var emailLogoFocused: Drawable
    private var errorIcn: Drawable

    init {
        emailLogo = ContextCompat.getDrawable(context, R.drawable.ic_mail) as Drawable
        emailLogoFocused = ContextCompat.getDrawable(context, R.drawable.ic_mail_focused) as Drawable
        errorIcn = ContextCompat.getDrawable(context, R.drawable.ic_error) as Drawable

        borderNotFocused()
        showNotFocusedEmail()

        onFocusChangeListener = this

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(cs: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(cs: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun afterTextChanged(view: Editable?) {
                if (view.toString().isEmpty() || isEmailValid(view.toString())) {
                    error = null
                    borderFocused()
                } else {
                    setError(ContextCompat.getString(context, R.string.email_error), null)
                    setButtonDrawables(endOfTheText = errorIcn)
                    borderError()
                }
            }
        })
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        isSingleLine = true
        hint = ContextCompat.getString(context, R.string.hint_email)
        textSize = 14F
    }

    override fun onFocusChange(v: View?, isFocused: Boolean) {
        if (isFocused) {
            showFocusedEmail()
            borderFocused()
        } else {
            showNotFocusedEmail()
            borderNotFocused()
        }
    }

    private fun showFocusedEmail() {
        setButtonDrawables(endOfTheText = emailLogoFocused)
    }

    private fun showNotFocusedEmail() {
        setButtonDrawables(endOfTheText = emailLogo)
    }

    private fun setButtonDrawables(startOfTheText: Drawable? = null, topOfTheText: Drawable? = null, endOfTheText: Drawable? = null, bottomOfTheText: Drawable? = null) {
        setCompoundDrawablesWithIntrinsicBounds(startOfTheText, topOfTheText, endOfTheText, bottomOfTheText)
    }

    private fun borderFocused() {
        background = ContextCompat.getDrawable(context, R.drawable.edittext_border_focused)
    }

    private fun borderNotFocused() {
        background = ContextCompat.getDrawable(context, R.drawable.edittext_border)
    }

    private fun borderError() {
        background = ContextCompat.getDrawable(context, R.drawable.edittext_border_error)
    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

}