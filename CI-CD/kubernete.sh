cd ../
export server="BARONS"
export app="spring-cloud-hello"
export target=$app/target
cp CI-CD/Baseline/*.yml  $target/
cp CI-CD/Baseline/profile.sh  $target/
cp -R CI-CD/Baseline/$server  $target/
cd $target
chmod -R +x  $server/bin
docker build -t hello:local --build-arg SERVER=$server --build-arg MODULE=HELLO -f ../../CI-CD/DockerFile .


echo "saving hello.tar"
docker save hello:local > hello.tar
echo "deleting hello.yml"
microk8s kubectl delete -f hello.yml
microk8s kubectl delete -f hello2.yml
echo "importing hello.tar"
microk8s ctr image import hello.tar
echo "applying helloworld.yml"
microk8s kubectl apply  -f hello.yml #means create or update
microk8s kubectl apply  -f hello2.yml #means create or update
#if you want to debug
#https://www.middlewareinventory.com/blog/how-to-exec-into-crashloopbackoff-pod-kubernetes-devopsjunction/
kubectl run testkubesample -it  --image localhost:32000/aomif-maven -- /bin/sh

https://minikube.sigs.k8s.io/docs/handbook/pushing/

minikube cache add alpine:latest #local save
minikube cache reload #if you update your image at your local host

#registry local ip olmuyor, sunu yaparsan localhost kullanabilirsin ya da $(minikube ip) kullancan hep
docker run --rm -it --network=host alpine ash -c "apk add socat && socat TCP-LISTEN:5000,reuseaddr,fork TCP:$(minikube ip):5000"
#burada da baska mini kube icin bi seyler var
#https://gist.github.com/trisberg/37c97b6cc53def9a3e38be6143786589
#https://stackoverflow.com/questions/69498412/minikube-accessing-images-added-with-registry-addon

#docker -> daemon json'a insecure registries de ekleyecek -> #systemctl docker reload
# minikube e registrt vremek icin de surasi -> ~/.minikube/machines/minikube/config.json -> HostOptions
# sonrasinda minikube ssh -> docker info'dan minikube'de insecure eklenmismi gorebilirsin

docker tag aomif-maven:latest 192.168.49.2:5000/aomif-maven
docker push 192.168.49.2:5000/aomif-maven
http://192.168.49.2:5000/v2/_catalog

#kubectl apply -f svc-hello.yml
kubectl run aomif-pod --image=aomif-maven
kubectl describe pod aomif-pod ile sorunu gorebilirsin
kubectl run aomif-pod --image=$(minikube ip):5000/aomif-maven --port=8181
minikube start --driver=docker  --kubernetes-version=1.26.13
minikube config set insecure-registry "192.168.49.0/24"

#minikube'un senin docker'ini kullanmasina izin vermek icin terminalinde sunu cagirabilirsin docker komutlarin artik minikube de calisir
#eval $(minikube docker-env)


# KUB GET #

#kubectl get pods -o wide      --> IP:8080'den erisebilirsin
#kubectl get services -o wide  --> localhost:30000'den erisebilirsin  localhost --> nodeIp

#KUB INFO #

#kubctl get nodes -o wide
#kubctl get namespace
#kubctl get pods n i2i-api o wide (i2i-api name space’indekini getir)
#kubctl get deployments -n i2i-api -> kaç pod konfigure edilmiş görebilirin
#kubectl get endpoints cache -o yaml veya json (get all podIps)
### POD INFO ###
#kubectl exec podName -- printenv
#pod'a girip env cagirirsan tum environment'lari gorebilirsin
#kubectl describe pod pod-name

#kubectl logs pod-name

#tum pod'lari almak icin
#https://kubernetes.io/docs/tasks/run-application/access-api-from-pod/
#headless-service
# health check icin liveness probe, readiness probe