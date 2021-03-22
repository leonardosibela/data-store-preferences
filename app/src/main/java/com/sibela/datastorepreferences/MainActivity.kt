package com.sibela.datastorepreferences

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.lifecycleScope
import com.sibela.datastorepreferences.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var dataStore: DataStore<Preferences>

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataStore = createDataStore(name = "settings")

        setupSaveButton()
        setupReadButton()
    }

    private fun setupSaveButton() {
        binding.btnSave.setOnClickListener {
            lifecycleScope.launch {
                save(
                        binding.etSaveKey.text.toString(),
                        binding.etSaveValue.text.toString()
                )
            }
        }
    }

    private fun setupReadButton() {
        binding.btnRead.setOnClickListener {
            lifecycleScope.launch {
                val value = read(binding.etReadkey.text.toString())
                binding.tvReadValue.text = value ?: "No value found"
            }
        }
    }

    private suspend fun save(key: String, value: String) {
        val dataStoreKey = preferencesKey<String>(key)
        dataStore.edit { settings ->
            settings[dataStoreKey] = value
        }
    }

    private suspend fun read(key: String): String? {
        val dataStoreKey = preferencesKey<String>(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }
}