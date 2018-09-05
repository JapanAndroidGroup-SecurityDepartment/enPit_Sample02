package jp.android_group.asj.enpit_sample02;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button mEmailSignInButton = (Button) findViewById(R.id.submitButton);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFile();
            }
        });

    }

    void deleteFile(){
        File file = new File(getExternalFilesDir(null),"password.txt");
        if(file != null){
            file.delete();
        }
    }
}
