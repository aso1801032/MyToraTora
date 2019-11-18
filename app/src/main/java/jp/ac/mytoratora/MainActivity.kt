package jp.ac.mytoratora

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)
            //アプリ起動時に共有プリファレンスを初期化
            val pref = PreferenceManager.getDefaultSharedPreferences(this)
            //編集モード取得　clear実行
            pref.edit{this.clear()}

            tora.setOnClickListener{ onJankenButtonTapped(it) }
            granma.setOnClickListener{ onJankenButtonTapped(it) }
            kato.setOnClickListener{ onJankenButtonTapped(it) }

        }

        override fun onResume() {
            super.onResume()
            //ボタンがクリックされたら処理を呼び出す
        }

        fun onJankenButtonTapped(view: View?){
            val  intent = Intent(this, ResultActivity::class.java)
            //インテントにおまけ情報extraを追加する
            intent.putExtra("MY_HAND",view?.id)
            startActivity(intent)
        }
}
