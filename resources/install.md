---
id: install
title: 安装K8s
sidebar_label: 安装K8s
---

## 设置系统主机名以及host文件的相互解析

```sh
# k8s-master01
hostnamectl set-hostname k8s-master01
# /etc/hosts
echo "192.168.1.32 k8s-master01
192.168.1.33 k8s-node01
192.168.1.34 k8s-node03" >> /etc/hosts
```

:::tip
大型集群使用DNS解析
:::

## 安装依赖包

```sh
yum install -y conntrack ntpdate ntp ipvsadm ipset jq iptables curl sysstat libseccomp wget vim
net-tools git
```

## 设置防火墙为 Iptables 并设置空规则

```sh
systemctl stop firewalld && systemctl disable firewalld

yum -y install iptables-services && systemctl start iptables && systemctl enable iptables&& iptables -F && service iptables save
```

## 关闭 SELINUX

```sh
# 关闭swap
swapoff -a && sed -i '/ swap / s/^\(.*\)$/#\1/g' /etc/fstab

setenforce 0 && sed -i 's/^SELINUX=.*/SELINUX=disabled/' /etc/selinux/config
```

## 调整内核参数，对于 K8S

```sh
cat > kubernetes.conf <<EOF
net.bridge.bridge-nf-call-iptables=1
net.bridge.bridge-nf-call-ip6tables=1
net.ipv4.ip_forward=1
net.ipv4.tcp_tw_recycle=0
vm.swappiness=0 # 禁止使用 swap 空间，只有当系统 OOM 时才允许使用它
vm.overcommit_memory=1 # 不检查物理内存是否够用
vm.panic_on_oom=0 # 开启 OOM
fs.inotify.max_user_instances=8192
fs.inotify.max_user_watches=1048576
fs.file-max=52706963
fs.nr_open=52706963
net.ipv6.conf.all.disable_ipv6=1
net.netfilter.nf_conntrack_max=2310720
EOF

cp kubernetes.conf /etc/sysctl.d/kubernetes.conf

sysctl -p /etc/sysctl.d/kubernetes.conf
```
 

## kube-proxy开启ipvs的前置条件

```sh
modprobe br_netfilter

cat > /etc/sysconfig/modules/ipvs.modules <<EOF
#!/bin/bash
modprobe -- ip_vs
modprobe -- ip_vs_rr
modprobe -- ip_vs_wrr
modprobe -- ip_vs_sh
modprobe -- nf_conntrack_ipv4
EOF

chmod 755 /etc/sysconfig/modules/ipvs.modules && bash /etc/sysconfig/modules/ipvs.modules &&lsmod | grep -e ip_vs -e nf_conntrack_ipv4
```

## 安装 Docker 软件

```sh
yum install -y yum-utils device-mapper-persistent-data lvm2
yum-config-manager \
--add-repo \
http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
yum update -y && yum install -y docker-ce
## 创建 /etc/docker 目录
mkdir /etc/docker
# 配置 daemon.
cat > /etc/docker/daemon.json <<EOF
{
    "exec-opts": ["native.cgroupdriver=systemd"],
    "log-driver": "json-file",
    "log-opts": {
        "max-size": "100m"
    }
}
EOF
mkdir -p /etc/systemd/system/docker.service.d
# 重启docker服务
systemctl daemon-reload && systemctl restart docker && systemctl enable docker
```

## 安装 Kubeadm （主从配置）

```sh
cat <<EOF > /etc/yum.repos.d/kubernetes.repo
[kubernetes]
name=Kubernetes
baseurl=http://mirrors.aliyun.com/kubernetes/yum/repos/kubernetes-el7-x86_64
enabled=1
gpgcheck=0
repo_gpgcheck=0
gpgkey=http://mirrors.aliyun.com/kubernetes/yum/doc/yum-key.gpg  http://mirrors.aliyun.com/kubernetes/yum/doc/rpm-package-key.gpg
EOF

yum install -y kubelet kubeadm kubectl\

systemctl enable kubelet.service
```

## 初始化主节点[kuboard](https://www.kuboard.cn/install/install-k8s.html#%E5%88%9D%E5%A7%8B%E5%8C%96-master-%E8%8A%82%E7%82%B9)

```sh
# 只在 master 节点执行
# 替换 x.x.x.x 为 master 节点实际 IP（请使用内网 IP）
# export 命令只在当前 shell 会话中有效，开启新的 shell 窗口后，如果要继续安装过程，请重新执行此处的 export 命令
export MASTER_IP=x.x.x.x
# 替换 apiserver.demo 为 您想要的 dnsName
export APISERVER_NAME=apiserver.demo
# Kubernetes 容器组所在的网段，该网段安装完成后，由 kubernetes 创建，事先并不存在于您的物理网络中
export POD_SUBNET=10.100.0.1/16
echo "${MASTER_IP}    ${APISERVER_NAME}" >> /etc/hosts
curl -sSL https://kuboard.cn/install-script/v1.18.x/init_master.sh | sh -s 1.18.2
```

## 初始化 worker节点

### master

```sh
kubeadm token create --print-join-command

# kubeadm token create 命令的输出
kubeadm join apiserver.demo:6443 --token mpfjma.4vjjg8flqihor4vt     --discovery-token-ca-cert-hash sha256:6f7a8e40a810323672de5eee6f4d19aa2dbdb38411845a1bf5dd63485c43d303
```

###  node

```sh
hostnamectl set-hostname nodex

# 安装Kubeadm
# 替换 x.x.x.x 为 master 节点的内网 IP
export MASTER_IP=x.x.x.x
# 替换 apiserver.demo 为初始化 master 节点时所使用的 APISERVER_NAME
export APISERVER_NAME=apiserver.demo
echo "${MASTER_IP}    ${APISERVER_NAME}" >> /etc/hosts

# 替换为 master 节点上 kubeadm token create 命令的输出
kubeadm join apiserver.demo:6443 --token mpfjma.4vjjg8flqihor4vt     --discovery-token-ca-cert-hash sha256:6f7a8e40a810323672de5eee6f4d19aa2dbdb38411845a1bf5dd63485c43d303
```

## 查看结果

```sh
[root@demo-master-a-1 ~]# kubectl get nodes
NAME     STATUS   ROLES    AGE     VERSION
demo-master-a-1   Ready    master   5m3s    v1.18.x
demo-worker-a-1   Ready    <none>   2m26s   v1.18.x
demo-worker-a-2   Ready    <none>   3m56s   v1.18.x
```

## [重启kubelet](https://www.jianshu.com/p/39896f2e429d)