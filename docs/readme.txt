https://www.baeldung.com/spring-cloud-netflix-eureka
https://github.com/eugenp/tutorials/tree/master/spring-cloud-modules/spring-cloud-eureka

""https://srvaroa.github.io/kubernetes/eureka/paas/microservices/loadbalancing/2020/02/12/eureka-kubernetes.html""





micro'ya local image atma
https://microk8s.io/docs/registry-images
(latest kullanılmıyor versiyonu ver mutlaka mesela local diye)
docker build . -t hello:local -f DockerFile
docker save hello:local > hello.tar

microk8s ctr image import hello.tar
microk8s ctr images ls | grep hello

kubectl apply  -f helloworld.yml
kubectl get pods


Delete deployment/pod
kubectl delete -f helloworld.yml
kubectl create -f helloworld.yml

We reference the image with image: mynginx:local. Kubernetes will behave as though there is an image in docker.io (the Dockerhub registry)
for which it already has a cached copy. This process can be repeated any time changes are made to the image.
Note that containerd will not cache images with the latest tag so make sure you avoid it.
