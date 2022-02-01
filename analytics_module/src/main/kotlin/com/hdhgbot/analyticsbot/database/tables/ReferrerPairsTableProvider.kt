package com.hdhgbot.analyticsbot.database.tables

import com.google.cloud.firestore.CollectionReference
import com.hdhgbot.analyticsbot.referrer.data.ReferrerPair

class ReferrerPairsTableProvider(private val subCollection: CollectionReference) {

    fun saveReferrerPaid(referrerPair: ReferrerPair) {
        subCollection.document(referrerPair.referralId.toString())
            .set(referrerPair)
    }

    fun checkHasReferrer(referrerPair: ReferrerPair): Boolean {
        return subCollection.whereEqualTo(ReferrerPair::referralId.name, referrerPair.referralId)
            .limit(1)
            .get()
            .get()
            .size() > 0
    }

    fun getAllReferralCount(): Int {
        return subCollection.get().get().count()
    }

}