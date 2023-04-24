package com.example.pixabayseachimage.data.datasources

import com.example.pixabayseachimage.data.remote.reponse.SearchAPIResponse
import com.google.gson.Gson

class FakeRemoteDataSourceImpl : RemoteDataSource {

    override suspend fun searchImage(
        searchKey: String,
        pageNo: Int
    ): SearchAPIResponse {
        val response = Gson().fromJson(
            responses[pageNo - 1], SearchAPIResponse::class
                .java
        )
        if(pageNo==1){
            val hits = mutableListOf<SearchAPIResponse.Hit>()
            for(i in 0..4){
                hits.addAll(response.hits)
            }
            return response.copy(hits = hits)
        }
        return response
    }

    private val responses = arrayOf(
        "{\n" +
                "  \"total\": 33630,\n" +
                "  \"totalHits\": 500,\n" +
                "  \"hits\": [\n" +
                "    {\n" +
                "      \"id\": 1085063,\n" +
                "      \"pageURL\": \"https://pixabay.com/photos/vegetables-fruits-food-ingredients-1085063/\",\n" +
                "      \"type\": \"photo\",\n" +
                "      \"tags\": \"vegetables, fruits, food\",\n" +
                "      \"previewURL\": \"https://cdn.pixabay.com/photo/2015/12/09/17/11/vegetables-1085063_150.jpg\",\n" +
                "      \"previewWidth\": 150,\n" +
                "      \"previewHeight\": 98,\n" +
                "      \"webformatURL\": \"https://pixabay.com/get/g3ad427a251cc249da5b9996ad9d8966b022eb50c098a57cbe2118c45ef17d9bd5a0d360401ead96a53fade1e1341580ed9916f5050e758cc074ed423da2dfb8e_640.jpg\",\n" +
                "      \"webformatWidth\": 640,\n" +
                "      \"webformatHeight\": 422,\n" +
                "      \"largeImageURL\": \"https://pixabay.com/get/ga6b3aca7b4d014015a7f48c03ae4a03c6f7c43dd0f8fe4a7e3eabfc24a417d6edfaeb613135dfbda588199d39304a3fba43660314319ff15119a2d05eefffe5b_1280.jpg\",\n" +
                "      \"imageWidth\": 4050,\n" +
                "      \"imageHeight\": 2676,\n" +
                "      \"imageSize\": 1814596,\n" +
                "      \"views\": 581023,\n" +
                "      \"downloads\": 314598,\n" +
                "      \"collections\": 924,\n" +
                "      \"likes\": 1075,\n" +
                "      \"comments\": 268,\n" +
                "      \"user_id\": 1643989,\n" +
                "      \"user\": \"dbreen\",\n" +
                "      \"userImageURL\": \"https://cdn.pixabay.com/user/2015/11/14/15-22-41-548_250x250.jpg\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 2548827,\n" +
                "      \"pageURL\": \"https://pixabay.com/photos/macarons-raspberries-pastries-2548827/\",\n" +
                "      \"type\": \"photo\",\n" +
                "      \"tags\": \"macarons, raspberries, pastries\",\n" +
                "      \"previewURL\": \"https://cdn.pixabay.com/photo/2017/07/28/14/29/macarons-2548827_150.jpg\",\n" +
                "      \"previewWidth\": 150,\n" +
                "      \"previewHeight\": 90,\n" +
                "      \"webformatURL\": \"https://pixabay.com/get/g06a5c0d0fb5b5168e219b54862de15c7284e12d11c6eb9cb7ac46ff72c409ed2647e26a7326b450efd8b238a2a354d166c3e737ebc6ac7def41c8cd8c68d02ce_640.jpg\",\n" +
                "      \"webformatWidth\": 640,\n" +
                "      \"webformatHeight\": 384,\n" +
                "      \"largeImageURL\": \"https://pixabay.com/get/gb4543f453d179b1ff59200c202c5b01eb29d6d24e41aed8589314c345ac374958d52af678335da5481d7af14c42367100916e5e56d024636fc4e96c2f5b2f42c_1280.jpg\",\n" +
                "      \"imageWidth\": 5184,\n" +
                "      \"imageHeight\": 3115,\n" +
                "      \"imageSize\": 2567755,\n" +
                "      \"views\": 379801,\n" +
                "      \"downloads\": 268887,\n" +
                "      \"collections\": 1144,\n" +
                "      \"likes\": 996,\n" +
                "      \"comments\": 92,\n" +
                "      \"user_id\": 2364555,\n" +
                "      \"user\": \"NoName_13\",\n" +
                "      \"userImageURL\": \"https://cdn.pixabay.com/user/2022/12/12/07-40-59-226_250x250.jpg\"\n" +
                "    }\n" +
                "  ]\n" +
                "}",
        "{\n" +
                "  \"total\": 33630,\n" +
                "  \"totalHits\": 500,\n" +
                "  \"hits\": [\n" +
                "    {\n" +
                "      \"id\": 256261,\n" +
                "      \"pageURL\": \"https://pixabay.com/photos/apple-books-still-life-fruit-food-256261/\",\n" +
                "      \"type\": \"photo\",\n" +
                "      \"tags\": \"apple, books, still life\",\n" +
                "      \"previewURL\": \"https://cdn.pixabay.com/photo/2014/02/01/17/28/apple-256261_150.jpg\",\n" +
                "      \"previewWidth\": 150,\n" +
                "      \"previewHeight\": 99,\n" +
                "      \"webformatURL\": \"https://pixabay.com/get/gde105093643fed13f5b587defcf6a0d1e4fbf60cb12705f08ecec35d31959e33fbde740d88ead2dc8bf45e9e4dfadb8f_640.jpg\",\n" +
                "      \"webformatWidth\": 640,\n" +
                "      \"webformatHeight\": 423,\n" +
                "      \"largeImageURL\": \"https://pixabay.com/get/g9d9d41443e72974c05b7e90ddbebde6390c53c3918af9c89d21ae2e396152ef7719d780a755728df91311737b3e336ffa9d66a9024018f10ea79d296e5b7d386_1280.jpg\",\n" +
                "      \"imageWidth\": 4928,\n" +
                "      \"imageHeight\": 3264,\n" +
                "      \"imageSize\": 2987083,\n" +
                "      \"views\": 521562,\n" +
                "      \"downloads\": 272268,\n" +
                "      \"collections\": 948,\n" +
                "      \"likes\": 972,\n" +
                "      \"comments\": 246,\n" +
                "      \"user_id\": 143740,\n" +
                "      \"user\": \"jarmoluk\",\n" +
                "      \"userImageURL\": \"https://cdn.pixabay.com/user/2019/09/18/07-14-26-24_250x250.jpg\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 1995056,\n" +
                "      \"pageURL\": \"https://pixabay.com/photos/oranges-citrus-fruits-fruits-1995056/\",\n" +
                "      \"type\": \"photo\",\n" +
                "      \"tags\": \"oranges, citrus fruits, fruits\",\n" +
                "      \"previewURL\": \"https://cdn.pixabay.com/photo/2017/01/20/15/06/oranges-1995056_150.jpg\",\n" +
                "      \"previewWidth\": 150,\n" +
                "      \"previewHeight\": 97,\n" +
                "      \"webformatURL\": \"https://pixabay.com/get/g84ac5fa57b5a66776efb7323a8c7fbd53c795e5c2f71f58f2ba14274593d20e7813e0750416329818fc10d5f388e2a34c89707d3ff83ccb7fbee08f983eee99b_640.jpg\",\n" +
                "      \"webformatWidth\": 640,\n" +
                "      \"webformatHeight\": 417,\n" +
                "      \"largeImageURL\": \"https://pixabay.com/get/gcf63ef31419af3e7e83644821ecf9ae083068b7bf3005de15e932f6ebecbb3a5e7c9df092648e08cfe360544fc8bc0fc452b7924ff744a263420da3aeefd7ea4_1280.jpg\",\n" +
                "      \"imageWidth\": 4980,\n" +
                "      \"imageHeight\": 3245,\n" +
                "      \"imageSize\": 2515891,\n" +
                "      \"views\": 415577,\n" +
                "      \"downloads\": 293080,\n" +
                "      \"collections\": 967,\n" +
                "      \"likes\": 979,\n" +
                "      \"comments\": 149,\n" +
                "      \"user_id\": 2364555,\n" +
                "      \"user\": \"NoName_13\",\n" +
                "      \"userImageURL\": \"https://cdn.pixabay.com/user/2022/12/12/07-40-59-226_250x250.jpg\"\n" +
                "    }\n" +
                "  ]\n" +
                "}",
        ""
    )
}
