package com.douglasharvey.fundtracker3


import android.net.http.SslError
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.SslErrorHandler
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
//https://stackoverflow.com/questions/18377769/webview-not-able-to-load-https-url-in-android?noredirect=1&lq=1
            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                handler?.proceed()
            }
        }
        val webAddress = arguments?.let {
            val safeArgs = WebViewFragmentArgs.fromBundle(it)
            safeArgs.webAddress
        }
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.settings.builtInZoomControls = true
        webView.settings.javaScriptEnabled = true //needed to enable graph from website
        webView!!.loadUrl(webAddress)
    }
}
