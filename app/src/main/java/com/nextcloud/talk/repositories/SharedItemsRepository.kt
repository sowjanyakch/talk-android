package com.nextcloud.talk.repositories

import autodagger.AutoInjector
import com.nextcloud.talk.R
import com.nextcloud.talk.api.NcApi
import com.nextcloud.talk.application.NextcloudTalkApplication
import com.nextcloud.talk.application.NextcloudTalkApplication.Companion.sharedApplication
import com.nextcloud.talk.models.database.UserEntity
import com.nextcloud.talk.models.json.chat.ChatShareOverall
import com.nextcloud.talk.utils.ApiUtils
import io.reactivex.Observable
import retrofit2.Response
import javax.inject.Inject

@AutoInjector(NextcloudTalkApplication::class)
class SharedItemsRepository {

    var parameters: Parameters? = null

    @Inject
    lateinit var ncApi: NcApi

    init {
        sharedApplication!!.componentApplication.inject(this)
    }

    fun media(type: String): Observable<Response<ChatShareOverall>>? {
        return media(type, null)
    }

    fun media(type: String, lastKnownMessageId: Int?): Observable<Response<ChatShareOverall>>? {
        val credentials = ApiUtils.getCredentials(parameters!!.userName, parameters!!.userToken)

        return ncApi.getSharedItems(
            credentials,
            ApiUtils.getUrlForChatSharedItems(1, parameters!!.baseUrl, parameters!!.roomToken),
            type,
            lastKnownMessageId,
            BATCH_SIZE
        )
    }

    fun authHeader(): Map<String, String> {
        return mapOf(Pair("Authorization", ApiUtils.getCredentials(parameters!!.userName, parameters!!.userToken)))
    }

    fun previewLink(fileId: String?): String {
        return ApiUtils.getUrlForFilePreviewWithFileId(
            parameters!!.baseUrl,
            fileId,
            sharedApplication!!.resources.getDimensionPixelSize(R.dimen.maximum_file_preview_size)
        )
    }

    data class Parameters(
        val userName: String,
        val userToken: String,
        val baseUrl: String,
        val userEntity: UserEntity,
        val roomToken: String
    )

    companion object {
        const val BATCH_SIZE: Int = 28
    }
}
