package com.example.roomdb

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.roomdb.abstractMethod.AppDatabase
import com.example.roomdb.dataClass.StudentDataClass
import com.example.roomdb.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appDB: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appDB = AppDatabase.getDatabase(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //write data
        binding.btnWriteData.setOnClickListener {
            writeData()
        }

        //read data
        binding.btnReadData.setOnClickListener {
            readData()
        }

        //delete all data from DB
        binding.btnDeleteAll.setOnClickListener {
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    appDB.studentDao().deleteAll()
                }
            }
        }

        //update the existing data
        binding.btnUpdate.setOnClickListener {
            updateData()
            //enter the same roll number to update the data
            //data will be updated according to roll number only
        }
    }

    private fun updateData() {


        val firstName = binding.etFirstName.text.toString()
        val lastName = binding.etLastName.text.toString()
        val rollNo = binding.etRollNo.text.toString()

        if (firstName.isNotEmpty() && lastName.isNotEmpty() && rollNo.isNotEmpty()) {

            GlobalScope.launch {
                appDB.studentDao().update(firstName,lastName,rollNo.toInt())
            }

            binding.etFirstName.text.clear()
            binding.etLastName.text.clear()
            binding.etRollNo.text.clear()

            Toast.makeText(this, "Data Updated", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Please Enter Data", Toast.LENGTH_SHORT).show()
        }


    }

    private fun writeData() {

        val firstName = binding.etFirstName.text.toString()
        val lastName = binding.etLastName.text.toString()
        val rollNo = binding.etRollNo.text.toString()

        if (firstName.isNotEmpty() && lastName.isNotEmpty() && rollNo.isNotEmpty()) {
            val student = StudentDataClass(null, firstName, lastName, rollNo.toInt())
            GlobalScope.launch {
                appDB.studentDao().insert(student)
            }

            binding.etFirstName.text.clear()
            binding.etLastName.text.clear()
            binding.etRollNo.text.clear()

            Toast.makeText(this, "Data added", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Please Enter Data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun readData() {

        val rollNo = binding.etRollNoRead.text.toString()
        if (rollNo.isNotEmpty()) {
            lateinit var studentDataClass: StudentDataClass

            GlobalScope.launch {
                studentDataClass = appDB.studentDao().findByRoll(rollNo.toInt())
                withContext(Dispatchers.Main) {
                    displayData(studentDataClass)
                }
            }
        }

    }

    private fun displayData(studentDataClass: StudentDataClass) {

        binding.tvFirstName.text = studentDataClass.firstName
        binding.tvLastName.text = studentDataClass.lastName
        binding.tvRollNo.text = studentDataClass.rollNumber.toString()

    }
}