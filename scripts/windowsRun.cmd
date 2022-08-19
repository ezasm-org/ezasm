@echo off

SET DIR="%~dp0\..\bin"
"java" -jar %DIR%\out.jar %*

pause