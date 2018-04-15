package com.kashifirshad.softwareprojects;

//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.GET_ACCOUNTS;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;

public class MainActivity extends Activity {
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<Project> listDataHeader;
    HashMap<Project, List<Project>> listDataChild;
    public static final int RequestPermissionCode = 1;
    String userEmail;
    public static User user;
    public static boolean isSuperUser = false;
    public static Timer syncTimer;
    public static Activity mainActivity;
    public static boolean active;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.mainActivity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button allowPermission = (Button) findViewById(R.id.allowPermission);
        allowPermission.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EnableRuntimePermission();
            }
        });

        if(checkPermissions() != true){
            return;
        }


        allowPermission.setVisibility(View.INVISIBLE);
        userEmail = new UserEmailFetcher().getEmail(MainActivity.this.getApplicationContext());
        if(userEmail.equals("kashif.ir@gmail.com"))
            this.isSuperUser = true;
        findViewById(R.id.addProj).setVisibility(View.VISIBLE);
        findViewById(R.id.btnPreferences).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.textMsg)).setText("You are logged in as " + userEmail);

        DatabaseHelper DH = new DatabaseHelper(MainActivity.this);
        this.user = DH.getUser(userEmail);

        if(this.user.getEmailAddress()==null){
            this.user = new User(null, null, null, userEmail, null, null, null,
                    null, null, null, null, 30, 0,
            0, 0);
            this.user.setId(DH.createUser(this.user));
        }
        Log.e("user***", "Id:"+ Long.toString(this.user.getId())+"Email:"+this.user.getEmailAddress() );

        Button btnAddProj = (Button) findViewById(R.id.addProj);
        btnAddProj.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddProjectActivity.class);
                startActivity(intent);
                finish();
            }
        });


        Button btnPreferences = (Button) findViewById(R.id.btnPreferences);
        btnPreferences.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PreferencesActivity.class);
                startActivity(intent);
                finish();
            }
        });






        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(MainActivity.this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview Group click listener
        expListView.setOnGroupClickListener(new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
//                 Toast.makeText(getApplicationContext(),
//                 "Group Clicked*** " + listDataHeader.get(groupPosition),
//                 Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
//                Toast.makeText(MainActivity.this,
//                        listDataHeader.get(groupPosition) + " Expanded ***",
//                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
//                Toast.makeText(MainActivity.this,
//                        listDataHeader.get(groupPosition) + " Collapsed",
//                        Toast.LENGTH_SHORT).show();

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
//                Toast.makeText(
//                        getApplicationContext(),
//                        listDataHeader.get(groupPosition)
//                                + " : "
//                                + listDataChild.get(
//                                listDataHeader.get(groupPosition)).get(
//                                childPosition), Toast.LENGTH_SHORT)
//                        .show();
                Project story = (Project) listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
                Project proj = (Project)listDataHeader.get(groupPosition);

                Intent intent = new Intent(getApplicationContext(), AddStoryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("projId",proj.getId());
                bundle.putString("projTitle", proj.getStory());
                bundle.putLong("storyId",story.getId());
                bundle.putString("story", story.getStory());
                bundle.putString("filePaths", story.getFilePaths());
                bundle.putString("devHrs",story.getEstimatedHrs());
                bundle.putString("devCost",story.getEstimateCost());
                intent.putExtras(bundle);
                startActivity(intent);
                finish();

//                listDataHeader.add("kk..");
//                List<String> comingSoon = new ArrayList<String>();
//                comingSoon.add("2 Guns");
//                comingSoon.add("The Smurfs 2");
//                comingSoon.add("The Spectacular Now");
//                comingSoon.add("The Canyons");
//                comingSoon.add("Europa Report");
//
//                listDataChild.put(listDataHeader.get(3), comingSoon); // Header, Child data
//
//                listAdapter.setNewItems(listDataHeader, listDataChild);
                return false;
            }
        });
//        listAdapter.IsFirstRun = 0;
        DH.closeDB();
        if(syncTimer == null){
            int delayMS = user.getSyncDuration()*1000*60;
            syncTimer = new Timer();
            syncTimer.scheduleAtFixedRate(
                    new TimerTask()
                    {
                        public void run()
                        {
                            startService(new Intent(MainActivity.this, SyncService.class));
                        }
                    },
                    0,      // run first occurrence immediately
                    delayMS);  // run every three seconds
        }
    }

    @Override
    public void onStart() {
        active = true;
        super.onStart();
    }

    @Override
    public void onStop() {
        active = false;
        super.onStop();
    }


    public void onClickStory(View v) {

        switch (v.getId()) {

            case R.id.btnAddStory:
                Project proj = (Project) v.getTag();
                Intent intent = new Intent(getApplicationContext(), AddStoryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("projId",proj.getId());
                bundle.putString("projTitle", proj.getStory());
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }

    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        DatabaseHelper dh = new DatabaseHelper(getApplicationContext());
        listDataHeader = dh.getChildProjects(0);
        listDataChild = new HashMap<Project, List<Project>>();
        for(int i=0; i< listDataHeader.size(); i++){
                List<Project> childProjects = dh.getChildProjects(listDataHeader.get(i).getId());
                childProjects.add( new Project(999999,"Add New Story",null, "","","",0,1,listDataHeader.get(i).getId(),0, this.user));
                listDataChild.put((Project) listDataHeader.get(i),childProjects);
        }
        dh.closeDB();
    }

    public void EnableRuntimePermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]
                {
                        GET_ACCOUNTS,
                        READ_PHONE_STATE,
                        INTERNET,
                        ACCESS_NETWORK_STATE,
                        READ_EXTERNAL_STORAGE
                }, RequestPermissionCode);
    }

    private boolean checkPermissions()
    {

        int res1 = getApplicationContext().checkCallingOrSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        int res2 = getApplicationContext().checkCallingOrSelfPermission(android.Manifest.permission.ACCESS_NETWORK_STATE);
        int res3 = getApplicationContext().checkCallingOrSelfPermission(android.Manifest.permission.INTERNET);
        int res4 = getApplicationContext().checkCallingOrSelfPermission(android.Manifest.permission.READ_PHONE_STATE);
        int res5 = getApplicationContext().checkCallingOrSelfPermission(android.Manifest.permission.GET_ACCOUNTS);

        if ((res1 == PackageManager.PERMISSION_GRANTED) && (res2 == PackageManager.PERMISSION_GRANTED)
        && res3 == PackageManager.PERMISSION_GRANTED && res4 == PackageManager.PERMISSION_GRANTED
        && res5 == PackageManager.PERMISSION_GRANTED){
            return true;
        }else{
            return false;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                finish();
                startActivity(getIntent());
                break;
        }
    }


}