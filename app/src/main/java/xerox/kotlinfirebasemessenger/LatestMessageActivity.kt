package xerox.kotlinfirebasemessenger

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth

class LatestMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_message)
        verifyUserISLoggedIn()

    }

    private fun verifyUserISLoggedIn(){
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null){
            performRegisterActivity()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_nav_latest_messages,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {


        when (item?.itemId) {
            R.id.new_message_menu -> {

            }
            R.id.sign_out_menu -> {
                FirebaseAuth.getInstance().signOut()
                performRegisterActivity()
            }
        }


        return super.onOptionsItemSelected(item)
    }

    private fun performRegisterActivity(){
        val intent = Intent(this, RegisterActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }


}
