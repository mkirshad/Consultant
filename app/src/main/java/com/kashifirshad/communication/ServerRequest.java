package com.kashifirshad.softwareprojects;


import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by biome on 3/31/2018.
 */

public class ServerRequest {
    static Context ct;
    public ServerRequest(Context ct){
        this.ct = ct;
        RequestQueue queue = Volley.newRequestQueue(ct);

        String url = "http://www.kashifirshad.com/AndroidProjects/SyncProjects.php";
        Gson gson = new Gson();
        Projects proj = new Projects(ct);
        String json = gson.toJson(proj);
        JSONObject request = new JSONObject();
        try {
             request = new JSONObject(json);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                Log.e("Response***",response.toString());
                try {
                    JSONObject resultsOBJ = response.getJSONObject("results");
                    String updatedAt = resultsOBJ.getString("UpdatedAt");
                    JSONArray resultNewProjArr = resultsOBJ.getJSONArray("newProjects");
                    JSONArray resultNewUserArr = resultsOBJ.getJSONArray("newUsers");
                    JSONArray resultUserIdArr = resultsOBJ.getJSONArray("userIds");
                    JSONArray resultProjIdArr = resultsOBJ.getJSONArray("projectIds");

                    Log.e("updatedAt ***",updatedAt);
                    Log.e("New Projects ***",resultNewProjArr.toString());
                    Log.e("New User ***",resultNewUserArr.toString());
                    Log.e("UserIds ***",resultUserIdArr.toString());
                    Log.e("ProjectIds ***",resultProjIdArr.toString());
                    DatabaseHelper dh = new DatabaseHelper(ServerRequest.ct);
                    // Update User Ids
                    Log.e("*** 1","its here");
                    for (int i=0; i < resultUserIdArr.length(); i++) {
                        JSONObject userIds = resultUserIdArr.getJSONObject(i);
                        Long userId = userIds.getLong("UserId");
                        Long serverId = userIds.getLong("ServerUserId");
                        User user = dh.getUser(userId);
                        user.setServerId(serverId);
                        user.setUpdatedAt(updatedAt);
                        user.setSynched(1);
                        dh.updateUser(user);
                        }
                        //Update Project Ids
                    Log.e("*** 2","its here");
                    for (int i=0; i < resultProjIdArr.length(); i++) {
                        JSONObject projIds = resultProjIdArr.getJSONObject(i);
                        Long projectId = projIds.getLong("ProjectId");
                        Long serverId = projIds.getLong("ServerProjectId");
                        Project proj = dh.getProject(projectId);
                        proj.setServerId(serverId);
                        proj.setUpdatedAt(updatedAt);
                        proj.setIsSynched(1);
                        dh.updateProject(proj);
                    }
                    // Insert New Users or Update
                    Log.e("*** 3","its here");
                    for (int i=0; i < resultNewUserArr.length(); i++) {
                        JSONObject userObj = resultNewUserArr.getJSONObject(i);

                        User user = dh.getUser(0 , userObj.getLong("Id"));
                        user.setFirstName(userObj.getString("FirstName"));
                        user.setMiddleName(userObj.getString("MiddleName"));
                        user.setLastName(userObj.getString("LastName"));
                        user.setEmailAddress(userObj.getString("EmailAddress"));
                        user.setSkypeId(userObj.getString("SkypeId"));
                        user.setWatsAppNo(userObj.getString("WatsAppNo"));
                        user.setAddressLine1(userObj.getString("AddressLine1"));
                        user.setAddressLine2(userObj.getString("AddressLine2"));
                        user.setCity(userObj.getString("City"));
                        user.setCity(userObj.getString("State"));
                        user.setCountry(userObj.getString("Country"));
                        user.setShowUnreadStoriesOnly(userObj.getInt("UnReadOnly"));
                        user.setSyncDuration(userObj.getInt("SyncDuration"));
                        user.setUpdatedAt(updatedAt);
                        user.setSynched(1);
                        user.setServerId(userObj.getLong("Id"));
                        if( user.getId() == 0 )
                            dh.createUser(user);
                        else
                            dh.updateUser(user);
                    }
                    Log.e("*** 4","its here");
                    // Insert New Projects or Update
                    for (int i=0; i < resultNewProjArr.length(); i++) {
                        Log.e("*** Project Creation ", Integer.toString(i));
                        JSONObject projObj = resultNewProjArr.getJSONObject(i);
                        long ParentId = 0;
                        Log.e(" *** 7", Long.toString(projObj.getLong("ParentId")));
                        if(projObj.getLong("ParentId") != 0)
                        {
                            Project parentProj = dh.getProject(0,projObj.getLong("ParentId"));
                            Log.e("*** 5 Parent Project", parentProj.toString());
                            ParentId = parentProj.getId();
                            Log.e("*** 5 Parent Project Id", Long.toString(ParentId));

                            parentProj.setIsRead(0);
                            dh.updateProject(parentProj);
                        }
                        Log.e(" *** 8","8");
Log.e("UserId ***", Long.toString(dh.getUser(0, projObj.getLong("UserId")).getId() ));
                        Log.e(" *** 9","9");
                        Project proj = dh.getProject(0 , projObj.getLong("ServerId"));
                        proj.setStory(projObj.getString("Story"));
                        proj.setFilePaths(projObj.getString("FilePaths"));
                        proj.setEstimatedHrs(projObj.getString("EstimatedHrs"));
                        proj.setEstimateCost(projObj.getString("EstimateCost"));
                        proj.setDeliveryDate(projObj.getString("DeliveryDate"));
                        proj.setParentId( ParentId);
                        proj.setUser(dh.getUser(0, projObj.getLong("UserId")));
                        Log.e("Project User Id ***", Long.toString( proj.getUser().getId() ));
                        proj.setUpdatedAt(updatedAt);
                        proj.setIsSynched(1);
                        proj.setIsRead(0);
                        proj.setServerId(projObj.getLong("ServerId"));

                        if( proj.getId() == 0 )
                            dh.createProject(proj);
                        else
                            dh.updateProject(proj);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
                if(MainActivity.active){
                    MainActivity.mainActivity.finish();
                    MainActivity.mainActivity.startActivity(MainActivity.mainActivity.getIntent());
                }
                }
            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
            // TODO Auto-generated method stub
                        }
                    });

        queue.add(jsObjRequest);

    }
}
