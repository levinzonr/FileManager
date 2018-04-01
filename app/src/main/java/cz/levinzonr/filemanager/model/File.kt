package cz.levinzonr.filemanager.model

class File(val path: String, val name: String, val isDirectory: Boolean){
    val type: String = name.substringAfter(".")
    companion object {
        fun fromFile(file: java.io.File) : File {
            return File(file.absolutePath, file.name, file.isDirectory)
        }
    }

    override fun toString(): String {
        return "File(path='$path', name='$name', isDirectory=$isDirectory)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as File

        if (path != other.path) return false

        return true
    }

    override fun hashCode(): Int {
        return path.hashCode()
    }


}