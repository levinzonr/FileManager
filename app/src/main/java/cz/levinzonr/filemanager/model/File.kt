package cz.levinzonr.filemanager.model

class File(val path: String, val isDirectory: Boolean){
    val name = path.split("/").last()

    companion object {
        fun fromFile(file: java.io.File) : File {
            return File(file.absolutePath, file.isDirectory)
        }
    }
}