export server="AOM"
cd ../../
cp ENV/Baseline/profile.sh  target/
cp -R ENV/Baseline/$server  target/
cd target
rm -rf ignoredJars
chmod -R +x  $server/bin
docker build -t aom --build-arg SERVER=$server --build-arg MODULE=AOMIF -f ../CI-CD/docker/DockerFile .