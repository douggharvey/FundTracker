package com.douglasharvey.fundtracker3


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.fragment_web_view.*


class WebViewFragment : androidx.fragment.app.Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_web_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webView!!.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean { //todo correct deprecated method
                view?.loadUrl(url)
                return true
            }
        }
        val webAddress = arguments?.let {
            val safeArgs = WebViewFragmentArgs.fromBundle(it)
            safeArgs.webAddress
        }
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.settings.builtInZoomControls = true
        webView.settings.javaScriptEnabled = true
        // https@ //stackoverflow.com/questions/32304237/android-webView-loading-data-performance-very-slow/34389203
//        webView.settings.cacheMode(WebSettings.LOAD_NO_CACHE)

      //  webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        webView!!.loadUrl(webAddress)
    }
}
