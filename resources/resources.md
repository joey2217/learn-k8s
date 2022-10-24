---
id: resources
title: K8s资源
sidebar_label: K8s资源
---

K8s 中所有的内容都抽象为资源， 资源实例化之后，叫做对象

## 名称空间级别

- 工作负载型资源( workload )

Pod、ReplicaSet、Deployment、StatefulSet、DaemonSet、Job、
CronJob ( ReplicationController 在 v1.11 版本被废弃 )

- 服务发现及负载均衡型资源( ServiceDiscovery LoadBalance )

Service、Ingress

- 配置与存储型资源

Volume( 存储卷 )、CSI( 容器存储接口,可以扩展各种各样的第三方存储卷 )

- 特殊类型的存储卷

ConfigMap( 当配置中心来使用的资源类型 )、Secret(保存敏感数据)、
DownwardAPI(把外部环境中的信息输出给容器)

- 集群级资源

Namespace、Node、Role、ClusterRole、RoleBinding、ClusterRoleBinding

- 元数据型资源

HPA、PodTemplate、LimitRange

## 资源清单

在 k8s 中，一般使用 `yaml` 格式的文件来创建符合我们预期期望的 `pod` ，这样的 `yaml` 文件我们一般
称为资源清单

## 常用字段

:::tip

```sh
kubectl explain pod # 获取对应资源字段
```

:::

### 必须属性

|         参数名          | 字段类型 | 说明                                                                  |
| :---------------------: | :------: | --------------------------------------------------------------------- |
|         version         |  String  | K8s API 版本,目前基本上是 v1,可以用哪个 kubectl api-versions 命令查询 |
|          kind           |  String  | yaml 文件定义的资源类型和角色,比如 Pod                                |
|        matedata         |  Object  | 元数据对象,固定值就写 matedata                                        |
|      matedata.name      |  String  | 元数据对象的名字,比如命名 pod 的名字                                  |
|   matedata.namespace    |  String  | 元数据对象的命名空间,默认 default                                     |
|          spec           |  Object  | 详细定义对象,固定值 spec                                              |
|    spec.containers[]    |   list   | spec 对象的容器列表定义列表                                           |
| spec.containers[].name  |  String  | 定义容器名称                                                          |
| spec.containers[].image |  String  | 定义镜像名称                                                          |

### 其他属性

|                      参数名                       | 字段类型 | 说明                                                                                                                                                              |
| :-----------------------------------------------: | :------: | ----------------------------------------------------------------------------------------------------------------------------------------------------------------- |
|         spec.containers[].imagePullPolicy         |  String  | 定义镜像拉取策略[Always(默认),Never,IfNotPresent] Always:每次都尝试重新拉取镜像 Never:仅使用本地镜像 IfNotPresent:如果本地有镜像就使用本地镜像,没有就拉取在线镜像 |
|            spec.containers[].command[]            |   List   | 指定容器启动命令,因为是数组可以指定多个,不指定使用镜像打包时使用的启动命令                                                                                        |
|             spec.containers[].args[]              |   List   | 指定容器启动命令,因为是数组可以指定多个,不指定使用镜像打包时使用的启动命令                                                                                        |
|           spec.containers[].workingDir            |  String  | 指定容器的工作目录                                                                                                                                                |
|         spec.containers[].volumeMounts[]          |   List   | 指定容器内部的存储卷的配置                                                                                                                                        |
|       spec.containers[].volumeMounts[].name       |  String  | 指定可以被容器挂载的存储卷的名称                                                                                                                                  |
|    spec.containers[].volumeMounts[].mountPath     |  String  | 指定可以被容器挂载的存储卷的路径                                                                                                                                  |
|     spec.containers[].volumeMounts[].readOnly     |  String  | 设置存储卷路径的读写模式,true 或者 false,默认为读写模式                                                                                                           |
|             spec.containers[].port[]              |   List   | 指定容器需要用到的                                                                                                                                                |
|           spec.containers[].port[].name           |  String  | 指定端口名称                                                                                                                                                      |
|      spec.containers[].port[].containerPort       |  String  | 指定容器需要监听的端口号                                                                                                                                          |
|         spec.containers[].port[].hostPort         |  String  | 指定容器所在主机需要监听的端口号,默认和 containerPort 相同,设置了 hostPort 同一台主机无法启动该容器的相同副本(端口冲突)                                           |
|         spec.containers[].port[].protocol         |  String  | 指定端口协议,支持 TCP 和 UDP,默认 TCP                                                                                                                             |
|              spec.containers[].env[]              |   List   | 指定容器运行前设置的环境变量列表                                                                                                                                  |
|           spec.containers[].env[].name            |  String  | 指定环境变量名称                                                                                                                                                  |
|           spec.containers[].env[].value           |  String  | 指定环境变量值                                                                                                                                                    |
|         spec.containers[].env[].resources         |  Object  | 指定资源限制和资源请求的值                                                                                                                                        |
|     spec.containers[].env[].resources.limits      |  Object  | 指定容器运行时资源的运行上限                                                                                                                                      |
|   spec.containers[].env[].resources.limits.cpu    |  String  | 指定 CPU 的限制,单位为 core 数,将用于 docker run --cpu-share 参数                                                                                                 |
|  spec.containers[].env[].resources.limits.memory  |  String  | 指定 MEM 内存的限制,单位 MIB,GIB                                                                                                                                  |
|    spec.containers[].env[].resources.requests     |  Object  | 指定容器启动和调度时的限制设置                                                                                                                                    |
|  spec.containers[].env[].resources.requests.cpu   |  String  | CPU 请求,单位为 core 数,容器启动时的初始化可用数量                                                                                                                |
| spec.containers[].env[].resources.requests.memory |  String  | 内存请求,单位 MIB,GIB,容器启动时的初始化可用数量                                                                                                                  |

### 额外属性

|        参数名         | 字段类型 | 说明                                                                                                                                 |
| :-------------------: | :------: | ------------------------------------------------------------------------------------------------------------------------------------ |
|  spec.restartPolicy   |  String  | 定义 Pod 的重启策略[Always(默认),OnFailure,Never]                                                                                    |
|   spec.nodeSelector   |  Object  | 定义 Node 的 Label 过滤标签,以 key:value 格式指定                                                                                    |
| spec.imagePullSecrets |  Object  | 定义 pull 镜像时使用 secret 名称,以 name:secretKey 格式指定                                                                          |
|   spec.hostNetwork    | Boolean  | 定义是否主机网络模式,默认 false,设置 true 表示使用宿主机网络,不是有 docker 网桥,同时设置了 true 将无法在同一台宿主机上启动第二个副本 |


## 容器生命周期

![lifecycle](https://ae01.alicdn.com/kf/H74034b53456e4e6b9f89ff4aa525e104L.png)

### Init 容器

Pod 能够具有多个容器，应用运行在容器里面，但是它也可能有一个或多个先于应用容器启动的 Init 容器
Init 容器与普通的容器非常像，除了如下两点：
- Init 容器总是运行到成功完成为止
- 每个 Init 容器都必须在下一个 Init 容器启动之前成功完成

如果 Pod 的 Init 容器失败，Kubernetes 会不断地重启该 Pod，直到 Init 容器成功为止。然而，
如果 Pod 对应的 restartPolicy 为 Never，它不会重新启动

因为 Init 容器具有与应用程序容器分离的单独镜像，所以它们的启动相关代码具有如下优势：
- 它们可以包含并运行实用工具，但是出于安全考虑，是不建议在应用程序容器镜像中包含这
些实用工具的
- 它们可以包含使用工具和定制化代码来安装，但是不能出现在应用程序镜像中。例如，创建
镜像没必要 FROM 另一个镜像，只需要在安装过程中使用类似 sed、 awk、 python 或 dig 这样的工具。
- 应用程序镜像可以分离出创建和部署的角色，而没有必要联合它们构建一个单独的镜像。
- Init 容器使用 Linux Namespace，所以相对应用程序容器来说具有不同的文件系统视图。因
此，它们能够具有访问 Secret 的权限，而应用程序容器则不能。
- 它们必须在应用程序容器启动之前运行完成，而应用程序容器是并行运行的，所以 Init 容
器能够提供了一种简单的阻塞或延迟应用容器的启动的方法，直到满足了一组先决条件。

- 在 Pod 启动过程中，Init 容器会按顺序在网络和数据卷初始化之后启动。每个容器必须在下一个
容器启动之前成功退出
- 如果由于运行时或失败退出，将导致容器启动失败，它会根据 Pod 的 restartPolicy 指定的策略
进行重试。然而，如果 Pod 的 restartPolicy 设置为 Always，Init 容器失败时会使用
RestartPolicy 策略
- 在所有的 Init 容器没有成功之前，Pod 将不会变成 Ready 状态。Init 容器的端口将不会在
Service 中进行聚集。 正在初始化中的 Pod 处于 Pending 状态，但应该会将 Initializing 状
态设置为 true
- 如果 Pod 重启，所有 Init 容器必须重新执行
- 对 Init 容器 spec 的修改被限制在容器 image 字段，修改其他字段都不会生效。更改 Init 容器的 image 字段，等价于重启该 Pod
- Init 容器具有应用容器的所有字段。除了 readinessProbe，因为 Init 容器无法定义不同于完成
（completion）的就绪（readiness）之外的其他状态。这会在验证过程中强制执行
- 在 Pod 中的每个 app 和 Init 容器的名称必须唯一；与任何其它容器共享同一个名称，会在验证
时抛出错误

### 容器探针

探针是由 kubelet 对容器执行的定期诊断。要执行诊断，kubelet 调用由容器实现的 Handler。有三
种类型的处理程序：
- ExecAction：在容器内执行指定命令。如果命令退出时返回码为 0 则认为诊断成功。
- TCPSocketAction：对指定端口上的容器的 IP 地址进行 TCP 检查。如果端口打开，则诊断
被认为是成功的。
- HTTPGetAction：对指定的端口和路径上的容器的 IP 地址执行 HTTP Get 请求。如果响应的
状态码大于等于200 且小于 400，则诊断被认为是成功的
每次探测都将获得以下三种结果之一：
- 成功：容器通过了诊断。
- 失败：容器未通过诊断。
- 未知：诊断失败，因此不会采取任何行动

#### 就绪检测

```yml
# readiness-httpget-pod.yml  
# kubectl create -f readiness-httpget-pod.yml 
apiVersion: v1
kind: Pod
metadata:
  name: readiness-httpget-pod
  labels:
    name: readiness-httpget-pod
spec:
  containers:
  - name: readiness-httpget-container
    image: my-app:v1
    imagePullPolicy: IfNotPresent
    readinessProbe:
      httpGet:
        port: 80
        path: /index.html
      initialDelaySeconds: 1
      periodSeconds: 3
```

```sh
# 进入pod容器
kubectl exec podName [-c containerName] -it -- /bin/sh
```

#### 存活检测

```yml
# livenessProbe-httpget
apiVersion: v1
kind: Pod
metadata:
  name: liveness-httpget-pod
  labels:
    name: liveness-httpget-pod
spec:
  containers:
  - name: liveness-httpget-pod
    image: my-app:v1
    imagePullPolicy: IfNotPresent
    ports:
      - name: http
        containerPort: 80
    livenessProbe:
      httpGet:
        port: http
        path: /index.html
      initialDelaySeconds: 1
      periodSeconds:  3
      timeoutSeconds: 10
```

```yml
#livenessProbe-tcp
apiVersion: v1
kind: Pod
  metadata:
    name: probe-tcp
  spec:
    containers:
    - name: nginx
      image: my-app:v1
      livenessProbe:
        initialDelaySeconds: 5
        timeoutSeconds: 1
        tcpSocket:
          port: 80
```

#### 启动、退出动作

```yml
# postStart preStop
apiVersion: v1
kind: Pod
metadata:
  name: lifecycle
  labels:
    name: lifecycle
spec:
  containers:
  - name: lifecycle-container
    image: registry.cn-beijing.aliyuncs.com/joey2020/k8s-test:v1
    lifecycle:
      postStart:
        exec: 
          command:  ["/bin/sh", "-c", "echo Hello from the postStart handler > /usr/share/message"]
      preStop:
        exec:
          command:  ["/bin/sh", "-c", "echo Hello from the preStop handler > /usr/share/message"]
```

### 探测方式

livenessProbe：指示容器是否正在运行。如果存活探测失败，则 kubelet 会杀死容器，并且容器将
受到其 重启策略 的影响。如果容器不提供存活探针，则默认状态为 Success

readinessProbe：指示容器是否准备好服务请求。如果就绪探测失败，端点控制器将从与 Pod 匹配的
所有 Service 的端点中删除该 Pod 的 IP 地址。初始延迟之前的就绪状态默认为 Failure。如果容
器不提供就绪探针，则默认状态为 Success

### Pod hook

Pod hook（钩子）是由 Kubernetes 管理的 kubelet 发起的，当容器中的进程启动前或者容器中的进
程终止之前运行，这是包含在容器的生命周期之中。可以同时为 Pod 中的所有容器都配置 hook
Hook 的类型包括两种：
- exec：执行一段命令
- HTTP：发送HTTP请求

### 重启策略

PodSpec 中有一个 restartPolicy 字段，可能的值为 Always、OnFailure 和 Never。默认为
Always。 restartPolicy 适用于 Pod 中的所有容器。restartPolicy 仅指通过同一节点上的
kubelet 重新启动容器。失败的容器由 kubelet 以五分钟为上限的指数退避延迟（10秒，20秒，40
秒...）重新启动，并在成功执行十分钟后重置。如 Pod 文档 中所述，一旦绑定到一个节点，Pod 将
永远不会重新绑定到另一个节点。

### Pod phase

Pod 的 status 字段是一个 PodStatus 对象，PodStatus中有一个 phase 字段。
Pod 的相位（phase）是 Pod 在其生命周期中的简单宏观概述。该阶段并不是对容器或 Pod 的综合汇
总，也不是为了做为综合状态机
Pod 相位的数量和含义是严格指定的。除了本文档中列举的状态外，不应该再假定 Pod 有其他的
phase 值

#### Pod phase 可能存在的值

- 挂起（Pending）：Pod 已被 Kubernetes 系统接受，但有一个或者多个容器镜像尚未创建。等待时间
包括调度 Pod 的时间和通过网络下载镜像的时间，这可能需要花点时间
- 运行中（Running）：该 Pod 已经绑定到了一个节点上，Pod 中所有的容器都已被创建。至少有一个容
器正在运行，或者正处于启动或重启状态
- 成功（Succeeded）：Pod 中的所有容器都被成功终止，并且不会再重启
- 失败（Failed）：Pod 中的所有容器都已终止了，并且至少有一个容器是因为失败终止。也就是说，容
器以非 0 状态退出或者被系统终止
- 未知（Unknown）：因为某些原因无法取得 Pod 的状态，通常是因为与 Pod 所在主机通信失败