package com.emiratz.collectiondata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.emiratz.collectiondata.uifragment.Collection

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.frmFragmentRoot, Collection.newInstance("","", "Collection Data"))
                .commit()
        }
    }
}