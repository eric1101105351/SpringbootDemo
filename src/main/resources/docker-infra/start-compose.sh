echo "########## compose up start ... ########## "
docker compose -f docker-compose.yml up -d
echo "########## compose up end ########## "
echo "########## container check ########## "
docker ps