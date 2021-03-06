package xerox.kotlinfirebasemessenger

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*

class NewMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)



        supportActionBar?.title = "Select User"

        fetchUsers()

        val adater = GroupAdapter<ViewHolder>()
        adater.add(UserItem())
        adater.add(UserItem())
        adater.add(UserItem())
        adater.add(UserItem())

        recyclerview_newmessage.adapter = adater
    }

    private fun fetchUsers(){

        val listner = EventListener<QuerySnapshot>{ snapshot, e ->
            if (e != null){
                Log.d("New Message Activity","Error SnapShot")
                return@EventListener
            }
        }


        val db = FirebaseFirestore.getInstance()
        db.collection("users")
                .addSnapshotListener(listner)


    }
}


class UserItem: Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {

    }

    override fun getLayout(): Int {
        return  R.layout.user_row_new_message
    }
}
