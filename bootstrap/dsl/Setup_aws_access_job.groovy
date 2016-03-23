freeStyleJob('Setup_AWS_Access') {
    logRotator(-1, 10)
    parameters {
        stringParam('AWS_ACCESS_KEY','',' ')
      	nonStoredPasswordParam('AWS_SECRET_KEY', '')
      	stringParam('AWS_REGION','eu-east-1','')
        fileParam('SSH_KEY_FILE', 'Upload your AWS SSH key pair here')
    }	
    label('docker')
    steps {
		shell('''#!/bin/bash
mkdir -p ~/.aws
echo "[default]" >  ~/.aws/credentials
echo "aws_access_key_id = $AWS_ACCESS_KEY" >> ~/.aws/credentials
echo "aws_secret_access_key = $AWS_SECRET_KEY" >> ~/.aws/credentials
echo "output = text"  >> ~/.aws/credentials
echo "region = $AWS_REGION" >> ~/.aws/credentials
cat SSH_KEY_FILE > /etc/fmw_oracle.pem & chmod 600 /etc/fmw_oracle.pem
         ''')
    }
}