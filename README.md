IDEA-Pic-Tinify
===

## 快速压缩图片的 Intellij 插件

压缩功能由 TinyPng 网站提供 https://tinypng.com/ —— powerd by [tinify-java](https://github.com/tinify/tinify-java)

![tinypng_homepage.png](./screenshots/tinypng_homepage.png "TinyPng")

网站 web 端一次只能上传 20 张图片，手动操作需要重复上传下载解压。

#### 申请 Api Key

> 在开发者页面 [tinypng](https://tinypng.com/developers) 申请 api key。  
单个 api key 每月有 500 次的免费压缩额度，增加额度需要另外付费。

![tinypng_develop.png](./screenshots/tinypng_develop.png "TinyPng")

## 安装 & 使用

1. 打开 File -> Settings -> Plugins 选择本地安装，点击这里 ➡️ [下载](https://github.com/alvince/TinyPic2/releases/latest "v1.0.2")

2. 安装完后重启，第一次使用会提示设置 Api Key  
![notification.png](http://osc7hu59y.bkt.clouddn.com/image/GitHub/art/tinypic/notification.png "Notification")

3. 输入在 [developers](https://tinypng.com/developers) 申请的 api key  
![settings.png](http://osc7hu59y.bkt.clouddn.com/image/GitHub/art/tinypic/settings.png "Settings")

4. 选择图片（多选，允许 `png` `jpg` 文件类型），完成图片压缩  
![select_images.png](./screenshots/select_images.png "Pick Images")

LICENSE
---

```
Copyright 2018 alvince

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
