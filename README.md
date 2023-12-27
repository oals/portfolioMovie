# portfolioMovie
영화 예매 어플 모바일 개인 프로젝트 포트폴리오

# 소개
영화 예매, 영화 검색 등을 할 수 있는 안드로이드 어플입니다.


# 제작기간 & 참여 인원
<UL>
  <LI>2023.09.18 ~ 2023.11.02</LI>
  <LI>개인 프로젝트</LI>
</UL>



# 사용기술
![js](https://img.shields.io/badge/kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![js](https://img.shields.io/badge/androidstudio-3DDC84?style=for-the-badge&logo=androidstudio&logoColor=white)
![js](https://img.shields.io/badge/sqlite-003B57?style=for-the-badge&logo=sqlite&logoColor=white)


# 핵심 기능 및 페이지 소개


<details>
 <summary> DBHelper 
 
 </summary> 
 



   

        class DBHelper(
        context: Context?,
        name: String?,
        factory: SQLiteDatabase.CursorFactory?,
        version: Int
    ): SQLiteOpenHelper(context, name, factory, version){


        override fun onCreate(db: SQLiteDatabase) {
        var sql: String = "CREATE TABLE if not exists userInfo(" +  //유저 테이블 생성
                "userId text primary key," +
                "userNm text," +
                "userPw text," +
                "userPhone text," +
                "userEmail text," +
                "userDob text," +
                "userSex text," +
                "userSignUpDate text);"

        db.execSQL(sql)

        var sql2: String = "CREATE TABLE if not exists ticketing(" +    //예매 내역 테이블 생성
                "num INTEGER PRIMARY KEY AUTOINCREMENT," +
                "userId text," +
                "movieId text," +
                "movieNm text," +
                "movieDate text,"+
                "movieTime text,"+
                "selectSeat text,"+
                "totalAmount INTEGER,"+
                "ticketingDate text);"

        db.execSQL(sql2)


    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val sql: String = "DROP TABLE if exists userInfo"
        db.execSQL(sql)
        onCreate(db)
    }
    //회원가입
    fun userRegister(userId: String,userNm : String, userPw: String, userPhone: String,userEmail : String ,userDob : String , userSex : String, time : String) {
        val db = this.writableDatabase

        db.execSQL("INSERT INTO userInfo VALUES('$userId','$userNm', $userPw, '$userPhone', '$userEmail','$userDob','$userSex', '$time')")

    }
    //아이디 중복 검사
    fun userIdCheck(userId : String) : Boolean{
        val db = this.writableDatabase

        val cursor = db.rawQuery("SELECT * FROM userInfo where userId = '$userId'",null)


        return when{
            cursor.count > 0 -> false
            else -> true
        }

    }

    //로그인
    fun userLogin(userId:String, userPw:String) : Boolean{

        val db = this.writableDatabase

       val cursor = db.rawQuery("SELECT * FROM userInfo where userId = '$userId' and userPw = '$userPw'",null)

        return when{
            cursor.count == 0 -> false
            else -> true
        }
    }

    //유저 정보 가져오기
    fun selectUserInfo(userId : String) : Cursor {
        val db = this.writableDatabase

        val cursor = db.rawQuery("SELECT * FROM userInfo where userId = '$userId'",null)
        val i : Int = 0
        cursor.moveToFirst()


        return cursor
    }

    //예매 내역 가져오기
    fun selectTicketingInfo(userId : String) : Cursor{
        val db = this.writableDatabase

        val cursor = db.rawQuery("SELECT * FROM ticketing where userId = '$userId' order by num desc",null)

        return cursor

    }

    //예매하기
    fun insertTicketing(userId : String,movieId : String, movieNm : String, movieDate : String, movieTime : String, selectSeat : String, totalAmount : Int,ticketingDate : String){
        val db = this.writableDatabase

        db.execSQL("INSERT INTO ticketing('userId','movieId','movieNm','movieDate','movieTime','selectSeat','totalAmount','ticketingDate') " +
                "VALUES('$userId','$movieId','$movieNm', '$movieDate', '$movieTime','$selectSeat','$totalAmount','$ticketingDate') ")

    }

    //예매된 좌석 가져오기
    fun selectMovieSeat(movieNm : String, movieDate : String, movieTime : String) : Cursor{
        val db = this.writableDatabase

        val cursor = db.rawQuery("SELECT selectSeat FROM ticketing where movieNm = '$movieNm' and movieDate = '$movieDate' and movieTime = '$movieTime' ",null)

        return cursor



    }







    }







</details>



<details>
 <summary> MovieAdapter
 
 </summary> 




    class MovieAdapter(val context: Context, val movieList : ArrayList<Movie>) : BaseAdapter() {
    override fun getCount(): Int {
       return movieList.size
    }

    override fun getItem(position: Int): Any {
        return movieList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    @SuppressLint("MissingInflatedId")
    override fun getView(position: Int, contentView: View?, parent: ViewGroup?): View {

        val view :View = LayoutInflater.from(context).inflate(R.layout.list_movie_item,null)

        val movieImg = view.findViewById<ImageView>(R.id.movieImg)
        val movieNm = view.findViewById<TextView>(R.id.movieNm)
        val openDt = view.findViewById<TextView>(R.id.openDt)
        val rating = view.findViewById<TextView>(R.id.rating)
        val adult = view.findViewById<TextView>(R.id.adult)


        val movie = movieList[position]

        //장르 가져오기
        for(i in 1 .. movie.genreIdsList.size){
            if(i == 6){
                break;
            }
            view.findViewWithTag<TextView>("genreIdsItem" + i).text = movie.genreIdsList.get(i - 1)
            view.findViewWithTag<TextView>("genreIdsItem" + i).setPadding(15)
        }


        val defaultImage = R.drawable.android
        val url = movie.movieImg

        //실제 이미지 가져오기
        try{
            Glide.with(this.context)
                .load(url) // 불러올 이미지 url
                .placeholder(defaultImage) // 이미지 로딩 시작하기 전 표시할 이미지
                .error(defaultImage) // 로딩 에러 발생 시 표시할 이미지
                .fallback(defaultImage) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                .into(movieImg) // 이미지를 넣을 뷰

        }catch (e : Exception){
            Glide.with(this.context)
                .load(url) // 불러올 이미지 url
                .placeholder(defaultImage) // 이미지 로딩 시작하기 전 표시할 이미지
                .error(defaultImage) // 로딩 에러 발생 시 표시할 이미지
                .fallback(defaultImage) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                .into(movieImg) // 이미지를 넣을 뷰
        }



        movieNm.text = movie.movieNm
        openDt.text = movie.openDt
        rating.text = movie.rating + "점"
        if(movie.adult == true){
            adult.text ="청소년 관람불가"
        }else{
            adult.text = "15세 이상 관람가능"
        }

        return view

    }




    }








</details>



<details>
 <summary> TicketingAdapter
 
 </summary> 
 



    
    class TicketingAdapter(val context: Context, val ticketingList : ArrayList<Ticket>)  : BaseAdapter() {

    var queue: RequestQueue? = null

    override fun getCount(): Int {
        return ticketingList.size
    }

    override fun getItem(position: Int): Any {
        return ticketingList[position]
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    @SuppressLint("MissingInflatedId")
    override fun getView(position: Int, contentView: View?, parent: ViewGroup?): View {

        val view : View = LayoutInflater.from(context).inflate(R.layout.list_ticket_item,null)

        if (queue == null) {
            queue = Volley.newRequestQueue(this.context)
        }

        val movieNm = view.findViewById<TextView>(R.id.itemMovieNm)

        val ticket = ticketingList[position]

        if(ticket.movieId.isNotEmpty()) {
             GetOtherMovieInfoList(ticket.movieId,view)
        }

        movieNm.text = ticket.movieNm
        view.findViewById<TextView>(R.id.clickText).paintFlags = Paint.UNDERLINE_TEXT_FLAG

        return view
    }

    //영화 썸네일 이미지 가져오기
    private fun GetOtherMovieInfoList(movieId: String, view: View) {


        val url =
            " https://api.themoviedb.org/3/movie/${movieId}?api_key=b30e9c35557f3ffb06960709c88d144b&language=ko-Kr"
        val jsonRequest: JsonObjectRequest =
            JsonObjectRequest(Request.Method.GET, url, null, { response ->

                val jsonObject: JSONObject = response
                val imageUrl = jsonObject.get("poster_path").toString()

                val defaultImage = R.drawable.android

                Glide.with(this.context)
                    .load(
                        "https://image.tmdb.org/t/p/w500" + imageUrl
                    ) // 불러올 이미지 url
                    .placeholder(defaultImage) // 이미지 로딩 시작하기 전 표시할 이미지
                    .error(defaultImage) // 로딩 에러 발생 시 표시할 이미지
                    .fallback(defaultImage) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                    .into(view.findViewById<ImageView>(R.id.itemMovieImg))

            }, { error ->
                println(error.printStackTrace())
            }
            )
        queue?.add(jsonRequest)

    }



    }









</details>


<hr>

<H3>메인 페이지</H3>
<BR>


![메인-엑티비티](https://github.com/oals/portfolioMovie/assets/136543676/a96a72a0-8775-4fa3-9479-a9f275c0a868)


<br>
<br>
<details>
 <summary> 메인 화면 엑티비티 플로우 차트
 
 </summary> 
 
<img src='https://github.com/oals/portfolioMovie/assets/136543676/07933bb8-ca17-451a-868c-3756ee3f2231'>
</details>

<UL>
  <LI>TMDB API를 사용하여 최신 영화 정보를 받아와 뿌려주도록 구현했습니다.</LI>
  <LI>프래그먼트를 사용하여 메인 엑티비티의 화면을 분할했습니다.</LI>
</UL>


<br>
<br>



<HR>


<H3>영화 상세 페이지</H3>
<BR>


![영화-상세-엑티비티](https://github.com/oals/portfolioMovie/assets/136543676/ab61cfd8-7690-42d9-84d8-fcddf76a24fb)

<br>
<br>
<details>
 <summary> 영화 상세 엑티비티 플로우 차트
 
 </summary> 
 
<img src='https://github.com/oals/portfolioMovie/assets/136543676/e2008abf-54e0-41a1-9a74-81040c6edf94'>
</details>

<UL>
  <LI>TMDB API를 사용하여 터치한 영화의 정보를 받아오도록 구현했습니다.</LI>
  <LI>로그인을 하지 않았을 시 예매 버튼이 비활성화 되도록 구현했습니다.</LI>
  
</UL>

<br>
<br>





<HR>

<H3>영화 예매 페이지</H3>
<BR>



![영화-예매-엑티비티](https://github.com/oals/portfolioMovie/assets/136543676/9f6735db-5d05-4c88-ba8a-24547a393754)

<br>
<br>
<details>
 <summary> 영화 예매 엑티비티 플로우 차트
 
 </summary> 
 
<img src='https://github.com/oals/portfolioMovie/assets/136543676/e83592f6-604f-4b11-b6c8-e01f6a9ad42e'>
</details>


<UL>
  <LI>영화의 개봉일로부터 3주 후 까지 예매 가능하도록 달력의 설정을 변경하였습니다.</LI>
   <LI>각 선택 항목을 순차적으로 활성화 되도록 구현했습니다. </LI>
   <LI>날짜 선택 후 예매 시간을 선택하면 DB에서 정보를 받아와 이미 예매된 좌석을 비활성화 하도록 구현했습니다. </LI>
 <LI>날짜 혹은 시간 변경 시 선택 정보가 초기화 되도록 구현했습니다. </LI>
   <LI>예매 엑티비티와 예매 확인 엑티비티를 프래그먼트를 통해 분할하였고 아래의 버튼을 통해 이동 가능하도록 구현했습니다. </LI>
  <LI>예매 내역은 마이페이지에서 확인 할 수 있습니다.</LI>
</UL>


<br>
<br>

<HR>
<H3>영화 검색</H3>
<BR>


![영화-검색-엑티비티](https://github.com/oals/portfolioMovie/assets/136543676/d6a10e07-aad9-4910-81fb-f21c9a9efc8d)



<br>
<details>
 <summary> 영화 검색 엑티비티 플로우 차트
 
 </summary> 
 
<img src='https://github.com/oals/portfolioMovie/assets/136543676/a6738cbd-c9c5-4498-a44c-c152ade52d0e'>
</details>


<UL>
   <LI>영화 검색 시 검색어를 TMDB API로 전달해 검색어가 포한되는 영화 목록을 반환 받도록 구현했습니다.</LI>
   <LI>검색 엑티비티를 통해서는 예매가 불가능하도록 구현했습니다.</LI>
</UL>


<br>
<br>

<HR>


<H3>예매 내역</H3>
<BR>


![영화-내역-엑티비티](https://github.com/oals/portfolioMovie/assets/136543676/9aefea5a-3dff-42d1-a83e-d24d4cd685a4)




<br>
<br>
<details>
 <summary> 예매 내역 엑티비티 플로우 차트
 
 </summary> 
 
<img src='https://github.com/oals/portfolioMovie/assets/136543676/79aa01da-58ed-4ced-9e34-e1c7874928dd'>
</details>


<UL>
  <LI>마이페이지의 예매내역 엑티비티에서 예매 완료 내역을 확인 할 수 있습니다.</LI>
    <LI>결제일,예매일,예매좌석 등을 확인 할 수 있도록 구현했습니다. </LI>
</UL>


<br>
<br>


<HR>



<H3>로그인 로그아웃</H3>
<BR>

<img src="https://github.com/oals/portfolioMovie/assets/136543676/f7e17e95-006b-4b80-a25e-df0b82b238ff" height="350">

<img src="https://github.com/oals/portfolioMovie/assets/136543676/8e7173e9-fd9b-4a81-aced-a3ac695a9cba" height="350">

<BR>
<BR>
<UL>
 <LI>로그인과 로그아웃 기능을 구현 했습니다.</LI>
</UL>
<BR>
<BR>

<HR>


<H3>내 정보</H3>
<BR>


<img src="https://github.com/oals/portfolioMovie/assets/136543676/e93b83e9-08f1-45ac-a2fb-db23fc9e83fd" height="350">

<BR>
<BR>
<UL>
 <LI> 가입된 내 개인 정보를 확인 할 수 있습니다. </LI>
</UL>
<BR>
<BR>


<HR>



# 프로젝트를 통해 느낀 점과 소감

처음으로 안드로이드 어플을 만들어보았다. <BR>
안드로이드를 거의 모르는 상태에서 공부해가며 만들었기 때문에 <BR> 시간도 오래 걸렸고 코드도 정석과는 거리가 멀어 보인다. <BR>
역시 책만 보는 것 보다는 직접 하나하나 만들어가면서 공부하는 게 안드로이드를 이해 하는데에 있어 큰 도움이 되는 것 같다. <BR>








