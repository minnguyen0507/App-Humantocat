package com.pettranslator.cattranslator.catsounds.utils.ad

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.pettranslator.cattranslator.catsounds.R
import com.pettranslator.cattranslator.catsounds.utils.ALog
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private var interstitialAd: InterstitialAd? = null

    fun showInterstitialAd(
        activity: Activity,
        onAdClosed: () -> Unit,
        onAdLoaded: (() -> Unit)? = null,
        onAdFailed: (String) -> Unit = { errorMessage -> }
    ) {
        // Nếu quảng cáo đã tải, hiển thị quảng cáo
        if (interstitialAd != null) {
            interstitialAd?.show(context as Activity)
        } else {
            // Nếu chưa có quảng cáo, tải lại quảng cáo
            loadInterstitialAd(activity, onAdClosed, onAdLoaded, onAdFailed)
        }
    }

    private fun loadInterstitialAd(
        activity: Activity,
        onAdClosed: () -> Unit,
        onAdLoaded: (() -> Unit)? = null,
        onFailed: ((String) -> Unit)? = null
    ) {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            activity, AdUnitIds.INTERSTITIAL, adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    interstitialAd = null
                    ALog.d("AdManager", "Interstitial ad failed to load: ${adError.message}")
                    onFailed?.invoke(adError.message)
                }

                override fun onAdLoaded(ad: InterstitialAd) {
                    ALog.d("AdManager", "Interstitial ad loaded: $context")
                    interstitialAd = ad
                    onAdLoaded?.invoke()
                    interstitialAd?.show(activity)
                    interstitialAd?.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                interstitialAd = null
                                onAdClosed()
                            }

                            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                                interstitialAd = null
                            }

                            override fun onAdShowedFullScreenContent() {
                                interstitialAd = null
                            }
                        }
                }
            })
    }

    fun loadNativeClickAd(
        container: ViewGroup, // Container để chứa NativeAdView
        onAdLoaded: (NativeAd) -> Unit,
        onAdFailed: (String) -> Unit
    ) {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.native_ad_layout, container, false)
        val adView = view.findViewById<NativeAdView>(R.id.native_ad_views)
        val shimmerViewContainer =
            view.findViewById<com.facebook.shimmer.ShimmerFrameLayout>(R.id.shimmer_view_container)
        val adLoader = AdLoader.Builder(context, AdUnitIds.NATIVE)
            .forNativeAd { nativeAd ->
                container.removeAllViews()
                displayNativeAd(adView, nativeAd, view)
                shimmerViewContainer.stopShimmer()
                shimmerViewContainer.visibility = View.GONE
                adView.visibility = View.VISIBLE
                adView.setNativeAd(nativeAd)

                container.addView(view)
                onAdLoaded(nativeAd)
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    ALog.d("AdManager", "Native ad failed to load: ${error.message}")
                    container.addView(view)
                    onAdFailed(error.message)
                }
            })
            .build()

        adLoader.loadAd(AdRequest.Builder().build())
    }


    private fun displayNativeAd(nativeAdView: NativeAdView, nativeAd: NativeAd, view: View) {
        // Gán các thành phần quảng cáo vào NativeAdView
        val headlineView = nativeAdView.findViewById<TextView>(R.id.ad_headline)
        val bodyView = nativeAdView.findViewById<TextView>(R.id.ad_body)
        val callToActionView = nativeAdView.findViewById<Button>(R.id.ad_call_to_action)
        val mediaView = nativeAdView.findViewById<MediaView>(R.id.ad_media)
        headlineView.isSelected = true
        // Gán dữ liệu quảng cáo
        headlineView.text = nativeAd.headline
        bodyView.text = nativeAd.body
        callToActionView.text = nativeAd.callToAction ?: "Hành động"
        mediaView.mediaContent = nativeAd.mediaContent

        // Đăng ký các thành phần với NativeAdView
        nativeAdView.headlineView = headlineView
        nativeAdView.bodyView = bodyView
        nativeAdView.callToActionView = callToActionView
        nativeAdView.mediaView = mediaView

        // Gán NativeAd vào NativeAdView
        nativeAdView.setNativeAd(nativeAd)
    }

    fun loadBannerAd(
        container: ViewGroup,
        onAdLoaded: (() -> Unit)? = null,
        onAdFailed: (String) -> Unit = { errorMessage -> }
    ) {
        container.removeAllViews()
        val adView = AdView(context).apply {
            adUnitId = AdUnitIds.BANNER
            setAdSize(getAdaptiveAdSize())
        }
        adView.adListener = object : AdListener() {
            override fun onAdFailedToLoad(error: LoadAdError) {
                ALog.d("AdManager", "Banner failed to load: ${error.message}")
                onAdFailed.invoke(error.message)
            }

            override fun onAdLoaded() {
                ALog.d("AdManager", "Banner loaded")
                onAdLoaded?.invoke()
            }
        }

        container.addView(adView)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

    private fun getAdaptiveAdSize(): AdSize {
        val displayMetrics = context.resources.displayMetrics
        val density = displayMetrics.density
        val screenWidthDp = (displayMetrics.widthPixels / density).toInt()
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, screenWidthDp)
    }
}