set GX_PROGRAM_DIR=C:\Program Files (x86)\GeneXus\GeneXusBeta

set KBPath=%1
set DataDir=%2
set ProjectName=%3
set AppName=%4
set ObjectNames=%5
set ProjFullPath=%KBPath%\%DataDir%\web\%ProjectName%.gxdproj
set OutputWar=%KBPath%\%DataDir%\Deploy\Local\%ProjectName%.war

"C:\Program Files (x86)\Microsoft Visual Studio\2019\BuildTools\MSBuild\Current\Bin\msbuild.exe" deploy.msbuild /p:ProjectName=%ProjectName%;KBPath=%KBPath%;ObjectNames=%ObjectNames%

"C:\Program Files (x86)\Microsoft Visual Studio\2019\BuildTools\MSBuild\Current\Bin\msbuild.exe" %ProjFullPath% /p:AppName=%AppName%