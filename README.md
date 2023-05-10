# 安卓快速集成文档
## 体验demo
下载[app文件](https://github.com/pili-engineering/QAuth_Android/blob/main/app-debug.apk)

## 快速跑通demo
* 申请七牛一键登录appID
* 修改build.gradle文件下的包名、签名文件、appID、appKey信息
* 运行demo工程

## 本地集成
### 导入sdk
下载sdk:QAuth-sdk-x.x.x.aar,导入你的安卓工程

### 权限配置

必要权限：

```<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
<uses-permission android:name="android.permission.CHANGE_NETWORK_STATE"/>
<uses-permission android:name="android.permission.CHANGE_WIFI_STATE"/>
```


| 权限名称             | 权限说明                 | 使用说明                                    |
|----------------------|--------------------------|---------------------------------------------|
| INTERNET             | 允许应用程序联网         | 用于访问网关和认证服务器                    |
| ACCESS_WIFI_STATE    | 允许访问WiFi网络状态信息 | 允许程序访问WiFi网络状态信息                |
| ACCESS_NETWORK_STATE | 允许访问网络状态         | 区分移动网络或WiFi网络                      |
| CHANGE_NETWORK_STATE | 允许改变网络连接状态     | 设备在WiFi跟数据双开时，强行切换使用数据网络 |
| CHANGE_WIFI_STATE    | 允许改变WiFi网络连接状态 | 设备在WiFi跟数据双开时，强行切换使用         |


### 配置对http协议的支持
两种方式（任选其一）：
方式一：在application标签中配置usesCleartextTraffic，示例如下

```<application
    android:name=".view.MyApplication"
    ***
    android:usesCleartextTraffic="true"
    ></application>
```

方式二：将域名10010.com设置为白名单，示例如下
```<application
    android:name=".view.MyApplication"
    ***
    android:networkSecurityConfig="@xml/network_security_config"
    ></application>

```
network_security_config.xml文件：
```<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">10010.com</domain>
    </domain-config>
</network-security-config>
```

## 一键登录api

### 初始化
调用SDK其他流程方法前，请确保已调用过初始化，否则会返回未初始化。建议在application.onCeate()中初始化。

示例代码
```java 
QAuth.init(this, "your appID", "your appKey")
```
方法原型
```java
/**
 * @param context applicationContext
 * @param appID   七牛一键登录appID
 * @param appKey  七牛一键登录appKey
 */
public static void init(Context context, String appID, String appKey) 
```
### 预取号

获取取号临时凭证；建议在调用拉起授权页前2~3秒调用，可以缩短拉起授权页耗时；如果启动app就需要展示授权页，中间没有2~3秒的间隔，不建议调用，起不到缩短时间的效果。

示例代码
```java
QAuth.preMobile(new QCallback<Void>() {
    @Override
    public void onError(int code, @NonNull String msg) {}

    @Override
    public void onSuccess(@Nullable Void data) {}
});
```

### 拉起授权页&获取token
* 拉起授权页方法将会调起运营商授权页面。已登录状态请勿调用 。
* 每次调用拉起授权页方法前均需先调用授权页配置方法，否则授权页可能会展示异常。
* 必须保证上一次拉起的授权页已经销毁再调用，否则SDK会返回请求频繁。

示例代码
```java
QAuth.openLoginAuth(this, new QCallback<String>() {
    @Override
    public void onError(int code, @NonNull String msg) {}
    @Override
    public void onSuccess(@Nullable String data) {}
});
```
方法原型
```java
/**
 * @param activity 当前activity
 * @param callback 操作回调
 */
public static void openLoginAuth(Activity activity, QCallback<String> callback)
```
当callback回调成功会获取到置换手机号所需的token。请参考「服务端」文档来实现获取手机号码的步骤

### 其他api
#### 关闭授权页面
```java
QAuth.closeAuthActivity()
```
方法原型
```java
public static void closeAuthActivity()
```

#### 设置协议
需要在授权页activity创建后并且未销毁的情况下调用。

示例代码
```java
QAuth.setPrivacyCheckBoxValue(false)
```
方法原型
```java
public static void setPrivacyCheckBoxValue(boolean state) 
```

#### 清理预取号缓存
示例代码
```java
QAuth.clearScripCache(this)
```
方法原型
```java
/**
 * @param context 安卓上下文
 */
public static void clearScripCache(Context context) 
```

#### 获取运营商类型
示例代码
```kotlin
String operatorType = QAuth.getOperatorType(this)
```
方法原型
```java
/**
 * @param context
 * @return 四种情况：CMCC（移动）；CUCC（联通）；CTCC（电信）； Unknown_Operator（无SIM卡或非三网运营商）
 */
public static String getOperatorType(Context context)  
```

#### 设置预取号超时时间
不建议设置小于4的值，否则可能会导致超时的概率增加。
示例代码
```java
QAuth.setTimeOutForPreLogin(3)
```
方法原型
```java
/**
 * @param timeSecond 秒
 */
public static void setTimeOutForPreLogin(int timeSecond) 
```


## 授权页界面配置

### 设计规范

<img src="http://qrnlrydxa.hn-bkt.clouddn.com/onekeylogin/asimg.png" width=400 height=400 />
<img src="http://qrnlrydxa.hn-bkt.clouddn.com/onekeylogin/WechatIMG39.jpeg" width=256 height=512 />

<img src="http://qrnlrydxa.hn-bkt.clouddn.com/onekeylogin/WechatIMG37.jpeg" width=256 height=512 />
<img src="http://qrnlrydxa.hn-bkt.clouddn.com/onekeylogin/WechatIMG36.jpeg" width=256 height=512 />
<img src="http://qrnlrydxa.hn-bkt.clouddn.com/onekeylogin/WechatIMG38.jpeg" width=512 height=255 />

**注意：开发者不得将授权页面的登录按钮、隐私栏、号码栏内容隐藏、覆盖，对于接入七牛一键登录SDK并上线的应用，我方和运营商会对授权页面做审查，如果有出现未按要求设计授权页面，我方有权将应用的一键登录功能下线。
登录按钮ID：qn_btn_one_key_login
隐私栏ID：qn_ll_privacy、qn_checkbox_privacy_status、qn_tv_privacy_text
号码栏ID：qn_tv_per_code** 

### 自定义修改授权页布局修改
步骤一 、拷贝[qlogin_activity_quick_login.xml](https://github.com/pili-engineering/QAuth_Android/blob/main/app/src/main/res/layout/qlogin_activity_quick_login.xml)并重命名至你的项目res->layout下
按需修改样式
步骤二 、按需设置配置项目
``` java
QUIConfig uiConfig = new QUIConfig();

//配置登录页面UI
LoginPage loginPage = new LoginPage();
//授权页面布局文件
loginPage.customLayoutID = R.layout.custom_activity_quick_login;
//提示同意隐私协议的弹窗
loginPage.privacyAlertDialogBuilder = new CustomPrivacyDialogBuilder();
//入场动画
loginPage.actInAnimalResName = "login_demo_bottom_in_anim";
//出场动画
loginPage.actOutAnimalResName = "login_demo_bottom_out_anim";
//状态栏
StatusBarConfig statusBarConfig = new StatusBarConfig();
//状态栏文字模式 白色/黑色
statusBarConfig.isLightColor = true;
//状态栏颜色
statusBarConfig.statusBarColor = Color.parseColor("#FFF15959");
loginPage.statusBarConfig = statusBarConfig;
//自定义勾选隐私协议提示
loginPage.checkTipText = "请勾选隐私协议";

//设置登录页面
uiConfig.loginPage = loginPage;

//隐私协议页配置
PrivacyPage privacyPage = new PrivacyPage();
//状态栏
privacyPage.statusBarConfig = statusBarConfig;
//横竖屏
privacyPage.isVerticalActivity = true;
//自定义布局文件
privacyPage.customLayoutID = R.layout.custom_activity_privacy;
//设置协议页面
uiConfig.privacyPage = privacyPage;

QAuth.setUIConfig(uiConfig);
``` 
### 自定义隐私协议

``` 
<com.qiniu.qlogin_core.view.PrivacyTextView
    android:gravity="center"
    android:id="@+id/qn_tv_privacy_text"
    android:layout_height="wrap_content"
    android:layout_marginStart="5dp"
    android:layout_width="wrap_content"
    android:textColor="#222222"
    android:textSize="11sp"
    app:innerPrivacyColor="#FFF15959"
    app:privacy_color1="@color/purple_500"
    app:privacy_color2="@color/teal_700"
    app:privacy_text1="《七牛云服务用户协议》"
    app:privacy_text2="《隐私权政策》"
    app:privacy_tip="同意 %s %s 和 %s 并授权获取本机号码"
    app:privacy_url1="https://www.qiniu.com/privacy-right"
    app:privacy_url2="https://www.qiniu.com/user-agreement" />
```

配置内置协议的属性和自定义协议的属性，最多支持配置4个自定义协议

| 属性              | 格式   | 描述                                           |
|:------------------|--------|------------------------------------------------|
| privacy_tip       | string | 显示文本，协议名字用%s代替（预留内置一个协议占位） |
| innerPrivacyColor | color  | 内置协议的颜色                                 |
| privacy_text1     | string | 自定义协议1名称                                |
| privacy_color1    | color  | 自定义协议1颜色                                |
| privacy_url1      | string | 定义协议1跳转路径                              |
| privacy_text...   |        |                                                |


### 混淆
aar中自带混淆文件无需配置

###  资源压缩过滤
如果使用AndResGuard资源压缩，需添加过滤，参考demo示例：

```
"R.anim.umcsdk*",
"R.drawable.umcsdk*",
"R.layout.layout_shanyan*",
"R.id.shanyan_view*",
```
如果使用系统shrinkResources true，需要在raw/keep里面配置资源过滤keep.xml：
```<?xml version="1.0" encoding="utf-8"?>
<resources xmlns:tools="http://schemas.android.com/tools" tools:keep="R.anim.umcsdk*,
    R.drawable.umcsdk*,
    R.layout.layout_shanyan*,
    R.id.shanyan_view*" />
```

## 错误码

sdk错误码
|**返回码**|**返回码描述**|
| :- | :- |
|10001001|运营商通道关闭|
|10001002|运营商信息获取失败|
|10001003|一键登录获取token失败|
|10001007|网络请求失败|
|10001011|点击返回，用户取消免密登录|
|10001014|未知错误|
|10001016|APPID为空|
|10001018|无网络（缺少时间参数）|
|10001021|运营商信息获取失败（accessToken失效）|
|10001023|预初始化失败|
|10001025|非联通号段（目前联通号段 46001 46006 46009）针对联通定制版|
|10001031|请求频繁|
|10001032|账户禁用|



移动
|返回码|返回码描述|
| - | - |
|103000|成功|
|102507|登录超时（授权页点登录按钮时）|
|103101|请求异常|
|103102|包签名错误（社区填写的appid和对应的包名包签名必须一致）|
|103111|错误的运营商请求（可能是用户正在使用代理或者运营商判断失败导致）|
|103119|appid不存在|
|103211|其他错误，联系技术支撑解决问题|
|103412|无效的请求（1.加密方式错误；2.非json格式；3.空请求等）|
|103414|参数校验异常|
|103511|服务器ip白名单校验失败|
|103811|token为空|
|103902|scrip失效（短时间内重复登录）|
|103911|token请求过于频繁，10分钟内获取token且未使用的数量不超过30个|
|104201|token已失效或不存在（重复校验或失效）|
|105001|联通取号失败|
|105002|移动取号失败|
|105003|电信取号失败|
|105012|不支持电信取号|
|105013|不支持联通取号|
|105018|token权限不足（使用了本机号码校验的token获取号码）|
|105019|应用未授权（未在开发者社区勾选能力）|
|105021|当天已达取号限额|
|105302|appid不在白名单|
|105312|余量不足（体验版到期或套餐用完）|
|105313|非法请求|
|200005|用户未授权（READ\_PHONE\_STATE）|
|200010|无法识别sim卡或没有sim卡（android）|
|200015|短信验证码格式错误|
|200020|用户取消登录|
|200021|数据解析异常|
|200022|无网络|
|200023|请求超时|
|200024|数据网络切换失败|
|200025|未知错误一般出现在线程捕获异常，请配合异常打印分析|
|200026|输入参数错误|
|200027|未开启数据网络或网络不稳定|
|200028|网络异常|
|200038|异网取号网络请求失败|
|200039|异网取号网关取号失败|
|200040|UI资源加载异常|
|200048|用户未安装sim卡|
|200050|EOF异常|
|200060|切换账号（未使用SDK短验时返回）|
|200072|CA根证书校验失败|
|200080|本机号码校验仅支持移动手机号|
|200082|服务器繁忙|
|200087|授权页成功调起|

联通
|**状态码（status）**|**信息（msg）**|**示例说明**|
| :-: | :-: | :-: |
|101001|授权码不能为空使用SDK|调用置换接口时没有填入授权码|
|101002|认证的手机号不能为空使用SDK|认证置换时没有填入需要认证的手机号码|
|101003|UiConfig|不能为空调用openActivity|
|101004|ApiKey 或PublicKey 不能为空|未进行初始化，调用SDKManager.init()进行初始化|
|101005|超时|超过了接入方设置的时间|
|101006|公钥出错|公钥错误，请核对配置的公钥是否与申请的公钥一致|
|101007|用户取消登录|免密登录时，进入授权页执行了返回操作|
|102001|选择流量通道失败|<p>取号功能必须使用流量访问，在wifi 和流量同时开启的情况下，</p><p>sdk 会选择使用流量进行访问，此返回码代表切换失败！（受不</p><p>同机型的影响）</p>|
|201001|操作频繁请稍后再试|超出10 分钟之内只能访问30 次的限制|
|302001|SDK 解密异常|服务端返回数据时sdk 会进行解密操作，如果解密出错则出现此错误|
|302002|网络访问异常sdk|网络请求异常|
|302003|服务端数据格式出错|服务端返回数据格式错误|
|10000|请求超时|移动网络复杂，超时时间设置过短时，容易发生超时错误。 建议超时时间设置的长一点，3秒以上。|
|10001|获取token失败，请先调用预取号接口||
|~~10002~~|~~服务响应解析异常~~|~~取号服务端返回的数据无法正常解析~~|
|10003|无法切换至数据网络|wifi和蜂窝数据网络都开启的情况下， 无法强制取号请求从蜂窝数据网络发出。|
|10004|数据网络未开启|检测到蜂窝数据网络没有开启。|
|~~10005~~|~~网络判断异常~~|~~在进行网络开通情况判断和切换过程中捕获的异常~~|
|10007|预取号过期|标准UI版本使用|
|~~10010~~|~~Http状态码是200，302之外的值~~|~~取号接口只处理200和302状态码，其他都作为失败处理。~~|
|10011|Https通讯抛出异常|取号接口用到的HttpsURLConnection通信抛出的异常|
|~~10012~~|~~200 但body为空~~||
|~~10013~~|~~跳转地址错误~~|~~基本不会发生~~|
|10021|初始化失败||
|~~10022~~|~~网络请求响应为空~~||
|10024|Http通讯抛出异常|取号接口用到的HttpURLConnection通信抛出的异常|
|10025|ios sdk用到的部分异常||
|10026|ios sdk用到的socket部分错误||
|100|应用未授权||
|101|应用秘钥错误|该应用秘钥即为client\_secret。<br>1\. 核对**应用秘钥**是否正确<br>2\. 联系客服详细处理|
|102|应用无效|1\. client\_id字段未传<br>2\. client\_id字段值错误|
|~~103~~|~~应用未授权该IP访问~~||
|104|应用访问次数不足||
|105|应用包名不正确|注册应用时填写的包名与实际包名不符，核对包名报备是否正确|
|106|应用状态非法|应用处于非正常状态|
|107|商户状态非法|商户处于非正常状态|
|108|商户请求次数超限额||
|200|tokenId无效||
|201|token已失效|登录接口token无效标识。<br>token即预取号获得的accessCode值，默认有效期**30min**且**单次有效**。<br>可能原因如下：<br>1\. 已过有效期；<br>2\. 已被消费；<br>3\. 使用不存在的token；<br>4\. 应用标识与token不匹配。例：用应用标识A获取token，而用应用标识B去消费|
|202|token未授权该应用访问||
|203|登录鉴权级别不满足接口鉴权要求||
|300|接口未开放||
|301|应用未授权访问该接口|client\_id无访问相关接口权限，联系客服详细处理|
|302|IP 未授权码访问该接口|核对client\_id所配**公网IP**是否正确|
|303|应用访问接口次数超日限额||
|400|请求参数为空||
|401|请求参数不完整|核对必填参数：<br>1\. 是否传值<br>2\. 字段名是否正确|
|402|请求参数非法|1\. timeStamp间隔时间太久<br>2\. 其他参数传了不可识别的值：请检查请求参数是否与接口文档相符|
|600|请求非法||
|1000|请求解析错误|服务端无法解析请求参数，请检查请求参数是否与接口文档相符|
|1001|请求已失效|请求时间戳与中国标准时间间隔太久。<br>处理建议：请使用中国标准时间|
|1002|验签失败|1\. 核对sign生成规则<br>2\. 核对调用生成sign的method<br>3\. 核对**公私钥**是否匹配|
|1003|授权码已过期|认证接口token失效标识。<br>token即预取号获得的accessCode值，默认有效期**30min**且**单次有效**。<br>可能原因如下：<br>1\. 已过有效期；<br>2\. 已被消费；<br>3\. 使用不存在的token；<br>4\. 应用标识与token不匹配。例：用应用标识A获取token，而用应用标识B去消费|
|1004|加密方式不支持||
|1005|RSA加密错误||
|1010|服务间访问失败||
|1011|服务间访问错误|系统内部访问，未得到正确结果，联系客服详细处理。|
|~~2004~~|~~用户不存在~~||
|~~3002~~|~~跳转异网取号~~||
|~~3003~~|~~本网执行取号失败,不需要重定向~~||
|~~3004~~|~~NET取号失败~~||
|~~3005~~|~~上网方式为WIFI，无法取号~~||
|~~3006~~|~~urlencode编码失败~~||
|~~3007~~|~~请求认证接口异常~~||
|~~3009~~|~~非联通号码~~||
|3010|网关取号错误||
|3011|源IP鉴权失败|1\. 当前非联通数据网络<br>2\. APN为3gwap，目前仅支持3gnet<br>3\. 物联网卡|
|3012|网关取号失败|服务内部错误，联系客服详细处理|
|3013|电信网关取号失败||
|3014|电信网关取号错误||
|3015|回调消息缓存已失效||
|3016|移动网关取号失败||
|3017|移动网关取号错误||
|3018|生成授权码失败||
|3032|APPID不存在||
|3050|取号网关内部错误||
|3051|公网IP校验错误|1\. 当前非联通数据网络<br>2\. APN为3gwap，目前仅支持3gnet<br>3\. 物联网卡|
|3052|公网IP无法找到对应省份||
|~~3053~~|~~公网IP省份编码与输入不符~~||
|3054|私网IP校验错误||
|3055|私网IP查找号码失败||
|3056|省份暂不支持取号||
|3057|网关鉴权码查找号码失败||
|3058|网关鉴权码格式错误||
|3059|网关鉴权码已失效||
|3060|网关账号认证失败||
|3061|网关取号配额不足||
|3062|IP未授权访问网关||
|3063|网关并发连接数受限||
|3064|访问网关参数非法||
|3065|未授权访问该网关能力||
|3066|网关服务暂时不可用||

电信
|错误码|含义|
| :-: | :-: |
|0|请求成功|
|-64|permission-denied(无权限访问)|
|-65|API-request-rates-Exceed-Limitations(调用接口超限)|
|-10001|取号失败|
|-10002|参数错误|
|-10003|解密失败|
|-10004|ip受限|
|-10005|异网取号回调参数异常|
|-10006|Mdn取号失败，且属于电信网络|
|-10007|重定向到异网取号|
|-10008|超过预设取号阈值|
|-10009|时间戳过期|
|-20005|sign-invalid(签名错误）|
|-20006|应用不存在|
|-20007|公钥数据不存在|
|-20100|内部解析错误|
|-20102|加密参数解析失败|
|-30001|时间戳非法|
|-30003|topClass失效，请查看5.3+5.4常见问题。|
|51002|参数为空|
|51114|无法获取手机号数据|
|80000|请求超时|
|80001|请求网络异常|
|80002|响应码错误|
|80003|无网络连接|
|80004|移动网络未开启|
|80005|Socket超时异常|
|80006|域名解析异常|
|80007|IO异常|
|80008|No route to host|
|80009|nodename nor servname provided, or not known|
|80010|Socket closed by remote peer|
|80100|登录结果为空|
|80101|登录结果异常|
|80102|预登录异常|
|80103|SDK未初始化|
|80104|未调用预登录接口|
|80105|加载nib文件异常|
|80200|用户关闭界面|
|80201|其他登录方式|
|80800|WIFI切换异常|
|80801|WIFI切换超时|




