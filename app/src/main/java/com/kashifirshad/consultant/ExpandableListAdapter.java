package com.kashifirshad.consultant;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;


public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private int HeaderCount = 0;
    private int CurrentHeaderCount = 0;
    private Context _context;
    private List<Project> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<Project, List<Project>> _listDataChild;

    public ExpandableListAdapter(Context context, List<Project> listDataHeader,
                                 HashMap<Project, List<Project>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.HeaderCount = listDataHeader.size() ;
    }

    @Override
    public Project getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        Project proj = (Project)getChild(groupPosition, childPosition);
        String childText = proj.getStory();

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }
        TextView txtListChild= (TextView) convertView
                .findViewById(R.id.lblListItem);


        if(childText == "Add New Story"){
            Button btn = (Button) convertView.findViewById(R.id.btnAddStory);
            btn.setVisibility(View.VISIBLE);
            btn.setTag(this._listDataHeader.get(groupPosition));
            txtListChild.setVisibility(View.GONE);
        }

        if(childText.length() > 30){
            childText = childText.substring(0,30)+" ...";
        }

        String filePaths = proj.getFilePaths();
//        if(filePaths != null && !filePaths.equals(""))
        if(proj.getIsRead() == 0 && proj.getParentId() != 0){
            childText = "* " +childText;
        }

        txtListChild.setText(childText);
        txtListChild.setTag(proj);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        List<Project> projList = (List)this._listDataChild.get(this._listDataHeader.get(groupPosition));
        int vSize = 0;
        if(projList != null)
             vSize = projList.size();

        return vSize;
    }

    @Override
    public Project getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = getGroup(groupPosition).getUser().getEmailAddress().replace("@gmail.com","")+"-" + getGroup(groupPosition).getStory();
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
        if(!isExpanded)
        if(CurrentHeaderCount < HeaderCount) {
            ExpandableListView eLV = (ExpandableListView) parent;
            eLV.expandGroup(groupPosition);
            CurrentHeaderCount++;
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void setNewItems(List<Project> listDataHeader, HashMap<Project, List<Project>> listChildData) {
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        CurrentHeaderCount = HeaderCount-1;
        notifyDataSetChanged();
    }

    public void onClickStory(View v) {
        switch (v.getId()) {

            case R.id.btnAddStory:
                Project proj = (Project) v.getTag();
                Intent intent = new Intent(this._context, AddStoryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("projId",proj.getId());
                bundle.putString("projTitle", proj.getStory());
                intent.putExtras(bundle);

//                startActivity(intent);


                break;

            default:
                break;
        }

    }
}