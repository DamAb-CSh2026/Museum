package com.example.museum

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
class MainActivity : AppCompatActivity() {

    private lateinit var drawer: DrawerLayout
    private lateinit var navView: NavigationView

    private var currentSectionTitle: String = "Экспозиции"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        drawer = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.navView)
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)

        val contentRoot = findViewById<View>(R.id.contentRoot)
        ViewCompat.setOnApplyWindowInsetsListener(contentRoot) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener { item ->
            openSection(item.itemId)
            drawer.closeDrawer(GravityCompat.START)
            true
        }

        val backCallback = object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {
                drawer.closeDrawer(GravityCompat.START)
            }
        }
        onBackPressedDispatcher.addCallback(this, backCallback)

        drawer.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
            override fun onDrawerOpened(drawerView: View) { backCallback.isEnabled = true }
            override fun onDrawerClosed(drawerView: View) { backCallback.isEnabled = false }
        })

        if (savedInstanceState == null) {
            openSection(R.id.nav_exhibitions)
        } else {
            // после поворота восстанавливаем название текущего раздела для «Поделиться»
            currentSectionTitle = supportActionBar?.title?.toString() ?: "Экспозиции"
        }
    }

    private fun openSection(itemId: Int) {
        val (title, text) = when (itemId) {
            R.id.nav_exhibitions -> "Экспозиции" to
                    "Постоянные экспозиции музея: живопись, скульптура, археология."
            R.id.nav_shows -> "Выставки" to
                    "Временные выставки и специальные проекты этого сезона."
            R.id.nav_tours -> "Экскурсии" to
                    "Групповые и индивидуальные экскурсии, расписание и запись."
            R.id.nav_tickets -> "Билеты" to
                    "Стоимость билетов, льготы и способы покупки."
            R.id.nav_route -> "Как добраться" to
                    "Адрес музея, схема проезда, ближайшие остановки транспорта."
            else -> return
        }
        currentSectionTitle = title

        supportFragmentManager.beginTransaction()
            .replace(R.id.content, PageFragment.newInstance(title, text))
            .commit()
        navView.setCheckedItem(itemId)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {

        // ОСОБОЕ ТРЕБОВАНИЕ: отправляем название текущего раздела
        R.id.action_share -> {
            val send = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "Смотрю раздел «$currentSectionTitle» в приложении «Музей»")
                putExtra(Intent.EXTRA_SUBJECT, currentSectionTitle)
            }
            startActivity(Intent.createChooser(send, getString(R.string.share_chooser)))
            true
        }

        R.id.action_search -> {
            Toast.makeText(this, "Поиск по разделу «$currentSectionTitle»", Toast.LENGTH_SHORT).show()
            true
        }

        R.id.action_about -> {
            openSection(R.id.nav_exhibitions) // или отдельный пункт «О музее»
            Toast.makeText(this, "Музей: демонстрационное приложение", Toast.LENGTH_SHORT).show()
            true
        }

        else -> super.onOptionsItemSelected(item)
    }
}