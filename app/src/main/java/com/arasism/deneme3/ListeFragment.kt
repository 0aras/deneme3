package com.arasism.deneme3

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.arasism.deneme3.databinding.FragmentListeBinding


class ListeFragment : Fragment() {
    var kitapismiListesi=ArrayList<String>()
   // var kitapIdListesi=ArrayList<Int>()
    var yazarismiListesi=ArrayList<String>()
    var gorselListesi=ArrayList<ByteArray>()
    private lateinit var listeAdapter:ListeRecyclerAdapter

private var _binding:FragmentListeBinding?=null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentListeBinding.inflate(inflater,container,false)
        val view=binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listeAdapter= ListeRecyclerAdapter(kitapismiListesi,yazarismiListesi,gorselListesi)
        binding.recyclerView.layoutManager=LinearLayoutManager(context)
        binding.recyclerView.adapter=listeAdapter


        sqlVeriAlma()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
    fun sqlVeriAlma(){
        try {
            activity?.let {
                val database =it.openOrCreateDatabase("kitaplar", Context.MODE_PRIVATE, null)
                val cursor=database.rawQuery("SELECT * FROM kitaplar",null)
                val kitapismiIndex=cursor.getColumnIndex("kitapismi")
                //val kitapIdIndex=cursor.getColumnIndex("id")
                val yazarismiIndex=cursor.getColumnIndex("yazarismi")
                val kitapGorseli=cursor.getColumnIndex("gorsel")

                kitapismiListesi.clear()

                yazarismiListesi.clear()


                while (cursor.moveToNext()){
                    //kitapIdListesi.add(cursor.getInt(kitapIdIndex))
                    kitapismiListesi.add(cursor.getString(kitapismiIndex))

                    yazarismiListesi.add(cursor.getString(yazarismiIndex))

                    val byteDizisi=cursor.getBlob(kitapGorseli)

                    // val bitmap=BitmapFactory.decodeByteArray(byteDizisi,0,byteDizisi.size)
                   gorselListesi.add(byteDizisi)

                }
                listeAdapter.notifyDataSetChanged()

            }
        }catch (e:Exception){
            e.printStackTrace()
        }

    }


}


