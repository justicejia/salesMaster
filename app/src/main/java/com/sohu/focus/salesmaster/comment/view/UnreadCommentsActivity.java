package com.sohu.focus.salesmaster.comment.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.sohu.focus.salesmaster.base.SingleFragmentActivity;

/**
 * Created by luckyzhangx on 14/03/2018.
 */

public class UnreadCommentsActivity extends SingleFragmentActivity {

    public static void showUnreadComments(Context context) {
        Intent intent = new Intent(context, UnreadCommentsActivity.class);
        ContextCompat.startActivity(context, intent, null);
    }

    @Override
    protected Fragment onGetFragment() {
        return new UnreadCommentsFragment();
    }

    @Override
    protected Bundle onGetArguments() {
        return null;
    }
}
