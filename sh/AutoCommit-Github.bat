::  这条命令使用当前时间来作为 commit 的信息
set commit_msg= %date:~0,10%   %time% AutoCommit.bat by Darian
git status 
git add * 
git commit -m "%commit_msg%" 

git pull github master
git push github master

:: make a pause 这条命令用来等待
:: pause
:: Exit 用来推出当前命令
Exit
