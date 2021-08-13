package com.example.practice

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import java.lang.Math.abs
import java.util.*

class MainActivity : AppCompatActivity() {
    var p_num = 2  // 몇번 돌릴건지
    var k = 1    // 참가자 번호
    val score_list = mutableListOf<Float>() //각 참가자의 점수를 저장할 리스트 생성
    var isBlind = false

    fun start() {
        setContentView(R.layout.activity_start)
        val tV_pnum: TextView = findViewById(R.id.tv_pnum)
        val btn_minus: Button = findViewById(R.id.btn_minus)
        val btn_plus: Button = findViewById(R.id.btn_plus)
        val btn_start: Button = findViewById(R.id.btn_start)
        val btn_blind: Button = findViewById(R.id.btn_blind)

        btn_blind.setOnClickListener {
            isBlind = !isBlind
            if (isBlind == true) {
                btn_blind.text = "BLIND 모드 ON"
            } else {
                btn_blind.text = "BLIND 모드 OFF"
            }
        }

        tV_pnum.text = p_num.toString()

        btn_minus.setOnClickListener {
            if(p_num > 0 ){
                p_num--
                tV_pnum.text = p_num.toString()
            }
        }
        btn_plus.setOnClickListener {
            p_num++
            tV_pnum.text = p_num.toString()
        }
        btn_start.setOnClickListener {
            main()
        }
    }



    fun main() {
        setContentView(R.layout.activity_main) // setContentView: activity_main을 불러와라

        var timerTask: Timer? = null

        var stage = 1
        var second : Int = 0 // Int형 second정수를 0으로 초기화
        
        val tV: TextView = findViewById(R.id.tv_pnum)
        val tV_t: TextView = findViewById(R.id.tv_timer)
        val tV_s: TextView = findViewById(R.id.tv_score)
        val tV_people: TextView = findViewById(R.id.tv_people)
        
        val btn: Button = findViewById(R.id.btn_start) // 시작/중지 버튼
        val btn_init: Button = findViewById(R.id.btn_init)

        val random_box = Random() //랜덤한 숫자를 뽑을 수 있는 객체
        val num = random_box.nextInt(1001) // 0~10 정수형 반환

        val bg_main : ConstraintLayout = findViewById(R.id.bg_main) // 배경화면 객체화

        val color_list = mutableListOf<String>("#32E9321E", "#32E98E1E", "#32E9C41E", "#3287E91E", "#321EBDE9", "#321E79E9", "#32651EE9")
        //하나의 리스토로 만들자 색 값들
        var color_index = k%7-1

        if(color_index == -1){
            color_index = 6
        }
        val color_slec = color_list.get(color_index)
        bg_main.setBackgroundColor(Color.parseColor(color_slec))  // 배경색 지정

        tV.text = ((num.toFloat()) / 100).toString() //랜덤한 숫자 뽑아오기
        btn.text = "시작"
        tV_people.text = "참가자 $k" // formatting

        btn_init.setOnClickListener {
            score_list.clear() // 초기화면으로 가므로 리스트랑 참가자 번호값 초기화
            k = 1
            p_num = 2
            start()
        }
        btn.setOnClickListener {
            stage++
            if ( stage == 2 ) {
                timerTask = kotlin.concurrent.timer(period = 10) {
                    second++
                    runOnUiThread {
                        if (isBlind == false){
                            tV_t.text = (second.toFloat() / 100).toString()
                        }
                        else if (isBlind == true && stage == 2){
                            tV_t.text = "????"
                        }

                    }
                }
                btn.text = "정지"
            }
            else if ( stage == 3 ) {
                tV_t.text = (second.toFloat() / 100).toString()
                timerTask?.cancel()
                val score = (abs(second - num).toFloat())/100 // 절대값으로 점수 계산
                score_list.add(score)                         // 점수를 리스트에 저장

                tV_s.text = score.toString()
                btn.text = "다음 참가자"
                stage = 0
                if (k == p_num) {
                    btn.text = "결과 확인"
                }
            }
            else if ( stage == 1 ) {
                if ( k < p_num ){
                    k++
                    main()
                } else {
                    end()
                }
            }
        }
    }

    fun end() {
        setContentView(R.layout.activity_end)

        val tV_lastR: TextView = findViewById(R.id.tv_lastRank)
        val tV_lastS: TextView = findViewById(R.id.tv_lastScore)
        val btn_reset: Button = findViewById(R.id.btn_reset)

        tV_lastS.text = (score_list.maxOrNull()).toString() // 리스트에 저장된 값중 최대값 뽑아와 꼴찌 스코어에 저장 maxOrNull
        var index_last = score_list.indexOf(score_list.maxOrNull()) // 리스트 최댓값의 인덱스 뽑아와 변수에 저장  indexOf
        tV_lastR.text = "참가자" + (index_last+1).toString()  // 참가자 + 위에 저장된 변수 값 넣어줌
        btn_reset.setOnClickListener{
            k = 1                     // 참가자 번호 1로 초기화
            score_list.clear()        // 리스트 clear로 초기화
            p_num = 2
            start()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        start()
    }
}