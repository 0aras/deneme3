package com.arasism.`deneme3`

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arasism.deneme3.databinding.RecyclerRowBinding


class ListeRecyclerAdapter
    (val kitapListesi: ArrayList<String>,
     val kitapYazaradi: ArrayList<String>,val gorsel:ArrayList<ByteArray>): RecyclerView.Adapter<ListeRecyclerAdapter.KitapHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KitapHolder{
        val binding=RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
    return KitapHolder(binding)
    }
   inner class KitapHolder ( val binding: RecyclerRowBinding):RecyclerView.ViewHolder(binding.root)


    override fun onBindViewHolder(holder: KitapHolder, position: Int) {

        val yedek=gorsel[position]
        val bitmap=BitmapFactory.decodeByteArray(yedek,0,yedek.size)
        with(holder){
            binding.recyclerText.setText(kitapListesi[position])
            binding.recyclerYazarText.setText(kitapYazaradi[position])

           binding.kitapKapakimage.setImageBitmap(bitmap)

                    }
    }

    override fun getItemCount(): Int {
      return kitapListesi.size
        return kitapYazaradi.size
        return gorsel.size

    }
}





