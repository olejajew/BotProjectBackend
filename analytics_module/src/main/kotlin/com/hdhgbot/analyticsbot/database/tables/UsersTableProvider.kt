package com.hdhgbot.analyticsbot.database.tables

import com.google.cloud.firestore.CollectionReference
import com.hdhgbot.analyticsbot.users.data.User

class UsersTableProvider(private val subCollection: CollectionReference) {

    fun saveUser(userId: Long) {
        subCollection
            .document(userId.toString())
            .set(
                User(
                    userId,
                    System.currentTimeMillis(),
                    true,
                    null
                )
            )
    }

    fun userBlocked(tgUserId: Long) {
        //todo Вот тут плохо, так как мы по одному планируем обновлять пользователей. Гавно)
        subCollection
            .document(tgUserId.toString())
            .update(
                mapOf(
                    User::alive.name to false,
                    User::blockedTime.name to System.currentTimeMillis()
                )
            )
    }

    fun getAliveUsers(): List<User> {
        return subCollection.whereEqualTo(User::alive.name, true)
            .get().get().toObjects(User::class.java)
    }

    fun getAllUsersCount(): Int {
        return subCollection.get().get().size()
    }

    fun getAliveUsersCount(): Int {
        return subCollection.whereEqualTo(User::alive.name, true).get().get().size()
    }

}