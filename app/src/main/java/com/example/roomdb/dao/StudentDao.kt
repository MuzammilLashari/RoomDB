package com.example.roomdb.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.Query
import com.example.roomdb.dataClass.StudentDataClass

@Dao
interface StudentDao {

    // hysteric ( * ) stands fro 'all'

    @Query("SELECT * FROM student_table")
    fun getAll(): List<StudentDataClass>

    // wrote 1 in the end of below query to select the very first student containing that roll number
    // for example : if i enter 5 to search among the roll number without out any limit then ->
    //<- every roll number containing 5 . by limiting it by 5 , it will show only the corresponding roll number
    @Query("SELECT * FROM student_table WHERE roll_number LIKE :roll LIMIT 1")
    suspend fun findByRoll(roll: Int): StudentDataClass

    //precautionary query just to overcome the the roll same roll numbers two time s
    @Insert(onConflict = IGNORE)
    suspend fun insert(studentDataClass: StudentDataClass)

    @Delete
    suspend fun delete(studentDataClass: StudentDataClass)

    //delete all entries from the table
    @Query("DELETE FROM student_table")
    suspend fun deleteAll()

    @Query("UPDATE student_table SET first_name=:firstName, last_name=:lastName WHERE roll_number LIKE :roll")
    suspend fun update(firstName: String, lastName: String, roll: Int)
}