#!/usr/bin/env bash

commit_msg=$(date "+%Y-%m-%d %H:%M:%S AutoCommit.sh by Darian")
git status
git add *
git commit -m "${commit_msg}"
echo "============================================================="
echo "${commit_msg}"
echo "============================================================="
git pull gitee master
git pull github master
git push gitee master
git push github master