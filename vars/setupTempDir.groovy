def call(Map args) {

	String tmpDir = "/tmp/$BUILD_TAG";
	if (args.tmpDir) {
		tmpDir = args.tmpDir
	}

	String appName = args.appName
	String deployEnv = args.deployEnv
	String deployVersion = args.deployVersion
    String deployCredentialsId = args.deployCredentialsId


    sh "if [ -d "$tmpDir" ]; then echo "Directory  exists."; else mkdir $tmpDir; fi"
}