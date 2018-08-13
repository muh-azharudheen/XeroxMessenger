package xerox.kotlinfirebasemessenger

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

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
                }
                .addOnFailureListener {
                    Log.d("Main", "Failed to create user: ${it.message}")
                }
         }

}
