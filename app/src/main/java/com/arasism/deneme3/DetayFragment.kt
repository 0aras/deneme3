package com.arasism.deneme3

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.arasism.deneme3.databinding.FragmentDetayBinding
import java.io.ByteArrayOutputStream


class DetayFragment : Fragment() {
    var secilengorsel: Uri? = null
    var secilenBitmap: Bitmap? = null

    private var _binding: FragmentDetayBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetayBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.button.setOnClickListener() {
            kaydet(it)
        }
        binding.imageView.setOnClickListener() {
            gorselSec(it)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun kaydet(view: View) {
        val kitapismi = binding.kitapAdiText.text.toString()
        val yazarismi = binding.yazarAdiText.text.toString()
        if (secilenBitmap != null) {
            val kucukBitmap = kucukBitmapOlustur(secilenBitmap!!, 300)

            val outputStream = ByteArrayOutputStream()
            kucukBitmap.compress(Bitmap.CompressFormat.PNG, 50, outputStream)
            val byteDizisi = outputStream.toByteArray()


            try {
                context?.let {
                    val database = it.openOrCreateDatabase("kitaplar", Context.MODE_PRIVATE, null)
                    database.execSQL("CREATE TABLE IF NOT EXISTS kitaplar (id INTEGER PRIMARY KEY,kitapismi VARCHAR,yazarismi VARCHAR, gorsel BLOB)")
                    val sqlString =
                        "INSERT INTO kitaplar( kitapismi, yazarismi,gorsel) VALUES(?, ?, ?)"
                    val statement = database.compileStatement(sqlString)
                    statement.bindString(1, kitapismi)
                    statement.bindString(2, yazarismi)
                    statement.bindBlob(3, byteDizisi)
                    statement.execute()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
            val action = DetayFragmentDirections.actionDetayFragmentToListeFragment()
            Navigation.findNavController(view).navigate(action)
        }

        //SQLite kaydetme
    }

    fun gorselSec(view: View) {

        activity?.let {
            if (ContextCompat.checkSelfPermission
                    (
                    it.applicationContext,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)

            } else {
                val galeriIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntent, 2)
            }

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val galeriIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntent, 2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            secilengorsel = data.data

            try {
                context?.let {
                    if (secilengorsel != null) {
                        if (Build.VERSION.SDK_INT >= 28) {
                            val source =
                                ImageDecoder.createSource(it.contentResolver, secilengorsel!!)
                            secilenBitmap = ImageDecoder.decodeBitmap(source)
                            binding.imageView.setImageBitmap(secilenBitmap)

                        } else {
                            secilenBitmap =
                                MediaStore.Images.Media.getBitmap(it.contentResolver, secilengorsel)
                            binding.imageView.setImageBitmap(secilenBitmap)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    fun kucukBitmapOlustur(kullaniciSectigiBitmap: Bitmap, maxBoyut: Int): Bitmap {
        var width = kullaniciSectigiBitmap.width
        var height = kullaniciSectigiBitmap.height

        val bitmapOrani: Double = width.toDouble() / height.toDouble()
        if (bitmapOrani > 1) {
            //yatay
            width = maxBoyut
            val kisaltilmisHeight = width / bitmapOrani
            height = kisaltilmisHeight.toInt()
        } else {
            //dikey
            height = maxBoyut
            val kisaltilmisWidth = height * bitmapOrani
            width = kisaltilmisWidth.toInt()
        }

        return Bitmap.createScaledBitmap(kullaniciSectigiBitmap, width, height, true)

    }

}