package jp.ac.mytoratora

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {

    val tora = 0;
    val granma = 1;
    val kato = 2;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

    }
    //再表示のたびに呼ばれるライフサイクルコールバックメソッド
    override fun onResume() {
        super.onResume()
        //じゃんけんで選んだview部品ボタンのidをintentから取り出す
        val id = intent.getIntExtra("MY_HAND",0)
        val myHand:Int
        //idの値により処理を分岐、自分のじゃんけん画像を切り替える
        myHand = when(id){
            R.id.tora -> {MyhandImage.setImageResource(R.drawable.tora);tora}
            R.id.granma -> {MyhandImage.setImageResource(R.drawable.ma);granma}
            R.id.kato -> {MyhandImage.setImageResource(R.drawable.kato);kato}
            else->kato

        }
        //コンピュータの手をランダム
        val comHand = getHand()
        when(comHand){
            tora -> ComHandImage.setImageResource(R.drawable.tora)
            granma -> ComHandImage.setImageResource(R.drawable.ma)
            kato -> ComHandImage.setImageResource(R.drawable.kato)
        }

        //勝敗判定
        val gameResult = (comHand - myHand + 3) % 3
        //計算結果に応じて勝敗メッセージを表示
        when(gameResult){
            0 -> ResultLabel.setText(R.string.result_draw)
            1 -> ResultLabel.setText(R.string.result_win)
            2 -> ResultLabel.setText(R.string.result_lose)
        }

        backbutton.setOnClickListener { finish() }

        //勝敗とじゃんけんで出した手を保存する
        this.saveData(myHand,comHand,gameResult);
    }
    //resultactivityクラスに勝敗データを保存するメソッドを追加する
    private fun saveData(myHand:Int,comHand:Int,gameResult:Int){
        //共有プリファレンスを使う　インスタンスを取得
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        //値を取得する。キーを指定して値を取得する。該当がなければデフォルト値
        val gameCount = pref.getInt("GAME_COUNT",0);
        val winningStreakCount = pref.getInt("WINNING_STREAK_COUNT",0);
        val lastComHand = pref.getInt("LAST_COM_HAND",0);
        val lastGameResult = pref.getInt("LAST_GAME_RESULT",-1);
        //連勝数
        val editWinningStreakCount = when{
            (lastGameResult == 2 && gameResult == 2)->(winningStreakCount+1)
            else->0
        }
        //共有プりファレンスの編集モードを取得
        val editor = pref.edit();
        //editorのメソッドをメソッドチェーンで呼び出し
        editor.putInt("GAME_COUNT",gameCount+1)
            .putInt("WINNING_STREAK_COUNT",editWinningStreakCount)
            .putInt("LAST_MY_HAND",myHand)
            .putInt("LAST_COM_HAND",comHand)
            .putInt("BEFORE_LAST_COM_HAND",lastComHand)
            .putInt("LAST_GAME_RESULT",gameResult)
            .apply()
    }

    //心理学ロジックとか
    private fun getHand():Int{
        var hand = (Math.random()*3).toInt();
        //共有プりファレンスを
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val gameCount = pref.getInt("GAME_COUNT",0);
        val lastMyHand = pref.getInt("LAST_MY_HAND",0);
        val winningStreakCount = pref.getInt("WINNING_STREAK_COUNT",0);
        val lastComHand = pref.getInt("LAST_COM_HAND",0);
        val beforeLastComHand = pref.getInt("BEFORE_LAST_COM_HAND",0);
        val lastGameResult = pref.getInt("LAST_GAME_RESULT",-1);
        val gameResult = pref.getInt("GAME_RESULT",0);



        //取得した保存値を使ってコンピュータの出す手を上書きする
        //前回が一回戦の時
        if(gameCount == 1){
            if(gameResult == 2){
                //前回が一回戦でさらにコンピュータの勝ち
                //comの次のて
                while(lastComHand == hand){
                    hand = (Math.random()*3).toInt();
                }
            }else if(gameResult == 1){
                //コンピュータの負け
                //ユーザが前回だした手にかつ手を出す
                hand = (lastMyHand - 1 + 3)% 3
            }
        }else if(winningStreakCount>0){
            //連勝中の時
            if(beforeLastComHand == lastComHand){
                while (lastComHand == hand){
                    hand = (Math.random()*3).toInt();
                }
            }
        }

        return hand;
    }
}
