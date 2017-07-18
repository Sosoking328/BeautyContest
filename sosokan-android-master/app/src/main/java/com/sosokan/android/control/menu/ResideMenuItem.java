package com.sosokan.android.control.menu;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sosokan.android.R;

/**
 * User: special
 * Date: 13-12-10
 * Time: 下午11:05
 * Mail: specialcyci@gmail.com
 */
public class ResideMenuItem extends LinearLayout{

    /** menu item  icon  */
    private ImageButton iv_icon;
    /** menu item  title */
    private TextView tv_title;
    /** menu item  title */
    private TextView tv_count;


    public ResideMenuItem(Context context) {
        super(context);
        initViews(context);
    }

    public ResideMenuItem(Context context, int icon, int title) {
        super(context);
        initViews(context);
        iv_icon.setImageResource(icon);
        tv_title.setText(title);


    }

    public ResideMenuItem(Context context, int icon, String title,boolean visible, String count) {
        super(context);
        initViews(context);
        iv_icon.setImageResource(icon);
        tv_title.setText(title);
        if(visible)
        {
            tv_count.setVisibility(View.VISIBLE);
            tv_count.setText(count);
        }else{
            tv_count.setVisibility(View.GONE);
        }
    }

    private void initViews(Context context){
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.residemenu_item, this);
        iv_icon = (ImageButton) findViewById(R.id.iv_icon);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_count = (TextView) findViewById(R.id.tv_count);
    }

    /**
     * set the icon color;
     *
     * @param icon
     */
    public void setIcon(int icon){
        iv_icon.setImageResource(icon);
    }

    /**
     * set the title with resource
     * ;
     * @param title
     */
    public void setTitle(int title){
        tv_title.setText(title);
    }

    /**
     * set the title with string;
     *
     * @param title
     */
    public void setTitle(String title){
        tv_title.setText(title);
    }

    /**
     * set the count with string;
     *
     * @param count
     */
    public void setCount(String count){
        tv_count.setText(count);
    }
}
