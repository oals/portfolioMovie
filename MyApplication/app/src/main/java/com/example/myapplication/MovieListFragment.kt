package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class MovieListFragment : Fragment() {
    var page : Int = 0
    var queue : RequestQueue? = null
    val movieList = arrayListOf<Movie>()
    val mutableNumberMap = mutableMapOf<String, String>()
    var resultCount : Int = 1
    var list : ArrayList<ArrayList<String>> = ArrayList()

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var view : View = inflater.inflate(R.layout.fragment_movie_list, container, false)


        if(queue == null){
            queue = Volley.newRequestQueue(this.requireContext())
        }


        getGenre()

        httpConnection(view)  // 로드 시 목록 가져오기



        view.findViewById<ListView>(R.id.movieList).setOnItemClickListener{ adapterView, view, i, l ->
            val clickMovie = movieList[i]

            val movieIntent = Intent(activity, MovieInfoActivity::class.java)

            movieIntent.putExtra("movieNm",clickMovie.movieNm)
            movieIntent.putExtra("movieInfoRating",clickMovie.rating)
            movieIntent.putExtra("movieInfoOpenDt",clickMovie.openDt)
            movieIntent.putExtra("movieInfo",clickMovie.movieInfo)
            movieIntent.putExtra("movieImg",clickMovie.movieImg)
            movieIntent.putExtra("genreIdsItem",clickMovie.genreIdsList)
            movieIntent.putExtra("movieId",clickMovie.movieId)
            movieIntent.putExtra("searchMovieChk","noSearch")


            if(clickMovie.adult){
                movieIntent.putExtra("adult","청소년 관람불가")
            }else{
                movieIntent.putExtra("adult","15세 이상 관람가능")
            }

            startActivity(movieIntent)

        }


        return view


    }



    private fun httpConnection(view :View){

        if(movieList.size < 10 ) {

            if(page < resultCount) {
                page++
                val url: String =
                    "https://api.themoviedb.org/3/movie/upcoming?api_key=b30e9c35557f3ffb06960709c88d144b&language=ko-KR&page=$page"
                val jsonRequest: JsonObjectRequest =
                    JsonObjectRequest(Request.Method.GET, url, null, { response ->
                        parseData(response, view)

                    }, { error ->
                    }
                    )

                queue?.add(jsonRequest)
            }
        }
    }



    private fun parseData(jsonObject: JSONObject,view : View){

        try {


                val data = jsonObject.get("results") as JSONArray

                resultCount = Integer.parseInt(jsonObject.get("total_pages").toString())


                var jsonList: ArrayList<Movie>  = ArrayList()

                for (i in 0..11) {
                    var arrayList: ArrayList<String> = ArrayList()


                    for (j in 0..<data.getJSONObject(i).get("genre_ids").toString().replace("[", "")
                        .replace("]", "")
                        .split(",").size) {

                        arrayList.add(
                            mutableNumberMap.get(
                                data.getJSONObject(i).get("genre_ids").toString().replace("[", "")
                                    .replace("]", "")
                                    .split(",").get(j)
                            ).toString()
                        )

                    }
                    list.add(arrayList)

                    val movie: Movie = Movie(
                        "https://image.tmdb.org/t/p/w500" + data.getJSONObject(i).get("poster_path")
                            .toString(),
                        data.getJSONObject(i).get("title").toString(),
                        data.getJSONObject(i).get("release_date").toString() + " 개봉",
                        data.getJSONObject(i).get("vote_average").toString(),
                        data.getJSONObject(i).get("overview").toString(),
                        data.getJSONObject(i).get("adult") as Boolean,
                        list.get(i),
                        data.getJSONObject(i).get("id").toString()
                    )

                    jsonList.add(movie)
                }



                for (i in 0..11) {
                    val dateString = data.getJSONObject(i).get("release_date").toString()
                    val dateString2 = "2023-10-15"  // 코드 변경 필요

                    val format = SimpleDateFormat("yyyy-MM-dd", Locale.US)

                    val firstDate = format.parse(dateString)
                    val secondDate = format.parse(dateString2)


                    when {
                        firstDate.after(secondDate) -> {
                            if(data.getJSONObject(i).get("poster_path") != null){
                                println( ""+ data.getJSONObject(i).get("title") + " : " + data.getJSONObject(i).get("poster_path"))
                                movieList.add(jsonList.get(i))
                            }else{
                                println("이미지 없음")
                            }

                        }


                        firstDate.before(secondDate) -> {

                        }

                        firstDate == secondDate -> {
                            movieList.add(jsonList.get(i))
                        }
                    }
                }

            if(movieList.size <= 10){
                httpConnection(view)
            }

                val adapter = MovieAdapter(this.requireContext(), movieList)
                view.findViewById<ListView>(R.id.movieList).adapter = adapter


        }catch (e: JSONException){
            e.printStackTrace()
        }
    }
    private fun getGenre() {

        val url = "https://api.themoviedb.org/3/genre/movie/list?api_key=b30e9c35557f3ffb06960709c88d144b&language=ko-KR"
        val jsonRequest: JsonObjectRequest = JsonObjectRequest(Request.Method.GET,url,null, { response ->

            val jsonObject : JSONObject = response
            val data = jsonObject.get("genres") as JSONArray


            for(i in 0..18){
                mutableNumberMap[data.getJSONObject(i).get("id").toString()] = data.getJSONObject(i).get("name").toString()
            }
        },{error ->
            println(error.printStackTrace())
        }
        )
        queue?.add(jsonRequest)
    }





    }