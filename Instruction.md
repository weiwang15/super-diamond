#super-diamond
###简介
- 配置管理系统提供系统参数配置管理，例如数据库的配置信息等，配置参数修改以后可以实时推送到客户端(基于netty4)，
方便系统动态修改运行参数。
- 可以建多个项目，每个项目分为三种profile（development、test、production）， 能够控制profile 级别的权限。
- 所有参数均由development profile配置，test和production profile继承development profile配置，也可以覆盖其配置。
  test和production profile只提供修改功能。
- client备份配置信息到本地文件系统，如果server不可用，可以使用本地备份。client 能够定时重连server，保证client高可用。
- client支持自定义备份文件的本地路径。
- client支持本地环境变量与系统参数的解析替换功能。
- client 提供ConfigurationListener，当某个属性发生变化（add、update、clear）, ConfigurationListener能够接收到ConfigurationEvent。
- server 支持配置跨工程替换（包含对加解密的处理）。
- 支持php项目从superdiamond中获取配置参数。
- 支持服务端配置的导入导出功能（目前导入导出的格式分别为Json, properties）。
- 支持配置管理页面列表中一些关键配置的隐藏与显示功能以及控制列表显示的记录数量。
- 支持项目中界面操作配置项更换模块。
- server 备份配置文件系统系统，如果数据库不用，能够保证对客户端提供数据（待完善）。

系统部分功能截图：
![](https://raw.githubusercontent.com/xiake2025/super-diamond/master/image/diamond_1.png "功能截图1")

![](https://raw.githubusercontent.com/xiake2025/super-diamond/master/image/diamond_2.png "功能截图2")

项目profile请参考：http://melin.iteye.com/blog/1339060

###super-diamond-server 安装
1. 下载super-diamond代码： git clone https://github.com/melin/super-diamond.git
2. 进入super-diamond目录，构建super-diamond父工程： mvn install
3. super-diamond-server中嵌入jetty运行，构建部署包：mvn install assembly:single -Pproduction，生成super-diamond-server-${version}-bin.tar.gz文件，
解压运行bin/server.sh start命令。
4. 在conf\META-INF\scripts目录中，提供mysql和oracle建表脚本，理论也支持其它数据库，在conf\META-INF\res\config-production.properties文件中修改数据库配置。
5. 在conf_user表中添加用户admin，密码000000的加密值为：670b14728ad9902aecba32e22fa4f6bd, mysql脚本：
insert into conf_user(id,USER_code,USER_NAME,PASSWORD,CREATE_TIME) values(1,'admin','admin','670b14728ad9902aecba32e22fa4f6bd',current_timestamp() );  
commit;  
6. 访问super-diamond-server，jetty默认端口为8090，可以在：conf/META-INF/res/jetty.properties中修改。
	http://localhost:8090/superdiamond

###super-diamond-server 管理界面使用
1. 用户管理
  * 用户列表
  ![](https://raw.githubusercontent.com/xiake2025/super-diamond/master/image/user_manager.png "用户管理列表")
  * 添加用户
  ![](https://raw.githubusercontent.com/xiake2025/super-diamond/master/image/user_new.png "新增用户")
2. 项目管理
  * 项目列表
  ![](https://raw.githubusercontent.com/xiake2025/super-diamond/master/image/project_manager.png "项目管理列表")
  * 添加项目
  ![](https://raw.githubusercontent.com/xiake2025/super-diamond/master/image/project_create.png "新增项目")    
  其中，项目编码为项目的唯一标识码，项目名称为项目的名称， 项目管理者为用户登录账号，复制其他项目配置表示该项目建立时使用其他项目中的配置进行初始化（这里填写其他项目的项目编码），是否为公共项目表示该项目是否只存储所有项目中所使用的公共配置信息。  

  * 项目添加用户  
  ![](https://raw.githubusercontent.com/xiake2025/super-diamond/master/image/project_user_relation.png "项目添加用户")  
  这里可以给这个项目添加相应的用户，并且同时可以给该用户赋予相应的项目权限。  
3. 配置管理
  * 配置列表   
  ![](https://raw.githubusercontent.com/xiake2025/super-diamond/master/image/configuration_list.png "配置列表")  
  该页面可以显示项目的某个profile环境下的配置信息列表（通过选择显示记录数可以控制列表的行数），通过选中高级配置复选框可以显示列表中所隐藏的相关信息，同时可以通过模块来过滤配置信息。
  列表中的操作列包括了对于该条记录的查看、删除、修改以及移动功能。
  * 添加模块  
  ![](https://raw.githubusercontent.com/xiake2025/super-diamond/master/image/module_create.png "添加模块")  
  * 添加配置  
  ![](https://raw.githubusercontent.com/xiake2025/super-diamond/master/image/configuration_create.png "添加配置")  
  这里是否为默认配置选项，如果选中则默认在列表中不会显示该配置信息，但可以通过选中高级配置复选框按钮来显示该配置信息。  
注：    
      1) 对于引用本项目内的相应配置信息则使用${配置名称}即可  
      2) 对于引用公共项目中的相应配置则使用${common:配置名称}  
      3) 对于引用服务本地环境变量则使用${env:变量名称}  
      4) 对于引用服务本地JVM参数变量则使用${env:变量名称}  
  * 移动配置  
  ![](https://raw.githubusercontent.com/xiake2025/super-diamond/master/image/configuration_move.png "移动配置")  
  这里可以将项目中的某一配置信息移动到项目的另一个模块中。
  * 导入导出  
  ![](https://raw.githubusercontent.com/xiake2025/super-diamond/master/image/configuration_import_export.png "配置导入导出")  
  导出功能可以将项目中某些模块的配置信息导出到文件中进行存储（文件的格式可以是Json以及properties）；  
  导入功能可以导入规定格式的.json以及.propertie文件；  
  1、json文件格式
   ```
   {
	  "configVer": "60",
	  "exportTime": 1458110732361,
	  "exportUser": "admin",
	  "modules": [
	    {
	      "configs": [
	        {
	          "description": "aaa",
	          "isShow": false,
	          "key": "jdbc.username",
	          "value": "xxxxx"
	        },
	        {
	          "description": "",
	          "isShow": true,
	          "key": "jdbc.password",
	          "value": "xxxx"
	        },
	        {
	          "description": "192.168.59.45/username:sjpan/password:${aaa.jdbc.password}",
	          "isShow": true,
	          "key": "jdbc.url",
	          "value": "192.168.59.45/username:${jdbc.username}/password:${common:aaa.jdbc.password}"
	        }
	      ],
	      "name": "jdbc"
	    }
	  ],
	  "projectCode": "test",
	  "projectDesc": "测试",
	  "serverIp": "192.168.63.132"
   }
   ```

  2、properties文件格式
  ```
  #ModuleName:jdbc
  #配置注释内容
  #IS_NOT_SHOW
  jdbc.username=xxxxx
  #配置注释内容
  jdbc.password=123456
  #配置注释内容
  jdbc.url=192.168.59.45/username:${jdbc.username}/password:${jdbc.password}

  #ModuleName:redis
  redis_host=${common:aaa.redis_host}
  redis_port=${aaa.redis_port}
  redis.pool.maxTotal=111
  ```  
  注：这里.properties文件中  
  (1) 必须以#ModuleName:xxx开头表示配置所在的模块名称；  
  (2) #IS_NOT_SHOW表示该配置是默认配置（即不需要再列表中显示） 如果没有#IS_NOT_SHOW则表示该配置不是默认配置；  
  (3) #配置注释内容---表示该配置的注释信息；  
  (4) 配置注释信息后则是配置信息的键值对信息；  
###super-diamond-client
   客户端参考apache configuration，实现其中的部分功能。例如：
	```java
	public class PropertiesConfigurationTest {
	
		@Test
		public void testConfig() throws ConfigurationRuntimeException  {
			String config = "username = melin \r\n";
			config += "port=8000 \r\n";
			config += "reload=true \r\n";
	
			PropertiesConfiguration configuration = new PropertiesConfiguration();
			configuration.load(config);
	
			Assert.assertEquals("melin", configuration.getString("username"));
			Assert.assertEquals(8000, configuration.getInt("port"));
			Assert.assertTrue(configuration.getBoolean("reload"));
		}
	
		@Test
		public void testInterpolator() throws ConfigurationRuntimeException  {
			String config = "app.home = /tmp/home \r\n";
			config += "zk.home=${app.home}/zk \r\n";
			config += "hbase.home=${app.home}/hbase \r\n";
	
			PropertiesConfiguration configuration = new PropertiesConfiguration();
			configuration.load(config);
	
			Assert.assertEquals("/tmp/home", configuration.getString("app.home"));
			Assert.assertEquals("/tmp/home/zk", configuration.getString("zk.home"));
			Assert.assertEquals("/tmp/home/hbase", configuration.getString("hbase.home"));
		}
	
		@Test
		public void testSysProperties() throws ConfigurationRuntimeException  {
			String config = "javaVersion = ${sys:java.version} \r\n";
	
			PropertiesConfiguration configuration = new PropertiesConfiguration();
			configuration.load(config);
	
			Assert.assertEquals(System.getProperty("java.version"), configuration.getString("javaVersion"));
		}
	
		@Test
		public void testSysEvns() throws ConfigurationRuntimeException  {
			String config = "javaHome = ${env:JAVA_HOME}/lib \r\n";
	
			PropertiesConfiguration configuration = new PropertiesConfiguration();
			configuration.load(config);
	
			Assert.assertEquals(System.getenv("JAVA_HOME") + "/lib", configuration.getString("javaHome"));
		}
	}
	```
	
	###客户端连接服务器端方式：
	<b>java 使用方式</b>
	```java
	PropertiesConfiguration config = new PropertiesConfiguration("localhost", 5001, "test", "development");
	config.addConfigurationListener(new ConfigurationListenerTest());
	config.getString("jdbc.url")
	```
	
	<b>spring 使用方式</b>
	```xml
	<bean class="org.springframework.context.support.PropertySourcesPlaceholderConfigurer">
		<property name="properties" ref="propertiesConfiguration" />
	</bean>
	
	<bean id="propertiesConfiguration" class="com.github.diamond.client.PropertiesConfigurationFactoryBean">
		<constructor-arg index="0" value="localhost" />
		<constructor-arg index="1" value="5001" />
		<constructor-arg index="2" value="test" />
		<constructor-arg index="3" value="development" />
	</bean>
	```
	<b>客户端连接服务端的参数</b>
	
	  客户端连接服务端的参数host、port、projcode、profile、modules、encryptPropNames以及loacalFilePath除了通过构造函数设置之外，同时还可以通过环境变量和jvm参数这两种方式设置，避免固定在工程配置文件中。
	  
	* 环境变量
	```shell
	export SPUERDIAMOND_HOST=192.168.0.1
	export SPUERDIAMOND_PORT=8283
	export SUPERDIAMOND_PROJCODE=javademo
	export SUPERDIAMOND_PROFILE=production
	export SUPERDIAMOND_MODULES=jdbc,common #多个模块之间用逗号分隔，可以设置为空，获取所有模块配置。
	export SUPERDIAMOND_ENCRYPTPROPNAMES=jdbc.url,jdbc.password #多个配置名之间用逗号分隔，可以设置为空，获取所有加密配置名。
	export SUPERDIAMOND_LOCALFILEPATH=classpath:/data.properties #自定义客户端本地配置文件备份路径，可以设置为空。
	```
	* jvm参数
	```shell
	 -Dsuperdiamond.host=127.0.0.1 -Dsuperdiamond.port=8283  -Dsuperdiamond.projcode=javademo -Dsuperdiamond.profile=production -Dsuperdiamond.modules=jdbc -Dsuperdiamond.encryptPropNames=common:jdbc.password -Dsuperdiamond.localFilePath=classpath:/data.properties
	```
	* 构造函数
	```xml
	<bean id="propertiesConfiguration" class="com.github.diamond.client.PropertiesConfigurationFactoryBean">
		<constructor-arg index="0" value="localhost" />
		<constructor-arg index="1" value="8283" />
		<constructor-arg index="2" value="projeceCode" />
		<constructor-arg index="3" value="development" />
		<constructor-arg index="4" value="jdbc" />
		<constructor-arg index="5" value="common:jdbc.password" />
		<constructor-arg index="6" value="classpath:/dubbo.properties" />
	</bean>
	```
	* java获取配置信息
	```java
	ApplicationContext applicationContext = new ClassPathXmlApplicationContext("bean.xml");
	PropertiesConfiguration config = PropertiesConfigurationFactoryBean.getPropertiesConfiguration();
	config.getString("jdbc.url")
	```

###Rest 接口获取配置：
通过http获取配置信息，http url格式为：

- properties格式

	http://host:port/superdiamond/preview/${项目编码}/${profile}
	http://host:port/superdiamond/preview/${项目编码}/${module}[,${module}]/${profile} //支持设置多个module值，用逗号分割
	http://host:port/superdiamond/preview/${项目编码}/${module}/${key}/${profile}
- php config格式

	http://host:port/superdiamond/preview/${项目编码}/${profile}?format=php
	http://host:port/superdiamond/preview/${项目编码}/${module}[,${module}]/${profile}? format=php  //支持设置多个module值，用逗号分割
- json config格式

	http://host:port/superdiamond/preview/${项目编码}/${profile}?format=json 
	http://host:port/superdiamond/preview/${项目编码}/${module}[,${module}]/${profile}?  //支持设置多个module值，用逗号分割format=json


###PHP项目应用：
结合Phing从superdiamond获取配置参数。完整的phing build.xml配置请参考：
https://gist.github.com/melin/fa4818acc9fd55666b77

```xml
<!--
    Target: config 
    Description: 通过http方式从superdiamond中获取系统配置参数信息。
    development profile 获取配置存放在Application/Common/Conf/user-config.php文件中（ThinkPHP 3.2）
    test & production profile 获取配置存放在build/user-config.php文件中，在执行build target时，
    复制build/user-config.php文件中build/Application/Common/Conf/user-config.php位置
-->
<target name="config" depends="profile">
    <if>
        <equals arg1="development" arg2="${project.profile}" trim="true" />
        <then>
            <httpget url="http://172.16.81.73:8001/superdiamond/preview/App.EduSNS/${project.profile}?format=php"
                     dir="Application/Common/Conf" filename="user-config.php" />
        </then>
        <else>
            <httpget url="http://172.16.81.73:8001/superdiamond/preview/App.EduSNS/${project.profile}?format=php"
                     dir="build" filename="user-config.php" />
        </else>
    </if>
</target>
```
