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
echo "deleting helloworld.yml"
microk8s kubectl delete -f helloworld.yml
echo "importing hello.tar"
microk8s ctr image import hello.tar
echo "applying helloworld.yml"
microk8s kubectl apply  -f hello.yml #means create or update


#kubectl get pods -o wide --> IP:8080'den erisebilirsin
#kubectl apply -f svc-hello.yml
#kubectl get services --> localhost:30000'den erisebilirsin  localhost --> nodeIĞ
#kubectl logs pod-name