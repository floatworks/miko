package com.softtime.miko.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.softtime.miko.BmobData.inviteNotice;
import com.softtime.miko.BmobData.match;
import com.softtime.miko.R;

import java.util.Date;
import java.util.List;

import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2015/1/10.
 */
public class InviteNoticeListAdapter extends ArrayAdapter<inviteNotice> {
    private int resourceId;
    Context thecontext;
    public InviteNoticeListAdapter(Context context, int resource, List<inviteNotice> objects) {
        super(context, resource, objects);
        resourceId = resource;//调用这个Adapter的时候会传入3个参数，第二个是item的layout，把这个id保存成变量方便一会用
        thecontext = context;
    }

    /**
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     *
     * 这个getView方法是一个回调方法，自己不用去管，系统自己调用，重要的是，后面三个参数也是系统给你做好的。
     *
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        final inviteNotice inviteNotice = getItem(position);//首先是根据位置选择List里的对象
        View view ;
        //下面这个是一个优化，convertView相当于一个view的缓存，如果缓存是空，说明之前没有创建过，那就创建一下。
        if(convertView==null){
            view=LayoutInflater.from(getContext()).inflate(resourceId,null);//其实就是创建了最开始调用的时候传入的item_layoutId
        }else {
          view =convertView;
        };
        //下面就可以通过刚才生成的view，和刚才第38行的那个，获取相应位置的对象的那句代码来设置显示了
        TextView msg = (TextView) view.findViewById(R.id.tv_list_item_invite_notice_text);
        final TextView btn = (TextView) view.findViewById(R.id.btn_list_item_invite_notice_accept);
        msg.setText(inviteNotice.getFrom()+"邀请您进行互动");
        btn.setTag(inviteNotice.getObjectId());
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(thecontext,inviteNotice.getObjectId(),Toast.LENGTH_SHORT).show();
                //点击以后要更改邀请消息的状态，然后在数据库里增加一个dating
                setInviteNoticeAs1(thecontext,inviteNotice);//更改消息状态

                addMatch(thecontext,inviteNotice);//增加一个约会表

                btn.setText("已接受");
                btn.setClickable(false);

            }
        });

        return view;
    }

    /**
     * 传入context和获得的inviteNotice对象，把inviteNotice表的状态更新为1
     * @param context
     * @param inviteNotice
     */
    public void setInviteNoticeAs1(final Context context, final inviteNotice inviteNotice){
       inviteNotice.setState(1);
       inviteNotice.update(context,inviteNotice.getObjectId(),new UpdateListener() {
           @Override
           public void onSuccess() {
               Toast.makeText(context,"邀请已经接受",Toast.LENGTH_SHORT).show();

           }

           @Override
           public void onFailure(int i, String s) {
               Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
           }
       });
    }

    /**
     * 在match表里增加一行数据，表示两人建立了链接
     */
    public void addMatch(Context context,inviteNotice inviteNotice){
       // Calendar cal=Calendar.getInstance();
        //Date endDate = cal.getTime();
        Date endDate = new Date(System.currentTimeMillis()+604800000);
        match match = new match();
        match.setUid1(inviteNotice.getFrom());
        match.setUid2(inviteNotice.getTo());
        match.setEndAt(new BmobDate(endDate));
        match.save(context);

    }


}
