Yuri书店

学号：
        
        202130440521

姓名:

        冯启超

测试用户

管理员

        账号：admin
        密码：123
        
销售人员
        账户：sellman1
        密码：123

普通用户

        账号：chigaya
        密码：123
项目开发环境:

        操作系统:Windows10
        Web服务器:Tomcat8.5.78
        数据库:MySQL8.0
        开发工具:vscode
        浏览器:Edge
主要目录介绍

-src
        
        -dao：用于与数据库进行交互
        -filter：统一全站编码
        -listener：监听所有商品
        -model：存储Goods、Order、Page、User等等实体类
        -service：为Servlet类重载的方法
        -servlet:实现网站功能的Servlet类
        -utils：数据库连接工具类、价格计算工具类
-web

        -admin：存储后台管理系统的所有JSP页面文件以及CSS、JS和图片等
        -css、js、fonts、images、layer、picture：前后台系统中用到的CSS、JS、字体样式和图片等
        -WEB-INF：jsp页面文件

项目部署
阿里云ECS服务器 公网IP：120.79.254.242

宝塔面板访问：

========================面板账户登录信息==========================
        
         外网面板地址: http://120.79.254.242:8888/baota
        内网面板地址: http://172.17.23.238:8888/baota
        username: chigaya
        password: 7ArxEsW7Wc
