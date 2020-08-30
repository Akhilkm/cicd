// The call(body) method in any file in secure-deploy.git/vars is exposed as a
// method with the same name as the file.
def call(tmpDir) {
	stage('Cleanup') {
		withEnv(["TMPDIR=${tmpDir}"]) {
			sh 'if [ -e "$TMPDIR" ]; then sh rm -rf "$TMPDIR"; fi'
		}
	}
}