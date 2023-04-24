package com.example.pixabayseachimage.domain.usecases

import com.example.pixabayseachimage.domain.repositories.SearchRepository

class SearchImageUseCase(private val repository: SearchRepository) {
    fun invoke(searchKey: String, pageNo: Int) =
        repository.searchImage(searchKey, pageNo)
}