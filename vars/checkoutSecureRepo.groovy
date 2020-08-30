def call(body = null) {
	checkout scm: [
			$class                           : 'GitSCM', branches: [[name: '*/master']],
            userRemoteConfigs   : [[
                                    credentialsId: '*******',
                                    url          : 'https://github.com/akhilkm/secure-repo.git'
                                  ]],
	]
}

static void main(String[] args) {
	call()
}