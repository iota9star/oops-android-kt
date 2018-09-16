//package io.nichijou.oops.widget
//
//import android.content.Context
//import android.util.AttributeSet
//import androidx.annotation.Nullable
//import androidx.lifecycle.Observer
//import com.google.android.material.textfield.TextInputLayout
//import io.nichijou.oops.Oops
//import io.nichijou.oops.DuangProvider
//import io.nichijou.oops.ext.*
//
//
//class DuangTextInputLayout : TextInputLayout, DuangProvider {
//
//    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {
//        theme(attrs)
//    }
//
//    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
//        theme(attrs)
//    }
//
//    // boxBackgroundMode: 0:none 1:filled 2:outline
//
//    override fun theme(attrs: AttributeSet?) {
//        val ctx = this.ctx()
//        val backgroundResId = ctx.resId(attrs, android.R.attr.background)
//        Oops.liveColor(ctx, backgroundResId, Oops.live(ctx, Oops.oops::colorAccent))?.observe(ctx, Observer {
//            this.tint(it)
//        })
//        Oops.live(ctx, Oops.oops::textColorSecondary).observe(ctx, Observer {
//            this.tintHint(it.adjustAlpha(0.7f))
//        })
//    }
//}
