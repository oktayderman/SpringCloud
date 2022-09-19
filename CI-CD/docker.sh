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
microk8s kubectl apply  -f hello.yml

#kubectl logs pod-name