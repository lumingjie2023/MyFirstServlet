@echo off
echo 正在启动后端服务...
cd /d "%~dp0"
"C:\Program Files\Maven\apache-maven-3.9.16-bin\apache-maven-3.9.16\bin\mvn.cmd" clean compile spring-boot:run
pause
