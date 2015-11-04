#### Umeng Social Android SDK 4.2.4 ####


版本:V4.3.0150716
发布日期：2015.07.16
更新内容

1. 修复bug。


下载包中目录跟文件说明

umeng_android_social_sdk
    ├── components
    ├── debug.keystore
    ├── dev_manual.pdf
    ├── docs
    ├── main
    ├── platforms
    ├── readme.txt
    ├── social_sdk_example
    ├── social_sdk_library_project
    ├── umeng_android_socialize_demo.apk
    └── umeng_social_custom_shareboard.zip

1. main目录：分享功能的核心文件，使用时必须加入。
2. components目录：相关组件包。包含评论组件、摇一摇组件、喜欢组件、用户中心组件以及@好友组件（分享编辑页@好友功能）。
3. platforms目录：各平台得相关jar跟资源文件。开发者可以根据自己的需求添加相应的平台。
4. social_sdk_example目录：友盟分享组件Demo工程。
5. social_sdk_library_project目录：友盟分享组件引用工程。该工程包含有所有的分享平台，组件功能。开发者可以直接导入此工程，然后引用即可。
6. debug.keystore文件：签名文件。友盟分享组件demo的签名文件，如果使用demo使用微信功能，则必须使用此签名文件。
7. docs目录：友盟分享组件文档。其中包括API文档跟集成文档。
8. umeng_android_socialize_demo.apk文件：友盟分享组件Demo apk文件。



文档地址：http://dev.umeng.com/social/android/share/quick-integration