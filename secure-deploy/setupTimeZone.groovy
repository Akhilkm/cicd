def call(String tz = 'Asia/Kolkata') {
    withEnv(["TZ_TO_SET=$tz"]) {
        sh 'sudo timedatectl set-timezone "$TZ_TO_SET"'
    }
}
