package  com.example.hypersonalbooster

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class BoosterActivity1 :AppCompatActivity(){

    private var end_time: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView((R.layout.layout_booster_main))
        settingButton()
    }

    fun settingButton(){
        val button = findViewById<Button>(R.id.more_before)
        button.setOnClickListener{
            val intent = Intent(this, BoosterBeforeActivity::class.java)
            startActivity(intent)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        }
    fun settingButton1(){
        val button = findViewById<Button>(R.id.more_after)
        button.setOnClickListener {
            val intent = Intent(this, BoosterAfterActivity::class.java)
            startActivity(intent)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        }
    }
    }
    override fun onBackPressed() {
        // super.onBackPressed()

        if (System.currentTimeMillis() - end_time >= 2000) {
            end_time = System.currentTimeMillis()
            Toast.makeText(applicationContext, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
        }
        else if (System.currentTimeMillis() - end_time < 2000) {
            finishAffinity()
        }
    }
}