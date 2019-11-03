# CRM_SSM练习
**关键字** 
***
## 搭建初始环境
### 新建web项目
![](img/2019-11-03-21-06-05.png)
### 引入前端文件
![](img/2019-11-03-21-06-43.png)
### 引入jar包
![](img/2019-11-03-21-07-26.png)
### 配置web.xml配置文件

```xml
 <!-- 加载spring容器 -->
	<context-param>
		<param-name>contextConfigLocation</param-name>
		<param-value>classpath:ApplicationContext-*.xml</param-value>
	</context-param>
	<listener>
		<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
	</listener>
  
  
  <!-- springmvc前端控制器 -->
  <servlet>
  	<servlet-name>springMvc</servlet-name>
  	<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
  	<init-param>
  		<param-name>contextConfigLocation</param-name>
  		<param-value>classpath:SpringMvc.xml</param-value>
  	</init-param>
  	<!-- 在tomcat启动的时候就加载这个servlet -->
  	<load-on-startup>1</load-on-startup>
  </servlet>
  <servlet-mapping>
  	<servlet-name>springMvc</servlet-name>
  	<!-- 
  	*.action    代表拦截后缀名为.action结尾的
  	/ 			拦截所有但是不包括.jsp
  	/* 			拦截所有包括.jsp
  	 -->
  	<url-pattern>*.action</url-pattern>
  </servlet-mapping>
  
  <!-- 配置Post请求乱码 -->
  <filter>
		<filter-name>CharacterEncodingFilter</filter-name>
		<filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
		<init-param>
			<param-name>encoding</param-name>
			<param-value>utf-8</param-value>
		</init-param>
	</filter>
	<filter-mapping>
		<filter-name>CharacterEncodingFilter</filter-name>
		<url-pattern>/*</url-pattern>
	</filter-mapping>
```
### 建立web(controller)层
**Controller包和引入SpringMVC的xml配置文件**
- com.公司名.controller
- 新建SpringMvc.xml
```xml
 
    <!-- 引入字典资源文件 -->
    <context:property-placeholder location="classpath:resource.properties"/>
    
    <!-- @Controller注解扫描 -->
    <context:component-scan base-package="cn.itheima.controller"></context:component-scan>
    
    <!-- 注解驱动:
    		替我们显示的配置了最新版的注解的处理器映射器和处理器适配器 -->
    <mvc:annotation-driven conversion-service="conversionService"></mvc:annotation-driven>
    
    <!-- 配置视图解析器 
	作用:在controller中指定页面路径的时候就不用写页面的完整路径名称了,可以直接写页面去掉扩展名的名称
	-->
	<bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
		<!-- 真正的页面路径 =  前缀 + 去掉后缀名的页面名称 + 后缀 -->
		<!-- 前缀 -->
		<property name="prefix" value="/WEB-INF/jsp/"></property>
		<!-- 后缀 -->
		<property name="suffix" value=".jsp"></property>
	</bean>
	
	<!-- 配置自定义转换器 
	注意: 一定要将自定义的转换器配置到注解驱动上
	-->
	<bean id="conversionService"
		class="org.springframework.format.support.FormattingConversionServiceFactoryBean">
		<property name="converters">
			<set>
				<!-- 指定自定义转换器的全路径名称 -->
				<bean class="cn.itheima.controller.converter.CustomGlobalStrToDateConverter"/>
			</set>
		</property>
	</bean>
```
**注意事项**
1. web.xml内springmvc前端控制器
```xml
<servlet>
  	<servlet-name>springMvc</servlet-name>
  	<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
  	<init-param>
  		<param-name>contextConfigLocation</param-name>
  		<param-value>classpath:SpringMvc.xml</param-value>
  	</init-param>
```
2. 配置文件名称是在web.xml文件里面设置
```web.xml片段
<servlet-mapping>
  	<servlet-name>springMvc</servlet-name>
  	<!-- 
  	*.action    代表拦截后缀名为.action结尾的
  	/ 			拦截所有但是不包括.jsp
  	/* 			拦截所有包括.jsp
  	 -->
  	<url-pattern>*.action</url-pattern>
  </servlet-mapping>
```
3. SpringMvc.xml配置converters配置自定义转换器 
```xml
<bean id="conversionService"
		class="org.springframework.format.support.FormattingConversionServiceFactoryBean">
		<property name="converters">
			<set>
				<!-- 指定自定义转换器的全路径名称 -->
				<bean class="com.dj.controller.converter.CustomGlobalStrToDateConverter"/>
			</set>
		</property>
	</bean>
```
### 建立service层
**建立service层包,引入service层配置文件,引入事物配置文件**
- com.公司名.controller
- ApplicationContext-service.xml(加入service注解扫描)
```ApplicationContext-service.xml
	<!-- @Service扫描 -->
	<context:component-scan base-package="cn.itheima.service"></context:component-scan>
```
- ApplicationContext-trans.xml(加入事物)
```xml
<!-- 事务管理器 -->
	<bean id="transactionManager"
		class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
		<!-- 数据源 -->
		<property name="dataSource" ref="dataSource" />
	</bean>
	
	<!-- 通知 -->
	<tx:advice id="txAdvice" transaction-manager="transactionManager">
		<tx:attributes>
			<!-- 传播行为 -->
			<tx:method name="save*" propagation="REQUIRED" />
			<tx:method name="insert*" propagation="REQUIRED" />
			<tx:method name="delete*" propagation="REQUIRED" />
			<tx:method name="update*" propagation="REQUIRED" />
			<tx:method name="find*" propagation="SUPPORTS" read-only="true" />
			<tx:method name="get*" propagation="SUPPORTS" read-only="true" />
		</tx:attributes>
	</tx:advice>
	
	<!-- 切面 -->
	<aop:config>
		<aop:advisor advice-ref="txAdvice"
			pointcut="execution(* cn.itheima.service.*.*(..))" />
	</aop:config>
```
**注意事项**:注意修改`pointcut="execution(* cn.itheima.service.*.*(..))"`此处包名
### 建立dao层
- 新建到层对应包结构
`com.公司名.dao`
- 配置ApplicationContext-dao.xml文件
```xml
<!-- 加载配置文件 -->
	<context:property-placeholder location="classpath:db.properties" />
	<!-- 数据库连接池 -->
	<bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource"
		destroy-method="close">
		<property name="driverClassName" value="${jdbc.driver}" />
		<property name="url" value="${jdbc.url}" />
		<property name="username" value="${jdbc.username}" />
		<property name="password" value="${jdbc.password}" />
		<property name="maxActive" value="10" />
		<property name="maxIdle" value="5" />
	</bean>
	
	<!-- mapper配置 -->
	<!-- 让spring管理sqlsessionfactory 使用mybatis和spring整合包中的 -->
	<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
		<!-- 数据库连接池 -->
		<property name="dataSource" ref="dataSource" />
		<!-- 加载mybatis的全局配置文件 -->
		<property name="configLocation" value="classpath:SqlMapConfig.xml" />
	</bean>

	
	<!-- 配置Mapper扫描器 -->
	<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
		<property name="basePackage" value="cn.itheima.dao"/>
	</bean>
```
**注意事项**:
- 注意配置ApplicationContext-dao.xml中加载mybatis的全局配置文件SqlMapConfig.xml
`<property name="configLocation" value="classpath:SqlMapConfig.xml" />`
SqlMapConfig.xml可以通过逆向工程生成
```xml
<configuration>
	
</configuration>
```
- 注意配置ApplicationContext-dao.xm配置Mapper扫描器value值
```xml
<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
		<property name="basePackage" value="cn.itheima.dao"/>
	</bean>
 ```
### 数据库层
- 新建数据库对应pojo包
![](img/2019-11-03-21-25-39.png)
- 引入对应包的pojo对象
- 数据库内新建对应pojo表
- 配置db.properties
1. 在ApplicationContext-dao.xml加载配置文件
```xml
<context:property-placeholder location="classpath:db.properties" />
```
2.db.properties文件
```properties
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/crm001?characterEncoding=utf-8
jdbc.username=root
jdbc.password=root
```
**注意事项**
注意配置数据库名称


## 业务功能实现
### 查询条件初始化
#### 启动项目展示customer.jsp页面
- controller层写入代码
找到地址栏进入方法
用@RequestMapping("/方法名称")跳转至方法内部,通过return返回jsp页面
```java
@Controller
@RequestMapping("/customer")
public class CustomerController {
	@RequestMapping("/list")
	public String list()  throws Exception{
		
		return "customer";
	}
}
```
#### 将下来菜单内的数据从数据库内回写值页面上 
- service层
新建service接口和实现类
![](img/2019-10-27-11-36-07.png)
- dao层
对应数据库表格新建dao层接口Mapper和xml文本
![](img/2019-10-27-11-37-42.png)
xml文件初始化
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.dj.dao.CustomerMapper">
</mapper>
```
**注意**在`<mapper></mapper>`内的`namespace`中将xml文件和到层java接口文件对应
- 用工具在数据库书写sql语句

####
### 客户列表展示

### 修改客户信息

### 删除客户