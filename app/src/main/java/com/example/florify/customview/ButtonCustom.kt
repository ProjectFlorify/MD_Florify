package com.example.florify.customview

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import com.example.florify.R

class ButtonCustom @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {

    init {
        when (id) {
            R.id.register -> setBackgroundResource(R.drawable.register_button)
            R.id.login -> setBackgroundResource(R.drawable.login_button)
            else -> setBackgroundResource(R.drawable.login_button)
        }
    }
}
