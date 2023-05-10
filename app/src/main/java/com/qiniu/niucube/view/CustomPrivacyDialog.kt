package com.qiniu.niucube.view

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import com.qiniu.niucube.R
import com.qiniu.qlogin.QAuth
import com.qiniu.qlogin_core.PrivacyAlertDialogBuilder


class CustomPrivacyDialogBuilder : PrivacyAlertDialogBuilder {
    override fun show(activity: Activity): AlertDialog {
        val dialog = AlertDialog.Builder(activity).create()
        val view =
            LayoutInflater.from(activity).inflate(R.layout.login_demo_dialog_privacy, null, false)
        view.findViewById<View>(R.id.login_demo_privace_cancel)
            ?.setOnClickListener(
                OnClickListener { //点击取消，将授权页协议勾选框设置勾选状态
                    QAuth.setPrivacyCheckBoxValue(false)
                    dialog.dismiss()
                })
        view.findViewById<View>(R.id.login_demo_privacy_ensure)
            ?.setOnClickListener(
                OnClickListener { //点击同意，将授权页协议勾选框设置勾选状态
                    QAuth.setPrivacyCheckBoxValue(true)
                    dialog.dismiss()
                })
        dialog.show()
        dialog.setContentView(view)
        return dialog;
    }
}



