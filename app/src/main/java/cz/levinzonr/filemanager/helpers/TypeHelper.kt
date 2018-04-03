package cz.levinzonr.filemanager.helpers

class TypeHelper {

    companion object {

        const val IMAGE = 1
        const val VIDEO = 2
        const val AUDIO = 3
        const val OTHER = 4

        fun typeFrom(string: String?): Int {
            return if (string == null) OTHER
            else when (string.substringBeforeLast("/")) {
                "image" ->  IMAGE
                "audio" ->  AUDIO
                "video" ->  VIDEO
                else ->  OTHER
            }
        }

    }

}