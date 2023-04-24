package com.example.pixabayseachimage.ui.mappers

import com.example.pixabayseachimage.domain.entity.ImageInfoEntity
import com.example.pixabayseachimage.utils.toKFormat
import com.example.pixabayseachimage.utils.toTagFormat
import com.example.pixabayseachimage.ui.models.ImageItemUIModel

class UIDataMapper {
    fun toImageItemUIModel(imageList: List<ImageInfoEntity>): List<ImageItemUIModel> {
        return imageList.map {
            ImageItemUIModel(
                it.id,
                it.user.name,
                it.user.picture,
                it.thumbnail,
                it.image,
                toKFormat(it.likes),
                toKFormat(it.comments),
                toKFormat(it.views),
                toKFormat(it.downloads),
                it.tags.map { tagName -> toTagFormat(tagName) })
        }
    }
}