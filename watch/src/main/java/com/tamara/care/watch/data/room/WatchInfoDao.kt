package com.tamara.care.watch.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

import androidx.room.Query

/**
 * Created by ArtemLampa on 25.10.2021.
 */

@Dao
interface WatchInfoDao {

    @Query("DELETE FROM watchInfo")
    suspend fun nukeTable()

    @Query("SELECT * from watchInfo")
    suspend fun getAllInfo(): List<WatchInfoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(watchInfoEntity: WatchInfoEntity): Long
}