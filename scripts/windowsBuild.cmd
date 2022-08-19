@echo off

SET PASTDIR="%cd%"
SET DIR=%~dp0..
cd %DIR%
SET CLASSPATH=lib\commons-cli-1.5.0.jar

if exist bin rmdir /s /q bin
if not exist bin mkdir bin
if exist docs rmdir /s /q docs
if not exist docs mkdir docs

dir /s /b %DIR%\src | findstr /IRC:"\.java" > scripts\sources.txt

echo Compiling source code...
javac -d bin @scripts\sources.txt || exit

echo PASSED
echo Compiling javadocs...
javadoc -d docs @scripts\sources.txt || exit
del scripts\sources.txt

echo PASSED
echo Compressing jarfile...
cd bin
jar cvfm %DIR%\scripts\manifest.txt out.jar EzASM\* ..\lib\commons-cli-1.5.0.jar || exit
echo PASSED

cd %PASTDIR%

pause