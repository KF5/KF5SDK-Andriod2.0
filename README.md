KF5SDK帮助开发者快速完成开发，提供给开发者创建工单、查看工单列表、回复工单、查看和搜索知识库文档、即时交谈、推送通知。目前支持minSdkVersion 14，同时KF5SDK-AndroidV2.0-beta已支持国际化。

v2.0版本的SDK较v1.0版本的sdk主要区别在于：基于Framework代码库开发，开源了UI和业务逻辑，开发者可以根据自己的需要任意定制不同风格的UI，使得SDK能更好与App无缝接入，当然开发者也可以使用SDK默认的UI以快速集成；v2.0版本对SDK的功能模块进行了分包，现已分成helpcenter、ticket、im、system四个包，system为其他三个包引用的一些必要文件、其他三个包helpcenter独立、im模块默认有入口进入到Ticket模块；如果开发者对部分功能暂无需求，可以关闭各自的入口，删掉不需要的功能包。

## 一、功能介绍

##### 1、帮助中心

帮助中心允许用户在SDK中根据权限查看和搜索您所注册的云客服平台上的知识库文档。

##### 2、创建工单

用户可以SDK中创建工单（反馈问题），反馈的问题您可以在您所注册的云客服平台的后台进行处理，用户可以通过推送或者工单列表中的红点提示方便知道创建的工单已被处理或者回复，用户也可以在SDK中回复工单。

##### 3、即时交谈

用户可以通过SDK中的即时交谈功能与客服人员实时交流，目前支持发送语音、文字、图片，接收语音、文字、图片、附件以及满意度评价。

## 二、集成步骤

1、下载[KF5SDK-Android2.0](https://github.com/KF5/KF5SDK-Andriod2.0/archive/master.zip)的官方demo；注：v2.0版本的SDK基于AndroidStudio开发，集成步骤均指的是AndroidStudio的IDE，若您的IDE是Eclipse，请[联系我们](http://www.kf5.com/)。

2、将demo里的kf5sdkModule导入到您的工程中，然后添加依赖，具体导入步骤请自行查阅相关资料，并将manifest.xml文件里的关于各种权限以及组件声明拷贝到工程的manifest.xml文件下，若有相同权限可以直接过滤掉。

3、初始化必要信息：`[SPUtils.saveHelpAddress(String helpAddress)],[SPUtils.saveAppID(String appID)];`

注：helpAddress即您所注册的kf5平台地址，如：demo.kf5.com；appid为验证是否是您平台的唯一标示，[查看与创建移动SDK APP](https://support.kf5.com/hc/kb/article/199665/),这俩接口需要在对SDK其他操作之前调用。

## 三、SDK使用方法  
1.使用SDK的相关功能，需要获取到用户的相关信息，用户信息的相关接口全部封装在UserInfoAPI中，SDK中除了`loginUser(Map<String, String> fieldMap, HttpRequestCallBack callBack)`与`createUser(Map<String, String> fieldMap, HttpRequestCallBack callBack)`不需要userToken（用户唯一标示），其他网络请求接口都需要userToken，所以开发者必须调用两者任意接口先获取到userToken，然后在调用`[SPUtils.saveUserToken(String userToken)]`将userToken保存，方便其他接口调用；每次接口的功能已在注释里做了说明，此处不再描述。  

 _注意：如果逻辑是先调用`loginUser`接口，这个接口只验证后台当前登录的用户是否存在，当用户不存在的时候返回用户不存在，这时需要调用`createUser`创建用户，创建成功之后将必要信息缓存起来即可，处理逻辑与`loginUser`相同；如果逻辑是先调用`createUser`接口，这个接口只会在用户不存在的时候创建用户，因此可能返回用户已存在的信息，这时调用`loginUser`直接登录用户即可，处理逻辑同上，如果用户创建成功处理逻辑同上。_  
 
2.需要用到的接口：TicketAPI（工单模块）,HelpCenterHttpAPI（帮助中心模块）,IMAPI（即时交谈模块）.

3.SDK中Activity组件说明：HelpCenterActivity（文档分区）、HelpCenterTypeActivity（文档分类）、HelpCenterTypeChildActivity（文档列表）、HelpCenterTypeDetailsActivity（文档详情）、KF5ChatActivity（即时交谈）、FeedBackActivity（创建工单）、LookFeedBackActivity（工单列表）、FeedBackDetailsActivity（工单详情）、OrderAttributeActivity（工单属性）

4.工单反馈模块**自定义字段**添加方法请参考FeedBackActivity下getDataMap回调接口示例；IM模块**用户自定义信息**在KF5ChatActivity中scConnect回调接口调用IMPresenter里的setMetadata接口即可。

_5.SDK现在支持滑动后退，并且默认开启，关闭则在BaseSwipeBackActivity自由设置；开发者若需自定义Activity切换动画，在BaseActivity中设置即可。_

6.日志查看，OkHttpManager中设置HttpLoggingInterceptor日志类型。

## 四、SDK中lib文件夹下的framework概述  
该jar包主要包含了核心的工具类、http框架、socket框架以及SDK中必要的接口。

## 五、SDK的UI设置
  v2.0版SDK将业务逻辑开源，开发者可根据需求自定义UI，组件Activity与xml视图的关系可从SDK中Activity组件说明入手。

## 六、第三方库  
 v2.0中依赖的第三方库有
 ```Java
compile 'com.google.code.gson:gson:2.7'
compile 'com.github.bumptech.glide:glide:3.7.0
 ```
## 七、关于混淆  
 ```Java
-keep class com.kf5.sdk.im.entity.**{*;}
-keep class com.kf5.sdk.helpcenter.entity.**{*;}
-keep class com.kf5.sdk.system.entity.**{*;}
-keep class com.kf5.sdk.ticket.entity.**{*;}
 ```
