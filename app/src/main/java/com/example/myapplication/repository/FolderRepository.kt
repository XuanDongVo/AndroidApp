package com.example.myapplication.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myapplication.model.Folder

@Dao
interface FolderRepository {

    @Query("SELECT * FROM Folder WHERE name =:name")
    suspend fun getFolderByName(name:String): Folder

    @Query("SELECT * FROM Folder ")
    suspend fun getAll() : List<Folder>

    @Query("UPDATE Folder SET isSelect = 1 WHERE id= :id")
    suspend fun updateIsSelectFolderById(id:Int);

    @Query("UPDATE Folder SET isSelect = 0 WHERE isSelect = 1")
    suspend fun cancleIsSelectFolder()

    @Insert
    suspend fun addNewFolder(folder: Folder)

    @Query("DELETE FROM Folder WHERE id = :folderId")
    suspend fun deleteFolder(folderId: Int)


}