package eu.pl.snk.senseibunny.places

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import eu.pl.snk.senseibunny.places.databinding.ActivityAddHappyPlaceBinding
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
                    1-> Toast.makeText(this, "Camera selection coming soon", Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(this@AddHappyPlaceActivity, "Permission Granted", Toast.LENGTH_SHORT).show()
                }

                }
                override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>, token: PermissionToken) {
                    showRationaleDialogForPermissions()
                    token?.cancelPermissionRequest()
                }
            }).onSameThread().check();

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

}