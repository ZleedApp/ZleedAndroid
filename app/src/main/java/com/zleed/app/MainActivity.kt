package com.zleed.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.android.volley.VolleyError
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import com.zleed.app.classes.ZleedSingleton
import com.zleed.app.fragments.HomeFragment
import com.zleed.app.fragments.SettingsFragment
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONObject

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarLayout: AppBarLayout
    private lateinit var toolBar: MaterialToolbar

    private lateinit var navigationView: NavigationView
    private lateinit var drawerLayout: DrawerLayout

    private lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences = getSharedPreferences("ZleedAppData", MODE_PRIVATE)

        if(!sharedPreferences.getBoolean("isLoggedIn", false)) {
            val i1 = Intent()

            i1.setClass(this, AuthActivity::class.java)
            startActivity(i1)
            finish()
        }

        appBarLayout = findViewById(R.id.appBarLayout)
        toolBar      = findViewById(R.id.toolBar)

        navigationView = findViewById(R.id.navigationView)
        drawerLayout   = findViewById(R.id.drawerLayout)

        setSupportActionBar(toolBar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)

        supportActionBar!!.subtitle = "Following"

        drawerToggle = ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.app_name, R.string.app_name)

        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener(this)

        val headerView: View = navigationView.getHeaderView(0)

        val profileImage: CircleImageView = headerView.findViewById(R.id.imageViewProfile)
        val profileName: TextView         = headerView.findViewById(R.id.textViewUserName)
        val profileEmail: TextView        = headerView.findViewById(R.id.textViewUserEmail)

        val jsonObjectRequest = object : JsonObjectRequest(Method.GET, "https://zleed.ga/api/v1/user/@me", null,
            { response ->
                val responseJsonObject = JSONObject(response.toString())

                if(responseJsonObject.getInt("status") == 0) {
                    val i1 = Intent()

                    i1.setClass(this, AuthActivity::class.java)
                    startActivity(i1)
                    finish()
                } else {
                    val dataJsonObject = responseJsonObject.getJSONObject("data")

                    profileName.text = dataJsonObject.getString("userDisplayName")
                    profileEmail.text = dataJsonObject.getString("userEmail")
                }
            },
            { error ->
                // TODO: Handle error
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer ${sharedPreferences.getString("jwtToken", "missing")}"
                return headers
            }
        }

        ZleedSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

        ZleedSingleton.getInstance(this).imageLoader.get("https://cdn.discordapp.com/avatars/394888268446957569/cef07171a56563effc4e10e59bdb2a83.webp?size=512", object : ImageLoader.ImageListener {
            override fun onResponse(response: ImageLoader.ImageContainer?, isImmediate: Boolean) {
                if (response != null) {
                    profileImage.setImageBitmap(response.bitmap)
                }
            }

            override fun onErrorResponse(error: VolleyError?) {
                Log.d("imageLoader", "wtf are you doing, you either don't have internet or the url is fucking wrong, btw the error is: ${error.toString()}")
            }
        })

        /*
        val executor = Executors.newSingleThreadExecutor()
        val handler  = Handler(Looper.getMainLooper())

        var image: Bitmap?

        executor.execute {
            val imageURL = "https://cdn.discordapp.com/avatars/394888268446957569/cef07171a56563effc4e10e59bdb2a83.webp?size=512"

            try {
                val `in` = java.net.URL(imageURL).openStream()
                image = BitmapFactory.decodeStream(`in`)

                handler.post {
                    profileImage.setImageBitmap(image)
                }
            }

            catch (e: Exception) {
                e.printStackTrace()
            }
        }
        */
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val drawerFragmentTransaction = supportFragmentManager.beginTransaction()

        when(item.itemId) {
            R.id.page_following -> {
                drawerLayout.closeDrawer(GravityCompat.START)
                navigationView.setCheckedItem(R.id.page_following)

                supportActionBar!!.subtitle = item.title

                val homeFragment = HomeFragment.newInstance()

                drawerFragmentTransaction
                    .replace(R.id.fragmentContainerView, homeFragment)
                    .commit()

                return true
            }

            R.id.page_explore -> {
                drawerLayout.closeDrawer(GravityCompat.START)
                navigationView.setCheckedItem(R.id.page_explore)

                supportActionBar!!.subtitle = item.title

                return true
            }

            R.id.page_categories -> {
                drawerLayout.closeDrawer(GravityCompat.START)
                navigationView.setCheckedItem(R.id.page_categories)

                supportActionBar!!.subtitle = item.title

                return true
            }

            R.id.page_settings -> {
                drawerLayout.closeDrawer(GravityCompat.START)
                navigationView.setCheckedItem(R.id.page_settings)

                supportActionBar!!.subtitle = item.title

                val settingsFragment = SettingsFragment()

                drawerFragmentTransaction
                    .replace(R.id.fragmentContainerView, settingsFragment)
                    .commit()

                return true
            }

            R.id.page_logout -> {
                val sharedPreferences = getSharedPreferences("ZleedAppData", MODE_PRIVATE)
                val sharedPreferencesEditor = sharedPreferences.edit()

                sharedPreferencesEditor.remove("isLoggedIn")
                sharedPreferencesEditor.remove("jwtToken")
                sharedPreferencesEditor.remove("jwtExpires")

                sharedPreferencesEditor.apply()

                val i1 = Intent()

                i1.setClass(this, AuthActivity::class.java)
                startActivity(i1)
                finish()

                return true
            }
        }

        return false
    }
}
