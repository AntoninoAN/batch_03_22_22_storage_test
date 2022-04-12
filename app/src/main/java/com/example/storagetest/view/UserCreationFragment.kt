package com.example.storagetest.view

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.contentValuesOf
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.example.storagetest.databinding.UserCreationFragmentLayoutBinding
import com.example.storagetest.model.UserProfile
import com.example.storagetest.model.local.UserCredentials
import com.example.storagetest.model.local.UserTable

private const val TAG = "UserCreationFragment"//logt

class UserCreationFragment : Fragment() {
    private val KEY_STORE_USER_PROFILE: String = "KEY_STORE_USER_PROFILE"
    private var _binding: UserCreationFragmentLayoutBinding? = null
    private val binding: UserCreationFragmentLayoutBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = UserCreationFragmentLayoutBinding.inflate(inflater, container, false)
        initViews()



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        savedInstanceState?.let {
            val parcel = it.getParcelable<UserProfile>(KEY_STORE_USER_PROFILE)
            Log.d(TAG, "onCreateView: Parcel ${parcel}")
            binding.etFirstName.setText(parcel?.phone)
            binding.etLastName.setText(parcel?.address)
            binding.etPhoneNumber.setText(parcel?.firstName)
            binding.etAddress.setText(parcel?.lastName)
        }
    }

    private fun initViews() {
        binding.btnSaveData.setOnClickListener(::saveSharedPreferences)
        binding.btnReadData.setOnClickListener {
            readSharedPreferences()
            queryDB()
        }
    }

    private fun saveSharedPreferences(view: View?) {
        val editor = requireContext().getSharedPreferences(
            "UserProfile",
            Context.MODE_PRIVATE
        ).edit()

        editor.putString("SP_FIRST_NAME", binding.etFirstName.text.toString())

        editor.apply()

        requireContext().getSharedPreferences(
            "UserProfile",
            Context.MODE_PRIVATE
        ).edit {
            putString("SP_LAST_NAME", binding.etLastName.text.toString())
            apply()
        }
        storeDB(factoryUserProfile())

    }

    private fun readSharedPreferences() {
        val spFile = requireContext().getSharedPreferences("UserProfile", Context.MODE_PRIVATE)
        Log.d(TAG, "readSharedPreferences: ${spFile.getString("SP_FIRST_NAME", null)}")
    }

    /**
     * Storage Data in the SavedInstance Bundle
     * To restore after configuration change.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "onSaveInstanceState: ")
        outState.putParcelable(KEY_STORE_USER_PROFILE, factoryUserProfile())
    }

    private fun factoryUserProfile(): UserProfile {
        return UserProfile(
            binding.etFirstName.text.toString(),
            binding.etLastName.text.toString(),
            binding.etAddress.text.toString(),
            binding.etPhoneNumber.text.toString()
        )
    }

    /**
     * Obtain a ReadableDB
     */
    fun queryDB() {
        val readableDB = UserCredentials(
            requireContext().applicationContext,
            "user_credentials_db",
            1
        ).readableDatabase
        val cursorResponse =
            readableDB.query(
                UserTable.TABLE_NAME,
                arrayOf(
                    UserTable.TABLE_COLUMN_FIRST,
                    UserTable.TABLE_COLUMN_LAST,
                    UserTable.TABLE_COLUMN_PHONE
                ),
                null,
                null,
                null,
                null,
                null
            )
        val result = mutableListOf<UserProfile>()

        while (cursorResponse.moveToNext()) {
            val currentProfile = UserProfile(
                firstName = cursorResponse.getString(
                    cursorResponse.getColumnIndexOrThrow(
                        UserTable.TABLE_COLUMN_FIRST
                    )
                    ),
                lastName =
                        cursorResponse . getString (
                        cursorResponse.getColumnIndexOrThrow(
                            UserTable.TABLE_COLUMN_LAST
                        )
                        ),
                phone =
                        cursorResponse . getString (
                        cursorResponse.getColumnIndexOrThrow(
                            UserTable.TABLE_COLUMN_PHONE
                        )
                        ),
                address = ""
            )
            result.add(currentProfile)
        }
        cursorResponse.close()
        createAlertDialog(result)
    }

    private fun createAlertDialog(result: MutableList<UserProfile>) {
        AlertDialog.Builder(requireContext())
            .setTitle("Results from Cursor")
            .setMessage(
                result.map {
                    it.toString()
                }.toString()
            )
            .setPositiveButton("Accept") { dialog, viewId ->
                dialog.dismiss()
            }
            .show()
    }

    /**
     * Obtain a WritableDatabase
     */
    fun storeDB(userProfile: UserProfile) {
        val writable = UserCredentials(requireContext(),
            "user_credentials_db",
            1).writableDatabase
        // -1 if Failed transaction ||
        // N new row insert.

        val transactionRow =
            writable.insert(
                UserTable.TABLE_NAME,
                null,
                ContentValues().apply {
                    put(UserTable.TABLE_COLUMN_FIRST, userProfile.firstName)
                    put(UserTable.TABLE_COLUMN_LAST, userProfile.lastName)
                    put(UserTable.TABLE_COLUMN_PHONE, userProfile.phone)
                    put(UserTable.TABLE_COLUMN_ADDR, userProfile.address)
                }
            )

        if (transactionRow > 0)
            Toast.makeText(requireContext(),
                "Success!",
                Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(requireContext(),
                "Error",
                Toast.LENGTH_SHORT).show();
    }
}
