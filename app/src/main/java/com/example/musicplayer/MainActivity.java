package com.example.musicplayer;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    String[] items;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listViewSong);
        runTimePermission();

    }

    public void runTimePermission()
    {


        Dexter.withContext(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO).withListener
                (new MultiplePermissionsListener()
        {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                displaySongs();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }


        }).check();
    }

    public ArrayList<File> findSong (File file)
    {
        ArrayList<File> arrayList = new ArrayList<>();
        File [] files = file.listFiles();
        if (files != null) {
            for (File singlefile: files)
            {
                if (singlefile.isDirectory() && !singlefile.isHidden())
                {
                    arrayList.addAll(findSong(singlefile));
                }
                else
                {
                    if (singlefile.getName().endsWith(".mp3")|| singlefile.getName().endsWith(".wav"))
                    {
                        arrayList.add(singlefile);
                    }
                }
            }
        }

        return arrayList;
    }
    void displaySongs()
    {
        final ArrayList<File> mySongs;
        mySongs = findSong(Environment.getExternalStorageDirectory());
        items =new String[mySongs.size()];
        for (int i = 0; i<mySongs.size();i++)
            items[i] = mySongs.get(i).getName().replace(".mp3", "").replace(".wav", "");

        /*ArrayAdapter<String> myAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(myAdapter);
        */
         customAdapter customAdapter = new customAdapter();
         listView.setAdapter(customAdapter);

         listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                 String songName = (String) listView.getItemAtPosition(i);
                 startActivity(new Intent(getApplicationContext(), PlayerActivity.class)
                 .putExtra("songs", mySongs).putExtra("songname", songName).putExtra("pos", i));


             }
         });


    }

    public class customAdapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }



        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View myView;
            myView = getLayoutInflater().inflate(R.layout.list_item, null);
            TextView textsong = myView.findViewById(R.id.txtsongname);
            textsong.setSelected(true);
            textsong.setText(items[i]);
            return myView;
        }
    }

}