package com.pettranslator.cattranslator.catsounds.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.pettranslator.cattranslator.catsounds.R
import com.pettranslator.cattranslator.catsounds.model.Animal
import com.pettranslator.cattranslator.catsounds.model.EAnimal
import com.pettranslator.cattranslator.catsounds.model.ETypeSong
import com.pettranslator.cattranslator.catsounds.model.IntroSlide
import com.pettranslator.cattranslator.catsounds.model.LanguageItem
import com.pettranslator.cattranslator.catsounds.model.Song
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private fun getLocalizedContext(): Context {
        val locales = AppCompatDelegate.getApplicationLocales()
        val config = Configuration(context.resources.configuration)

        if (!locales.isEmpty) {
            config.setLocales(locales.unwrap() as android.os.LocaleList)
            config.setLayoutDirection(locales[0])
        }

        return context.createConfigurationContext(config)
    }

    fun getSettings(): List<Animal> {
        val localizedContext = getLocalizedContext()
        return listOf(
            Animal(
                EAnimal.CAT,
                "",
                R.drawable.ngonngu_2,
                localizedContext.getString(R.string.language)
            ),
            Animal(EAnimal.CAT, "", R.drawable.share_2, localizedContext.getString(R.string.share)),
            Animal(
                EAnimal.CAT,
                "",
                R.drawable.danhgia_2,
                localizedContext.getString(R.string.rate)
            ),
            Animal(
                EAnimal.CAT,
                "",
                R.drawable.nhanxet_2,
                localizedContext.getString(R.string.feedback)
            ),
            Animal(
                EAnimal.CAT,
                "",
                R.drawable.baomat_2,
                localizedContext.getString(R.string.privacy)
            ),
        )
    }

    fun getDogSounds(): List<Animal> {
        val localizedContext = getLocalizedContext()
        return listOf(
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_love.mp3",
                R.drawable.d_1,
                localizedContext.getString(R.string.d_curious)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_yes.mp3",
                R.drawable.d_2,
                localizedContext.getString(R.string.d_lets_play)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_super_angry.mp3",
                R.drawable.d_3,
                localizedContext.getString(R.string.d_angry)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_friendly_greet.mp3",
                R.drawable.d_4,
                localizedContext.getString(R.string.d_friendly)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_happy.mp3",
                R.drawable.d_5,
                localizedContext.getString(R.string.d_happy)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_cry.mp3",
                R.drawable.d_6,
                localizedContext.getString(R.string.d_cry)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_agree.mp3",
                R.drawable.d_7,
                localizedContext.getString(R.string.d_agree)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_begging.mp3",
                R.drawable.d_8,
                localizedContext.getString(R.string.d_begging)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_cry_lying.mp3",
                R.drawable.d_9,
                localizedContext.getString(R.string.d_cry_lying)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_dance.mp3",
                R.drawable.d_10,
                localizedContext.getString(R.string.d_dance)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_exhausted.mp3",
                R.drawable.d_11,
                localizedContext.getString(R.string.d_exhausted)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_handclap.mp3",
                R.drawable.d_12,
                localizedContext.getString(R.string.d_hand_clap)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_happy_cry.mp3",
                R.drawable.d_13,
                localizedContext.getString(R.string.d_happy_cry)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_happy_walk.mp3",
                R.drawable.d_14,
                localizedContext.getString(R.string.d_happy_walk)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_hi_fence.mp3",
                R.drawable.d_15,
                localizedContext.getString(R.string.d_hi_fence_)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_hi.mp3",
                R.drawable.d_16,
                localizedContext.getString(R.string.d_hi)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_lie.mp3",
                R.drawable.d_17,
                localizedContext.getString(R.string.d_lie)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_love.mp3",
                R.drawable.d_18,
                localizedContext.getString(R.string.d_love)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_no.mp3",
                R.drawable.d_19,
                localizedContext.getString(R.string.d_no)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_pet.mp3",
                R.drawable.d_20,
                localizedContext.getString(R.string.d_pet)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_raise_hand.mp3",
                R.drawable.d_21,
                localizedContext.getString(R.string.d_raise_hand)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_sad.mp3",
                R.drawable.d_22,
                localizedContext.getString(R.string.d_sad)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_scratch.mp3",
                R.drawable.d_23,
                localizedContext.getString(R.string.d_scratch)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_shy.mp3",
                R.drawable.d_24,
                localizedContext.getString(R.string.d_shy)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_soft_angry.mp3",
                R.drawable.d_25,
                localizedContext.getString(R.string.d_soft_angry)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_begging.mp3",
                R.drawable.d_26,
                localizedContext.getString(R.string.d_begging)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_startle.mp3",
                R.drawable.d_27,
                localizedContext.getString(R.string.d_startle)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_super_angry.mp3",
                R.drawable.d_28,
                localizedContext.getString(R.string.d_super_angry)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_wonder.mp3",
                R.drawable.d_29,
                localizedContext.getString(R.string.d_wonder)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_wow.mp3",
                R.drawable.d_30,
                localizedContext.getString(R.string.d_wow)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_yeah.mp3",
                R.drawable.d_31,
                localizedContext.getString(R.string.d_yeah)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_yes.mp3",
                R.drawable.d_32,
                localizedContext.getString(R.string.d_yes)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_adoring_chatter.mp3",
                R.drawable.d_33,
                localizedContext.getString(R.string.d_adoring_chatter)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_affectionate_barking.mp3",
                R.drawable.d_34,
                localizedContext.getString(R.string.d_affectionate_barking)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_affectionate_howl.mp3",
                R.drawable.d_35,
                localizedContext.getString(R.string.d_affectionate_howl)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_affectionate_lick.mp3",
                R.drawable.d_36,
                localizedContext.getString(R.string.d_affectionate_lick)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_affectionate_neigh.mp3",
                R.drawable.d_1,
                localizedContext.getString(R.string.d_affectionate_neigh)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_affectionate_nuzzle.mp3",
                R.drawable.d_2,
                localizedContext.getString(R.string.d_affectionate_nuzzle)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_alert_bark.mp3",
                R.drawable.d_3,
                localizedContext.getString(R.string.d_alert_bark)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_amused_honking.mp3",
                R.drawable.d_4,
                localizedContext.getString(R.string.d_amused_honking)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_amused_roaring.mp3",
                R.drawable.d_5,
                localizedContext.getString(R.string.d_amused_roaring)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_amused_squeak.mp3",
                R.drawable.d_6,
                localizedContext.getString(R.string.d_amused_squeak)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_anxious_howl.mp3",
                R.drawable.d_7,
                localizedContext.getString(R.string.d_anxious_howl)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_blissful_chatter.mp3",
                R.drawable.d_8,
                localizedContext.getString(R.string.d_blissful_chatter)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_blissful_gobbling.mp3",
                R.drawable.d_9,
                localizedContext.getString(R.string.d_blissful_gobbling)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_blissful_roar.mp3",
                R.drawable.d_10,
                localizedContext.getString(R.string.d_blissful_roar)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_cautious_growl.mp3",
                R.drawable.d_11,
                localizedContext.getString(R.string.d_cautious_growl)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_cheerful_growl.mp3",
                R.drawable.d_12,
                localizedContext.getString(R.string.d_cheerful_growl)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_cheerful_quacking.mp3",
                R.drawable.d_13,
                localizedContext.getString(R.string.d_cheerful_quacking)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_confused_yelp.mp3",
                R.drawable.d_14,
                localizedContext.getString(R.string.d_confused_yelp)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_contented_cluck.mp3",
                R.drawable.d_15,
                localizedContext.getString(R.string.d_contented_cluck)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_contented_ruff.mp3",
                R.drawable.d_16,
                localizedContext.getString(R.string.d_contented_ruff)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_curious_sniff.mp3",
                R.drawable.d_17,
                localizedContext.getString(R.string.d_curious_sniff)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_delighted_trilling.mp3",
                R.drawable.d_18,
                localizedContext.getString(R.string.d_delighted_trilling)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_delighted_whistle.mp3",
                R.drawable.d_19,
                localizedContext.getString(R.string.d_delighted_whistle)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_delighted_yelp.mp3",
                R.drawable.d_20,
                localizedContext.getString(R.string.d_delighted_yelp)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_eager_pant.mp3",
                R.drawable.d_21,
                localizedContext.getString(R.string.d_eager_pant)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_eager_whine.mp3",
                R.drawable.d_22,
                localizedContext.getString(R.string.d_eager_whine)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_ecstatic_barking.mp3",
                R.drawable.d_23,
                localizedContext.getString(R.string.d_ecstatic_barking)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_ecstatic_baying.mp3",
                R.drawable.d_24,
                localizedContext.getString(R.string.d_ecstatic_baying)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_ecstatic_peep.mp3",
                R.drawable.d_25,
                localizedContext.getString(R.string.d_ecstatic_peep)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_elated_snuffling.mp3",
                R.drawable.d_26,
                localizedContext.getString(R.string.d_elated_snuffling)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_elated_trill.mp3",
                R.drawable.d_27,
                localizedContext.getString(R.string.d_elated_trill)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_euphoric_crooning.mp3",
                R.drawable.d_28,
                localizedContext.getString(R.string.d_euphoric_crooning)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_excitable_crowing.mp3",
                R.drawable.d_29,
                localizedContext.getString(R.string.d_excitable_rowing)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_excited_chase.mp3",
                R.drawable.d_30,
                localizedContext.getString(R.string.d_excited_chase)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_excited_chirp.mp3",
                R.drawable.d_31,
                localizedContext.getString(R.string.d_excited_chirp)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_excited_woof.mp3",
                R.drawable.d_32,
                localizedContext.getString(R.string.d_excited_woof)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_friendly_greet.mp3",
                R.drawable.d_33,
                localizedContext.getString(R.string.d_friendly_greet)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_frightened_whine.mp3",
                R.drawable.d_34,
                localizedContext.getString(R.string.d_frightened_whine)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_gleeful_coo.mp3",
                R.drawable.d_35,
                localizedContext.getString(R.string.d_gleeful_coo)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_gleeful_squealing.mp3",
                R.drawable.d_36,
                localizedContext.getString(R.string.d_gleeful_squealing)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_grumpy_grumble.mp3",
                R.drawable.d_1,
                localizedContext.getString(R.string.d_grumpy_grumble)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_happy_bark.mp3",
                R.drawable.d_2,
                localizedContext.getString(R.string.d_happy_bark)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_happy_dance.mp3",
                R.drawable.d_3,
                localizedContext.getString(R.string.d_happy_dance)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_happy_wag.mp3",
                R.drawable.d_4,
                localizedContext.getString(R.string.d_happy_wag)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_hopeful_yelp.mp3",
                R.drawable.d_5,
                localizedContext.getString(R.string.d_hopeful_yelp)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_hungry_chew.mp3",
                R.drawable.d_6,
                localizedContext.getString(R.string.d_hungry_chew)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_jealous_hiss.mp3",
                R.drawable.d_7,
                localizedContext.getString(R.string.d_jealous_hiss)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_jovial_mumble.mp3",
                R.drawable.d_8,
                localizedContext.getString(R.string.d_jovial_mumble)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_jovial_squeal.mp3",
                R.drawable.d_9,
                localizedContext.getString(R.string.d_jovial_squeal)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_joyful_howl.mp3",
                R.drawable.d_10,
                localizedContext.getString(R.string.d_joyful_howl)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_jubilant_whinny.mp3",
                R.drawable.d_11,
                localizedContext.getString(R.string.d_jubilant_whinny)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_loving_hoot.mp3",
                R.drawable.d_12,
                localizedContext.getString(R.string.d_loving_hoot)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_loving_hooting.mp3",
                R.drawable.d_13,
                localizedContext.getString(R.string.d_loving_hooting)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_loving_snarl.mp3",
                R.drawable.d_14,
                localizedContext.getString(R.string.d_loving_snarl)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_loving_whimper.mp3",
                R.drawable.d_15,
                localizedContext.getString(R.string.d_loving_whimper)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_merry_trumpeting.mp3",
                R.drawable.d_16,
                localizedContext.getString(R.string.d_merry_trumpeting)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_mournful_sob.mp3",
                R.drawable.d_17,
                localizedContext.getString(R.string.d_mournful_sob)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_nervous_tremor.mp3",
                R.drawable.d_18,
                localizedContext.getString(R.string.d_nervous_tremor)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_overjoyed_purr.mp3",
                R.drawable.d_19,
                localizedContext.getString(R.string.d_overjoyed_purr)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_overjoyed_screech.mp3",
                R.drawable.d_20,
                localizedContext.getString(R.string.d_overjoyed_screech)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_overjoyed_sniveling.mp3",
                R.drawable.d_21,
                localizedContext.getString(R.string.d_overjoyed_sniveling)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_peaceful_snarl.mp3",
                R.drawable.d_22,
                localizedContext.getString(R.string.d_peaceful_snarl)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_playful_bleat.mp3",
                R.drawable.d_23,
                localizedContext.getString(R.string.d_playful_bleat)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_playful_chirping.mp3",
                R.drawable.d_24,
                localizedContext.getString(R.string.d_playful_chirping)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_playful_hiss.mp3",
                R.drawable.d_26,
                localizedContext.getString(R.string.d_playful_hiss)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_playful_nudge.mp3",
                R.drawable.d_27,
                localizedContext.getString(R.string.d_playful_nudge)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_playful_squeak.mp3",
                R.drawable.d_28,
                localizedContext.getString(R.string.d_playful_squeak)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_playful_yip.mp3",
                R.drawable.d_29,
                localizedContext.getString(R.string.d_playful_yip)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_pleased_hiss.mp3",
                R.drawable.d_30,
                localizedContext.getString(R.string.d_pleased_hiss)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_pleased_rumbling.mp3",
                R.drawable.d_31,
                localizedContext.getString(R.string.d_pleased_rumbling)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_relaxed_purr.mp3",
                R.drawable.d_32,
                localizedContext.getString(R.string.d_relaxed_purr)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_satisfied_grunt.mp3",
                R.drawable.d_33,
                localizedContext.getString(R.string.d_satisfied_grunt)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_sleepy_sigh.mp3",
                R.drawable.d_34,
                localizedContext.getString(R.string.d_sleepy_sign)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_thankful_whistle.mp3",
                R.drawable.d_35,
                localizedContext.getString(R.string.d_thankful_whistle)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_thirsty_lap.mp3",
                R.drawable.d_36,
                localizedContext.getString(R.string.d_thirsty_lap)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_thrilled_braying.mp3",
                R.drawable.d_11,
                localizedContext.getString(R.string.d_thrilled_braying)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_trusting_whuff.mp3",
                R.drawable.d_12,
                localizedContext.getString(R.string.d_trusting_whuff)
            ),
            Animal(
                EAnimal.DOG,
                "dog_sound/dog_upbeat_whickering.mp3",
                R.drawable.d_13,
                localizedContext.getString(R.string.d_upbeat_whickering)
            )
        )
    }


    fun getCatSounds(): List<Animal> {
        val localizedContext = getLocalizedContext()
        return listOf(
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_hello.mp3",
                R.drawable.xinchao,
                localizedContext.getString(R.string.c_hello),
                resultTranslate = localizedContext.getString(R.string.r_8)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_i_love_you.mp3",
                R.drawable.toiyeuban,
                localizedContext.getString(R.string.c_love_you),
                resultTranslate = localizedContext.getString(R.string.r_9)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_come_here.mp3",
                R.drawable.denday,
                localizedContext.getString(R.string.c_come_here),
                resultTranslate = localizedContext.getString(R.string.r_10)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_bad_cat.mp3",
                R.drawable.doibung,
                localizedContext.getString(R.string.c_hungry),
                resultTranslate = localizedContext.getString(R.string.r_11)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_fine.mp3",
                R.drawable.khoe,
                localizedContext.getString(R.string.c_fine),
                resultTranslate = localizedContext.getString(R.string.r_12)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_angry.mp3",
                R.drawable.tucgian,
                localizedContext.getString(R.string.c_angry),
                resultTranslate = localizedContext.getString(R.string.r_13)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_nono.mp3",
                R.drawable.khongkhong,
                localizedContext.getString(R.string.c_no_no),
                resultTranslate = localizedContext.getString(R.string.r_14)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_good_cat.mp3",
                R.drawable.buon,
                localizedContext.getString(R.string.c_sad),
                resultTranslate = localizedContext.getString(R.string.r_15)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_warning.mp3",
                R.drawable.chiendau,
                localizedContext.getString(R.string.c_fighting),
                resultTranslate = localizedContext.getString(R.string.r_16)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_good_cat.mp3",
                R.drawable.vuimung,
                localizedContext.getString(R.string.c_happy),
                resultTranslate = localizedContext.getString(R.string.r_17)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_what_have_you_done.mp3",
                R.drawable.met,
                localizedContext.getString(R.string.c_tired),
                resultTranslate = localizedContext.getString(R.string.r_18)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_yes.mp3",
                R.drawable.ngu,
                localizedContext.getString(R.string.c_sleep),
                resultTranslate = localizedContext.getString(R.string.r_19)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_anxious_moan.mp3",
                R.drawable.renri,
                localizedContext.getString(R.string.c_anxious_moan),
                resultTranslate = localizedContext.getString(R.string.r_20)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_bass_meow.mp3",
                R.drawable.chiendauritlen,
                localizedContext.getString(R.string.c_bass_meow),
                resultTranslate = localizedContext.getString(R.string.r_21)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_bossy_hiss.mp3",
                R.drawable.keuthap,
                localizedContext.getString(R.string.c_bossy_hiss),
                resultTranslate = localizedContext.getString(R.string.r_22)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_bright_mew.mp3",
                R.drawable.caunahu,
                localizedContext.getString(R.string.c_bright_mew),
                resultTranslate = localizedContext.getString(R.string.r_23)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_cheery_yowl.mp3",
                R.drawable.meoconmeo,
                localizedContext.getString(R.string.c_cherry_yowl),
                resultTranslate = localizedContext.getString(R.string.r_24)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_crack_meow.mp3",
                R.drawable.meomeo,
                localizedContext.getString(R.string.c_crack_meow),
                resultTranslate = localizedContext.getString(R.string.r_25)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_croaky_chirp.mp3",
                R.drawable.meomeo2,
                localizedContext.getString(R.string.c_croaky_chirp),
                resultTranslate = localizedContext.getString(R.string.r_26)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_deep_purring.mp3",
                R.drawable.tiengritdaidang,
                localizedContext.getString(R.string.c_deep_purring),
                resultTranslate = localizedContext.getString(R.string.r_27)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_defensive_hiss.mp3",
                R.drawable.tiengkeudangghet,
                localizedContext.getString(R.string.c_defensive_hiss),
                resultTranslate = localizedContext.getString(R.string.r_28)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_edgy_yowl.mp3",
                R.drawable.sohai,
                localizedContext.getString(R.string.c_edgy_yowl),
                resultTranslate = localizedContext.getString(R.string.r_29)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_you_are_punisshed.mp3",
                R.drawable.c_37,
                localizedContext.getString(R.string.c_excited_trill),
                resultTranslate = localizedContext.getString(R.string.r_30)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_feeble_yowl.mp3",
                R.drawable.c_38,
                localizedContext.getString(R.string.c_feeble_yowl),
                resultTranslate = localizedContext.getString(R.string.r_31)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_fight_hiss.mp3",
                R.drawable.c_39,
                localizedContext.getString(R.string.c_fight_hiss),
                resultTranslate = localizedContext.getString(R.string.r_32)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_frail_moan.mp3",
                R.drawable.ritlon,
                localizedContext.getString(R.string.c_frail_moan),
                resultTranslate = localizedContext.getString(R.string.r_33)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_friendly_meow.mp3",
                R.drawable.rendangyeu,
                localizedContext.getString(R.string.c_friendly_meow),
                resultTranslate = localizedContext.getString(R.string.r_34)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_grating_meow.mp3",
                R.drawable.renri_yeuduoi,
                localizedContext.getString(R.string.c_grating_meow),
                resultTranslate = localizedContext.getString(R.string.r_35)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_growly_meow.mp3",
                R.drawable.thanthien,
                localizedContext.getString(R.string.c_growly_meow),
                resultTranslate = localizedContext.getString(R.string.r_36)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_hateful_yowl.mp3",
                R.drawable.ngotngao,
                localizedContext.getString(R.string.c_hateful_yowl),
                resultTranslate = localizedContext.getString(R.string.r_37)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_high_mew.mp3",
                R.drawable.gamgumui,
                localizedContext.getString(R.string.c_high_mew),
                resultTranslate = localizedContext.getString(R.string.r_38)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_insistent_hiss.mp3",
                R.drawable.dau,
                localizedContext.getString(R.string.c_insistent_hiss),
                resultTranslate = localizedContext.getString(R.string.r_39)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_kitten_mew.mp3",
                R.drawable.lacquan,
                localizedContext.getString(R.string.c_kitten_mew),
                resultTranslate = localizedContext.getString(R.string.r_40)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_lilting_purr.mp3",
                R.drawable.yowldai,
                localizedContext.getString(R.string.c_lilting_purr),
                resultTranslate = localizedContext.getString(R.string.r_41)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_long_grunt.mp3",
                R.drawable.c_36,
                localizedContext.getString(R.string.c_long_grunt),
                resultTranslate = localizedContext.getString(R.string.r_42)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_long_yowls.mp3",
                R.drawable.c_40,
                localizedContext.getString(R.string.c_long_yowls),
                resultTranslate = localizedContext.getString(R.string.r_43)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_lound_hiss.mp3",
                R.drawable.tiengkeudangghet,
                localizedContext.getString(R.string.c_lound_hiss),
                resultTranslate = localizedContext.getString(R.string.r_44)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_lovely_moan.mp3",
                R.drawable.sohai,
                localizedContext.getString(R.string.c_lovely_moan),
                resultTranslate = localizedContext.getString(R.string.r_45)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_low_purr.mp3",
                R.drawable.buon,
                localizedContext.getString(R.string.c_low_purr),
                resultTranslate = localizedContext.getString(R.string.r_46)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_luscious_mew.mp3",
                R.drawable.keuthap,
                localizedContext.getString(R.string.c_luscious_mew),
                resultTranslate = localizedContext.getString(R.string.r_47)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_mawkish_yowl.mp3",
                R.drawable.meocao,
                localizedContext.getString(R.string.c_mawkish_yowl),
                resultTranslate = localizedContext.getString(R.string.r_48)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_nasally_growl.mp3",
                R.drawable.meowmeow,
                localizedContext.getString(R.string.c_nasally_growl),
                resultTranslate = localizedContext.getString(R.string.r_49)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_orotund_meow.mp3",
                R.drawable.ngu,
                localizedContext.getString(R.string.c_orotund_meow),
                resultTranslate = localizedContext.getString(R.string.r_50)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_pain_meow.mp3",
                R.drawable.khoe,
                localizedContext.getString(R.string.c_pain_meow),
                resultTranslate = localizedContext.getString(R.string.r_51)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_petulant_yowl.mp3",
                R.drawable.toiyeuban,
                localizedContext.getString(R.string.c_petulant_yowl),
                resultTranslate = localizedContext.getString(R.string.r_1)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_raspy_meow.mp3",
                R.drawable.tucgian,
                localizedContext.getString(R.string.c_raspy_meow),
                resultTranslate = localizedContext.getString(R.string.r_52)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_scare_chirp.mp3",
                R.drawable.lacquan,
                localizedContext.getString(R.string.c_scare_chirp_),
                resultTranslate = localizedContext.getString(R.string.r_53)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_short_yowl.mp3",
                R.drawable.keuthap,
                localizedContext.getString(R.string.c_short_yowl),
                resultTranslate = localizedContext.getString(R.string.r_54)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_shrill_meow.mp3",
                R.drawable.khongkhong,
                localizedContext.getString(R.string.c_shrill_meow),
                resultTranslate = localizedContext.getString(R.string.r_55)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_snarly_snarl.mp3",
                R.drawable.image_cat,
                localizedContext.getString(R.string.c_snarly_snarl),
                resultTranslate = localizedContext.getString(R.string.r_56)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_snooty_meow.mp3",
                R.drawable.meocao,
                localizedContext.getString(R.string.c_snooty_meow),
                resultTranslate = localizedContext.getString(R.string.r_57)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_soft_chirp.mp3",
                R.drawable.vuimung,
                localizedContext.getString(R.string.c_soft_chirp),
                resultTranslate = localizedContext.getString(R.string.r_58)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_soft_purr.mp3",
                R.drawable.buon,
                localizedContext.getString(R.string.c_soft_purr),
                resultTranslate = localizedContext.getString(R.string.r_59)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_squeak_meow.mp3",
                R.drawable.c_40,
                localizedContext.getString(R.string.c_squeak_meow),
                resultTranslate = localizedContext.getString(R.string.r_60)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_stressed_purr.mp3",
                R.drawable.c_39,
                localizedContext.getString(R.string.c_stressed_purr),
                resultTranslate = localizedContext.getString(R.string.r_61)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_sullen_grunt.mp3",
                R.drawable.caunahu,
                localizedContext.getString(R.string.c_sullen_grunt),
                resultTranslate = localizedContext.getString(R.string.r_62)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_sweet_meow.mp3",
                R.drawable.ngotngao,
                localizedContext.getString(R.string.c_sweet_meow),
                resultTranslate = localizedContext.getString(R.string.r_63)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_trembling_mew.mp3",
                R.drawable.ngaonagn,
                localizedContext.getString(R.string.c_trembling_mew),
                resultTranslate = localizedContext.getString(R.string.r_64)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_upbeat_mew.mp3",
                R.drawable.sohai,
                localizedContext.getString(R.string.c_upbeat_mew),
                resultTranslate = localizedContext.getString(R.string.r_65)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_weak_mew.mp3",
                R.drawable.rendangyeu,
                localizedContext.getString(R.string.c_weak_mew),
                resultTranslate = localizedContext.getString(R.string.r_66)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_weak_whine.mp3",
                R.drawable.renri_yeuduoi,
                localizedContext.getString(R.string.c_weak_whine),
                resultTranslate = localizedContext.getString(R.string.r_67)
            ),
            Animal(
                EAnimal.CAT,
                "cat_sound/cat_whiny_yowl.mp3",
                R.drawable.ritlon,
                localizedContext.getString(R.string.c_whiny_yowl),
                resultTranslate = localizedContext.getString(R.string.r_68)
            )
        )

    }

    fun getCatSongs(): List<Song> {
        val localizedContext = getLocalizedContext()
        return listOf(
            Song("Bad Cat Guy", "cat_song/bad_cat_guy.mp3"),
            Song("Unstoppable Cat", "cat_song/unstoppable_cat.mp3"),
            Song("Cat Dance Monkey", "cat_song/cat_dance_monkey.mp3"),
            Song("Cat Dynamite", "cat_song/cat_dynamite.mp3"),
            Song("Cat More & More", "cat_song/cat_more_more.mp3"),
            Song("Cat Shake It Off", "cat_song/cat_shake_it_off.mp3"),
            Song("Cat Solo", "cat_song/cat_solo.mp3"),
            Song("Cat With Love", "cat_song/cat_with_love.mp3"),
            Song("Christmas Song", "cat_song/christmas_song.mp3"),
            Song("How Cat Like That", "cat_song/how_cat_like_that.mp3"),
            Song("Shape Of You Cat", "cat_song/shape_of_you_cat.mp3"),
            Song(
                localizedContext.getString(R.string.challenge_1),
                "cat_challenge/goi_meo1.mp3",
                type = ETypeSong.Challenge
            ),
            Song(
                localizedContext.getString(R.string.challenge_2),
                "cat_challenge/goi_meo2.mp3",
                type = ETypeSong.Challenge
            ),
            Song(
                localizedContext.getString(R.string.challenge_3),
                "cat_challenge/meo_hoang_so.mp3",
                type = ETypeSong.Challenge
            ),
            Song(
                localizedContext.getString(R.string.challenge_4),
                "cat_challenge/meo_bao_ve.mp3",
                type = ETypeSong.Challenge
            ),
            Song(
                localizedContext.getString(R.string.challenge_5),
                "cat_challenge/meo_cuu_con.mp3",
                type = ETypeSong.Challenge
            ),
            Song(
                localizedContext.getString(R.string.challenge_6),
                "cat_challenge/meo_phan_ung1.mp3",
                type = ETypeSong.Challenge
            ),
            Song(
                localizedContext.getString(R.string.challenge_7),
                "cat_challenge/meo_phan_ung2.mp3",
                type = ETypeSong.Challenge
            ),
            Song(
                localizedContext.getString(R.string.challenge_8),
                "cat_challenge/meo_phan_ung3.mp3",
                type = ETypeSong.Challenge
            ),
            Song(
                localizedContext.getString(R.string.challenge_9),
                "cat_challenge/meo_yeu_thich.mp3",
                type = ETypeSong.Challenge
            ),
            Song(
                localizedContext.getString(R.string.challenge_10),
                "cat_challenge/meo_ton_tho.mp3",
                type = ETypeSong.Challenge
            ),
            Song(
                localizedContext.getString(R.string.challenge_11),
                "cat_challenge/meo_boi_roi.mp3",
                type = ETypeSong.Challenge
            ),
            Song(
                localizedContext.getString(R.string.challenge_12),
                "cat_challenge/meo_phat_dien.mp3",
                type = ETypeSong.Challenge
            ),
        )
    }


    fun getLanguageList(): List<LanguageItem> = listOf(
        LanguageItem("Tiếng Việt", R.drawable.flag_vietnam, "vi"),
        LanguageItem("English", R.drawable.flag_eng, "en"),
        LanguageItem("Français", R.drawable.flag_france, "fr"),
        LanguageItem("हिंदी", R.drawable.flag_india, "hi"),
        LanguageItem("日本語", R.drawable.flag_japan, "ja"),
        LanguageItem("한국어", R.drawable.flag_korea, "ko"),
        LanguageItem("Türkçe", R.drawable.flag_tukey, "tr"),
        LanguageItem("Español", R.drawable.flag_spain, "es"),
        LanguageItem("Italiano", R.drawable.flag_italy, "it"),
        LanguageItem("Deutsch", R.drawable.flag_germany, "de"),
        LanguageItem("Português", R.drawable.flag_portugal, "pt")
    )


    fun getSlideList(): List<IntroSlide> {
        val localizedContext = getLocalizedContext()
        return listOf(
            IntroSlide(
                R.drawable.pic_1,
                localizedContext.getString(R.string.welcome),
                localizedContext.getString(R.string.intro_1)
            ),
            IntroSlide(
                R.drawable.pic_2,
                localizedContext.getString(R.string.personalization),
                localizedContext.getString(R.string.intro_2)
            ),
            IntroSlide(
                R.drawable.pic_3,
                localizedContext.getString(R.string.make_happy),
                localizedContext.getString(R.string.intro_3)
            )
        )
    }


}
