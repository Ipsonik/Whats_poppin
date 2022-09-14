package com.example.whatspopin.repository

import com.example.whatspopin.models.PopItem
import org.jsoup.Jsoup
import java.io.IOException

class FunkoRepository {

    fun getFunkoPops(): MutableList<PopItem> {
        val listData = mutableListOf<PopItem>()
        try {
            val url = "https://funko.fandom.com/wiki/Pop!_Animation"
            val doc = Jsoup.connect(url).get()
            val pops = doc.select(".wikitable:first-of-type tr")

            for (i: Int in 1 until 200) {

                val name = pops.select("th:nth-last-of-type(1)")
                    .eq(i)
                    .text()

                val imgUrl = pops.select("td:nth-of-type(1)").eq(i - 1)

                val img = if (imgUrl.toString() == "<td> </td>") {
                    "https://www.vectorkhazana.com/assets/images/products/Funko_Pup.png"
                } else {
                    imgUrl.select("a").attr("href")
                }


                val series = pops.select("td:nth-of-type(4)")
                    .eq(i - 1)
                    .text()

                listData.add(PopItem(i, name, img, series))
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return listData
    }
}

