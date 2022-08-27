# third-part-login
第三方登录示例


1. 启动`vue-element-admin`项目
    + 先执行`npm install`安装依赖
    + 再执行`npm run dev`命令启动项目
    + 修改 `api/user.js`的`backend_url`变量中的地址（修改为 backend 项目的地址）
    + 注意：可以修改该项目中的端口，也可以不做修改（默认9527），如修改端口，需要修改`backend`中的内容
2. 启动`backend`项目
   + 修改配置文件中的`oauth2`下的`clientId`和`clientSecret`，包括 github 和 gitee（gitee 需要修改 redirectUrl）
   + 修改配置文件中的`custom.backend`下的`host`，可以填写为`localhost`或本机ip
   + 修改配置文件中的`custom.frontend`下的`redirectUrl`，需要修改前端服务器运行在的主机的ip
   + 注意：若修改了上面项目中的启动端口，则需要修改上一点中`redirectUrl`中的端口
3. 打开`vue-element-admin`控制台中的访问地址，进入登录界面
4. 选择 gitee 登录
5. 同意授权
