package com.bangkit.android.dermatify.ui.component

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.bangkit.android.dermatify.R
import com.bangkit.android.dermatify.ui.register.RegisterFragment

class PasswordEditText@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs), View.OnTouchListener, View.OnFocusChangeListener {

    private var notVisibleBtn: Drawable
    private var notVisibleBtnFocused: Drawable
    private var visibleBtn: Drawable
    private var visibleBtnFocused: Drawable
    private var errorIcn: Drawable
    var fragmentType: String = "empty"
    init {
        notVisibleBtn = ContextCompat.getDrawable(context, R.drawable.ic_not_visible) as Drawable
        notVisibleBtnFocused = ContextCompat.getDrawable(context, R.drawable.ic_not_visible_focused) as Drawable

        visibleBtn = ContextCompat.getDrawable(context, R.drawable.ic_visible) as Drawable
        visibleBtnFocused = ContextCompat.getDrawable(context, R.drawable.ic_visible_focused) as Drawable

        errorIcn = ContextCompat.getDrawable(context, R.drawable.ic_error) as Drawable

        borderNotFocused()
        showNotVisibleBtn()

        setOnTouchListener(this)
        onFocusChangeListener = this

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(cs: CharSequence?, start: Int, count: Int, after: Int) {
                if (transformationMethod != null) showNotVisibleBtnFocused() else showVisibleBtnFocused()

                if (cs.toString().length < 8 &&
                    fragmentType == "REGISTER"
                ) {
                    setError(ContextCompat.getString(context, R.string.password_error), null)
                    showErrorIcon()
                    borderError()
                } else if (!cs.toString().isPasswordValid() &&
                    fragmentType == "REGISTER"
                ) {
                    setError(ContextCompat.getString(context, R.string.password_notvalid_error), null)
                    borderError()
                } else {
                    error = null
                    borderFocused()
                }
            }

            override fun afterTextChanged(ed: Editable?) {

            }

        })
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        hint = ContextCompat.getString(context, R.string.hint_password)
        textSize = 14F
    }

    override fun onTouch(view: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] == notVisibleBtnFocused) {
            val visibleButtonStart: Float
            val visibleButtonEnd: Float
            var isVisibleButtonClicked = false

            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                visibleButtonEnd = (notVisibleBtnFocused.intrinsicWidth + paddingStart).toFloat()
                when {
                    event.x < visibleButtonEnd -> isVisibleButtonClicked = true
                }
            } else {
                visibleButtonStart = (width - paddingEnd - notVisibleBtnFocused.intrinsicWidth).toFloat()
                when {
                    event.x > visibleButtonStart -> isVisibleButtonClicked = true
                }
            }
            return if (isVisibleButtonClicked) {
                when (event.action) {

                    MotionEvent.ACTION_UP -> {
                        showVisibleBtnFocused()
                        transformationMethod = null
                        true
                    }

                    else -> false
                }
            } else false
        } else if (compoundDrawables[2] == visibleBtnFocused) {
            val visibleButtonStart: Float
            val visibleButtonEnd: Float
            var isVisibleButtonClicked = false

            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                visibleButtonEnd = (visibleBtnFocused.intrinsicWidth + paddingStart).toFloat()
                when {
                    event.x < visibleButtonEnd -> isVisibleButtonClicked = true
                }
            } else {
                visibleButtonStart = (width - paddingEnd - visibleBtnFocused.intrinsicWidth).toFloat()
                when {
                    event.x > visibleButtonStart -> isVisibleButtonClicked = true
                }
            }
            return if (isVisibleButtonClicked) {
                when (event.action) {
                    MotionEvent.ACTION_UP -> {
                        showNotVisibleBtnFocused()
                        transformationMethod = PasswordTransformationMethod()
                        true
                    }

                    else -> false
                }
            } else false
        }
        return false
    }

    override fun onFocusChange(view: View?, isFocused: Boolean) {
        when  {
            error == null && isFocused && compoundDrawables[2] == visibleBtn -> {
                borderFocused()
                showVisibleBtnFocused()
            }
            error == null && isFocused && compoundDrawables[2] == notVisibleBtn -> {
                borderFocused()
                showNotVisibleBtnFocused()
            }
            error == null && !isFocused && compoundDrawables[2] == notVisibleBtnFocused -> {
                borderNotFocused()
                showNotVisibleBtn()
            }
            error == null && !isFocused && compoundDrawables[2] == visibleBtnFocused -> {
                borderNotFocused()
                showVisibleBtn()
            }
            else -> {}
        }
    }

    private fun showErrorIcon() {
        setButtonDrawables(endOfTheText = errorIcn)
    }
    private fun showNotVisibleBtn() {
        setButtonDrawables(endOfTheText = notVisibleBtn)
    }

    private fun showNotVisibleBtnFocused() {
        setButtonDrawables(endOfTheText = notVisibleBtnFocused)
    }

    private fun showVisibleBtn() {
        setButtonDrawables(endOfTheText = visibleBtn)
    }

    private fun showVisibleBtnFocused() {
        setButtonDrawables(endOfTheText = visibleBtnFocused)
    }

    private fun setButtonDrawables(startOfTheText: Drawable? = null, topOfTheText: Drawable? = null, endOfTheText: Drawable? = null, bottomOfTheText: Drawable? = null) {
        setCompoundDrawablesWithIntrinsicBounds(startOfTheText, topOfTheText, endOfTheText, bottomOfTheText)
    }

    // Check if password contains at least one uppercase letter,
    // one lowercase letter, and one number
    private fun String.isPasswordValid(): Boolean {
        return Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*\$").containsMatchIn(this)
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
}