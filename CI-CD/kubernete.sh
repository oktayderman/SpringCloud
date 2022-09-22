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




#kubectl apply -f svc-hello.yml




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