echo "########## container stop start ... ########## "
docker stop $(docker ps -a -q)
echo "########## container stop end ########## "
echo "########## container remove start ... ########## "
docker rm $(docker ps -a -q)
echo "########## container remove end ... ########## "
echo "########## container check ########## "
docker ps -a