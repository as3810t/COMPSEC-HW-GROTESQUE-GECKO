package com.example.grotesquegecko.ui.caffsearcher

import co.zsmb.rainbowcake.withIOContext
import com.example.grotesquegecko.ui.caffsearcher.models.CaffPreview
import javax.inject.Inject

class CaffSearcherPresenter @Inject constructor() {

    suspend fun getData(): MutableList<CaffPreview> = withIOContext {
        //TODO get data from caff interactor
        return@withIOContext mutableListOf(
            CaffPreview(
                "1",
                "Grotesque",
                "https://scx2.b-cdn.net/gfx/news/2017/2-5-universityof.jpg"
            ),
            CaffPreview(
                "2",
                "Gecko",
                "https://i.insider.com/53bdb318ecad04c707b61b17?width=700&format=jpeg&auto=webp"
            )
        )
    }

}
