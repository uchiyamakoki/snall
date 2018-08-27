snall learning
## 每日笔记

2018.8.27：

今天主要是进行用户模块功能的测试.然后遇到了tomcat没啥问题，但是测试接口确实404的bug。

emmm，起初认为是springmvc拦截器出问题了。但是检查了一遍，感觉都没错。然后打断点，竟然没用。因为是idea初学者，debug能力很弱。
最后突发奇想，会不会是idea的锅。发现。。。原来是打的包不对，即tomcat的deployment配置的包要选择：war。还是idea用的太少了。

然后稍微复习一下。