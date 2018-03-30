package cz.levinzonr.filemanager.model

class File(val path: String, val name: String, val isDirectory: Boolean){

    companion object {
        fun fromFile(file: java.io.File) : File {
            return File(file.absolutePath, file.name, file.isDirectory)
        }
    }

    override fun toString(): String {
        return "File(path='$path', name='$name', isDirectory=$isDirectory)"
    }

}