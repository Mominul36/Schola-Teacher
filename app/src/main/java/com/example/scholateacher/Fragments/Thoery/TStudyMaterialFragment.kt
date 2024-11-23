package com.example.scholateacher.Fragments.Thoery

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.scholateacher.DriveSetActivity
import com.example.scholateacher.Model.StudyMaterial
import com.example.scholateacher.R
import com.example.scholateacher.databinding.FragmentTStudyMaterialBinding
import com.google.firebase.database.FirebaseDatabase

class TStudyMaterialFragment : Fragment() {
    lateinit var binding: FragmentTStudyMaterialBinding
    lateinit var assignCourseId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTStudyMaterialBinding.inflate(inflater, container, false)

        assignCourseId = arguments?.getString("assignCourseId") ?: ""

        // Set up WebView settings
        val webSettings: WebSettings = binding.webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true
        binding.webView.webViewClient = WebViewClient()

        fetchDriveLink()

        binding.link.setOnClickListener {

            var intent = Intent(requireContext(),DriveSetActivity::class.java)
            intent.putExtra("assignCourseId",assignCourseId)
            startActivity(intent)
        }

        return binding.root
    }


    override fun onResume() {
        super.onResume()
        fetchDriveLink()
    }

    private fun fetchDriveLink() {
        var database = FirebaseDatabase.getInstance().getReference("StudyMaterial")

        database.child(assignCourseId).get().addOnSuccessListener {
            if (it.exists()){
                binding.webView.loadUrl(it.child("driveLink").value.toString())
               // binding.edtlink.setText(it.child("driveLink").value.toString())
            }else{
                Toast.makeText(requireContext(),"Drive is not set",Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener{
            Toast.makeText(requireContext(),"Something went wrong",Toast.LENGTH_SHORT).show()
        }





    }




}
