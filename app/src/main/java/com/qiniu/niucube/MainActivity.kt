package com.qiniu.niucube

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.qiniu.niucube.view.AbScreenUtils
import com.qiniu.niucube.view.CustomPrivacyDialogBuilder
import com.qiniu.qlogin.QAuth
import com.qiniu.qlogin_core.DialogStyleConfig
import com.qiniu.qlogin_core.LoginPage
import com.qiniu.qlogin_core.Privacy
import com.qiniu.qlogin_core.PrivacyClickListener
import com.qiniu.qlogin_core.PrivacyPage
import com.qiniu.qlogin_core.QCallback
import com.qiniu.qlogin_core.QUIConfig
import com.qiniu.qlogin_core.StatusBarConfig
import com.qiniu.qlogin_core.inner.QLogUtil
import com.qiniu.qlogin_core.inner.backGround
import com.qiniu.qlogin_core.inner.http.HttpClient
import com.qiniu.qlogin_core.inner.mode.HttpResp
import org.json.JSONObject
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class MainActivity : AppCompatActivity() {

    companion object {
        init {
            QAuth.preMobile(object : QCallback<Void> {
                override fun onSuccess(data: Void?) {
                    Log.d("QAuth", "预取号成功")
                }

                override fun onError(code: Int, msg: String) {
                    Log.d("QAuth", "预取号失败 ")
                }
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("QAuth", "preMobile")

        val openLoginAuthCallback = object : QCallback<String> {
            override fun onSuccess(data: String?) {
                Log.d("QAuth", "openLoginAuthCallback $data ")
                Toast.makeText(this@MainActivity, "授权成功 $data", Toast.LENGTH_SHORT).show()

                //模拟接入获取token
                backGround {
                    doWork {
                        val ret = postHttpReq(
                            "https://niucube-api.qiniu.com/v1/verify/login",
                            JSONObject().apply {
                                put("token", data)
                            }.toString(), null
                        )
                        Log.d("QAuth", "openLoginAuthCallback-postHttpReq ${ret.data} ")
                        Toast.makeText(
                            this@MainActivity,
                            "获取号码 ${JSONObject(ret.data).opt("mobile")}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    catchError {
                        it.printStackTrace()
                        Toast.makeText(
                            this@MainActivity,
                            "获取号码失败 ${it.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }

            override fun onError(code: Int, msg: String) {
                Log.d("QAuth", "onError $code $msg ")
                Toast.makeText(this@MainActivity, "授权失败 $code $msg", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<View>(R.id.btn1).setOnClickListener {
            QAuth.setUIConfig(null)
            QAuth.openLoginAuth(true, this, openLoginAuthCallback)
        }

        findViewById<View>(R.id.btn2).setOnClickListener {
            QAuth.setUIConfig(QUIConfig().apply {
                loginPage = LoginPage().apply {
                    isVerticalActivity = false
                }
            })
            QAuth.openLoginAuth(true, this, openLoginAuthCallback)
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

            QAuth.openLoginAuth(true, this, openLoginAuthCallback)
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

            QAuth.openLoginAuth(true, this, openLoginAuthCallback)
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

                    //使用代码方式配置协议-（也可以在xml配置-代码配置优先级高于xml
                    privacyTextTip = "同意 %s 和 %s 并授权获取本机号码"
                    privacyList = listOf(
                        Privacy(
                            "《七牛云服务用户协议》",
                            Color.parseColor("#FF6200EE"),
                            "https://www.qiniu.com/privacy-right"
                        )
                    )
                    //自定义隐私协议跳转
                    privacyClickListener = PrivacyClickListener { context, url, privacyTittle ->
                        Toast.makeText(
                            context,
                            " 点击了 $privacyTittle  $url ",
                            Toast.LENGTH_SHORT
                        ).show()
                        true
                    }
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

            QAuth.openLoginAuth(true, this, openLoginAuthCallback)
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
                    privacyClickListener = PrivacyClickListener { context, url, privacyTittle ->
                        Toast.makeText(
                            context,
                            " 点击了 $privacyTittle  $url ",
                            Toast.LENGTH_SHORT
                        ).show()
                        false
                    }
                }
            })
            QAuth.openLoginAuth(true, this, openLoginAuthCallback)
        }

        findViewById<View>(R.id.btn7).setOnClickListener {
            val phoneText = findViewById<EditText>(R.id.tvPhone).text.toString()
            if (phoneText.isEmpty()) {
                Toast.makeText(this, "请输入号码", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            QAuth.mobileAuth(object : QCallback<String> {
                override fun onSuccess(data: String?) {
                    Log.d("QAuth", "openLoginAuthCallback $data ")
                    Toast.makeText(this@MainActivity, "授权成功 $data", Toast.LENGTH_SHORT).show()

                    //模拟接入获取token
                    backGround {
                        doWork {
                            val ret = postHttpReq(
                                "https://niucube-api.qiniu.com/v1/verify/check",
                                JSONObject().apply {
                                    put("token", data)
                                    put("mobile", phoneText)
                                }.toString(), null
                            )
                            Toast.makeText(
                                this@MainActivity,
                                "本机校验结果 ${JSONObject(ret.data).opt("is_verify")}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        catchError {
                            it.printStackTrace()
                            Toast.makeText(
                                this@MainActivity,
                                "本机校验结果 ${it.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                }

                override fun onError(code: Int, msg: String) {
                    Log.d("QAuth", "onError $code $msg ")
                    Toast.makeText(this@MainActivity, "授权失败 $code $msg", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            )
        }
    }

    override fun onResume() {
        super.onResume()
    }

    private suspend fun postHttpReq(
        path: String,
        jsonString: String,
        headers: Map<String, String>?
    ) = suspendCoroutine<HttpResp> { continuation ->

        Thread {
            val url = URL(path)
            val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
            var writer: BufferedWriter? = null
            var inputStream: InputStream? = null
            var br: BufferedReader? = null
            var resultStr: String = ""// 返回结果字符串
            var resultCode = -1
            var resultMsg = ""
            QLogUtil.d("QLiveHttpService", " req  $path $jsonString")
            try {
                urlConnection.connectTimeout = 2000
                urlConnection.readTimeout = 2000
                urlConnection.useCaches = true
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
                urlConnection.addRequestProperty("Connection", "Keep-Alive")
                urlConnection.addRequestProperty("Q-Plat", "android")
                urlConnection.addRequestProperty("Q-Ver", "1.0.0")
                headers?.entries?.forEach {
                    urlConnection.setRequestProperty(it.key, it.value)
                }
                urlConnection.requestMethod = "POST"
                writer = BufferedWriter(OutputStreamWriter(urlConnection.outputStream, "UTF-8"))
                writer.write(jsonString)
                writer.flush()
                resultCode = urlConnection.responseCode
                resultMsg = urlConnection.responseMessage
                QLogUtil.d(" response  code-> $resultCode resultMsg-> $resultMsg")
                if (resultCode == HttpURLConnection.HTTP_OK) {
                    inputStream = urlConnection.inputStream
                    br = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
                    val sbf = StringBuffer()
                    var temp: String? = null
                    while (br.readLine().also { temp = it } != null) {
                        sbf.append(temp)
                        sbf.append("\r\n")
                    }
                    resultStr = sbf.toString()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                continuation.resumeWithException(Exception("${e.message}"))
                return@Thread
            } finally {
                try {
                    writer?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                try {
                    br?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                try {
                    inputStream?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                urlConnection.disconnect() // 关闭远程连接
            }

            QLogUtil.d(" response ->  $path $jsonString   $resultCode resultStr-> $resultStr")
            if (resultCode == HttpURLConnection.HTTP_OK) {
                val jsonObj = JSONObject(resultStr)
                continuation.resume(HttpResp().apply {
                    httpCode = 200
                    bzCode = jsonObj.optInt("code")
                    message = jsonObj.optString("message")
                    requestID = jsonObj.optString("request_id")
                    data = jsonObj.optString("data")
                })
            } else {
                continuation.resumeWithException(Exception("httpCode-> $resultCode msg->$resultMsg"))
            }
        }.start()

    }
}