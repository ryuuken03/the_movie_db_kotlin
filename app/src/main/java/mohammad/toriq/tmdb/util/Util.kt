package mohammad.toriq.tmdb.util

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.util.TypedValue
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.signature.ObjectKey
import mohammad.toriq.tmdb.R
import mohammad.toriq.tmdb.api.RetrofitClient
import org.json.JSONObject
import retrofit2.Response
import java.net.SocketTimeoutException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

/***
 * Created By Mohammad Toriq on 18/10/2023
 */
class Util {
    companion object {

        fun getAPIHelper(context: Context?): APIHelper? {
            return RetrofitClient.getClient(context)?.create(APIHelper::class.java)
        }

        fun parseError(response: Response<*>): String? {
            var error_message = "System Failure"
            if (response.isSuccessful) {
                // Do your success stuff...
            } else {
                try {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    error_message = jObjError.getString("message")
                } catch (e: Exception) {
                    error_message = e.message!!
                }
            }
            return error_message
        }

        fun parseThrowable(context: Context, t : Throwable): String{
            var errorMessage = context.getString(R.string.error_server)
            if (t is SocketTimeoutException) {
                errorMessage = context.getString(R.string.error_rto)
            }
            return errorMessage
        }

        fun getDisplay(activity: Activity): DisplayMetrics? {
            val displayMetrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
            return displayMetrics
        }

        fun convertPxToDp(dp: Int, context: Context): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp.toFloat(), context.resources.displayMetrics
            ).toInt()
        }

        fun convertDpToPx(dp: Int, context: Context): Int {
            val d = context.resources.displayMetrics.density
            return (dp * d).toInt()
        }
        fun convertDate(
            dateInput: String?,
            inFormat: String?,
            outFormat: String?,
            isLocal: Boolean = false
        ): String {
            var tmp = ""

            val locale = Locale("in")
            val origin =
                SimpleDateFormat(inFormat, Locale.getDefault())
            origin.timeZone = TimeZone.getTimeZone("UTC")
            if (isLocal) {
                origin.timeZone = TimeZone.getDefault()
            }
            val result = SimpleDateFormat(outFormat, locale)
            result.timeZone = TimeZone.getDefault()

            try {
                tmp = result.format(origin.parse(dateInput))
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return tmp
        }
        fun getShowTime(time: Int /** in minute**/): String {
            var showTime = ""
            var hour = time/ 60
            var minute = time % 60

            var hourText = hour.toString()

            var minuteText = minute.toString()

            showTime = hourText+" Jam "+minuteText+" Menit"
            return showTime
        }
        fun getRequestOptions(
            imageCropType: Int,
            rounded: Int,
            isTopRounded: Boolean = false
        ): RequestOptions {
            var data = RequestOptions()
            var roundedCorners = RoundedCorners(rounded)
            var roundedTopCorners = GranularRoundedCorners(
                rounded.toFloat(),
                rounded.toFloat(),
                0f,
                0f
            )
            var type: BitmapTransformation = CenterCrop()
            if (imageCropType == 2) {
                type = FitCenter()
            }
            if (isTopRounded) {
                data = data.transform(type, roundedTopCorners)
            } else {
                data = data.transform(type, roundedCorners)
            }
            return data.diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
        }

        fun loadImage(
            context: Context,
            uri: Any,
            view: ImageView,
            signature: String,
            radius: Int = 1,
            width: Int,
            height: Int,
            imageCropType: Int = 1,
            isRoundedTop : Boolean = false
        ) {
            try {
                val isSignature =
                    if (signature == null) false else if (signature == "") false else true
                val GRequest = GlideApp.with(context)

                var src = uri
                if(src is String){
                    if(src.contains("Â")){
                        src = src.replace("Â","")
                    }
                }
                var glideApp = GRequest.load(src)
                if(width != 0 && height != 0){
                    glideApp.override(width,height)
                }
                var rounded = convertDpToPx(radius, context)
                var dataRequestOptions = getRequestOptions(imageCropType, rounded, isRoundedTop)

                glideApp.diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.HIGH)
                glideApp.apply(dataRequestOptions
                    .format(DecodeFormat.PREFER_RGB_565))
                if (isSignature) glideApp.signature(ObjectKey(signature))
                glideApp.error(R.color.grey)
                var sizeMultiplier = 0.5f
                glideApp.thumbnail(sizeMultiplier)
                if(height == Target.SIZE_ORIGINAL){
                    glideApp.into(object : CustomTarget<Drawable>(width, 1) {
                        override fun onLoadCleared(placeholder: Drawable?) {
                            // clear resources
                        }
                        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                            view.updateLayoutParams<ConstraintLayout.LayoutParams> {
                                dimensionRatio = "${resource.intrinsicWidth}:${resource.intrinsicHeight}"
                            }
                            view.setImageDrawable(resource)
                        }
                    })
                }else{
                    glideApp.into(view)
                }
            } catch (e: Exception) {
            }

        }

        fun loadCircleImage(context: Context, uri: Any, view: ImageView, signature: String) {
            try {
                if (!signature.equals("", ignoreCase = true)) {
                    GlideApp.with(context)
                        .load(uri)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .priority(Priority.HIGH)
                        .signature(ObjectKey(signature))
                        .circleCrop()
                        .placeholder(R.drawable.baseline_person_24)
                        .error(R.drawable.baseline_person_24).into(view)
                } else {
                    GlideApp.with(context)
                        .load(uri)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .priority(Priority.HIGH)
                        .circleCrop()
                        .placeholder(R.drawable.baseline_person_24)
                        .error(R.drawable.baseline_person_24)
                        .into(view)
                }
            } catch (e: Exception) {
            }
        }
    }
}