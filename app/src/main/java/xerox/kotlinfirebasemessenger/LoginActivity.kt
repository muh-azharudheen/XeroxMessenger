package xerox.kotlinfirebasemessenger

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        back_textview_login.setOnClickListener {
            finish()
        }

        signin_button_login.setOnClickListener {
            val email = email_edittext_login.text.toString()
            val password = password_edittext_login.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please Fill Email/Password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            performSignIn(email, password)
        }




    }

    private fun performSignIn(email: String, password: String){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful){
                        return@addOnCompleteListener
                    }
                    Log.d("Main", "Succesfully created user with uuid : ${it.result.user.uid}")

                }
                .addOnFailureListener {
                    Toast.makeText(this,"Failed to create user: ${it.message}", Toast.LENGTH_SHORT).show()
                    return@addOnFailureListener
                }
    }

}