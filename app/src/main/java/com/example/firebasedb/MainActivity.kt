package com.example.firebasedb
import CustomAdapter
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // this list contains the items in the recycler view
        val data = ArrayList<ItemsViewModel>()
        val database = Firebase.database
        // database.getReference("message").setValue("Hello, World!")
        // add data to the table
        val myRef = database.getReference("message")
        var array = listOf("Example val 1", "Example val 2")
        myRef.setValue(array)

        val button = findViewById<Button>(R.id.button3)
        button.setOnClickListener {
            val input = findViewById<EditText>(R.id.editTextTextPersonName).text
            array = array + "$input"
            myRef.setValue(array)
        }


        // create a recycler view
        val recyclerview = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerview.layoutManager = LinearLayoutManager(this)

        val adapter = CustomAdapter(data)
        recyclerview.adapter = adapter

        val button2 = findViewById<Button>(R.id.button)
        // comments inside were for debugging
        button2.setOnClickListener {
            val indexInfo = findViewById<EditText>(R.id.editTextNumberSigned).text
            val input = findViewById<EditText>(R.id.editTextTextPersonName).text
            val myRef2 = database.getReference("message")
            // Log.d(TAG, "index: $indexInfo")
            if (indexInfo.isNotEmpty()) {
                // Log.d(TAG, "1")
                val index = indexInfo.toString().toInt()
                if (index > 0) {
                    val arrayCopy = array.toMutableList()
                    // Log.d(TAG, "2")
                    arrayCopy[index - 1] = input.toString()
                    if (index <= array.size) {
                        data[index - 1] = ItemsViewModel(R.drawable.baseline_account_circle_24, "Name: $input")
                        myRef2.setValue(array)
                        // Log.d(TAG, "Value is: $array")
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }



        // this listens for changes in the database
        myRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                data.clear()
                for (itemSnapshot in snapshot.children) {
                    Log.e(TAG, "value: ${itemSnapshot.value}")
                    val item = itemSnapshot.value
                    data.add(ItemsViewModel(R.drawable.baseline_account_circle_24, "Name: $item"))
                    adapter.notifyItemChanged(data.size - 1)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }

        })
    }
}
