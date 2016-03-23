// Jobs
def generateAEMLoadPlatformJob = freeStyleJob("Load_FusionMiddleware_Cartridge")
 
// Setup generateBuildPipelineJobs
generateAEMLoadPlatformJob.with {
		scm {
				git {
						remote {
								url('https://github.com/ejsds09/githubrepo.git')
								//credentials("adop-jenkins-master")
						}	
						branch('*/master')
				}
		}
		steps {
				shell('''#!/bin/bash -ex
						token="$(curl -X POST "http://gitlab:9080/api/v3/session?login=root&password=admin01aem" | python -c "import json,sys;obj=json.load(sys.stdin);print obj['private_token'];")"
						
						key=$(cat ~/.ssh/id_rsa.pub)

						curl --header "PRIVATE-TOKEN: $token" -X POST "http://gitlab:9080/api/v3/user/keys" --data-urlencode "title=jenkins@adop-core" --data-urlencode "key=${key}"

						# create platform-management into gitlab
						target_repo_name="platform-management"

						curl --header "PRIVATE-TOKEN: $token" -X POST "http://gitlab:9080/api/v3/projects?name=${target_repo_name}"

						# Create Gerrit repository
						# push the sample codes to the sample Gitlab project
						git remote add adop git@gitlab:root/$target_repo_name.git
						git fetch adop
						git push adop +refs/remotes/origin/*:refs/heads/*
				''')
			dsl{
						external('DSL/Create_Folder.groovy',
								'DSL/EnvironmentBuildPipeline.groovy',
								'DSL/Set_Provisioning_Parameters.groovy',
								'DSL/Create_Database_Environment.groovy',
								'DSL/Create_FMW_Environment.groovy',
								'DSL/Check_Instance_Status.groovy',
								'DSL/Requirement_Preparation.groovy',
								'DSL/Install_FMW.groovy',
								'DSL/Create_SOA_Domain.groovy',
								'DSL/Create_Schemas.groovy',
								'DSL/Smoke_Test.groovy')
						ignoreExisting()
			}
		}
}

