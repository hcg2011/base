package com.chungo.baseapp.activity

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.chungo.baseapp.R
import com.chungo.basemore.mvp.model.entity.User
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        val data = intent.extras

        val url = data.get(User.KEY_URL) as String
        fab.setOnClickListener { view ->
            Snackbar.make(view, url, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

}
