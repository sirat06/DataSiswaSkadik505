package com.example.siswaskadik

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FetchingActivity : AppCompatActivity() {

    private lateinit var studentRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var studentList: ArrayList<StudentsModel>
    private lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_fetching)

        studentRecyclerView = findViewById(R.id.rvStudents)
        studentRecyclerView.layoutManager = LinearLayoutManager(this)
        studentRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        studentList = arrayListOf<StudentsModel>()

        getStudentsData()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun getStudentsData(){
        studentRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Students")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                studentList.clear()
                if (snapshot.exists()){
                    for (empSnap in snapshot.children){
                        val empData = empSnap.getValue(StudentsModel::class.java)
                        studentList.add(empData!!)
                    }
                    val mAdapter = StudentsAdapter(studentList)
                    studentRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : StudentsAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@FetchingActivity,StudentsDetailActivity::class.java)

                            intent.putExtra("stId", studentList[position].stId)
                            intent.putExtra("stName", studentList[position].stName)
                            intent.putExtra("stUniversity", studentList[position].stUniversity)
                            intent.putExtra("stMajor", studentList[position].stMajor)

                            startActivity(intent)


                        }

                    })

                    studentRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}