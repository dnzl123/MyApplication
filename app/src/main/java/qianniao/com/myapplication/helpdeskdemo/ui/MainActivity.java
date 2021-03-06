/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package qianniao.com.myapplication.helpdeskdemo.ui;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.easemob.bottomnavigation.BottomNavigation;
import com.easemob.bottomnavigation.OnBottomNavigationSelectedListener;

import com.hyphenate.chat.ChatClient;
import com.hyphenate.chat.ChatManager;
import com.hyphenate.chat.Message;
import com.hyphenate.helpdesk.Error;
import com.hyphenate.helpdesk.easeui.runtimepermission.PermissionsManager;
import com.hyphenate.helpdesk.easeui.runtimepermission.PermissionsResultAction;

import java.util.List;

import qianniao.com.myapplication.R;
import qianniao.com.myapplication.helpdeskdemo.Constant;
import qianniao.com.myapplication.helpdeskdemo.DemoHelper;

public class MainActivity extends DemoBaseActivity implements OnBottomNavigationSelectedListener {

    private Fragment shopFragment;
    private Fragment settingFragment;
    private Fragment ticketListFragment;
    private Fragment[] fragments;
    private int currentTabIndex = 0;
    private MyConnectionListener connectionListener = null;
    private BottomNavigation mBottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //看不懂
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);
            }
        }

        setContentView(R.layout.em_activity_main);
        if (savedInstanceState != null){
            currentTabIndex = savedInstanceState.getInt("selectedIndex", 0);
        }
        if (shopFragment != null){
            shopFragment = getSupportFragmentManager().findFragmentByTag(shopFragment.getClass().getName());
            settingFragment = getSupportFragmentManager().findFragmentByTag(settingFragment.getClass().getName());
            ticketListFragment = getSupportFragmentManager().findFragmentByTag(ticketListFragment.getClass().getName());
            fragments = new Fragment[]{shopFragment, ticketListFragment, settingFragment};
            getSupportFragmentManager().beginTransaction().hide(shopFragment).hide(settingFragment).hide(ticketListFragment)
                    .show(fragments[currentTabIndex]).commit();

        } else {
            shopFragment = new ShopFragment();
            settingFragment = new SettingFragment();
            ticketListFragment = new TicketListFragment();
            fragments = new Fragment[]{shopFragment, ticketListFragment, settingFragment};
            // 把shopFragment设为选中状态
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.add(R.id.fragment_container, shopFragment, shopFragment.getClass().getName())
                    .add(R.id.fragment_container, ticketListFragment, ticketListFragment.getClass().getName())
                    .add(R.id.fragment_container, settingFragment, settingFragment.getClass().getName())
                    .hide(settingFragment)
                    .hide(ticketListFragment)
                    .hide(shopFragment).show(fragments[currentTabIndex]);
            trx.commit();
        }
        mBottomNav = $(R.id.bottom_navigation);
        mBottomNav.setBottomNavigationSelectedListener(this);
        //注册一个监听连接状态的listener
        connectionListener = new MyConnectionListener();
        ChatClient.getInstance().addConnectionListener(connectionListener);
        //6.0运行时权限处理，target api设成23时，demo这里做的比较简单，直接请求所有需要的运行时权限
        requestPermissions();
    }

    @TargetApi(23)
    private void requestPermissions() {
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
            @Override
            public void onGranted() {

            }

            @Override
            public void onDenied(String permission) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }


    @Override
    public void onValueSelected(int index) {
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager()
                    .beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.fragment_container, fragments[index]);
            }
            trx.show(fragments[index]).commitAllowingStateLoss();
        }
        currentTabIndex = index;
    }

    public class MyConnectionListener implements ChatClient.ConnectionListener {

        @Override
        public void onConnected() {

        }

        @Override
        public void onDisconnected(final int errorCode) {
            if (errorCode == Error.USER_NOT_FOUND || errorCode == Error.USER_LOGIN_ANOTHER_DEVICE
                    || errorCode == Error.USER_AUTHENTICATION_FAILED
                    || errorCode == Error.USER_REMOVED) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //demo中为了演示当用户被删除或者修改密码后验证失败,跳出会话界面
                        //正常APP应该跳到登录界面或者其他操作
                        if (ChatActivity.instance != null) {
                            ChatActivity.instance.finish();
                        }
                        ChatClient.getInstance().logout(false, null);
                    }
                });
            }
        }

    }

    //修改源码
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            moveTaskToBack(false);
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    public void contactCustomer(View view) {
        switch (view.getId()) {
            case R.id.ll_setting_list_customer:
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, LoginActivity.class);
                intent.putExtra(Constant.MESSAGE_TO_INTENT_EXTRA, Constant.MESSAGE_TO_DEFAULT);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (connectionListener != null) {
            ChatClient.getInstance().removeConnectionListener(connectionListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        DemoHelper.getInstance().pushActivity(this);
        ChatClient.getInstance().getChat().addMessageListener(messageListener);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selectedIndex", currentTabIndex);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentTabIndex = savedInstanceState.getInt("selectedIndex", 0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        ChatClient.getInstance().getChat().removeMessageListener(messageListener);
        DemoHelper.getInstance().popActivity(this);

    }

    ChatManager.MessageListener messageListener = new ChatManager.MessageListener() {

        @Override
        public void onMessage(List<Message> msgs) {

            /*runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //未读数可以显示在UI上
                    int unreadMsgCount = ChatClient.getInstance().getChat().getUnreadMsgsCount();
                }
            });*/
        }

        @Override
        public void onCmdMessage(List<Message> msgs) {

        }

        @Override
        public void onMessageStatusUpdate() {

        }

        @Override
        public void onMessageSent() {

        }
    };

}

