package com.example.digitspan;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Game extends AppCompatActivity implements View.OnClickListener{
    StringBuilder curr_ans;
    int curr_level;
    int lives_left;
    int score;
    int rounds;
    double ave;
    String current_char;
    String answer;
    boolean difficulty;
    List<String> dictionary;
    Random r = new Random();
    int state; // 0 is waiting ready button, 1 is busy, 2 is waiting answer,
    MediaPlayer beep;
    MediaPlayer correct;
    MediaPlayer wrong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        beep = MediaPlayer.create(this, R.raw.beep);
        correct = MediaPlayer.create(this, R.raw.correct);
        wrong = MediaPlayer.create(this, R.raw.wrong);
        curr_ans = new StringBuilder();
        curr_level = 3;
        lives_left = 3;
        score = 0;
        state = 0;
        rounds = 0;
        ave = 0.0;
        updateView();
        CustomReader dict = new CustomReader(getApplicationContext());
        dictionary = dict.readLine("words.txt");
        Button a = findViewById(R.id.button_a);
        a.setOnClickListener(this);
        Button b = findViewById(R.id.button_b);
        b.setOnClickListener(this);
        Button c = findViewById(R.id.button_c);
        c.setOnClickListener(this);
        Button d = findViewById(R.id.button_d);
        d.setOnClickListener(this);
        Button e = findViewById(R.id.button_e);
        e.setOnClickListener(this);
        Button f = findViewById(R.id.button_f);
        f.setOnClickListener(this);
        Button g = findViewById(R.id.button_g);
        g.setOnClickListener(this);
        Button h = findViewById(R.id.button_h);
        h.setOnClickListener(this);
        Button i = findViewById(R.id.button_i);
        i.setOnClickListener(this);
        Button j = findViewById(R.id.button_j);
        j.setOnClickListener(this);
        Button k = findViewById(R.id.button_k);
        k.setOnClickListener(this);
        Button l = findViewById(R.id.button_l);
        l.setOnClickListener(this);
        Button m = findViewById(R.id.button_m);
        m.setOnClickListener(this);
        Button n = findViewById(R.id.button_n);
        n.setOnClickListener(this);
        Button o = findViewById(R.id.button_o);
        o.setOnClickListener(this);
        Button p = findViewById(R.id.button_p);
        p.setOnClickListener(this);
        Button q = findViewById(R.id.button_q);
        q.setOnClickListener(this);
        Button r = findViewById(R.id.button_r);
        r.setOnClickListener(this);
        Button s = findViewById(R.id.button_s);
        s.setOnClickListener(this);
        Button t = findViewById(R.id.button_t);
        t.setOnClickListener(this);
        Button u = findViewById(R.id.button_u);
        u.setOnClickListener(this);
        Button v = findViewById(R.id.button_v);
        v.setOnClickListener(this);
        Button w = findViewById(R.id.button_w);
        w.setOnClickListener(this);
        Button x = findViewById(R.id.button_x);
        x.setOnClickListener(this);
        Button y = findViewById(R.id.button_y);
        y.setOnClickListener(this);
        Button z = findViewById(R.id.button_z);
        z.setOnClickListener(this);
        Button ready = findViewById(R.id.ready_button);
        ready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRound();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Difficulty");
        builder.setMessage("Please select difficulty.");
        builder.setPositiveButton("Easy",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        difficulty = false;
                        Toast.makeText(getApplicationContext(),"Easy Mode. Click 'Ready' to start.",Toast.LENGTH_LONG).show();
                    }
                });
        builder.setNegativeButton("Hard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                difficulty = true;
                Toast.makeText(getApplicationContext(),"Hard Mode. Click 'Ready' to start.",Toast.LENGTH_LONG).show();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void onBackPressed(){
        if (state!=1) {
            if (rounds != 0) {
                final EditText input = new EditText(this);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(true);
                builder.setTitle("Are you sure?");
                builder.setMessage("Do you want to quit the game?\nCurrent score will be stored. Enter your name below.");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                saveScore(input.getText().toString(), score, curr_level, difficulty, ave);
                                Game.super.onBackPressed();

                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Please continue the game.", Toast.LENGTH_LONG).show();
                    }
                });
                input.setText("Your name here.");
                LinearLayout layout = new LinearLayout(this);
                layout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(50, 0, 50, 0);
                layout.addView(input, params);
                builder.setView(layout);
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                super.onBackPressed();
            }
        }
    }

    public void updateView(){
        TextView lives = findViewById(R.id.live_count);
        lives.setText(String.format(Locale.getDefault(), "Lives Left: %d", lives_left));
        TextView level = findViewById(R.id.level_count);
        level.setText(String.format(Locale.getDefault(),"Level %d",curr_level));
        TextView curr_char = findViewById(R.id.current_alphabet);
        curr_char.setText(current_char);
        TextView curr_score = findViewById(R.id.score);
        curr_score.setText(String.valueOf(score));
        TextView perf = findViewById(R.id.performance);
        if (rounds==0){
            ave = 0.00;
        }
        else{
            ave = (double)score/(double)rounds;
        }
        perf.setText(String.format(Locale.getDefault(),"%.2f", ave));
    }
    public void saveScore(String name, int score, int last_level, boolean difficulty, double performance){
        try {
            File file;
            if (difficulty){
                file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/hard_score.txt");
            }
            else {
                file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/easy_score.txt");
            }
            System.out.println(file.getPath());
            FileWriter writer = new FileWriter(file,true);
            writer.write(String.format(Locale.getDefault(),"%s~%d~%d~%.2f\n",name, score, last_level, performance));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(getApplicationContext(),"Your score had been saved.",Toast.LENGTH_LONG).show();
    }
    public void endRound(){
        state = 1;
        rounds++;
        if (curr_ans.toString().equals(answer)){
            score+=curr_level;
            curr_level++;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Correct Answer");
            builder.setMessage(String.format("The correct answer is %s.\nYou answered %s.", answer, curr_ans))
                    .setCancelable(true)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //do things
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            correct.start();
        }
        else{
            lives_left--;
            updateView();
            if(lives_left>0){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Incorrect Answer");
                builder.setMessage(String.format("The correct answer is %s.\nYou answered %s.", answer, curr_ans))
                        .setCancelable(true)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                wrong.start();
                curr_level = Math.max(1, curr_level-1);
            }
            else{
                final EditText input = new EditText(this);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Incorrect Answer");
                builder.setMessage(String.format("The correct answer is %s.\nYou answered %s\nYou have no lives left. Enter your name below.", answer, curr_ans))
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                saveScore(input.getText().toString().trim(), score, curr_level, difficulty, ave);
                                Intent intent = new Intent(Game.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                input.setText("Your name here.");
                LinearLayout layout = new LinearLayout(this);
                layout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(50, 0, 50, 0);
                layout.addView(input, params);
                builder.setView(layout);
                AlertDialog alert = builder.create();
                alert.show();
                wrong.start();
            }
        }
        updateView();
        curr_ans = new StringBuilder();
        state = 0;
    }
    public void startRound(){
        if (state == 0){
            state = 1;

            if (difficulty){
                answer = generateString(curr_level, "");
            }
            else{
                answer = dictionary.get(r.nextInt(dictionary.size()));
                answer = answer.trim();
                if (answer.length()>curr_level){
                    answer = answer.substring(0, curr_level);
                }
                else if(answer.length()<curr_level){
                    answer = generateString(curr_level, answer);
                }
            }
            answer = answer.toUpperCase();
            Thread t = new Thread() {

                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final TextView txtCount = findViewById(R.id.current_alphabet);
                            new CountDownTimer((answer.length()) * 1400, 700) // Wait 5 secs, tick every 1 sec
                            {
                                int index = 0;
                                @Override
                                public final void onTick(final long millisUntilFinished)
                                {
                                    if (index/2<answer.length() && index%2==0) {
                                        txtCount.setText(String.valueOf(answer.charAt(index/2)));
                                    }
                                    else{
                                        txtCount.setText("");
                                    }
                                    index++;
                                }
                                @Override
                                public final void onFinish()
                                {
                                    state = 2;
                                    txtCount.setText("");
                                    beep.start();
                                    Toast.makeText(getApplicationContext(), "You may begin answering now.", Toast.LENGTH_SHORT).show();
                                }
                            }.start();
                        }
                    });
                }
            };
            t.start();
        }
    }
    public String generateString(int length, String prefix){
        StringBuilder prefixBuilder = new StringBuilder(prefix);
        while(prefixBuilder.length() < length){
            prefixBuilder.append((char) (Math.random() * 26 + 97));
        }
        prefix = prefixBuilder.toString();
        return prefix;
    }
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                if (state == 2) {
                    Button button = findViewById(v.getId());
                    curr_ans.append(button.getText());
                    Toast.makeText(getApplicationContext(), String.format("Current Text: %s", curr_ans), Toast.LENGTH_SHORT).show();
                    if (curr_ans.length() >= curr_level) {
                        endRound();
                    }
                }
        }
    }
}
