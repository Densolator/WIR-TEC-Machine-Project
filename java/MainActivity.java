package com.example.paint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

//    private RelativeLayout parent;
    private PaintView paintView;
    private Button greenButton, redButton, blueButton;
    private SeekBar seekBar;
    private SeekBar.OnSeekBarChangeListener seekBarListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(fromUser)
            {
                changeStrokeWidth(progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Paint variables instantiation
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        paintView = (PaintView) findViewById(R.id.paintView);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        paintView.init(metrics);

        //UI elements
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setMax(50);
        seekBar.setOnSeekBarChangeListener(seekBarListener);
        greenButton = findViewById(R.id.greenButton);
        greenButton.setBackgroundColor(Color.GREEN);
        redButton = findViewById(R.id.redButton);
        redButton.setBackgroundColor(Color.RED);
        blueButton = findViewById(R.id.blueButton);
        blueButton.setBackgroundColor(Color.BLUE);
    }

    public void changeStrokeWidth(int position)
    {
        paintView.setStrokeWidth(position);
    }

    public void setColorGreen(View view)
    {
        paintView.setColor(Color.GREEN);
    }

    public void setColorBlue(View view)
    {
        paintView.setColor(Color.BLUE);
    }

    public void setColorRed(View view)
    {
        paintView.setColor(Color.RED);
    }

    public void saveDrawing(View view)
    {
        boolean grantedPermission = false;
        paintView.setBackgroundColor(Color.TRANSPARENT);
        paintView.setDrawingCacheEnabled(true);
        paintView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = paintView.getDrawingCache();
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        System.out.println(path);
        File file = new File(path+"/newImage.png");
        FileOutputStream ostream;
        while(!grantedPermission)
        {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                try {
                    grantedPermission = true;
                    file.createNewFile();
                    ostream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                    ostream.flush();
                    ostream.close();
                    Toast.makeText(getApplicationContext(), "image saved", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                }
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.normal:
                paintView.normal();
                return true;
            case R.id.emboss:
                paintView.emboss();
                return true;
            case R.id.blur:
                paintView.blur();
                return true;
            case R.id.clear:
                paintView.clear();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}