package com.tkhskt.theremin.feature.license.data

import android.content.Context
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.tkhskt.theremin.feature.license.data.model.ArtifactResult
import com.tkhskt.theremin.feature.license.R
import java.io.BufferedReader
import java.io.InputStreamReader

interface ArtifactsRepository {
    fun getArtifacts(): List<ArtifactResult>?
}

class ArtifactsRepositoryImpl(
    private val context: Context,
    private val moshi: Moshi,
) : ArtifactsRepository {

    override fun getArtifacts(): List<ArtifactResult>? {
        val stream = context.resources.openRawResource(R.raw.artifacts)
        val inputStreamReader = InputStreamReader(stream)
        val bufferedReader = BufferedReader(inputStreamReader)
        val stringBuilder = StringBuilder()
        bufferedReader.readLines().forEach {
            stringBuilder.append(it)
        }
        stream.close()
        val type = Types.newParameterizedType(List::class.java, ArtifactResult::class.java)
        val listAdapter: JsonAdapter<List<ArtifactResult>> = moshi.adapter(type)
        return listAdapter.fromJson(stringBuilder.toString())
    }
}
