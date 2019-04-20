package com.example.digitspan;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    boolean ins_opened, score_opened;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ins_opened = false;
        score_opened = false;
        setContentView(R.layout.activity_main);
        final Button ins_button = findViewById(R.id.ins_button);
        final Button start_button = findViewById(R.id.start_button);
        final TextView title = findViewById(R.id.title);
        final ScrollView ins = findViewById(R.id.ins_view);
        final Button hiscore = findViewById(R.id.hiscore_but);
        final LinearLayout scoreboard = findViewById(R.id.scoreboard);
        final ListView list = findViewById(R.id.easy_list);
        final ListView list2 = findViewById(R.id.hard_list);
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.header, list, false);
        list.addHeaderView(header, null, false);
        LayoutInflater inflater2 = getLayoutInflater();
        ViewGroup header2 = (ViewGroup) inflater2.inflate(R.layout.header, list, false);
        list2.addHeaderView(header2, null, false);
        ins_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ins_button.setVisibility(View.INVISIBLE);
                start_button.setVisibility(View.INVISIBLE);
                title.setVisibility(View.INVISIBLE);
                hiscore.setVisibility(View.INVISIBLE);
                ins.setVisibility(View.VISIBLE);
                ins_opened = true;
            }
        });
        start_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, Game.class);
                MainActivity.this.startActivity(myIntent);
            }
        });
        hiscore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    }
                }
                final ListView list = findViewById(R.id.easy_list);
                ArrayList<Score> arrayList = new ArrayList<Score>();
                CustomReader r = new CustomReader(getApplicationContext());
                List<String> l = r.readLineFromExternal(Environment.getExternalStorageDirectory().getAbsolutePath() + "/easy_score.txt");
                for (String s : l) {
                    String[] temp = s.split("~");
                    arrayList.add(new Score(temp[0], Integer.parseInt(temp[1]), Integer.parseInt(temp[2]), Double.parseDouble(temp[3])));
                }

                Collections.sort(arrayList, new Comparator<Score>() {
                    public int compare(Score m1, Score m2) {
                        if (m1.score == m2.score) {
                            if (m1.last_level > m2.last_level) {
                                return -1;
                            } else if (m1.last_level < m2.last_level) {
                                return 1;
                            }
                            return 0;
                        } else if (m1.score > m2.score) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                });
                if (arrayList.size() == 0) {
                    arrayList.add(new Score("Not Available", -1, -1, -1.0));
                }
                CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), arrayList);
                list.setAdapter(customAdapter);

                final ListView list2 = findViewById(R.id.hard_list);
                ArrayList<Score> arrayList2 = new ArrayList<Score>();
                List<String> l2 = r.readLineFromExternal(Environment.getExternalStorageDirectory().getAbsolutePath() + "/hard_score.txt");
                for (String s : l2) {
                    String[] temp = s.split("~");
                    arrayList2.add(new Score(temp[0], Integer.parseInt(temp[1]), Integer.parseInt(temp[2]), Double.parseDouble(temp[3])));
                }

                Collections.sort(arrayList2, new Comparator<Score>() {
                    public int compare(Score m1, Score m2) {
                        if (m1.score == m2.score) {
                            if (m1.performance > m2.performance) {
                                return -1;
                            } else if (m1.performance < m2.performance) {
                                return 1;
                            } else {
                                if (m1.last_level > m2.last_level) {
                                    return -1;
                                } else if (m1.last_level < m2.last_level) {
                                    return 1;
                                }
                            }
                            return 0;
                        } else if (m1.score > m2.score) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                });
                if (arrayList2.size() == 0) {
                    arrayList2.add(new Score("Not Available", -1, -1, -1.0));
                }
                CustomAdapter customAdapter2 = new CustomAdapter(getApplicationContext(), arrayList2);
                list2.setAdapter(customAdapter2);
                ins_button.setVisibility(View.INVISIBLE);
                start_button.setVisibility(View.INVISIBLE);
                title.setVisibility(View.INVISIBLE);
                hiscore.setVisibility(View.INVISIBLE);
                scoreboard.setVisibility(View.VISIBLE);
                score_opened = true;
            }
        });
    }

    public void onBackPressed() {
        if (ins_opened) {
            final Button ins_button = findViewById(R.id.ins_button);
            final Button start_button = findViewById(R.id.start_button);
            final TextView title = findViewById(R.id.title);
            final ScrollView ins = findViewById(R.id.ins_view);
            final Button hiscore = findViewById(R.id.hiscore_but);
            ins_button.setVisibility(View.VISIBLE);
            start_button.setVisibility(View.VISIBLE);
            title.setVisibility(View.VISIBLE);
            hiscore.setVisibility(View.VISIBLE);
            ins.setVisibility(View.INVISIBLE);
            ins_opened = false;
        } else if (score_opened) {
            final Button ins_button = findViewById(R.id.ins_button);
            final Button start_button = findViewById(R.id.start_button);
            final TextView title = findViewById(R.id.title);
            final LinearLayout scoreboard = findViewById(R.id.scoreboard);
            final Button hiscore = findViewById(R.id.hiscore_but);
            ins_button.setVisibility(View.VISIBLE);
            start_button.setVisibility(View.VISIBLE);
            title.setVisibility(View.VISIBLE);
            hiscore.setVisibility(View.VISIBLE);
            scoreboard.setVisibility(View.INVISIBLE);
            score_opened = false;
        } else {
            super.onBackPressed();
        }
    }
}
