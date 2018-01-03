package rejasupotaro.rebuild.data

class GuestNameTable {

    companion object {
        private val table = mapOf(
                "Naoya Ito" to "naoya_ito",
                "伊藤直也" to "naoya_ito",
                "Matz" to "yukihiro_matz",
                "まつもとゆきひろ" to "yukihiro_matz",
                "zzak" to "_zzak",
                "gfx" to "__gfx__",
                "Kenn Ejima" to "kenn",
                "Naoki Hiroshima" to "N",
                "Gosuke Miyashita" to "gosukenator",
                "Hakuro Matsuda" to "hak",
                "Jesse Vincent" to "obra",
                "高林哲" to "")

        @JvmStatic
        fun inquire(name: String): String? {
            return if (!table.containsKey(name)) name else table[name]
        }
    }
}