package com.rikkei.tra_02t0115browser

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_web.*
import android.widget.FrameLayout
import android.content.pm.ActivityInfo
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.os.Build
import android.text.TextUtils
import android.webkit.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import android.app.Activity
import android.util.Log
import android.view.inputmethod.InputMethodManager
import com.google.gson.GsonBuilder
import com.rikkei.tra_02t0115browser.data.AppPreferencesHelper


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [WebFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 *
 */
class WebFragment : Fragment(), View.OnClickListener {

    private val TAG = WebFragment::class.java.simpleName

    private var listener: OnFragmentInteractionListener? = null

    private var strUrl: String? = ""

    fun getUrl(): String? {
        return strUrl
    }

    private lateinit var appPreferences: AppPreferencesHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_web, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpWebView()
        ivSearch.setOnClickListener(this)
        ibBack.setOnClickListener(this)
        ibForward.setOnClickListener(this)

        val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
        appPreferences = AppPreferencesHelper(activity!!.applicationContext, AppPreferencesHelper.PREF_NAME, gson)

        val strData = appPreferences.getHistory()
        if (!strData.isNullOrEmpty()) {
            Log.e(TAG, "history" + strData.toString())
        }
    }


    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.ivSearch -> {
                actionSearch()
            }
            R.id.ibBack -> {
                actionGoBack()
            }
            R.id.ibForward -> {
                actionGoForward()
            }
        }
    }

    private fun actionGoBack() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            Toast.makeText(activity, "You can't go back!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun actionGoForward() {
        if (webView.canGoForward()) {
            webView.goForward()
        } else {
            Toast.makeText(activity, "You can't go forward!", Toast.LENGTH_SHORT).show()
        }
    }


    private fun actionSearch() {
        hideSoftKeyboard(activity)
        strUrl = etInput.text.toString().trim()
        if (!TextUtils.isEmpty(strUrl)) {
            webView.loadUrl(strUrl)
        }
    }

    private fun setUpWebView() {
        webView.isVerticalScrollBarEnabled = false
        webView.isHorizontalScrollBarEnabled = false
        webView.settings.javaScriptEnabled = true
        webView.webChromeClient = WebChromeCustom()
        webView.webViewClient = WebViewCustom()
    }


    private inner class WebViewCustom : WebViewClient() {

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            Log.e(TAG, "onPageFinished")
            strUrl = url
            url?.let { appPreferences.setHistory(it) }
        }

        override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
            layoutLoading.visibility = View.GONE
            Toast.makeText(activity, description, Toast.LENGTH_SHORT).show()
        }

        @RequiresApi(Build.VERSION_CODES.M)
        override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
            onReceivedError(view, error!!.errorCode, error.getDescription().toString(), request?.getUrl().toString());
        }
    }

    private inner class WebChromeCustom : android.webkit.WebChromeClient() {
        private var mCustomView: View? = null
        private var mOriginalSystemUiVisibility: Int = 0
        private var mOriginalOrientation: Int = 0
        private var mCustomViewCallback: CustomViewCallback? = null
        protected var mFullscreenContainer: FrameLayout? = null

        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            if (newProgress == 100) {
                layoutLoading.visibility = View.GONE
            } else {
                layoutLoading.visibility = View.VISIBLE
            }

        }

        override fun getDefaultVideoPoster(): Bitmap? {
            return if (mCustomView == null) {
                null
            } else BitmapFactory.decodeResource(context!!.resources, 2130837573)
        }

        override fun onShowCustomView(view: View, callback: CustomViewCallback) {
            if (mCustomView != null) {
                onHideCustomView()
                return
            }

            mCustomView = view
            mOriginalSystemUiVisibility = activity!!.window.decorView.systemUiVisibility
            mOriginalOrientation = activity!!.requestedOrientation

            mCustomViewCallback = callback

            val decor = activity!!.window.decorView as FrameLayout
            decor.addView(
                mCustomView, FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )

            activity!!.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_IMMERSIVE
            activity!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        override fun onHideCustomView() {
            val decor = activity!!.window.decorView as FrameLayout
            decor.removeView(mCustomView)
            mCustomView = null

            activity!!.window.decorView.systemUiVisibility = mOriginalSystemUiVisibility
            activity!!.requestedOrientation = mOriginalOrientation

            mCustomViewCallback!!.onCustomViewHidden()
            mCustomViewCallback = null

        }
    }

    fun hideSoftKeyboard(activity: Activity?) {
        if (activity != null && activity.window != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (imm != null) {
                imm!!.hideSoftInputFromWindow(activity.window.decorView.windowToken, 0)
            }
        }

    }



    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction()
    }

}
