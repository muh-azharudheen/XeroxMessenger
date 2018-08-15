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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.HashMap

class RegisterActivity : AppCompatActivity() {

    var selectedPhotoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        register_button_register.setOnClickListener {
            performRegister()
        }

        already_have_an_account_textview.setOnClickListener {
            Log.d("Register Activity", "try to show login activity")
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
            selectphoto_button_register.alpha = 0.0f

            selectphoto_imageview_register.setImageBitmap(bitmap)

        }
    }


    private fun performRegister(){
        val email = email_edittext_registration.text.toString()
        val password = password_edittext_registration.text.toString()
        val userName = username_edittext_registration.text.toString()

        if (email.isEmpty() || password.isEmpty() || userName.isEmpty()) {
            Toast.makeText(this, "Please Fill Name/Email/Password", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("Register Activity", "Email $email")
        Log.d("Register Activity", "Password $password")

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
                    Log.d("Register Activity", "Successfully uploaded image ${it.metadata?.path}")
                    ref.downloadUrl.addOnSuccessListener {
                        Log.d("Register Activity","download url ok ${it.toString()}")
                        saveuserToDatabase(it.toString())
                    }
                    ref.downloadUrl.addOnFailureListener {
                        saveuserToDatabase("")
                    }
                }
                .addOnFailureListener {
                    Log.d("Register Activity","Failure in uploading Image")
                }


    }

    private fun saveuserToDatabase(profileImageURL: String){
        val uid = FirebaseAuth.getInstance().uid ?: ""

        val dbRef = FirebaseFirestore.getInstance()
        val user = User(uid,username_edittext_registration.text.toString() , profileImageURL)
        val uuuu = HashMap<String,Any>()
        uuuu.put("id", user.userId)
        uuuu.put("userName",user.username)
        uuuu.put("profileImageUrl",user.profileImageURL)

        val userData = mapOf(uid to uuuu)

        dbRef.collection("user")
                .add(userData)
                .addOnSuccessListener {
                    Log.d("Register", "successfully added Profile Image")
                    val intent = Intent(this,LatestMessageActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }
                .addOnFailureListener {
                    Log.d("Register", it.toString())
                    Log.d("Register", "failed to load profile image")
                }
        }
}


class User(val userId: String, val username: String, val profileImageURL: String){

}
