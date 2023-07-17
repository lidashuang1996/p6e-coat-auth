@echo off
echo "Executing first command"
start /B java -jar C:\myapp\myapp.jar

echo "Executing second command"
start /B java -jar C:\myapp\myapp.jar

:: sc create smart-site-backend binPath= "cmd /c init.bat"


echo "===== HKSI START ====="
echo "===== HKSI START ====="
echo "===== HKSI START ====="

cd /home/darvi/opt/apps/backend

# nohup /root/jdk-17.0.4.1/bin/java -jar hksi-gateway-1.0.0.jar > /dev/null 2>&1 --server.port=25000 --spring.profiles.active=nacos-dev &

echo "===== START [ hksi-bicycle-auth ] ====="
/home/darvi/opt/jdk/jdk17/bin/java -jar /home/darvi/opt/apps/backend/hksi-bicycle-auth-1.0.0.jar --spring.profiles.active=nacos-dev > /home/darvi/opt/apps/backend/auth.log  &

echo "===== START [ hksi-bicycle-gateway ] ====="
/home/darvi/opt/jdk/jdk17/bin/java -jar /home/darvi/opt/apps/backend/hksi-bicycle-gateway-1.0.0.jar --spring.profiles.active=nacos-dev > /home/darvi/opt/apps/backend/gateway.log  &

echo "===== START [ hksi-bicycle-common ] ====="
/home/darvi/opt/jdk/jdk17/bin/java -jar /home/darvi/opt/apps/backend/hksi-bicycle-common-1.0.0.jar --spring.profiles.active=nacos-dev > /home/darvi/opt/apps/backend/common.log  &

echo "===== START [ hksi-bicycle-resource ] ====="
/home/darvi/opt/jdk/jdk17/bin/java -jar /home/darvi/opt/apps/backend/hksi-bicycle-resource-1.0.0.jar --spring.profiles.active=nacos-dev > /home/darvi/opt/apps/backend/resource.log  &

echo "===== START [ hksi-bicycle-batch ] ====="
/home/darvi/opt/jdk/jdk17/bin/java -jar /home/darvi/opt/apps/backend/hksi-bicycle-batch-1.0.0.jar --spring.profiles.active=nacos-dev > /home/darvi/opt/apps/backend/batch.log  &


echo "===== START [ hksi-bicycle-file-achieve ] ====="
/home/darvi/opt/jdk/jdk17/bin/java -jar -Xms2g -Xmx2g /home/darvi/opt/apps/backend/hksi-bicycle-file-achieve-1.0.0.jar --spring.profiles.active=nacos-dev > /home/darvi/opt/apps/backend/file.log  &



echo "===== HKSI END ====="
echo "===== HKSI END ====="
echo "===== HKSI END ====="