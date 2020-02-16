# awspractise
aws practise examples
adding eclipse project into git
>git init
>git remote add origin  https://github.com/srinu494u/awspractise.git
if you face any issues after hitting the above command use below link for refer
https://stackoverflow.com/questions/5834014/lf-will-be-replaced-by-crlf-in-git-what-is-that-and-is-it-important
example issues : LF will be replaced by CRLF in

>git config --global core.autocrlf false
>git add .
>git commit -m "add all files"
at the time of pushing files to git you may get the error again use the below command
>git pull --rebase origin master
>git push origin master

