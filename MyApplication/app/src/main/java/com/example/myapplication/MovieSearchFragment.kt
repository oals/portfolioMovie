package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ListView
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class MovieSearchFragment : Fragment() {

    var queue : RequestQueue? = null
    val movieList = arrayListOf<Movie>()
    val mutableNumberMap = mutableMapOf<String, String>()


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var view: View = inflater.inflate(R.layout.fragment_movie_search, container, false)

        val searchView : SearchView = view.findViewById<SearchView>(R.id.movieSearch)


        if(queue == null){
            queue = Volley.newRequestQueue(this.requireContext())
        }


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // 검색 버튼을 눌렀을 때 처리할 내용을 작성합니다.
                val searchView = view.findViewById<SearchView>(R.id.movieSearch)
                searchView.clearFocus()

                getGenre()
                httpConnection(view,query.toString())  // 로드 시 목록 가져오기


                view.findViewById<ListView>(R.id.searchMovieList).setOnItemClickListener{ adapterView, view, i, l ->

                    val clickMovie = movieList[i]

                    val movieIntent = Intent(activity, MovieInfoActivity::class.java)


                    movieIntent.putExtra("movieNm",clickMovie.movieNm)
                    movieIntent.putExtra("movieInfoRating",clickMovie.rating)
                    movieIntent.putExtra("movieInfoOpenDt",clickMovie.openDt)
                    movieIntent.putExtra("movieInfo",clickMovie.movieInfo)
                    movieIntent.putExtra("movieImg",clickMovie.movieImg)
                    movieIntent.putExtra("genreIdsItem",clickMovie.genreIdsList)
                    movieIntent.putExtra("movieId",clickMovie.movieId)
                    movieIntent.putExtra("searchMovieChk","search")

                    if(clickMovie.adult){
                        movieIntent.putExtra("adult","청소년 관람불가")
                    }else{
                        movieIntent.putExtra("adult","15세 이상 관람가능")
                    }

                    startActivity(movieIntent)

                }


                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // 검색어가 변경될 때마다 처리할 내용을 작성합니다.

                return true
            }
        })


        return view
    }


    private fun httpConnection(view :View,query : String){

        val url : String = "https://api.themoviedb.org/3/search/movie?api_key=b30e9c35557f3ffb06960709c88d144b&query=${query}&language=ko-KR&page=1"

        val jsonRequest: JsonObjectRequest = JsonObjectRequest(Request.Method.GET,url,null, { response ->
            parseData(response,view)

        },{error ->

        }
        )

        queue?.add(jsonRequest)
    }





    private fun parseData(jsonObject: JSONObject, view : View){


        try {
            val data = jsonObject.get("results") as JSONArray

            var list : ArrayList<ArrayList<String>> = ArrayList()
            if(data.length() == 0 ){
                val build = AlertDialog.Builder(this.requireContext())

                build.setTitle("검색 결과 없음")
                build.setMessage(
                    "다시 검색해주세요.."
                )
                build.setPositiveButton("확인",
                    DialogInterface.OnClickListener { dialog, id ->

                    })
                .setCancelable(false);
                build.show()
            }else {
                Snackbar.make(
                    view.findViewById(R.id.searchFrameLayout),
                    "${data.length()}개의 영화를 찾았습니다..",
                    Snackbar.LENGTH_LONG
                ).show()
                movieList.clear()
            }

            for(i in 0..data.length() - 1){
                var arrayList : ArrayList<String> = ArrayList()

                for(j in 0 .. data.getJSONObject(i).get("genre_ids").toString().replace("[","")
                    .replace("]","")
                    .split(",").size - 1){

                    arrayList.add(mutableNumberMap.get(data.getJSONObject(i).get("genre_ids").toString().replace("[","")
                        .replace("]","")
                        .split(",").get(j)).toString())


                }
                list.add(arrayList)

                val movie : Movie = Movie("https://image.tmdb.org/t/p/w500" + data.getJSONObject(i).get("poster_path").toString(),
                    data.getJSONObject(i).get("title").toString(),
                    "개봉일 : "+ data.getJSONObject(i).get("release_date").toString(),
                    data.getJSONObject(i).get("vote_average").toString(),
                    data.getJSONObject(i).get("overview").toString(),
                    data.getJSONObject(i).get("adult") as Boolean,
                    list.get(i),
                    data.getJSONObject(i).get("id").toString())

                movieList.add(movie)
            }

            val adapter = MovieAdapter(this.requireContext(),movieList)
            view.findViewById<ListView>(R.id.searchMovieList).adapter=adapter

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