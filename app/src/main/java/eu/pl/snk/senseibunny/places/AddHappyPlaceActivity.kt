package eu.pl.snk.senseibunny.places

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import eu.pl.snk.senseibunny.places.databinding.ActivityAddHappyPlaceBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

class AddHappyPlaceActivity : AppCompatActivity() {

    private var binding: ActivityAddHappyPlaceBinding ?=null

    private var tvSelectedDate: TextView? = null

    private var buttonAnim: Button ?=null

    private var animation: Animation?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddHappyPlaceBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolBar)

        if(supportActionBar!=null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding?.toolBar?.setNavigationOnClickListener{
            onBackPressed()
        }

        binding?.date?.setOnClickListener{
            clickDatePicker()
        }

        buttonAnim=binding?.breathButton

        animation= AnimationUtils.loadAnimation(this,R.anim.breath)
        buttonAnim?.startAnimation(animation)

        binding?.uploadImage?.setOnClickListener{
            val pictureDialog = AlertDialog.Builder(this)
            pictureDialog.setTitle("Select Action")
            val pictureDialogItems = arrayOf("Choose from Gallery", "Take a Picture")
            pictureDialog.setItems(pictureDialogItems) { dialog, which ->
                when(which){
                    0->chooseFromGallery()
                    1-> takeApic()
                }
            }
            pictureDialog.show()
        }


    }

    private fun chooseFromGallery(){
        Dexter.withContext(this).withPermissions(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).withListener(object: MultiplePermissionsListener {

                override fun onPermissionsChecked(report: MultiplePermissionsReport) {if(report.areAllPermissionsGranted()){
                    val pickInent= Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    GalleryLaucnher.launch(pickInent)
                }

                }
                override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>, token: PermissionToken) {
                    showRationaleDialogForPermissions()
                    token?.cancelPermissionRequest()
                }
            }).onSameThread().check();

    }

    private val GalleryLaucnher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
            if(result.resultCode== RESULT_OK && result.data!=null){

                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, Uri.parse(result.data?.data?.toString()))
               val saveImage = saveImageToInternalStorage(bitmap)
                Log.e("Image", "Path :: $saveImage")

                binding?.imagePlaceholder?.setImageURI(result.data?.data) // setting background of our app
            }
        }

    private fun takeApic(){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, CAMERA)
        }
        else{
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAMERA)
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == CAMERA){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAMERA)
            }
            else{
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK && requestCode == CAMERA){

            val thumbnail: Bitmap = data?.extras?.get("data") as Bitmap
            val saveImage = saveImageToInternalStorage(thumbnail)

            Log.e("Image", "Path :: $saveImage")
            binding?.imagePlaceholder?.setImageBitmap(thumbnail)
        }
    }

    private fun showRationaleDialogForPermissions(){
        AlertDialog.Builder(this).setMessage("It looks like you didnt give permissions thats sad L").setPositiveButton("Go to Settings") { dialog, which ->
            try{
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            } catch (e:Exception){
                e.printStackTrace()
            }
        }.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }.show()

    }

    private fun clickDatePicker() {

        val myCalendar = Calendar.getInstance()
        val year = myCalendar.get(Calendar.YEAR)
        val month = myCalendar.get(Calendar.MONTH)
        val day = myCalendar.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, selectedYear, selectedMonth, selectedDayOfMonth ->

                val selectedDate = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
                Toast.makeText(this, selectedDate.toString(), Toast.LENGTH_LONG).show()
                binding?.date?.setText(selectedDate)
            },
            year,
            month,
            day
        )
        dpd.datePicker.maxDate= System.currentTimeMillis()-86400000 // setting max date to yesterday
        dpd.show()

    }

    private fun saveImageToInternalStorage(bitmap: Bitmap):Uri {
        val wrapper=ContextWrapper(applicationContext)
        var file = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE) //file accessible only from application
        file = File(file,"${UUID.randomUUID()}.jpg")// store image and add random id to it


        try{
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        }catch (e:IOException){
            e.printStackTrace()
        }

        return Uri.parse(file.absolutePath)
    }

    companion object {
        private const val GALLERY=1
        private const val CAMERA=2
        private const val IMAGE_DIRECTORY="PlacesImages"
    }

}