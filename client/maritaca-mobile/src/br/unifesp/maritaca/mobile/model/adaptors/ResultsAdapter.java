package br.unifesp.maritaca.mobile.model.adaptors;

import java.util.ArrayList;

import br.unifesp.maritaca.mobile.activities.R;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ResultsAdapter extends BaseExpandableListAdapter {	 
	 
    private LayoutInflater inflater;
    private ArrayList<Parent> mParent;
 
    public ResultsAdapter(Context context, ArrayList<Parent> parent){
        mParent = parent;
        inflater = LayoutInflater.from(context);
    } 
 
    @Override
    public int getGroupCount() {
        return mParent.size();
    }
 
    @Override
    public int getChildrenCount(int i) {
        return mParent.get(i).getChildren().size();
    }
 
    @Override
    public Object getGroup(int i) {
        return mParent.get(i).getTitle();
    }
 
    @Override
    public Object getChild(int i, int i1) {
        return mParent.get(i).getChildren().get(i1);
    }
 
    @Override
    public long getGroupId(int i) {
        return i;
    }
 
    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }
 
    @Override
    public boolean hasStableIds() {
        return true;
    }
 
    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {    	
        if (view == null) {
            view = inflater.inflate(R.layout.result_item, viewGroup,false);
        } 
        TextView textView = (TextView) view.findViewById(R.id.name_result_item);
        textView.setText(getGroup(i).toString());
        return view;
    }
 
    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.report_list, viewGroup,false);
        } 
        TextView nameReport = (TextView) view.findViewById(R.id.name_report_item);
        nameReport.setText(mParent.get(i).getChildren().get(i1).getName());
        
        TextView urlReport = (TextView) view.findViewById(R.id.url_report_item);
        urlReport.setText(mParent.get(i).getChildren().get(i1).getUrl());
        return view;
    }
 
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
 
    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        super.registerDataSetObserver(observer);
    }
}