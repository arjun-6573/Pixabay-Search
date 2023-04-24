package com.example.pixabayseachimage.di

import android.content.Context
import com.example.pixabayseachimage.BuildConfig
import com.example.pixabayseachimage.data.datasources.RemoteDataSourceImpl
import com.example.pixabayseachimage.data.mappers.DataMapper
import com.example.pixabayseachimage.data.remote.MyApi
import com.example.pixabayseachimage.data.remote.RequestInterceptor
import com.example.pixabayseachimage.data.repositories.SearchRepositoryImpl
import com.example.pixabayseachimage.data.datasources.RemoteDataSource
import com.example.pixabayseachimage.domain.repositories.SearchRepository
import com.example.pixabayseachimage.domain.usecases.SearchImageUseCase
import com.example.pixabayseachimage.ui.mappers.UIDataMapper
import com.example.pixabayseachimage.utils.dispatcher.MyDispatchers
import com.example.pixabayseachimage.utils.dispatcher.MyDispatchersImpl
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.scopes.ViewModelScoped
import okhttp3.Cache
import java.io.File

@Module
@InstallIn(ActivityRetainedComponent::class)
object AppModule {

    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    fun provideInterceptor(): Interceptor {
        return HttpLoggingInterceptor {
            Timber.tag("OK-HTTP").i(it)
        }.setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    fun provideOkHttpCache(@ApplicationContext context: Context): Cache {
        //  2 MB cache file
        val file = File(context.cacheDir, "offline_cache")
        return Cache(file, (2 * 1024 * 1024))
    }

    @Provides
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        interceptor: Interceptor,
        cache: Cache
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(RequestInterceptor(context))
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .cache(cache)
            .build()

    }

    @ActivityRetainedScoped
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): MyApi {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build().run {
                create(MyApi::class.java)
            }
    }

    @ActivityRetainedScoped
    @Provides
    fun provideDispatcher(): MyDispatchers {
        return MyDispatchersImpl()
    }


    @ActivityRetainedScoped
    @Provides
    fun provideDataMapper(): DataMapper {
        return DataMapper()
    }

    @ActivityRetainedScoped
    @Provides
    fun provideUIDataMapper(): UIDataMapper {
        return UIDataMapper()
    }
}

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {
    @ViewModelScoped
    @Provides
    fun provideSearchImageUseCase(repository: SearchRepository): SearchImageUseCase {
        return SearchImageUseCase(repository)
    }
}

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class AppBinding {
    @Binds
    abstract fun bindRemoteDataSource(dataSource: RemoteDataSourceImpl):
            RemoteDataSource

    @ActivityRetainedScoped
    @Binds
    abstract fun bindSearchRepository(repo: SearchRepositoryImpl): SearchRepository
}