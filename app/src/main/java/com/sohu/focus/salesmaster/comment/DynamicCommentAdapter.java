package com.sohu.focus.salesmaster.comment;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.CharacterStyle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.dynamics.model.DynamicCommentBean;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;

import java.util.List;


/**
 * Created by luckyzhangx on 12/03/2018.
 */

public class DynamicCommentAdapter extends BaseAdapter {

    List<DynamicCommentBean> comments;

    @Override
    public int getCount() {
        return CommonUtils.isEmpty(comments) ? 0 : comments.size();
    }

    @Override
    public DynamicCommentBean getItem(int position) {
        return CommonUtils.isEmpty(comments) ? null : comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView != null) {
            view = convertView;
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.holder_review, parent, false
            );


            ViewHolder holder = new ViewHolder();
            holder.textView = view.findViewById(R.id.review);
            view.setTag(holder);
        }


        DynamicCommentBean bean = getItem(position);
        if (bean != null) {

            String content = bean.getSalesUserName() +
                    (CommonUtils.isEmpty(bean.getReplyToUserName()) ? "" : (" 回复 " + bean
                            .getReplyToUserName()))
                    + "：" + bean.getContent();

            Spannable spanText = new SpannableString(content);

            ((ViewHolder) (view.getTag())).position = position;

            int name1Len = bean.getSalesUserName().length();
            int name2Len = CommonUtils.isEmpty(bean.getReplyToUserName()) ? 0 :
                    bean.getReplyToUserName().length();

            ((ViewHolder) (view.getTag())).applyName1Span(0, name1Len, spanText);

            if (name2Len > 0) {
                ((ViewHolder) (view.getTag())).applyName2Span(name1Len + 4, name1Len + 4 +
                                name2Len,
                        spanText);
            }

            ((ViewHolder) view.getTag()).textView.setText(spanText);
        }

        return view;
    }

    public void addAll(List<DynamicCommentBean> reviews) {
        this.comments.addAll(reviews);
        notifyDataSetChanged();
    }

    private class NameSpan extends CharacterStyle {
        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
            ds.setColor(0xff5B709E);
            ds.setTypeface(Typeface.DEFAULT);
        }
    }


    private class ViewHolder {
        int position;

        TextView textView;
        NameSpan nameSpan1 = new NameSpan();
        NameSpan nameSpan2 = new NameSpan();


        void applyName1Span(int start, int end, Spannable spanText) {
            spanText.setSpan(nameSpan1, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        }

        void applyName2Span(int start, int end, Spannable spanText) {
            spanText.setSpan(nameSpan2, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

    }

    public void setComments(List<DynamicCommentBean> reviews) {
        this.comments = reviews;
        notifyDataSetChanged();
    }

    public List<DynamicCommentBean> getComments() {
        return comments;
    }
}
