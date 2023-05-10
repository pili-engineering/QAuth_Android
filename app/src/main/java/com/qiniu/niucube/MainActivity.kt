package com.qiniu.niucube

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.qiniu.niucube.view.AbScreenUtils
import com.qiniu.niucube.view.CustomPrivacyDialogBuilder
import com.qiniu.qlogin.QAuth
import com.qiniu.qlogin_core.DialogStyleConfig
import com.qiniu.qlogin_core.LoginPage
import com.qiniu.qlogin_core.PrivacyPage
import com.qiniu.qlogin_core.QCallback
import com.qiniu.qlogin_core.QUIConfig
import com.qiniu.qlogin_core.StatusBarConfig

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("QAuth", "preMobile")
        QAuth.preMobile(object : QCallback<Void> {
            override fun onSuccess(data: Void?) {
                Log.d("QAuth", "预取号成功")
                Toast.makeText(this@MainActivity, "预取号成功", Toast.LENGTH_SHORT).show()
            }

            override fun onError(code: Int, msg: String) {
                Log.d("QAuth", "预取号失败 ")
                Toast.makeText(this@MainActivity, "预取号失败 ${msg}", Toast.LENGTH_SHORT).show()
            }
        })
        val openLoginAuthCallback = object : QCallback<String> {
            override fun onSuccess(data: String?) {
                Log.d("QAuth", "openLoginAuthCallback $data ")
                Toast.makeText(this@MainActivity, "授权成功 $data", Toast.LENGTH_SHORT).show()
            }

            override fun onError(code: Int, msg: String) {
                Log.d("QAuth", "onError $code $msg ")
                Toast.makeText(this@MainActivity, "授权失败 $code $msg", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<View>(R.id.btn1).setOnClickListener {
            QAuth.setUIConfig(null)
            QAuth.openLoginAuth(this, openLoginAuthCallback)
        }

        findViewById<View>(R.id.btn2).setOnClickListener {
            QAuth.setUIConfig(QUIConfig().apply {
                loginPage = LoginPage().apply {
                    isVerticalActivity = false
                }
            })
            QAuth.openLoginAuth(this, openLoginAuthCallback)
        }

        findViewById<View>(R.id.btn3).setOnClickListener {
            QAuth.setUIConfig(QUIConfig().apply {
                loginPage = LoginPage().apply {
                    dialogStyleConfig = DialogStyleConfig().apply {
                        dialogWidth =
                            (AbScreenUtils.getScreenWidth(this@MainActivity, true) * 0.8).toInt()
                        dialogHeight = 300
                    }
                    customLayoutID = R.layout.qlogin_activity_quick_login_b3
                }
            })

            QAuth.openLoginAuth(this, openLoginAuthCallback)
        }
        findViewById<View>(R.id.btn4).setOnClickListener {

            QAuth.setUIConfig(QUIConfig().apply {
                loginPage = LoginPage().apply {
                    dialogStyleConfig = DialogStyleConfig().apply {
                        dialogWidth =
                            (AbScreenUtils.getScreenWidth(this@MainActivity, true)).toInt()
                        dialogHeight = 300
                        isDialogBottom = true
                        dialogDimAmount = 0.3f
                    }
                    customLayoutID = R.layout.qlogin_activity_quick_login_b3
                }
            })

            QAuth.openLoginAuth(this, openLoginAuthCallback)
        }

        findViewById<View>(R.id.btn5).setOnClickListener {

            QAuth.setUIConfig(QUIConfig().apply {
                loginPage = LoginPage().apply {
                    //授权页面布局文件
                    customLayoutID = R.layout.qlogin_activity_quick_login_b5
                    //提示同意隐私协议的弹窗
                    privacyAlertDialogBuilder = CustomPrivacyDialogBuilder()
                    //入场动画
                    actInAnimalResName = "login_demo_bottom_in_anim"
                    //出场动画
                    actOutAnimalResName = "login_demo_bottom_out_anim"
                    //状态栏
                    statusBarConfig = StatusBarConfig().apply {
                        //状态栏文字模式 白色/黑色
                        isLightColor = true
                        //状态栏颜色
                        statusBarColor = Color.parseColor("#FFF15959")
                    }
                    //自定义勾选隐私协议提示
                    checkTipText = "请勾选隐私协议"
                }

                //隐私协议页配置
                privacyPage = PrivacyPage().apply {
                    //状态栏
                    statusBarConfig = StatusBarConfig().apply {
                        //状态栏文字模式 白色/黑色
                        isLightColor = true
                        //状态栏颜色
                        statusBarColor = Color.parseColor("#F15959")
                    }
                    //横竖屏
                    isVerticalActivity = true
                    //自定义布局文件
                    customLayoutID = R.layout.qlogin_activity_privacy_b5
                }
            })

            QAuth.openLoginAuth(this, openLoginAuthCallback)
        }

        findViewById<View>(R.id.btn6).setOnClickListener {

            QAuth.setUIConfig(QUIConfig().apply {
                loginPage = LoginPage().apply {
                    customLayoutID = R.layout.qlogin_activity_quick_login_b5
                    actInAnimalResName = "login_demo_bottom_in_anim"
                    actOutAnimalResName = "login_demo_bottom_out_anim"
                    statusBarConfig = StatusBarConfig().apply {
                        isLightColor = true
                        statusBarColor = Color.parseColor("#FFF15959")
                    }
                    isVerticalActivity = false
                }
            })
            QAuth.openLoginAuth(this, openLoginAuthCallback)
        }

        findViewById<View>(R.id.btn7).setOnClickListener {
            QAuth.mobileAuth(openLoginAuthCallback)
        }
    }

    override fun onResume() {
        super.onResume()
    }
}