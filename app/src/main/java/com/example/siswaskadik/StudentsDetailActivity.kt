package com.example.siswaskadik

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.FirebaseDatabase

class StudentsDetailActivity : AppCompatActivity() {

    private lateinit var tvStId: TextView
    private lateinit var tvStName: TextView
    private lateinit var tvStUniversity: TextView
    private lateinit var tvStMajor: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_students_detail)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener{
            openUpdateDialog(
                intent.getStringExtra("stId").toString(),
                intent.getStringExtra("stName").toString()
                )
        }

        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("stId").toString()
                )
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun deleteRecord(id: String){
        val dbRef = FirebaseDatabase.getInstance().getReference("Students").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Student data deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this,FetchingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Deleting error ${error.message}", Toast.LENGTH_LONG).show()
        }

    }

    private fun initView() {
        tvStId= findViewById(R.id.tvStudentId)
        tvStName = findViewById(R.id.tvStudentName)
        tvStUniversity = findViewById(R.id.tvStudentUniversity)
        tvStMajor = findViewById(R.id.tvStudentMajor)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        tvStId.text = intent.getStringExtra("stId")
        tvStName.text = intent.getStringExtra("stName")
        tvStUniversity.text = intent.getStringExtra("stUniversity")
        tvStMajor.text = intent.getStringExtra("stMajor")

    }

    private fun openUpdateDialog(
        stId: String,
        stName: String
    ){
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog, null)

        mDialog.setView(mDialogView)
        val etStName = mDialogView.findViewById<EditText>(R.id.etStName)
        val etStUniversity = mDialogView.findViewById<EditText>(R.id.etStUniversity)
        val etStMajor = mDialogView.findViewById<EditText>(R.id.etStMajor)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        etStName.setText(intent.getStringExtra("stName").toString())
        etStUniversity.setText(intent.getStringExtra("stUniversity").toString())
        etStMajor.setText(intent.getStringExtra("stMajor").toString())

        mDialog.setTitle("Updating $stName Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateEmpData(
                stId,
                etStName.text.toString(),
                etStUniversity.text.toString(),
                etStMajor.text.toString()
            )

            Toast.makeText(applicationContext, "Student Data Updated", Toast.LENGTH_LONG).show()

            //we are setting updated data to our textviews
            tvStName.text = etStName.text.toString()
            tvStUniversity.text = etStUniversity.text.toString()
            tvStMajor.text = etStMajor.text.toString()

            alertDialog.dismiss()
        }
    }

    private fun updateEmpData(
        id: String,
        name: String,
        university: String,
        major: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Students").child(id)
        val empInfo = StudentsModel(id, name, university, major)
        dbRef.setValue(empInfo)
    }

}


