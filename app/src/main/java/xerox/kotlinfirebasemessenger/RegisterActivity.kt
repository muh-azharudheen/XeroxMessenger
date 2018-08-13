package xerox.kotlinfirebasemessenger

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

    var selectedPhotoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        register_button_register.setOnClickListener {
            performRegister()
        }

        already_have_an_account_textview.setOnClickListener {
            Log.d("Main Activity", "try to show login activity")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

        }

        selectphoto_button_register.setOnClickListener {
            Log.d("Register Activity", "Try to show photo selector")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            //proceed and check if the selected image was
            Log.d("Register Activity", "Photo was selected")

            selectedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,selectedPhotoUri)

            val bitmapDrawable = BitmapDrawable(bitmap)
            selectphoto_button_register.setBackgroundDrawable(bitmapDrawable)

        }
    }


    private fun performRegister(){
        val email = email_edittext_registration.text.toString()
        val password = password_edittext_registration.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please Fill Email/Password", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("Main Activity", "Email $email")
        Log.d("Main Activity", "Password $password")

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful)return@addOnCompleteListener
                    Log.d("Main","Successfully created user with uuid : ${it.result.user.uid}")
                    uploadImageToFirebase()
                }
                .addOnFailureListener {
                    Log.d("Main", "Failed to create user: ${it.message}")
                }
         }

    private fun uploadImageToFirebase(){
        if (selectedPhotoUri == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("images/$filename")

        ref.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {

                }


    }

}
