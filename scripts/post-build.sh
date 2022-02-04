TIMESTAMP=$1

VERSION=`scripts/get-version.sh`

TARGET_DIR="target/learn-$VERSION/"
TARGET_SCRIPT_RUN="target/learn-$VERSION/run.sh"
TARGET_SCRIPT_UPDATE="target/learn-$VERSION/update-learn.sh"

sed -e "s/BUILD_TIMESTAMP/${TIMESTAMP}/" scripts/run.sh.template \
    > $TARGET_SCRIPT_RUN

sed -e "s/BUILD_TIMESTAMP/${TIMESTAMP}/" scripts/update-learn.sh.template \
    > $TARGET_SCRIPT_UPDATE

chmod +x $TARGET_SCRIPT_RUN
chmod +x $TARGET_SCRIPT_UPDATE