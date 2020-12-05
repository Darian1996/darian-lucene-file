git init
git remote add gitee git@gitee.com:Darian1996/darian-lucene-file.git
git remote add github git@github.com:Darian1996/darian-lucene-file.git
git add .
git commit -m "$(date "+%Y-%m-%d %H:%M:%S AutoCommit.sh by Darian")"
git push -f gitee master
git push -f github master