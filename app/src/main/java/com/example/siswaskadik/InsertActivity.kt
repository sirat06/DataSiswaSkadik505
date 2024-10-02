package com.example.siswaskadik

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InsertActivity : AppCompatActivity() {

    private lateinit var edtName: EditText
    private lateinit var edtUniversity: EditText
    private lateinit var edtMajor: EditText
    private lateinit var btnSubmit: Button

    private lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_insert)

        edtName = findViewById(R.id.edtStName)
        edtUniversity = findViewById(R.id.edtUniversity)
        edtMajor = findViewById(R.id.edtMajor)
        btnSubmit = findViewById(R.id.btnSubmit)

        dbRef = FirebaseDatabase.getInstance().getReference("Students")

        btnSubmit.setOnClickListener{
            saveEmployeeData()
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun saveEmployeeData() {
        val stName = edtName.text.toString()
        val stUniversity = edtUniversity.text.toString()
        val stMajor = edtMajor.text.toString()

        if (stName.isEmpty()){
            edtName.error = "Silahkan isi nama anda"
        }

        if (stUniversity.isEmpty()){
            edtUniversity.error = "Silahkan isi universitas anda"
        }

        if (stMajor.isEmpty()){
            edtMajor.error = "Silahkan isi jurusan anda"
        }

        val stId =dbRef.push().key!!

        val students = StudentsModel(stId, stName, stUniversity, stMajor)

        dbRef.child(stId).setValue(students)
            .addOnCompleteListener{
                Toast.makeText(this,"Data inserted successfully", Toast.LENGTH_LONG).show()

                edtName.text.clear()
                edtUniversity.text.clear()
                edtMajor.text.clear()

            }.addOnFailureListener{ err ->
                Toast.makeText(this,"Error ${err.message}", Toast.LENGTH_LONG).show()
            }
    }
}