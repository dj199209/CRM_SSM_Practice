# CRM_SSM练习
**关键字** 
***
## 1 搭建初始环境
### 1.1 新建web项目
![](img/2019-11-03-21-06-05.png)
### 1.2 引入前端文件
![](img/2019-11-03-21-06-43.png)
### 1.3 引入jar包
![](img/2019-11-03-21-07-26.png)
### 1.4 配置web.xml配置文件

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

### 1.5 建立web(controller)层

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
3. SpringMvc.xml配置converters配置自定义转换器 (此功能是拦截时间格式的转换)
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
### 1.6 建立service层
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
### 1.7 建立dao层
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
### 1.8 数据库层
- 新建数据库对应pojo包

![](img/2019-11-03-21-25-39.png)

- 引入对应包的pojo对象

- 数据库内新建对应pojo表
- 配置db.properties
(1) 在`ApplicationContext-dao.xml`加载配置文件

```xml
	<context:property-placeholder location="classpath:db.properties" />
```

(2) db.properties文件

```properties
	jdbc.driver=com.mysql.jdbc.Driver
	jdbc.url=jdbc:mysql://localhost:3306/crm001?characterEncoding=utf-8
	jdbc.username=root
	jdbc.password=root
```

**注意事项**

- 注意配置数据库名称


## 2 业务功能实现

### 2.1 查询条件初始化

#### 2.1.1 启动项目展示customer.jsp页面

- controller层写入代码找到地址栏进入方法

用 `@RequestMapping("/方法名称")` 跳转至方法内部,通过return返回jsp页面

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
**注意** 在springmvc.xml文件中配置return返回jsp文件的后缀
```xml
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
```

#### 2.1.2 将下来菜单内的数据从数据库内回写值页面上 

**需求:** 
**通过给一个指定的编码查询出对应的客户来源,所属行业,客户级别**
(1) 将`客户来源` `所属行业` `客户级别`的下来菜单从数据库内拿出来

(2) 然后回写至jsp页面上

![](img/2019-11-10-21-54-59.png)

**思路:**

通过给查询数据库,了解客户来源,所属行业,客户级别对应的编码

把编码传值service层,service传至到层的xml文件内通过xml文件内的sql语句对数据查询返回一个list,对象为一个Polo的`BaseDict`类对象.

代码编写从dao层开始编写

##### 2.1.2.1 dao层

**(1) 先在xml文件内书写查询语句**
![](img/2019-11-10-22-18-13.png)

- 写完后将要返回的数据类型写在`resultType`后`resultType="com.dj.pojo.BaseDict"`

- 将要穿入的数据类型写在`parameterType`内`parameterType="string"`

- 在`<mapper></mapper>`内的`namespace`中将xml文件和到层java接口文件对应
	```xml
	<?xml version="1.0" encoding="UTF-8" ?>
	<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
	<mapper namespace="com.dj.dao.DictMapper">
	<select id="findDictCode" parameterType="string" resultType="com.dj.pojo.BaseDict">
			select * from base_dict a WHERE a.dict_enable=1 and a.dict_type_code=#{code}  ORDER BY a.dict_sort
		</select>
	</mapper>
	```
**(2) 对应的dao层的接口文件`DictMapper.java`**

接口中书写通过编码(String)查询类别的的方法
```java
	public interface DictMapper {
		public  List<BaseDict> findDictCode (String code);
	}

```
![](img/2019-10-27-11-37-42.png)
##### 2.1.2.2 service层

**(1) 新建service层的接口,在接口中书写通过编码(String)查询类别的的方法**

```java
public interface CustomerService {
	public List<BaseDict> findDictCode(String code);
}
```

**(2) 实现接口中的**

![](img/2019-10-27-11-36-07.png)

导入注解`@Service`

通过`@Autowired`new `dictMapper`层

```java
@Service
public class CustomerServiceImpl implements CustomerService {
	@Autowired
	private DictMapper dictMapper;
	@Override
	public List<BaseDict> findDictCode(String code) {
		List<BaseDict> list = dictMapper.findDictCode(code);
		return list;
	}
}
```

##### 2.1.2.3 controller层

导入注解`@Controller`和`@RequestMapping("/customer")和@RequestMapping("/list")`
通过`@Autowired`new `service`层
```java
@Controller
@RequestMapping("/customer")
public class CustomerController {
	@Autowired
	private CustomerService customerService;
	
	
	@RequestMapping("/list")
	public String list(String custName, QueryVo vo,Model model)  throws Exception{
		//客户来源
		List<BaseDict> list = customerService.findDictCode("002");
		model.addAttribute("fromType", list);
		//所属行业
		List<BaseDict> industry = customerService.findDictCode("001");
		model.addAttribute("industryType", industry);
		//客户级别
		List<BaseDict> level = customerService.findDictCode("006");
		model.addAttribute("levelType", level);
		//设置编码
		if (vo.getCustName()!=null) {
			vo.setCustName(new String(vo.getCustName().getBytes("iso8859-1"),"utf-8"));
		}
		//回写
		model.addAttribute("custName", vo.getCustName());
		model.addAttribute("custSource", vo.getCustSource());
		model.addAttribute("custIndustry", vo.getCustIndustry());
		model.addAttribute("custLevel", vo.getCustLevel());
		return"customer";
	}
}
```
通过`model`的`addAttribute`方法回写数据至jsp文件`addAttribute`中的(对应jsp的那么参数,调用vo.方法),springMVC的框架自动封装的方法

设置编码
```java
if (vo.getCustName()!=null) {
			vo.setCustName(new String(vo.getCustName().getBytes("iso8859-1"),"utf-8"));
		}
```

### 2.2 客户列表展示

通过传入一个对象查询出一个pojo对象customer

#### 2.2.1 dao层

#### 2.2.2 service层

#### 2.2.3 controller层

### 2.3 修改客户信息

### 2.4 删除客户