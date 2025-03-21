package com.example.myapplication.viewModel

import android.app.Application
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
    val selectedNote: StateFlow<String> = _selectFolder.asStateFlow()

    internal var _folderId: Int? = null;

    private val _hasUncategorizedNotes = MutableStateFlow(false)
    val hasUncategorizedNotes: StateFlow<Boolean> = _hasUncategorizedNotes.asStateFlow()

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

    suspend fun hasNotesWithFolderNull():Unit {
        if(noteDao.hasNotesWithFolderNull() > 0)  _hasUncategorizedNotes.value =true;
    }

    suspend fun selectFolder(folder: String) {
        _selectFolder.value = folder
        val selectedFolder: Folder? = folderDao.getFolderByName(folder)

        folderDao.cancleIsSelectFolder()

        // thư mục
        selectedFolder?.let {
            _folderId = it.id;
            folderDao.updateIsSelectFolderById(it.id)
            return;
        }
        // chưa phân loại
        _folderId = null;
    }

    suspend fun getAllFolders(): Map<String, Int> {
        val map = mutableMapOf<String, Int>()

        // thư mục tất cả
        map["Tất cả"] = noteDao.getAllNote().size

        // Chỉ lấy các thư mục thực sự từ database
        val folders = folderDao.getAll()
        folders.forEach { folder ->
            map[folder.name] = noteDao.countNotesInFolder(folder.id)
            if (folder.isSelect) _selectFolder.value = folder.name
        }

        // Đếm số ghi chú chưa phân loại
        val countNotes = noteDao.hasNotesWithFolderNull()
        if(countNotes>0) map["Chưa phân loại"] = countNotes

        _folderList.value = map
        return map
    }

    suspend fun addNewFolder(name:String){
        var folder = Folder(name= name , isSelect = false);
        folderDao.addNewFolder(folder)
        getAllFolders()
    }

}