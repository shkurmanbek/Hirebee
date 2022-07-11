package com.example.hirebee

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hirebee.databinding.RowEmployerBinding
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class AdapterEmployer : RecyclerView.Adapter<AdapterEmployer.HolderEmployer>{
    private var context: Context

    private var employersArrayList: ArrayList<ModelEmployer>

    private lateinit var binding: RowEmployerBinding

    constructor(context: Context, clothesArrayList: ArrayList<ModelEmployer>) : super() {
        this.context = context
        this.employersArrayList = clothesArrayList
    }

    inner class HolderEmployer(itemView: View): RecyclerView.ViewHolder(itemView){
        val imageView = binding.imageView
        val companyTv = binding.titleTv
        val categoryTv = binding.categoryTv
        val timeTv = binding.timeTv
        val dateTv = binding.dateTv
        val priceTv = binding.priceTv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderEmployer {
        binding = RowEmployerBinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderEmployer(binding.root)
    }

    override fun onBindViewHolder(holder: HolderEmployer, position: Int) {
        val model = employersArrayList[position]
        val clothesId = model.id
        val categoryId = model.categoryId
        val company = model.company
        val time = model.time
//        val city = model.city
//        val location = model.location
//        val description = model.description
        val price = model.price
        val timestamp = model.timestamp
        val imageUrl = model.url
        val category = model.category
        //convert timestamp
//
        val formattedDate = MyApplication.formatTimeStamp(timestamp)
        holder.timeTv.text = time
        holder.companyTv.text = company
        holder.dateTv.text = formattedDate
        holder.priceTv.text = price.toString()

        MyApplication.loadCategory(categoryId, holder.categoryTv)
        //set data
        holder.categoryTv.text = category

        val ref = FirebaseStorage.getInstance().reference.child("Images/$imageUrl")
        val localfile = File.createTempFile("tempImage", "jpeg")

        ref.getFile(localfile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            holder.imageView.setImageBitmap(bitmap)
        }
            .addOnFailureListener{
                Log.d("TAG", "All is not okay")
            }
        Log.d("TAG", "All is okay")

        holder.itemView.setOnClickListener{
            val intent = Intent(context, SendRequestActivity::class.java)
            intent.putExtra("categoryId", categoryId)
            intent.putExtra("category", category)
            intent.putExtra("company", company)
            intent.putExtra("price", price)
            intent.putExtra("time", time)
            intent.putExtra("imageUrl", imageUrl)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return employersArrayList.size
    }
}