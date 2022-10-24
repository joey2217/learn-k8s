# learn-k8s

## Minikube安装

### 关闭swap

sudo swapoff -a
sudo sed -i '/ swap / s/^/#/' /etc/fstab

https://developer.aliyun.com/article/886463