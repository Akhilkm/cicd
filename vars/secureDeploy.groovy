def call(Map args) {
	String tmpDir = "/tmp/$BUILD_TAG";
	if (args.tmpDir) {
		tmpDir = args.tmpDir
	}

	String appName = args.appName
	String deployEnv = args.deployEnv
	String deployVersion = args.deployVersion

	withEnv(["TMPDIR=$tmpDir", "APP_NAME=$appName",
			 "DEPLOY_ENV=$deployEnv", "DEPLOY_VERSION=$deployVersion"]) {
		sh "./jenkins/scripts/deliver-for-development.sh"
	}
}