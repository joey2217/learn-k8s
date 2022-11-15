---
sidebar_position: 3
---

# 安装

## 准备

### 设置防火墙为 Iptables 并设置空规则

```sh
systemctl stop firewalld && systemctl disable firewalld

yum -y install iptables-services && systemctl start iptables && systemctl enable iptables&& iptables -F && service iptables save
```

### 关闭 SELINUX

```sh
# 关闭swap
swapoff -a && sed -i '/ swap / s/^\(.*\)$/#\1/g' /etc/fstab

setenforce 0 && sed -i 's/^SELINUX=.*/SELINUX=disabled/' /etc/selinux/config
```


## 安装 kubectl

> https://developer.aliyun.com/mirror/kubernetes

## 安装 Minikube

> https://minikube.sigs.k8s.io/docs/start/

### 使用阿里云

```sh
 minikube start --image-mirror-country='cn'
```

> https://blog.csdn.net/weixin_44559544/article/details/123497742