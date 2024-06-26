package com.example.myapplication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    private VideoView vv;
    private TextView statusTextView;
    private Handler handler;
    private Runnable updateStatusTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Button bt = findViewById(R.id.bt_second);
        Button btGetArea = findViewById(R.id.bt_get_area);
        vv = findViewById(R.id.vv);
        statusTextView = findViewById(R.id.statusTextView);

        // 비디오 Uri 설정
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.slide_vid);

        // 비디오뷰의 재생, 일시정지 등을 할 수 있는 '컨트롤바'를 붙여주는 작업
        vv.setMediaController(new MediaController(this));

        // VideoView가 보여줄 동영상의 경로 주소(Uri) 설정하기
        vv.setVideoURI(videoUri);

        // 비디오 로딩 준비가 끝났을 때 실행하도록 리스너 설정
        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                // 비디오 시작
                vv.start();
            }
        });

        // 버튼 클릭 이벤트 설정
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 영상 재생
                if (!vv.isPlaying()) {
                    vv.start();
                }
            }
        });

        // '선택 영역 확인' 버튼 클릭 이벤트 설정
        btGetArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // GetArea 액티비티 시작
                Intent intent = new Intent(SecondActivity.this, GetArea.class);
                startActivity(intent);
            }
        });

        // 주기적으로 상태를 업데이트하는 작업을 설정합니다.
        handler = new Handler();
        updateStatusTask = new Runnable() {
            @Override
            public void run() {
                if (ThirdActivity.currentStatus != null) {
                    statusTextView.setText("분류 결과: " + ThirdActivity.currentStatus);
                }
                handler.postDelayed(this, 2000); // 2초마다 상태 업데이트
            }
        };
        handler.post(updateStatusTask);
    }

    // 화면에 안 보일 때...
    @Override
    protected void onPause() {
        super.onPause();
        // 비디오 일시 정지
        if (vv != null && vv.isPlaying()) vv.pause();
    }

    // 액티비티가 메모리에서 사라질 때..
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 비디오 정지
        if (vv != null) vv.stopPlayback();
        handler.removeCallbacks(updateStatusTask); // 상태 업데이트 작업 중지
    }
}
