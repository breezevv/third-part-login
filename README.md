# third-part-login
第三方登录示例


1. 启动`vue-element-admin`项目
    + 先执行`npm install`安装依赖
    + 再执行`npm run dev`命令启动项目
    + 修改 `api/user.js`中`getInfo`和`getAuthorizationUrl`方法中的地址
    + 注意：可以修改该项目中的端口，也可以不做修改（默认9527），如修改端口，需要修改`backend`中的内容
2. 启动`backend`项目
   + 修改`AuthController`中的`clientId`和`clientSecret`
   + 注意：若修改了上面项目中的启动端口，则需要修改`AuthController`中的`callback`方法中的端口，如下
   + ```response.sendRedirect("http://localhost:9527/#/auth-redirect?token=" + token);```
3. 打开`vue-element-admin`控制台中的访问地址，进入登录界面
4. 选择 gitee 登录
5. 同意授权
