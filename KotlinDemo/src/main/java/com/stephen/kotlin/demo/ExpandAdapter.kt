package com.stephen.kotlin.demo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.stephen.kotlin.demo.bean.Children
import com.stephen.kotlin.demo.bean.ChildrenX
import kotlinx.android.synthetic.main.item_expand_item.view.*
import java.util.ArrayList

class ExpandAdapter(val context: Context,val mGroup: ArrayList<Children>,val mItemList: ArrayList<ArrayList<ChildrenX>>) : BaseExpandableListAdapter(){
    private lateinit var mInflater: LayoutInflater

    init {
        mInflater = LayoutInflater.from(context)
    }

    override fun getGroupCount(): Int {
        return mGroup.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return mItemList[groupPosition].size
    }

    override fun getGroup(groupPosition: Int): Any {
        return mGroup[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return mItemList[groupPosition][childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View? {
        var view: View? = convertView
        if(null == view)view = mInflater.inflate(R.layout.item_expand_group, parent, false)
        view!!.findViewById<TextView>(R.id.nameT)?.text = mGroup[groupPosition].name ?: "-"
        view!!.findViewById<TextView>(R.id.info0T)?.text = "${mGroup[groupPosition].today.confirm}" ?: "-"
        view!!.findViewById<TextView>(R.id.info1T)?.text = "${mGroup[groupPosition].total.confirm}" ?: "-"
        view!!.findViewById<TextView>(R.id.info2T)?.text = "${mGroup[groupPosition].total.heal}" ?: "-"
        view!!.findViewById<TextView>(R.id.info3T)?.text = "${mGroup[groupPosition].total.dead}" ?: "-"
        view!!.findViewById<TextView>(R.id.info4T)?.text = "${mGroup[groupPosition].total.deadRate}%" ?: "-%"
        view!!.findViewById<ImageView>(R.id.arrowImgV)?.setImageDrawable(ResourcesCompat.getDrawable(context.resources,
            if(isExpanded) R.drawable.icon_arrow_up else R.drawable.icon_arrow_down, null))
        view!!.tag = mGroup[groupPosition].name ?: "-"
        return view
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View? {
        var view: View? = convertView
        if(null == view)view = mInflater.inflate(R.layout.item_expand_item, parent, false)
        val tvChild: TextView = view!!.findViewById(R.id.nameT)
        val child = mItemList[groupPosition][childPosition]
        tvChild.text = child.name ?: "-"
        view!!.findViewById<TextView>(R.id.info0T)?.text = "${child.today.confirm}" ?: "-"
        view!!.findViewById<TextView>(R.id.info1T)?.text = "${child.total.confirm}" ?: "-"
        view!!.findViewById<TextView>(R.id.info2T)?.text = "${child.total.heal}" ?: "-"
        view!!.findViewById<TextView>(R.id.info3T)?.text = "${child.total.dead}" ?: "-"
        view!!.findViewById<TextView>(R.id.info4T)?.text = "${child.total.deadRate}%" ?: "-%"
        view!!.tag = child.name ?: "-"
        /*tvChild.setOnClickListener{
            Toast.makeText(context, "===>$child", Toast.LENGTH_LONG).show()
        }*/
        return view
    }

    override fun isChildSelectable(p0: Int, p1: Int): Boolean {
        return true
    }
}