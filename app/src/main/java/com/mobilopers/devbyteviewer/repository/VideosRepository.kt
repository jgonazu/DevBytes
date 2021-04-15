 /*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.mobilopers.devbyteviewer.repository

 import androidx.lifecycle.LiveData
 import androidx.lifecycle.Transformations
 import com.mobilopers.devbyteviewer.database.VideoDatabase
 import com.mobilopers.devbyteviewer.database.asDomainModel
 import com.mobilopers.devbyteviewer.domain.Video
 import com.mobilopers.devbyteviewer.network.Network
 import com.mobilopers.devbyteviewer.network.asDatabaseModel
 import kotlinx.coroutines.Dispatchers
 import kotlinx.coroutines.withContext

 class VideosRepository (val dataBase: VideoDatabase) {

     val videos: LiveData<List<Video>> = Transformations.map(dataBase.videoDao.getVideos()) {
         it.asDomainModel()
     }

     suspend fun refreshVideos() {
         withContext(Dispatchers.IO) {
             val playlist = Network.devbytes.getPlaylist().await()
             dataBase.videoDao.addVideo(*playlist.asDatabaseModel())
         }
     }
 }