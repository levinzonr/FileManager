package cz.levinzonr.filemanager.model

class MockData {

    companion object {

        fun data() : List<File> {
            val list = ArrayList<File>()
            for (i in 0..5) {
                list.add(File("/home/item$i", false))
            }

            for (i in 0..3) {
                list.add(File("/home/dir$i", true))
            }
            list.add(File("/home/dir1/item", false))
            return list
        }

    }
}