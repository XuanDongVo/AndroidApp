package com.example.myapplication.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.model.Folder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FolderViewModel (application: Application) : AndroidViewModel(application) {
    private val folderDao  = AppDatabase.getDatabase(application).folderDao();
    private val noteDao  = AppDatabase.getDatabase(application).noteDao();
    private var _selectFolder = MutableStateFlow<String>("Tất cả");
    val selectedFolder: StateFlow<String> = _selectFolder.asStateFlow()

    private val _folderId = MutableStateFlow<Int?>(null)
    val folderId: StateFlow<Int?> = _folderId.asStateFlow()

    private val _hasUncategorizedNotes = MutableStateFlow(false)

    // State để lưu danh sách thư mục
    private val _folderList = MutableStateFlow<Map<String, Int>>(emptyMap())
    val folderList: StateFlow<Map<String, Int>> = _folderList.asStateFlow()


    init {
        viewModelScope.launch {
            _hasUncategorizedNotes.value = noteDao.hasNotesWithFolderNull() > 0
            // Lấy danh sách thư mục từ DB
            val allFolders = folderDao.getAll()

            if (allFolders.isNotEmpty()) {
                val selectedFolder = allFolders.find { it.isSelect }?.name ?: "Tất cả"
                _selectFolder.value = selectedFolder

            }

            // load danh sách folder
            getAllFolders()
        }
    }

    suspend fun hasNotesWithFolderNull() {
        if(noteDao.hasNotesWithFolderNull() > 0)  _hasUncategorizedNotes.value =true;
    }

    suspend fun selectFolder(folder: String) {
        _selectFolder.value = folder
        val selectedFolder: Folder? = folderDao.getFolderByName(folder)
        Log.d("FolderViewModel", "Selected folder: $folder, folderId=${selectedFolder?.id}")
        folderDao.cancleIsSelectFolder()
        _folderId.value = selectedFolder?.id
        selectedFolder?.let {
            folderDao.updateIsSelectFolderById(it.id)
        }
        getAllFolders()
    }

    suspend fun getAllFolders(): Map<String, Int> {
        val map = mutableMapOf<String, Int>()
        map["Tất cả"] = noteDao.getAllNote().size

        val folders = folderDao.getAll()
        folders.forEach { folder ->
            map[folder.name] = noteDao.countNotesInFolder(folder.id)
        }

        val countNotes = noteDao.hasNotesWithFolderNull()
        if (countNotes > 0) map["Chưa phân loại"] = countNotes

        _folderList.value = map
        return map
    }

    suspend fun addNewFolder(name:String){
        var folder = Folder(name= name , isSelect = false);
        folderDao.addNewFolder(folder)
        getAllFolders()
    }

    suspend fun deleteFolder(folderId: Int){
        folderDao.deleteFolder(folderId)
        getAllFolders()
        _folderId.value = -1
    }
}