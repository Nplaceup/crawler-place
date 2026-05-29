# ==========================
# 인자 처리: 프로파일(default: prd)
# ==========================
PROFILE=${1:-prd}

if [ "$PROFILE" == "dev" ]; then
  BRANCH="develop"
else
  BRANCH="main"
fi


BUILD_DIR=./build/libs

echo "==== 1. Checkout Branch: $BRANCH ===="
git checkout $BRANCH || { echo "Checkout Failure"; exit 1; }

echo "==== 2. Git Pull Start... ===="
git pull origin $BRANCH || { echo "Git Pull Failure"; exit 1; }

echo "==== 3. Build Gradle Start... ===="
./gradlew clean build -x test || { echo "Build Failure"; exit 1; }

JAR_NAME=$(ls $BUILD_DIR/*-SNAPSHOT.jar | grep -v plain | head -n 1)

echo "==== 4. Stopping Previous Server... ===="
PID=$(pgrep -f $JAR_NAME)
if [ -n "$PID" ]; then
  echo "Server Process Stopped (PID: $PID)"
  kill -15 $PID
  sleep 5
else
  echo "No Progressing Server"
fi

echo "==== 5. Server Restarting... ===="
nohup java -jar "$JAR_NAME" --spring.profiles.active="$PROFILE" > /dev/null 2>&1 &

echo "Server Started"