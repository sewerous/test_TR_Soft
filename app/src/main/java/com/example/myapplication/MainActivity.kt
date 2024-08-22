package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.AppDatabase
import com.example.Password
import com.example.myapplication.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.reflect.KFunction

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}


class LoadingFragment : Fragment(R.layout.fragment_loading) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Имитируем загрузку данных
        Handler(Looper.getMainLooper()).postDelayed({
            // Переход на экран словарей после загрузки
            findNavController().navigate(R.id.action_loadingFragment_to_dictionaryFragment)
        }, 3000) // 3 секунды задержки
    }
}

class DictionaryFragment : Fragment(R.layout.fragment_dictionary) {

    private lateinit var db: AppDatabase
    private lateinit var generateButton: Button
    private lateinit var loadFileButton: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        db = Room.databaseBuilder(requireContext(), AppDatabase::class.java, "passwords-db").build()

        generateButton = view.findViewById(R.id.generateButton)
        loadFileButton = view.findViewById(R.id.loadFileButton)

        generateButton.setOnClickListener {
            generatePassword()
        }

        loadFileButton.setOnClickListener {
            loadPasswordsFromFile()
        }
    }


    private fun generatePassword() {
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()"
        val password = (1..12)
            .map { charset.random() }
            .joinToString("")
        val entropy = calculateEntropy(password, charset)

        // Сохранение пароля в БД
        lifecycleScope.launch {
            db.passwordDao().insert(Password(password = password, entropy = entropy, charsetUsed = charset))
        }

        Toast.makeText(requireContext(), "Пароль сгенерирован: $password (Энтропия: $entropy)", Toast.LENGTH_SHORT).show()
    }

    private fun calculateEntropy(password: String, charset: String): Double {
        val entropy = password.length * Math.log(charset.length.toDouble()) / Math.log(2.0)
        return String.format("%.2f", entropy).toDouble()
    }

    private fun loadPasswordsFromFile() {
        // Код для загрузки файла и сохранения паролей в БД
    }
}

private fun Room.databaseBuilder(context: Context, klass: Class<AppDatabase>, name: String): Unit =
    Unit

private fun Fragment.onViewCreated(view: View) {
    TODO("Not yet implemented")
}

private fun Any.insert(password: Any) {
    TODO("Not yet implemented")
}

private fun Any.passwordDao(): Any {
    TODO("Not yet implemented")
}

class PasswordListFragment() : Fragment(R.layout.fragment_password_list), Parcelable {

    private lateinit var recyclerView: RecyclerView
    private lateinit var passwordAdapter: PasswordAdapter
    private lateinit var db: AppDatabase

    constructor(parcel: Parcel) : this() {
        passwordAdapter = parcel.readParcelable(PasswordAdapter::class.java.classLoader)!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        db = Room.databaseBuilder(requireContext(), AppDatabase::class.java, "passwords-db").build()
        recyclerView = view.findViewById(R.id.recyclerView)
        passwordAdapter = PasswordAdapter(::onPasswordClicked)


        lifecycleScope.launch {
            val passwords = db.passwordDao().getAllPasswords()
            passwordAdapter.submitList(passwords)
        }
    }

    private fun PasswordAdapter(kFunction1: KFunction<Unit>): PasswordAdapter {
        TODO("Not yet implemented")
    }

    private fun onPasswordClicked(password: Password) {
        // Код для копирования пароля в буфер обмена или удаления
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(passwordAdapter, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PasswordListFragment> {
        override fun createFromParcel(parcel: Parcel): PasswordListFragment {
            return PasswordListFragment(parcel)
        }

        override fun newArray(size: Int): Array<PasswordListFragment?> {
            return arrayOfNulls(size)
        }
    }
}

private fun Parcel.writeParcelable(passwordAdapter: PasswordAdapter, flags: Int) {
    TODO("Not yet implemented")
}

private fun Any.getAllPasswords(): Any {
    TODO("Not yet implemented")
}

    fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    fun writeToParcel(dest: Parcel, flags: Int) {
        TODO("Not yet implemented")
    }

    object CREATOR : Parcelable.Creator<PasswordAdapter> {
        override fun createFromParcel(parcel: Parcel): PasswordAdapter {
            return PasswordAdapter(parcel)
        }

        override fun newArray(size: Int): Array<PasswordAdapter?> {
            return arrayOfNulls(size)
        }
    }


class PasswordAdapter(private val onClick: Parcel) : ListAdapter<Password, PasswordViewHolder>(PasswordDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PasswordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_password, parent, false)
        return PasswordViewHolder(view, onClick)
    }

    private fun PasswordViewHolder(itemView: View?, onClick: Parcel): PasswordViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: PasswordViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class PasswordViewHolder(itemView: View, private val onClick: (Password) -> Unit) : RecyclerView.ViewHolder(itemView) {
    fun bind(password: Password) {
        itemView.findViewById<TextView>(R.id.passwordTextView).text = password.password
        itemView.setOnClickListener { onClick(password) }
    }
}

class PasswordDiffCallback : DiffUtil.ItemCallback<Password>() {
    override fun areItemsTheSame(oldItem: Password, newItem: Password): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Password, newItem: Password): Boolean = oldItem == newItem
}

private fun exportPasswordsToFile(folderName: String, lifecycleScope: Any) {
    val passwords = lifecycleScope.launch {
        if (folderName.isNotEmpty()) {
            val db = null
            db?.passwordDao()?.getPasswordsInFolder(folderName)
        } else {
            val db = null
            db?.passwordDao()?.getAllPasswords()
        }
    }

    // Код для создания файла и записи паролей
}

private fun Any.getPasswordsInFolder(name: Any) {
    TODO("Not yet implemented")
}

private fun Any.launch(block: suspend CoroutineScope.() -> Unit): Any {
    TODO("Not yet implemented")
}
