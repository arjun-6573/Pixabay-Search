package com.example.pixabayseachimage.data.mappers

import com.example.pixabayseachimage.data.remote.reponse.SearchAPIResponse
import com.example.pixabayseachimage.domain.entity.ImageInfoEntity
import com.example.pixabayseachimage.domain.entity.UserEntity

class DataMapper {
    fun toSearchImagePageData(
        responseModel: SearchAPIResponse,
    ): List<ImageInfoEntity> {
        return responseModel.run {
            hits.map {
                toImageInfoEntity(it)
            }
        }
    }

    private fun toImageInfoEntity(response: SearchAPIResponse.Hit): ImageInfoEntity {
        return response.run {
            ImageInfoEntity(
                id = "$id",
                user = toUserEntity(this),
                thumbnail = previewURL,
                image = largeImageURL,
                likes = likes.toLong(),
                comments = comments.toLong(),
                views = views.toLong(),
                downloads = downloads.toLong(),
                tags = toTags(tags)
            )
        }
    }

    private fun toTags(tagString: String): List<String> {
        return tagString.split(",")
    }


    private fun toUserEntity(response: SearchAPIResponse.Hit): UserEntity {
        return response.run { UserEntity("$userId", user, userImageURL) }
    }
}