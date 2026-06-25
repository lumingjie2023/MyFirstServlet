@echo off
echo ========================================
echo   大学成绩管理系统 - 一键启动
echo ========================================
echo.
echo 正在启动后端服务...
start "后端服务" cmd /k cd /d C:\Users\hhxh_\Documents\trae_projects\MyFirstServlet\backend ^& echo 正在启动后端服务... ^& "C:\Program Files\Maven\apache-maven-3.9.16-bin\apache-maven-3.9.16\bin\mvn.cmd" clean compile spring-boot:run
echo.
echo 等待后端启动完成（约15秒）...
timeout /t 15 /nobreak
echo.
echo 正在启动前端服务...
start "前端服务" cmd /k cd /d C:\Users\hhxh_\Documents\trae_projects\MyFirstServlet\frontend ^& echo 正在启动前端服务... ^& npm run dev
echo.
echo ========================================
echo   启动完成！
echo ========================================
echo.
echo 请在浏览器访问: http://localhost:5174/
echo.
echo 注意事项:
echo   1. 后端窗口显示 "Started GradebookApplication" 表示启动成功
echo   2. 前端窗口显示 "Local: http://localhost:5174/" 表示启动成功
echo   3. 请等待两个服务都启动成功后再登录
echo.
pause