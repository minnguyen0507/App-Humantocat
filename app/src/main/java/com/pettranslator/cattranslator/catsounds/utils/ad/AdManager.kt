package com.pettranslator.cattranslator.catsounds.utils.ad

import android.app.Activity
import android.content.Context
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.pettranslator.cattranslator.catsounds.R
import com.pettranslator.cattranslator.catsounds.utils.ALog
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private var interstitialAd: InterstitialAd? = null
    private var rewardedAd: RewardedAd? = null
    private val lastAdShownTimeMap: MutableMap<String, Long> = mutableMapOf()
    private val isFirstAdShownMap: MutableMap<String, Boolean> = mutableMapOf()
    private val TAG: String = "AdManager"
    fun loadInterstitialAdIfNeeded(activity: Activity) {
        if (interstitialAd != null || !isActivityValid(activity)) return

        InterstitialAd.load(
            activity,
            AdUnitIds.INTERSTITIAL,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    interstitialAd = null
                }
            }
        )
    }

    fun loadRewardAdIfNeeded(activity: Activity) {
        if (rewardedAd != null || !isActivityValid(activity)) return
        Log.d(TAG, "loadRewardAdIfNeeded")
        RewardedAd.load(
            activity,
            AdUnitIds.REWARD,
            AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    Log.d(TAG, "onAdLoaded")
                    rewardedAd = ad
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, " onAdFailedToLoad " + adError.message)
                    rewardedAd = null
                }
            },
        )
    }

    fun showInterstitialAdIfEligible(
        activity: Activity,
        adTag: String, // unique key per screen
        minIntervalMillis: Long,
        onAdStartShowing: () -> Unit = {},
        onAdClosed: () -> Unit,
        onAdSkipped: () -> Unit,
        onAdImpression: () -> Unit,
        onAdFailedToShow: (String) -> Unit = {}
    ) {
        val now = SystemClock.elapsedRealtime()
        val lastShown = lastAdShownTimeMap[adTag] ?: 0L
        val isFirst = isFirstAdShownMap[adTag] != false
        ALog.d("themd", "minIntervalMillis: $minIntervalMillis")
        val canShow = isFirst || now - lastShown >= minIntervalMillis
        println("canShow ${canShow} ${isFirst} ${now - lastShown >= minIntervalMillis}")
        if (!canShow) {
            onAdSkipped()
            return
        }

        showInterstitialAd(
            activity = activity,
            onAdStartShowing = onAdStartShowing,
            onAdClosed = {
                lastAdShownTimeMap[adTag] = SystemClock.elapsedRealtime()
                isFirstAdShownMap[adTag] = false
                loadInterstitialAdIfNeeded(activity)
                onAdClosed()
            },
            onAdFailedToShow = {
                onAdFailedToShow(it)
            },
            onAdImpression = onAdImpression
        )
    }

    fun showRewardAd(
        activity: Activity,
        onAdStartShowing: () -> Unit = {},
        onAdClosed: () -> Unit,
        onAdSkipped: () -> Unit,
        onAdImpression: () -> Unit,
        onAdFailedToShow: (String) -> Unit = {}
    ) {
        rewardedAd?.fullScreenContentCallback =
            object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    // Called when fullscreen content is dismissed.
                    Log.d(TAG, "Ad was dismissed.")
                    // Don't forget to set the ad reference to null so you
                    // don't show the ad a second time.
                    rewardedAd = null
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    // Called when fullscreen content failed to show.
                    Log.d(TAG, "Ad failed to show.")
                    // Don't forget to set the ad reference to null so you
                    // don't show the ad a second time.
                    rewardedAd = null
                }

                override fun onAdShowedFullScreenContent() {
                    // Called when fullscreen content is shown.
                    Log.d(TAG, "Ad showed fullscreen content.")
                }

                override fun onAdImpression() {
                    // Called when an impression is recorded for an ad.
                    Log.d(TAG, "Ad recorded an impression.")
                }

                override fun onAdClicked() {
                    // Called when an ad is clicked.
                    Log.d(TAG, "Ad was clicked.")
                }
            }
        rewardedAd?.show(
            activity,
            OnUserEarnedRewardListener { rewardItem ->
                onAdClosed()
            },
        )
    }


    private fun showInterstitialAd(
        activity: Activity,
        onAdStartShowing: () -> Unit,
        onAdClosed: () -> Unit,
        onAdImpression: () -> Unit,
        onAdFailedToShow: (String) -> Unit,
    ) {
        if (!isActivityValid(activity)) {
            onAdFailedToShow("Activity invalid")
            return
        }

        interstitialAd?.let { ad ->
            onAdStartShowing()
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    interstitialAd = null
                    onAdClosed()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    interstitialAd = null
                    onAdFailedToShow(adError.message)
                }

                override fun onAdShowedFullScreenContent() {
                    interstitialAd = null
                }

                override fun onAdImpression() {
                    super.onAdImpression()
                    onAdImpression()
                }
            }

            ad.show(activity)
        } ?: run {
            loadAndShowInterstitialAd(
                activity,
                onAdStartShowing,
                onAdClosed,
                onAdFailedToShow,
                onAdImpression
            )
        }
    }

    private fun loadAndShowInterstitialAd(
        activity: Activity,
        onAdStartShowing: () -> Unit,
        onAdClosed: () -> Unit,
        onAdFailedToShow: (String) -> Unit,
        onAdImpression: () -> Unit,
    ) {
        onAdStartShowing()
        InterstitialAd.load(
            activity,
            AdUnitIds.INTERSTITIAL,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    showInterstitialAd(
                        activity,
                        onAdStartShowing,
                        onAdClosed,
                        onAdImpression,
                        onAdFailedToShow
                    )
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    onAdFailedToShow(error.message)
                }
            }
        )
    }

    private fun isActivityValid(activity: Activity?): Boolean {
        return activity != null && !activity.isFinishing && !activity.isDestroyed
    }

    fun loadNativeClickAd(
        container: ViewGroup, // Container để chứa NativeAdView
        onAdLoaded: (NativeAd) -> Unit,
        onAdFailed: (String) -> Unit,
        onAdImpression: () -> Unit
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

                override fun onAdImpression() {
                    super.onAdImpression()
                    onAdImpression()
                }
            })
            .build()

        adLoader.loadAd(AdRequest.Builder().build())
    }

    fun loadNativeIntroAd(
        container: ViewGroup, // Container để chứa NativeAdView
        onAdLoaded: (NativeAd) -> Unit,
        onAdFailed: (String) -> Unit,
        onAdImpression: () -> Unit
    ) {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.native_ad_intro, container, false)
        val adView = view.findViewById<NativeAdView>(R.id.native_ad_view_intro)
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

                val params = container.layoutParams
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT
                container.layoutParams = params
                onAdLoaded(nativeAd)
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    ALog.d("AdManager", "Native ad failed to load: ${error.message}")
                    container.addView(view)
                    onAdFailed(error.message)
                }

                override fun onAdImpression() {
                    super.onAdImpression()
                    onAdImpression()
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
        onAdImpression: () -> Unit,
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
                onAdFailed(error.message)
            }

            override fun onAdLoaded() {
                ALog.d("AdManager", "Banner loaded")
                onAdLoaded?.invoke()
            }

            override fun onAdImpression() {
                super.onAdImpression()
                onAdImpression()
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

