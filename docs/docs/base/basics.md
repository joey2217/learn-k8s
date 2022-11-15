---
sidebar_position: 1
---

# 基础命令

## 教程

> https://kuboard.cn/learning/k8s-bg/component.html#master%E7%BB%84%E4%BB%B6


## 集群信息

```sh
kubectl version
```

```sh
kubectl cluster-info
```

```sh
kubectl get nodes
```

## 部署应用 deployment

> https://kubernetes.io/zh-cn/docs/tutorials/kubernetes-basics/deploy-app/deploy-intro/

### 创建deployment

```sh
kubectl create deployment hello-k8s --image=nginx
```

```
kubectl get deployments #查看deployments
```

## 了解应用

- `kubectl get` - 列出资源
- `kubectl describe` - 显示有关资源的详细信息
- `kubectl logs` - 打印 pod 和其中容器的日志
- `kubectl exec` - 在 pod 中的容器上执行命令

```sh
kubectl get pods
```
```sh
kubectl describe pods
```

### POD_NAME

```sh
export POD_NAME=$(kubectl get pods -o go-template --template '{{range .items}}{{.metadata.name}}{{"\n"}}{{end}}')
echo Name of the Pod: $POD_NAME
```

```sh
kubectl logs $POD_NAME
```

```sh
kubectl exec $POD_NAME -- env # 环境变量
```

```sh
kubectl exec -ti $POD_NAME -- bash # 进入bash  exit退出
```

## 暴露应用 Service

```sh
kubectl get services
```

```sh
kubectl expose deployment/hello-k8s --type="NodePort" --port 80
```

```sh
kubectl describe services/hello-k8s
```

### Service NodePort

```sh
export NODE_PORT=$(kubectl get services/hello-k8s -o go-template='{{(index .spec.ports 0).nodePort}}')
echo NODE_PORT=$NODE_PORT
```

### labels

```sh
kubectl describe deployment
```

```sh
kubectl get pods -l app=hello-k8s
kubectl get services -l app=hello-k8s
```

#### 设置label

```
kubectl label pods $POD_NAME version=v1
```

```
kubectl get pods -l version=v1
```

### 删除Service

```sh
kubectl delete service -l app=hello-k8s
```

## 扩展应用

```sh
kubectl get rs # 查看 ReplicaSet
```

```sh
kubectl scale deployments/hello-k8s --replicas=4 # 扩展副本4个
```

```sh
kubectl get pods -o wide
```

## 更新应用

### 设置镜像

```sh
kubectl set image deployments/nginx nginx=nginx:alpine
```

### 更新状态

```sh
kubectl rollout status deployments/nginx
```

### 退回更新 Rollback

```sh
kubectl rollout undo deployments/nginx
```