package com.bangkit.android.dermatify.ui.component

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.bangkit.android.dermatify.R

class NameEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs), View.OnFocusChangeListener {

    private var personImg: Drawable
    private var personImgFocused: Drawable
    private var errorIcn: Drawable

    init {
        personImg = ContextCompat.getDrawable(context, R.drawable.ic_account_circle) as Drawable
        personImgFocused = ContextCompat.getDrawable(context, R.drawable.ic_account_circle_focused) as Drawable
        errorIcn = ContextCompat.getDrawable(context, R.drawable.ic_error) as Drawable

        borderNotFocused()
        showNotFocusedPerson()

        onFocusChangeListener = this

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun afterTextChanged(view: Editable?) {
                if (view.toString().isEmpty()) {
                    setError(ContextCompat.getString(context, R.string.name_error), null)
                    setButtonDrawables(endOfTheText = errorIcn)
                    borderError()
                } else {
                    error = null
                    borderFocused()
                    showFocusedPerson()
                }
            }

        })
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        isSingleLine = true
        hint = ContextCompat.getString(context, R.string.hint_name)
        textSize = 14F
    }

    override fun onFocusChange(v: View?, isFocused: Boolean) {
        if (error != null) {
            borderError()
            showErrorIcon()
        }
        else if (isFocused) {
            showFocusedPerson()
            borderFocused()
        } else {
            showNotFocusedPerson()
            borderNotFocused()
        }
    }

    private fun showFocusedPerson() {
        setButtonDrawables(endOfTheText = personImgFocused)
    }

    private fun showNotFocusedPerson() {
        setButtonDrawables(endOfTheText = personImg)
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

    private fun showErrorIcon() {
        setButtonDrawables(endOfTheText = errorIcn)
    }
}