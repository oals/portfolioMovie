package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.databinding.ActivityMovieBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date


class MovieActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,BottomNavigationView.OnNavigationItemSelectedListener  {

    var queue : RequestQueue? = null
    val movieList = arrayListOf<Movie>()
    lateinit var dataText : TextView
    lateinit var drawerLayout : DrawerLayout


    private val binding by lazy {
        ActivityMovieBinding.inflate(layoutInflater)
    }

    @SuppressLint("ResourceType")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.toolbar_menu,menu)
        val search = menu?.findItem(androidx.appcompat.R.id.search_badge)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item!!.itemId){
            android.R.id.home->{ // 메뉴 버튼

                drawerLayout.openDrawer(GravityCompat.START)
                return true

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val nextIntent : Intent

        when(item.itemId){
            R.id.loginItem-> {
                nextIntent = Intent(this,LoginActivity::class.java)
                startActivity(nextIntent)
            }
            R.id.logoutItem-> {
                nextIntent = Intent(this,LogoutActivity::class.java)
                startActivity(nextIntent)
            }
            R.id.registerItem-> {
                nextIntent = Intent(this,RegisterActivity::class.java);
                startActivity(nextIntent)
            }

            R.id.item1 ->{

                item.isChecked = true
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.movieFragmentContainerView,MovieListFragment())
                transaction.commit()
            }
            R.id.item2 -> {
                item.isChecked = true
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.movieFragmentContainerView,MovieSearchFragment())
                transaction.commit()
            }
            R.id.item3 -> {
                val prefs : SharedPreferences = this.getSharedPreferences("Prefs", Context.MODE_PRIVATE)
                if (prefs.getString("userId",null) != null) {
                    item.isChecked = true
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.movieFragmentContainerView,MyPageFragment())
                    transaction.commit()
                }else{
                    val build = AlertDialog.Builder(this)
                    build.setMessage(
                        "로그인이 필요합니다."
                    )
                    build.setPositiveButton("확인",
                        DialogInterface.OnClickListener{ dialog, id ->
                            val nextIntent = Intent(this,LoginActivity::class.java)
                            startActivity(nextIntent)
                        })
                    build.setCancelable(false);
                    build.show()

                }




            }
        }
        drawerLayout.closeDrawers()
        return false

    }



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowCustomEnabled(true);
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)  // 왼쪽 버튼 사용 여부 true
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.baseline_format_list_bulleted_24)  // 왼쪽 버튼 이미지 설정

        supportActionBar!!.title = "TestApp"

        drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)

        val navigationView : NavigationView = findViewById(R.id.nav)
        navigationView.setNavigationItemSelectedListener(this)

        val bottomNavigationView : BottomNavigationView = findViewById(R.id.bottomNav)
        bottomNavigationView.setOnNavigationItemSelectedListener(this)





        val prefs : SharedPreferences = this.getSharedPreferences("Prefs", Context.MODE_PRIVATE)
        val menu = navigationView.menu

        val headerView = navigationView.getHeaderView(0)
        if (prefs.getString("userId",null) != null) {

            headerView.findViewById<TextView>(R.id.menuUserId).text = prefs.getString("userId",null) + "님"
            menu.findItem(R.id.loginItem).isVisible = false
            menu.findItem(R.id.registerItem).isVisible = false

            menu.findItem(R.id.logoutItem).isVisible = true
        }else{

            headerView.findViewById<TextView>(R.id.menuUserId).text = "로그인이 필요합니다"
            menu.findItem(R.id.loginItem).isVisible = true
            menu.findItem(R.id.registerItem).isVisible = true

            menu.findItem(R.id.logoutItem).isVisible = false
        }




        ChangeFragment(0)



    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun ChangeFragment(idx : Int) {

        val movieListFragment = MovieListFragment()   //예매 하려는 영화의 정보 및 동의사항

        supportFragmentManager.beginTransaction().add(R.id.movieFragmentContainerView, movieListFragment)
            .commit()
//        val transaction = supportFragmentManager.beginTransaction()
//
//        transaction.commit()


    }




}


