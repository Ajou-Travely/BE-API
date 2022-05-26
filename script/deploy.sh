export DATABASE_HOST=$(aws ssm get-parameters --region ap-northeast-2 --names /travely/dev/DATABASE_HOST --with-decryption --query Parameters[0].Value --output text)
export DATABASE_PORT=$(aws ssm get-parameters --region ap-northeast-2 --names /travely/dev/DATABASE_PORT --with-decryption --query Parameters[0].Value --output text)
export DATABASE_USERNAME=$(aws ssm get-parameters --region ap-northeast-2 --names /travely/dev/DATABASE_USERNAME --with-decryption --query Parameters[0].Value --output text)
export DATABASE_PASSWORD=$(aws ssm get-parameters --region ap-northeast-2 --names /travely/dev/DATABASE_PASSWORD --with-decryption --query Parameters[0].Value --output text)
export DATABASE_NAME=$(aws ssm get-parameters --region ap-northeast-2 --names /travely/dev/DATABASE_NAME --with-decryption --query Parameters[0].Value --output text)

export MAIL_SENDER_USERNAME=$(aws ssm get-parameters --region ap-northeast-2 --names /travely/dev/MAIL_SENDER_USERNAME --with-decryption --query Parameters[0].Value --output text)
export MAIL_SENDER_PASSWORD=$(aws ssm get-parameters --region ap-northeast-2 --names /travely/dev/MAIL_SENDER_PASSWORD --with-decryption --query Parameters[0].Value --output text)


REPOSITORY=/home/ec2-user/app
cd $REPOSITORY

APP_NAME=travely #1
JAR_NAME=$(ls $REPOSITORY/build/libs/ | grep '.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/build/libs/$JAR_NAME

CURRENT_PID=$(pgrep -f $APP_NAME)

if [ -z $CURRENT_PID ] #2
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> kill -15 $CURRENT_PID"
  sudo kill -15 $CURRENT_PID
  sleep 5
fi


echo "> $JAR_PATH 배포" #3
nohup java -jar /home/ec2-user/app/build/libs/travely-0.0.1-SNAPSHOT.jar --logging.file.path=/home/ec2-user/ > /dev/null 2> /dev/null < /dev/null &