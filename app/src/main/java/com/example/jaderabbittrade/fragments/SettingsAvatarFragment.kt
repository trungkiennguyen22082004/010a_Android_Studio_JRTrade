package com.example.jaderabbittrade.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.example.jaderabbittrade.Constants
import com.example.jaderabbittrade.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class SettingsAvatarFragment : Fragment()
{
    private lateinit var _auth : FirebaseAuth
    private lateinit var _firestore: FirebaseFirestore

    private var _currentChosenAvatarIndex: Int = 1

    private val _avatarBackgrounds: MutableList<ImageView> = mutableListOf()
    private val _avatarButtons: MutableList<ImageButton> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        // Initialize FireBase Auth and Firestore
        _auth = FirebaseAuth.getInstance()
        _firestore = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings_avatar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the views
        _avatarBackgrounds.add(view.findViewById(R.id.avatar_1_background))
        _avatarBackgrounds.add(view.findViewById(R.id.avatar_2_background))
        _avatarBackgrounds.add(view.findViewById(R.id.avatar_3_background))
        _avatarBackgrounds.add(view.findViewById(R.id.avatar_4_background))
        _avatarBackgrounds.add(view.findViewById(R.id.avatar_5_background))
        _avatarBackgrounds.add(view.findViewById(R.id.avatar_6_background))
        _avatarBackgrounds.add(view.findViewById(R.id.avatar_7_background))
        _avatarBackgrounds.add(view.findViewById(R.id.avatar_8_background))
        _avatarBackgrounds.add(view.findViewById(R.id.avatar_9_background))

        _avatarButtons.add(view.findViewById(R.id.avatar_1_btn))
        _avatarButtons.add(view.findViewById(R.id.avatar_2_btn))
        _avatarButtons.add(view.findViewById(R.id.avatar_3_btn))
        _avatarButtons.add(view.findViewById(R.id.avatar_4_btn))
        _avatarButtons.add(view.findViewById(R.id.avatar_5_btn))
        _avatarButtons.add(view.findViewById(R.id.avatar_6_btn))
        _avatarButtons.add(view.findViewById(R.id.avatar_7_btn))
        _avatarButtons.add(view.findViewById(R.id.avatar_8_btn))
        _avatarButtons.add(view.findViewById(R.id.avatar_9_btn))

        _currentChosenAvatarIndex = if (Constants.avatarID != 0)
        {
            Constants.avatarID
        }
        else
        {
            1
        }
        this.updateChosenAvatar()

        for (index in 1 .. _avatarButtons.size)
        {
            _avatarButtons[index - 1].setOnClickListener()
            {
                _currentChosenAvatarIndex = index
                this.updateChosenAvatar()
            }
        }

        val saveButton: Button = view.findViewById(R.id.save_button)
        saveButton.setOnClickListener()
        {
            Constants.avatarID = _currentChosenAvatarIndex

            // Update in the Firestore database
            val userID: String = _auth.currentUser?.uid ?: ""
            val userDocumentReference: DocumentReference = _firestore.collection("users").document(userID)
            userDocumentReference.update("avatarID", Constants.avatarID)

            findNavController().navigate(R.id.action_settingsAvatarFragment_to_settingsFragment)
        }
    }

    private fun updateChosenAvatar()
    {
        for (index in 1.._avatarBackgrounds.size)
        {
            if (index == _currentChosenAvatarIndex)
            {
                _avatarBackgrounds[index - 1].visibility = View.VISIBLE
            }
            else
            {
                _avatarBackgrounds[index - 1].visibility = View.INVISIBLE
            }
        }
    }
}